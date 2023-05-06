package com.xingying.travel.controller.User;

import com.xingying.travel.common.Result;
import com.xingying.travel.dao.HotelDao;
import com.xingying.travel.dao.ScenicDao;
import com.xingying.travel.pojo.Hotel;
import com.xingying.travel.pojo.Hotel_orders;
import com.xingying.travel.pojo.Scenic;
import com.xingying.travel.service.HotelService;
import com.xingying.travel.service.Hotel_ordersService;
import com.xingying.travel.service.ScenicService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @author:tz
 * @Date:2019
 */
@Controller
@CrossOrigin
@RequestMapping("/travel")
public class RecomTravelController {

    @Autowired
    private ScenicService scenicService;

    @Autowired
    private ScenicDao scenicDao;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelDao hotelDao;

    /**
     * 查询所有景点
     * @return
     */
    @RequestMapping("/test")
    public String all_attrs1(Model model){
        return "page/test.html";
    }


    /**
     *查询星级
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/star",method = RequestMethod.POST)
    public Result judgeStar(String id,String start){
        System.out.println(id+"===="+start);
        Optional<Scenic> s = scenicDao.findById(id);
        if (s.isPresent()){
            Scenic scenic = s.get();
            int valuestar = (Integer.valueOf(start)+Integer.valueOf(scenic.getStart()))/2;
            scenic.setStart(valuestar);
            scenicDao.save(scenic);
            System.out.println("数据不为空！");
            return new Result(true,1,"","");
        }else {
            System.out.println("数据为空！");
            return new Result(false,0,"","");

        }

    }


    /**
     *查询星级
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/hotel",method = RequestMethod.POST)
    public Result judgeHotelStar(String id,String start){
        System.out.println(id+"===="+start);
        Hotel scenic = hotelService.findById(id);
        if (scenic!=null){
            int valuestar = (Integer.valueOf(start)+Integer.valueOf(scenic.getStar()))/2;
            scenic.setStar(valuestar);
            hotelDao.save(scenic);
            System.out.println("数据不为空！");
            return new Result(true,1,"","");
        }else {
            System.out.println("数据为空！");
            return new Result(false,0,"","");

        }

    }

    /**
     * 查询所有景点
     * @return
     */
    @RequestMapping("/AllAttrs")
    public String all_attrs(Model model){
        List<Scenic> attrs=scenicService.findAll();
        System.out.println("进入查询！"+attrs.toString());
        model.addAttribute("attrs",attrs);
        return "page/travel::table_refresh";
    }

    /**
     * 景点分页
     * @param start
     * @param limit
     * @return
     */
    @RequestMapping("/page_attrs")
    public String page_attrs(Model model,@RequestParam(value = "start" ,defaultValue = "0")Integer start,
                                   @RequestParam(value = "limit" ,defaultValue = "6")Integer limit){
        start=start<0?0:start;
        Sort sort=new Sort(Sort.DEFAULT_DIRECTION,"id");
        Pageable pageable=PageRequest.of(start,limit,sort);
        Page<Scenic> page=scenicDao.findAll(pageable);
        model.addAttribute("attrs",page);
        model.addAttribute("number",page.getNumber());
        model.addAttribute("numberOfElements",page.getNumberOfElements());
        model.addAttribute("size",page.getSize());
        model.addAttribute("totalElements",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("first",page.isFirst());
        model.addAttribute("last",page.isLast());
        return "page/travel::table_refresh";
    }


    /**
     * 查询单个景点
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/oneAttr")
    public String One_attr(Model model,String id){
        System.out.println("id为:"+id);
        Scenic scenic=scenicService.findById(id).get();
        System.out.println(scenic.toString());
        model.addAttribute("oneAttr",scenic);
        //return "page/product::table_refresh";
        return "page/product.html";
    }


    /**
     * 景点模糊查询分页
     * @param model
     * @param start
     * @param limit
     * @param search_key
     * @return
     */
    @RequestMapping("/search_attrs")
    public String search_attrs(Model model,@RequestParam(value = "start" ,defaultValue = "0")Integer start,
                             @RequestParam(value = "limit" ,defaultValue = "6")Integer limit,
                               @RequestParam String search_key){
        start=start<0?0:start;
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        Pageable pageable=PageRequest.of(start,limit,sort);
        Specification specification=new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> scenics=new ArrayList<>();
                if (StringUtils.isNotBlank(search_key)){
                    scenics.add( criteriaBuilder.like(root.get("name"),"%"+search_key+"%"));
                }
                return criteriaBuilder.and(scenics.toArray(new Predicate[scenics.size()]));
            }
        };
        Page<Scenic> page=scenicDao.findAll(specification,pageable);

        model.addAttribute("attrs",page);
        model.addAttribute("number",page.getNumber());
        model.addAttribute("numberOfElements",page.getNumberOfElements());
        model.addAttribute("size",page.getSize());
        model.addAttribute("totalElements",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("first",page.isFirst());
        model.addAttribute("last",page.isLast());
        return "page/travel";
    }

    @RequestMapping("/local")
    public String localRefresh(Model model,String id) {
        System.out.println("id::====="+id);
        Scenic scenic=scenicService.findById(id).get();
        //	Sort sort=new Sort(Sort.Direction.DESC,"star");
        System.out.println(scenic.toString());
        List<Hotel> hotels=hotelService.findByCountryLike(scenic.getContry());
        System.out.println("2222"+hotels.toString());
        Collections.sort(hotels, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel o1, Hotel o2) {
                if (o1.getStar()<o2.getStar()){
                    return 2;
                }
                if (o1.getStar().equals(o2.getStar()) ){
                    return 1;
                }
                return -1;
            }
        });
        if (hotels.size()>=4){
            List newList=hotels.subList(0,3);
            model.addAttribute("scenics",newList);
            System.out.println("个数："+newList.size());
        }else {
            model.addAttribute("scenics",hotels);
            System.out.println("个数2："+hotels.size());

        }
        return "page/product::table_refresh";
    }



}
