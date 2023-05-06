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
@Table(name="hotel")
public class Hotel implements Serializable{

	@Id
	private String id;//


	
	private String img;//图片
	private String name;//酒店名称
	private Double price;//价格
	private String miaoshu;//描述
	private Integer bed;//床数
	private Integer day;//天数
	private Integer star;//评论星
	private java.util.Date startdate;//入住时间
	private String addr;   //地址

	
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

	public String getName() {		
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {		
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}

	public String getMiaoshu() {		
		return miaoshu;
	}
	public void setMiaoshu(String miaoshu) {
		this.miaoshu = miaoshu;
	}

	public Integer getBed() {		
		return bed;
	}
	public void setBed(Integer bed) {
		this.bed = bed;
	}

	public Integer getDay() {		
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getStar() {		
		return star;
	}
	public void setStar(Integer star) {
		this.star = star;
	}

	public java.util.Date getStartdate() {		
		return startdate;
	}
	public void setStartdate(java.util.Date startdate) {
		this.startdate = startdate;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}
}
