package com.xingying.travel.service;

import com.xingying.travel.config.ConstantConfig;
import com.xingying.travel.dao.GalleryDao;
import com.xingying.travel.pojo.Gallery;
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
public class GalleryService {

	@Autowired
	private GalleryDao galleryDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
    private ConstantConfig constantConfig;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Gallery> findAll() {
		return galleryDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Gallery> findSearch(Map whereMap, int page, int size) {
		Specification<Gallery> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return galleryDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Gallery> findSearch(Map whereMap) {
		Specification<Gallery> specification = createSpecification(whereMap);
		return galleryDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Gallery findById(String id) {
		return galleryDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param gallery
	 */
	public void add(Gallery gallery) {
		gallery.setId( idWorker.nextId()+"" );

		//取到缓存中的文件url
		String fileurl = (String) redisTemplate.opsForValue().get("gallery");

		gallery.setImg( constantConfig.getUrlPrefix() +fileurl);

		galleryDao.save(gallery);
	}

	/**
	 * 修改
	 * @param gallery
	 */
	public void update(Gallery gallery) {
		galleryDao.save(gallery);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		galleryDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Gallery> createSpecification(Map searchMap) {

		return new Specification<Gallery>() {

			@Override
			public Predicate toPredicate(Root<Gallery> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 图片
                if (searchMap.get("img")!=null && !"".equals(searchMap.get("img"))) {
                	predicateList.add(cb.like(root.get("img").as(String.class), "%"+(String)searchMap.get("img")+"%"));
                }
                // 标题
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                	predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 内容
                if (searchMap.get("comment")!=null && !"".equals(searchMap.get("comment"))) {
                	predicateList.add(cb.like(root.get("comment").as(String.class), "%"+(String)searchMap.get("comment")+"%"));
                }
                // 城市国家
                if (searchMap.get("city")!=null && !"".equals(searchMap.get("city"))) {
                	predicateList.add(cb.like(root.get("city").as(String.class), "%"+(String)searchMap.get("city")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
