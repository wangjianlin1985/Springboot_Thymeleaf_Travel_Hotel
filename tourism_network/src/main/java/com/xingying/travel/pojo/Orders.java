package com.xingying.travel.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 实体类
 *
 * @author Administrator
 */
@Entity
@Table(name = "orders")
public class Orders implements Serializable {

    @Id

    private String id;//id


    private String scenicid;//商品id
    private String userid;//用户id
    private Integer qty;//数量
    private BigDecimal payment;//总支付金额
    private String status;//状态
    private java.util.Date paytime;//支付时间
    private String phone;//电话
    private String username;
    private String scenicname;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScenicid() {
        return scenicid;
    }

    public void setScenicid(String scenicid) {
        this.scenicid = scenicid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public java.util.Date getPaytime() {
        return paytime;
    }

    public void setPaytime(java.util.Date paytime) {
        this.paytime = paytime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScenicname() {
        return scenicname;
    }

    public void setScenicname(String scenicname) {
        this.scenicname = scenicname;
    }


    @Override
    public String toString() {
        return "Orders{" +
                "id='" + id + '\'' +
                ", scenicid='" + scenicid + '\'' +
                ", userid='" + userid + '\'' +
                ", qty=" + qty +
                ", payment=" + payment +
                ", status='" + status + '\'' +
                ", paytime=" + paytime +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", scenicname='" + scenicname + '\'' +
                '}';
    }
}
