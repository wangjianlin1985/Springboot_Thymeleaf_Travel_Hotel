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
@Table(name="user")
public class User implements Serializable{

	@Id
	private String id;//id


	
	private String name;//姓名
	private String password;//密码
	private String mobile;//电话
	private String email;//邮箱
	private String sex;//性别，男1，女0
	private Integer age;//年龄
	private String salt;//盐

	
	public String getId() {		
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {		
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {		
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {		
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {		
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSalt() {		
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}


	
}
