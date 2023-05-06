package com.xingying.travel.service;

import com.xingying.travel.common.Result;
import com.xingying.travel.common.StatusCode;
import com.xingying.travel.dao.Hotel_ordersDao;
import com.xingying.travel.dao.OrdersDao;
import com.xingying.travel.pojo.*;
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
public class Hotel_ordersService {

	@Autowired
	private HotelService hotelService;

	@Autowired
	private UserService userService;


	@Autowired
	private IdWorker idWorker;

	@Autowired
	private Hotel_ordersDao hotel_ordersDao;

	/**
	 * 增加
	 * @param orders
	 * @param scenicid
	 * @param count
	 * @return
	 */
	public Result add(Hotel_orders orders, User user, String scenicid , Integer count,String begin,String end) {
		//通过商品id找商品
		Hotel hotel=hotelService.findById(scenicid);
		System.out.println("hotel================="+hotel);

		//计算总金额
		BigDecimal payment = BigDecimalUtil.mul(hotel.getPrice(),count);

		int stock = hotel.getBed();
		//校验库存
		if (stock<count){
			return new Result(false, StatusCode.ERROR,"库存不足");
		}else {
			//减少库存数量
			int newStock = hotel.getBed() - count;
			System.out.println("新的库存"+newStock);
			hotel.setBed(newStock);
			hotelService.update(hotel);
		}

		//获取当前时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(new Date());

		//存入数据库
		orders.setId( idWorker.nextId()+"" );
		orders.setUserid(user.getId());
		orders.setPayment(payment);
		orders.setQty(count);
		orders.setPhone(user.getMobile());
		orders.setScenicid(scenicid);
		orders.setStatus("0");
		orders.setPaytime(DateTimeUtil.strToDate(date,"yyyy-MM-dd HH:mm:ss"));
		orders.setUsername(user.getName());
		orders.setScenicname(hotel.getName());
		orders.setBegin(begin);
		orders.setEnd(end);
		hotel_ordersDao.save(orders);
		return new Result(true,StatusCode.OK,"添加成功");
	}



	/**
	 * 根据ID查询实体
	 * @param userId
	 * @return
	 */
	public List<Hotel_orders> hotel_orders(String userId){
		return hotel_ordersDao.findByUserid(userId);
	}


}
