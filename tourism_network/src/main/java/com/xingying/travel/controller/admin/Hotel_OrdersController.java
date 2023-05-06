package com.xingying.travel.controller.admin;

import com.xingying.travel.common.Result;
import com.xingying.travel.common.StatusCode;
import com.xingying.travel.pojo.Hotel_orders;
import com.xingying.travel.pojo.Orders;
import com.xingying.travel.pojo.User;
import com.xingying.travel.service.Hotel_ordersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @Author: tz
 * @Date: 2019/3/12 21:42
 */
@Controller
@CrossOrigin
@RequestMapping("/hotelorders")
public class Hotel_OrdersController {


    @Autowired
    private Hotel_ordersService hotel_ordersService;


    /**
     * 增加
     * @param orders
     */
    @ResponseBody
    @RequestMapping(method=RequestMethod.POST)
    public Result add(@RequestBody Hotel_orders orders, HttpSession session){

        //获取数量
        System.out.println("id为--->"+orders.getId());
        System.out.println("数量为--->"+orders.getQty());
        System.out.println("数量为--->"+orders.getBegin());
        System.out.println("数量为--->"+orders.getEnd());
        User user = (User) session.getAttribute("user");
        if (user == null){
            return new Result(false,StatusCode.ACCESSERROR,"请登录");
        }

        return hotel_ordersService.add(orders,user,orders.getId(),orders.getQty(),orders.getBegin(),orders.getEnd());
    }
}
