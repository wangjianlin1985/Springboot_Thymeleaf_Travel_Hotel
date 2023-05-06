package com.xingying.travel.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="scenic")
public class Scenic implements Serializable{

	@Id
	private String id;//id


	
	private String img;//景点图片
	private String name;//景点名称
	private Double price;//价格
	private String comment;//详情
	private Integer start;//评价
	private String contry;//国家地区
	private Integer stock;//库存
    private String miaoshu;//描述
	private Date  startdate; //出发时间

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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getContry() {
		return contry;
	}

	public void setContry(String contry) {
		this.contry = contry;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getMiaoshu() {
		return miaoshu;
	}

	public void setMiaoshu(String miaoshu) {
		this.miaoshu = miaoshu;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}


	@Override
	public String toString() {
		return "Scenic{" +
				"id='" + id + '\'' +
				", img='" + img + '\'' +
				", name='" + name + '\'' +
				", price=" + price +
				", comment='" + comment + '\'' +
				", start=" + start +
				", contry='" + contry + '\'' +
				", stock=" + stock +
				", miaoshu='" + miaoshu + '\'' +
				", startdate=" + startdate +
				'}';
	}
}
