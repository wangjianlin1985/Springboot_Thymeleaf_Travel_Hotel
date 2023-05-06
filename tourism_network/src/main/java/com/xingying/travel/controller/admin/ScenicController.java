package com.xingying.travel.controller.admin;

import com.xingying.travel.common.PageResult;
import com.xingying.travel.common.Result;
import com.xingying.travel.common.StatusCode;
import com.xingying.travel.pojo.Scenic;
import com.xingying.travel.service.ScenicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


/**
 * 控制器层
 * @author Administrator
 *
 */
@Controller
@CrossOrigin
@RequestMapping("/scenic")
public class ScenicController {

	@Autowired
	private ScenicService scenicService;


	/**
	 * 查询全部数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true, StatusCode.OK,"查询成功",scenicService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",scenicService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@ResponseBody
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Scenic> pageList = scenicService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Scenic>(pageList.getTotalElements(), pageList.getContent()) );
	}


	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
	@ResponseBody
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",scenicService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param scenic
	 */
	@ResponseBody
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Scenic scenic){
		scenicService.add(scenic);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param scenic
	 */
	@ResponseBody
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Scenic scenic, @PathVariable String id ){
		scenic.setId(id);
		scenicService.update(scenic);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@ResponseBody
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		scenicService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	@RequestMapping(value = "/scenicList")
	public String scenicList(){
		return "admin/scenicmanage/scenicList";
	}

	@RequestMapping(value = "/scenicAdd")
	public String scenicAdd(){
		return "admin/scenicmanage/scenicAdd";
	}

}
