package com.xingying.travel.service;

import com.xingying.travel.dao.CarouselDao;
import com.xingying.travel.pojo.Carousel;
import com.xingying.travel.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
public class CarouselService {

	@Autowired
	private CarouselDao carouselDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Carousel> findAll() {
		return carouselDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Carousel> findSearch(Map whereMap, int page, int size) {
		Specification<Carousel> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return carouselDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Carousel> findSearch(Map whereMap) {
		Specification<Carousel> specification = createSpecification(whereMap);
		return carouselDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Carousel findById(String id) {
		return carouselDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param carousel
	 */
	public void add(Carousel carousel) {
		carousel.setId( idWorker.nextId()+"" );
		carouselDao.save(carousel);
	}

	/**
	 * 修改
	 * @param carousel
	 */
	public void update(Carousel carousel) {
		carouselDao.save(carousel);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		carouselDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Carousel> createSpecification(Map searchMap) {

		return new Specification<Carousel>() {

			@Override
			public Predicate toPredicate(Root<Carousel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 标题
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                	predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 内容
                if (searchMap.get("comment")!=null && !"".equals(searchMap.get("comment"))) {
                	predicateList.add(cb.like(root.get("comment").as(String.class), "%"+(String)searchMap.get("comment")+"%"));
                }
                // 时间
                if (searchMap.get("scenictime")!=null && !"".equals(searchMap.get("scenictime"))) {
                	predicateList.add(cb.like(root.get("scenictime").as(String.class), "%"+(String)searchMap.get("scenictime")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
