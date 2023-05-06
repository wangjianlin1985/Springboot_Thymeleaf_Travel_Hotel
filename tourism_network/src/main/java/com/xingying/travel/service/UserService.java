package com.xingying.travel.service;

import com.xingying.travel.dao.UserDao;
import com.xingying.travel.pojo.User;
import com.xingying.travel.util.IdWorker;
import com.xingying.travel.util.MailUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * user服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Resource
	MailUtil mailService;

	@Resource
	TemplateEngine templateEngine;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param user
	 */
	public void add(User user) {
		user.setId( idWorker.nextId()+"" );
		String newpassword = encoder.encode(user.getPassword());//加密后的密码
		user.setPassword(newpassword);
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // id
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 姓名
                if (searchMap.get("name")!=null && !"".equals(searchMap.get("name"))) {
                	predicateList.add(cb.like(root.get("name").as(String.class), "%"+(String)searchMap.get("name")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 邮箱
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 性别，男1，女0
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 盐
                if (searchMap.get("salt")!=null && !"".equals(searchMap.get("salt"))) {
                	predicateList.add(cb.like(root.get("salt").as(String.class), "%"+(String)searchMap.get("salt")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	/**
	 * 发送邮箱验证码
	 * @param email
	 */
	public void sendEmail(String email) {
		//产生随机数
		String checkcode = RandomStringUtils.randomNumeric(6);
		//向缓存发一份
		redisTemplate.opsForValue().set("checkcode_",checkcode,1, TimeUnit.HOURS);


		Context context = new Context();
		context.setVariable("id",checkcode);
		String emailContent = templateEngine.process("emailTemplate",context);

		System.out.println("邮箱-->"+ email);
		mailService.sendHtmlMail(email,"模板邮件",emailContent);

		//测试
		System.out.println("验证码："+checkcode);
	}


	/**
	 * 发送验证码
	 * @param mobile
	 */
	public void sendSms(String mobile) {
		//产生随机数
		String checkcode = RandomStringUtils.randomNumeric(6);
		//向缓存发一份
		redisTemplate.opsForValue().set("checkcode_"+mobile,checkcode,1, TimeUnit.HOURS);
		//放入消息队列
		Map<String,String> map = new HashMap();
		map.put("mobile",mobile);
		map.put("checkcode",checkcode);
		//发送给用户
		rabbitTemplate.convertAndSend("sms",map);
		//测试
		System.out.println("验证码："+checkcode);
	}

	/**
	 * 根据手机号和密码查询用户
	 * @param mobile
	 * @param password
	 * @return
	 */
	public User findByMobileAndPassword(String mobile,String password){
		User user = userDao.findByMobile(mobile);
		if(user!=null && encoder.matches(password,user.getPassword())){
			return user;
		}else{
			return null;
		}
	}

	public User findByEmail(String email){
		return userDao.findByEmail(email);
	}

	/**
	 * 根据手机号查询
	 * @param mobile
	 * @return
	 */
	public User findByMobile(String mobile){
		return userDao.findByMobile(mobile);
	}


	/**
	 * 通过姓名和密码查人
	 * @param name
	 * @param password
	 * @return
	 */
	public User findByNameAndPassword(String name,String password){
		User user = userDao.findByName(name);

		if (user!=null && encoder.matches(password,user.getPassword())){
			return user;
		}else {
			return null;
		}

	}

}
