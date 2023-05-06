package com.xingying.travel.service;

import com.xingying.travel.common.Result;
import com.xingying.travel.common.StatusCode;
import com.xingying.travel.dao.OrdersDao;
import com.xingying.travel.pojo.Orders;
import com.xingying.travel.pojo.Scenic;
import com.xingying.travel.pojo.User;
import com.xingying.travel.util.BigDecimalUtil;
import com.xingying.travel.util.DateTimeUtil;
import com.xingying.travel.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class OrdersService {

	@Autowired
	private OrdersDao ordersDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private ScenicService scenicService;

	@Autowired
	private UserService userService;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Orders> findAll() {
		return ordersDao.findAll();
	}


	public List<Orders> findOrders(String userId){
		return ordersDao.findByUserid(userId);
	}


	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Orders> findSearch(Map whereMap, int page, int size) {
		Specification<Orders> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return ordersDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Orders> findSearch(Map whereMap) {
		Specification<Orders> specification = createSpecification(whereMap);
		return ordersDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Orders findById(String id) {
		return ordersDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param orders
     * @param count
	 */
	public Result add(Orders orders, String userId, String scenicid , Integer count) {

		//通过商品id找商品
		Scenic scenic = scenicService.findById(scenicid).get();

		User user = userService.findById(userId);

		//计算总金额
		BigDecimal payment = BigDecimalUtil.mul(scenic.getPrice(),count);

		int stock = scenic.getStock();
		//校验库存
		if (stock<count){
			return new Result(false, StatusCode.ERROR,"库存不足");
		}else {
			//减少库存数量
			int newStock = scenic.getStock() - count;
			System.out.println("新的库存"+newStock);
			scenic.setStock(newStock);
			scenicService.update(scenic);
		}

		//获取当前时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳

		//存入数据库
		orders.setId( idWorker.nextId()+"" );
		orders.setUserid(userId);
		orders.setPayment(payment);
		orders.setQty(count);
		orders.setPhone(user.getMobile());
        orders.setScenicid(scenicid);
        orders.setStatus("0");
		orders.setPaytime(DateTimeUtil.strToDate(date,"yyyy-MM-dd HH:mm:ss"));
		orders.setUsername(user.getName());
		orders.setScenicname(scenic.getName());
		ordersDao.save(orders);
		return new Result(true,StatusCode.OK,"添加成功");
	}

	/**
	 * 修改
	 * @param orders
	 */
	public void update(Orders orders) {
		ordersDao.save(orders);
	}

	public void updateStatus(String status) {
		ordersDao.updateStatus(status);
	}


	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		ordersDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Orders> createSpecification(Map searchMap) {

		return new Specification<Orders>() {

			@Override
			public Predicate toPredicate(Root<Orders> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // id
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 商品id
                if (searchMap.get("scenicid")!=null && !"".equals(searchMap.get("scenicid"))) {
                	predicateList.add(cb.like(root.get("scenicid").as(String.class), "%"+(String)searchMap.get("scenicid")+"%"));
                }
                // 用户id
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 状态
                if (searchMap.get("status")!=null && !"".equals(searchMap.get("status"))) {
                	predicateList.add(cb.like(root.get("status").as(String.class), "%"+(String)searchMap.get("status")+"%"));
                }
                // 电话
                if (searchMap.get("phone")!=null && !"".equals(searchMap.get("phone"))) {
                	predicateList.add(cb.like(root.get("phone").as(String.class), "%"+(String)searchMap.get("phone")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
