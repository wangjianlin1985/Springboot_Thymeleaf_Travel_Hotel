package com.xingying.travel.service;

import com.xingying.travel.common.Result;
import com.xingying.travel.common.StatusCode;
import com.xingying.travel.config.ConstantConfig;
import com.xingying.travel.dao.HotelDao;
import com.xingying.travel.pojo.Hotel;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class HotelService {

	@Autowired
	private HotelDao hotelDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
    private ConstantConfig constantConfig;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Hotel> findAll() {
		return hotelDao.findAll();
	}


	/**
	 * 增加
	 * @param hotel
	 */
	public void add(Hotel hotel) {
		hotel.setId( idWorker.nextId()+"" );
		//取到缓存中的文件url
		String fileurl = (String) redisTemplate.opsForValue().get("fileurl");

		hotel.setImg(constantConfig.getUrlPrefix()+fileurl);
		hotelDao.save(hotel);
	}


	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Hotel> findSearch(Map whereMap, int page, int size) {
		Specification<Hotel> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return hotelDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Hotel> findSearch(Map whereMap) {
		Specification<Hotel> specification = createSpecification(whereMap);
		return hotelDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Hotel findById(String id) {
		return hotelDao.findById(id).get();
	}





	/**
	 * 修改
	 * @param hotel
	 */
	public void update(Hotel hotel) {
		hotelDao.save(hotel);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		hotelDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Hotel> createSpecification(Map searchMap) {

		return new Specification<Hotel>() {

			@Override
			public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 图片
                if (searchMap.get("img")!=null && !"".equals(searchMap.get("img"))) {
                	predicateList.add(cb.like(root.get("img").as(String.class), "%"+(String)searchMap.get("img")+"%"));
                }
                // 酒店名称
                if (searchMap.get("name")!=null && !"".equals(searchMap.get("name"))) {
                	predicateList.add(cb.like(root.get("name").as(String.class), "%"+(String)searchMap.get("name")+"%"));
                }
                // 描述
                if (searchMap.get("miaoshu")!=null && !"".equals(searchMap.get("miaoshu"))) {
                	predicateList.add(cb.like(root.get("miaoshu").as(String.class), "%"+(String)searchMap.get("miaoshu")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}


	/**
	 * 根据城市进行模糊查询
	 * @return
	 */
	public List<Hotel> findByCountryLike(String contry){
		return hotelDao.findByAddrLike(contry);
	}

}
