package com.xingying.travel.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="carousel")
public class Carousel implements Serializable{

	@Id
	private String id;//


	
	private String title;//标题
	private String comment;//内容
	private String scenictime;//时间

	
	public String getId() {		
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {		
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {		
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getScenictime() {		
		return scenictime;
	}
	public void setScenictime(String scenictime) {
		this.scenictime = scenictime;
	}


	
}
