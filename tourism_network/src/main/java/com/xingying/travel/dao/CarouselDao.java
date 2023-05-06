package com.xingying.travel.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xingying.travel.pojo.Carousel;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface CarouselDao extends JpaRepository<Carousel,String>,JpaSpecificationExecutor<Carousel>{
	
}
