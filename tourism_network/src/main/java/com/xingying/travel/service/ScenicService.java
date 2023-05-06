package com.xingying.travel.service;

import com.xingying.travel.config.ConstantConfig;
import com.xingying.travel.dao.ScenicDao;
import com.xingying.travel.pojo.Scenic;
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
import java.util.Optional;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class ScenicService {

	@Autowired
	private ScenicDao scenicDao;
	
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
	public List<Scenic> findAll() {
		return scenicDao.findAll();
	}


	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Scenic> findSearch(Map whereMap, int page, int size) {
		Specification<Scenic> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return scenicDao.findAll(specification, pageRequest);
	}


	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Scenic> findSearch(Map whereMap) {
		Specification<Scenic> specification = createSpecification(whereMap);
		return scenicDao.findAll(specification);
	}



	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Optional<Scenic> findById(String id) {
		return scenicDao.findById(id);
	}

	/**
	 * 增加
	 * @param scenic
	 */
	public void add(Scenic scenic) {
		scenic.setId( idWorker.nextId()+"" );
		//取到缓存中的文件url
		String fileurl = (String) redisTemplate.opsForValue().get("fileurl");

        scenic.setImg(constantConfig.getUrlPrefix()+fileurl);
		scenicDao.save(scenic);
	}

	/**
	 * 修改
	 * @param scenic
	 */
	public void update(Scenic scenic) {
		scenicDao.save(scenic);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		scenicDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Scenic> createSpecification(Map searchMap) {

		return new Specification<Scenic>() {

			@Override
			public Predicate toPredicate(Root<Scenic> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // id
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 景点图片
                if (searchMap.get("img")!=null && !"".equals(searchMap.get("img"))) {
                	predicateList.add(cb.like(root.get("img").as(String.class), "%"+(String)searchMap.get("img")+"%"));
                }
                // 景点名称
                if (searchMap.get("name")!=null && !"".equals(searchMap.get("name"))) {
                	predicateList.add(cb.like(root.get("name").as(String.class), "%"+(String)searchMap.get("name")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}


	/**
	 * 根据城市进行模糊查询
	 * @return
	 */
	public List<Scenic> findByCountryLike(String contry){
		return scenicDao.findByContryLike(contry);
	}

}
