package com.xingying.travel.dao;

import com.xingying.travel.pojo.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface OrdersDao extends JpaRepository<Orders,String>,JpaSpecificationExecutor<Orders>{



    @Modifying@Transactional
    @Query(value = "UPDATE orders SET `status`='1' WHERE id = ? ",nativeQuery = true)
    void updateStatus(String id);


    List<Orders> findByUserid(String userId);

}
