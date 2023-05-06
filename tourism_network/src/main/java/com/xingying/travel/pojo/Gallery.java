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
@Table(name="gallery")
public class Gallery implements Serializable{

	@Id
	private String id;//


	
	private String img;//图片
	private String title;//标题
	private String comment;//内容
	private String city;//城市国家

	
	public String getId() {		
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getImg() {		
		return img;
	}
	public void setImg(String img) {
		this.img = img;
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

	public String getCity() {		
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}


	
}
