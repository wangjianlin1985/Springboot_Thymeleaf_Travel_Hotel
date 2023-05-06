package com.xingying.travel.service;

import com.xingying.travel.dao.AdminDao;
import com.xingying.travel.pojo.Admin;
import com.xingying.travel.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class AdminService {

	@Autowired
	private AdminDao adminDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	BCryptPasswordEncoder encoder;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Admin> findAll() {
		return adminDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Admin> findSearch(Map whereMap, int page, int size) {
		Specification<Admin> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return adminDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Admin> findSearch(Map whereMap) {
		Specification<Admin> specification = createSpecification(whereMap);
		return adminDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Admin findById(String id) {
		return adminDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param admin
	 */
	public void add(Admin admin) {
		admin.setId( idWorker.nextId()+"" );
		String newpassword = encoder.encode(admin.getPassword());//加密后的密码
		admin.setPassword(newpassword);
		adminDao.save(admin);
	}

	/**
	 * 修改
	 * @param admin
	 */
	public void update(Admin admin) {
		adminDao.save(admin);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		adminDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Admin> createSpecification(Map searchMap) {

		return new Specification<Admin>() {

			@Override
			public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
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
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	/**
	 *  查admin
	 * @param name
	 * @param password
	 * @return
	 */
	public Admin finbyNameAndPassword(String name ,String password){
		Admin admin = adminDao.findByName(name);
		if(admin!=null && encoder.matches(password,admin.getPassword())){
			return admin;
		}
		return null;
	}



}
