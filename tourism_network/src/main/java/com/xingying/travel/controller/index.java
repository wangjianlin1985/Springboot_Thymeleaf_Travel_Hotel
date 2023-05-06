package com.xingying.travel.controller;

import com.xingying.travel.dao.HotelDao;
import com.xingying.travel.dao.ScenicDao;
import com.xingying.travel.dao.UserDao;
import com.xingying.travel.pojo.Hotel;
import com.xingying.travel.pojo.Scenic;
import com.xingying.travel.pojo.User;
import com.xingying.travel.service.CarouselService;
import com.xingying.travel.service.GalleryService;
import com.xingying.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: 跳转页面
 * @author: 陈宏松
 * @create: 2019-01-20 16:02
 * @version: 1.0.0
 **/

@Controller
@RequestMapping(value = "/dist")
public class index {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private ScenicDao scenicDao;

    @Autowired
    private UserService userService;

    @Autowired
    private HotelDao hotelDao;

    @RequestMapping(value = "/view")
    public String view(Model model){

        model.addAttribute("carousels",carouselService.findAll());

        return "page/index";
    }

    @RequestMapping(value = "/login")
    public String login(){
        return "user/login";
    }

    @RequestMapping(value = "/register")
    public String register(){
        return "user/register";
    }


    @RequestMapping(value = "/travel")
    public String page_attrs(Model model,@RequestParam(value = "start" ,defaultValue = "0")Integer start,
                             @RequestParam(value = "limit" ,defaultValue = "6")Integer limit,
                             @RequestParam(value = "price" ,defaultValue = "1")Integer price,
                             @RequestParam(value = "star" ,defaultValue = "1")Integer star){

        start=start<0?0:start;
        System.out.println(price+"==================="+star);
        List<Sort.Order> sort=new ArrayList<>();
        if (star==3){
            System.out.println("price asc star asc");
            sort.add(new Sort.Order(Sort.Direction.ASC,"start"));
        }else if (star==2){
            System.out.println("price desc star desc");
            sort.add(new Sort.Order(Sort.Direction.DESC,"start"));
        }else if (price==2){
            System.out.println("price dsc star asc");
            sort.add(new Sort.Order(Sort.Direction.DESC,"price"));
        }else if (price==3 ){
            System.out.println("price asc star desc");
            sort.add(new Sort.Order(Sort.Direction.ASC,"price"));
        }
        Pageable pageable=PageRequest.of(start,limit,Sort.by(sort));
        Page<Scenic> page=scenicDao.findAll(pageable);
        System.out.println(pageable.getSort()+"排序结果："+page.toString());
        model.addAttribute("attrs",page);
        model.addAttribute("number",page.getNumber());
        model.addAttribute("numberOfElements",page.getNumberOfElements());
        model.addAttribute("size",page.getSize());
        model.addAttribute("totalElements",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("first",page.isFirst());
        model.addAttribute("last",page.isLast());
        model.addAttribute("price",price);
        model.addAttribute("star",star);
        return "page/travel";
    }


    @RequestMapping(value = "/hotels")
    public String hotels(Model model,@RequestParam(value = "start" ,defaultValue = "0")Integer start,
                         @RequestParam(value = "limit" ,defaultValue = "6")Integer limit,
                         @RequestParam(value = "price" ,defaultValue = "1")Integer price,
                         @RequestParam(value = "star" ,defaultValue = "1")Integer star){

        start=start<0?0:start;
        List<Sort.Order> sort=new ArrayList<>();
        if (star==3){
            System.out.println("price asc star asc");
            sort.add(new Sort.Order(Sort.Direction.ASC,"star"));
        }else if (star==2){
            System.out.println("price desc star desc");
            sort.add(new Sort.Order(Sort.Direction.DESC,"star"));
        }else if (price==2){
            System.out.println("price dsc star asc");
            sort.add(new Sort.Order(Sort.Direction.DESC,"price"));
        }else if (price==3 ){
            System.out.println("price asc star desc");
            sort.add(new Sort.Order(Sort.Direction.ASC,"price"));
        }
        Pageable pageable=PageRequest.of(start,limit,Sort.by(sort));
        Page<Hotel> page=hotelDao.findAll(pageable);
        System.out.println("排序结果："+page);
        model.addAttribute("attrs",page);
        model.addAttribute("number",page.getNumber());
        model.addAttribute("numberOfElements",page.getNumberOfElements());
        model.addAttribute("size",page.getSize());
        model.addAttribute("totalElements",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("first",page.isFirst());
        model.addAttribute("last",page.isLast());
        return "page/hotels";
    }

    @RequestMapping(value = "/gallery")
    public String gallery(Model model){
        model.addAttribute("gallerys",galleryService.findAll());
        return "page/gallery";
    }

    @RequestMapping(value = "/contacts")
    public String contacts()
    {
        return "page/contacts";
    }

    @RequestMapping(value = "/about")
    public String about()
    {
        return "page/about";
    }

    @RequestMapping(value = "/product")
    public String product()
    {
        return "page/product";
    }

    @RequestMapping(value = "/forget")
    public String forget()
    {
        return "user/forget";
    }

    @RequestMapping(value = "/orders")
    public String orders(){
        return "/user/orders";
    }



    @RequestMapping(value = "/info",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public User info(HttpSession session,Model model){
        User u= (User) session.getAttribute("user");
        User user=userService.findById(u.getId());
        return user;
    }



}
