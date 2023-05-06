package com.xingying.travel.controller.admin;

import com.xingying.travel.common.PageResult;
import com.xingying.travel.common.Result;
import com.xingying.travel.common.StatusCode;
import com.xingying.travel.pojo.Admin;
import com.xingying.travel.pojo.User;
import com.xingying.travel.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * 控制器层
 * @author Administrator
 *
 */
@Controller
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@Autowired
	BCryptPasswordEncoder encoder;

	/**
	 * 查询全部数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true, StatusCode.OK,"查询成功",adminService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",adminService.findById(id));
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
		Page<Admin> pageList = adminService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Admin>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
	@ResponseBody
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",adminService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param admin
	 */
	@ResponseBody
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Admin admin  ){
		adminService.add(admin);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param admin
	 */
	@ResponseBody
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Admin admin, @PathVariable String id ){
		admin.setId(id);
		adminService.update(admin);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@ResponseBody
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		adminService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 管理员跳转
	 * @return
	 */
	@RequestMapping(value = "/adminlogin")
	public String adminlogin()
	{
		return "admin/login/adminlogin";
	}


	/**
	 *  admin登录
	 * @param loginMap
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/login",method= RequestMethod.POST)
	public Result login(@RequestBody Map<String,String> loginMap,HttpServletRequest request){

		String name = loginMap.get("name");
		System.out.println("打印name：--->"+name);


		Admin admin = adminService.finbyNameAndPassword(loginMap.get("name"),loginMap.get("password"));
		if (admin!=null){

			request.getSession().setAttribute("admin",admin);
			Map map=new HashMap();
			map.put("name",admin.getName());
			return new Result(true,StatusCode.OK,"登录成功");
		}else {
			return new Result(false,StatusCode.ERROR,"账号密码错误");
		}
	}

	/**
	 * 管理员登录成功
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String success(){
		return "admin/index";
	}


	/**
	 * 用户列表
	 * @return
	 */
	@RequestMapping(value = "/userList")
	public String user(){
		return "admin/usermanage/userList";
	}

	@RequestMapping(value = "/echars")
	public String analysis(){
		return "admin/echars/console";
	}


	/**
	 * 管理员退出登录
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpSession session){
		session.removeAttribute("admin");
		return "admin/login/adminlogin";
	}


	/**
	 * 管理员修改密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/passwd")
	public Result passwd(HttpSession session,String passwd,String oldpad){

		Admin admindmin= (Admin) session.getAttribute("admin");
		Admin admins=adminService.findById(admindmin.getId());
		boolean old=encoder.matches(oldpad,admins.getPassword());
		if (old){
			String newPassd=encoder.encode(passwd);
			admins.setPassword(newPassd);
			adminService.update(admins);
			return new Result(true,StatusCode.OK,"成功");
		}else {
			return new Result(false,StatusCode.ERROR,"更新失败");
		}
	}
}
