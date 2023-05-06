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
@Table(name="admin")
public class Admin implements Serializable{

	@Id
	private String id;//


	
	private String name;//姓名
	private String password;//密码

	
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


	
}
