package com.xingying.travel.controller;

import com.xingying.travel.common.PageResult;
import com.xingying.travel.common.Result;
import com.xingying.travel.common.StatusCode;
import com.xingying.travel.pojo.User;
import com.xingying.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
    BCryptPasswordEncoder encoder;



    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
    }


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@ResponseBody
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		User oldUser=userService.findById(id);
		user.setId(id);
		user.setPassword(oldUser.getPassword());
		userService.update(user);
		return new Result(true,StatusCode.OK,"修改成功");
	}

	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/change",method= RequestMethod.POST,consumes = "application/json")
	public Result update(@RequestBody User user, HttpSession session){
		User us= (User) session.getAttribute("user");
		user.setId(us.getId());
		user.setPassword(us.getPassword());
		userService.update(user);
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	/**
	 * 发送短信验证码
	 *
	 * @return
	 */
	@RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
	public Result sendSms(@PathVariable String mobile) {

		userService.sendSms(mobile);
		return new Result(true, StatusCode.SMS, "验证码已发送至你的手机，请注意查收");
	}


	/**
	 * 发送邮箱验证码
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/sendEmail/{email}", method = RequestMethod.POST)
	public Result sendEmail(@PathVariable String email) {

		userService.sendEmail(email);
		return new Result(true, StatusCode.SMS, "验证码已发送至你的邮箱，请注意查收");
	}


	/**
	 * 用户注册
	 * @param code
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register/{code}",method = RequestMethod.POST)
	public Result regist(@PathVariable String code,@RequestBody User user){
		//得到redis缓存中的验证码
		String checkcodeRedis =(String) redisTemplate.opsForValue().get("checkcode_"+user.getMobile());


		if(checkcodeRedis==null){
			return new Result(false,StatusCode.ERROR,"请获取验证码");
		}
		 if(!checkcodeRedis.equals(code)){
			return new Result(false,StatusCode.ERROR,"验证码错误");
		}
		else {

			userService.add(user);
			return new Result(true,StatusCode.OK,"注册成功");
		}
	}

	/**
	 *判断账号是否存在
	 * @param user
	 * @return
	 */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
	@ResponseBody
    public Result findsame(@RequestBody User user){

		System.out.println("拿到的手机号码"+user.getMobile());
		String phone = user.getMobile();

		User userphone= userService.findByMobile(phone);

		if (userphone == null){
			return new Result(true,StatusCode.OK,"该手机号可以注册");
		}
		return new Result(false,StatusCode.ERROR,"该手机号已经被注册");
	}



	@RequestMapping(value = "/email",method = RequestMethod.POST)
	@ResponseBody
	public Result findemail(@RequestBody User user){

		System.out.println("拿到的email"+user.getEmail());
		String email = user.getEmail();

		User useremail= userService.findByEmail(email);

		if (useremail == null){
			return new Result(true,StatusCode.OK,"该邮箱可以注册");
		}
		return new Result(false,StatusCode.ERROR,"该邮箱已经被注册");
	}





	/**
	 * 用户登录
	 * @param loginMap
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Result login(@RequestBody Map<String,String> loginMap,HttpServletRequest request){
		  String mobile = loginMap.get("login");
		  System.out.println("打印手机号：--->"+mobile);
		  //手机号正则，判断输入是否为手机号
		  String ph = "^[1][3578]\\d{9}$";
		  System.out.println("正则---->"+mobile.matches(ph));
		if (mobile.matches(ph)){
			User user = userService.findByMobileAndPassword(loginMap.get("login"),loginMap.get("password"));
			if(user!=null){
				request.getSession().setAttribute("user",user);
				//String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
				Map map=new HashMap();
				//	map.put("token",token);
				map.put("name",user.getName());//姓名
				return new Result(true,StatusCode.OK,"登陆成功",map);
			}else{
				return new Result(false,StatusCode.LOGINERROR,"用户名或密码错误");
			}
		}else {
			User user =  userService.findByNameAndPassword(loginMap.get("login"),loginMap.get("password"));
			if(user!=null){
				request.getSession().setAttribute("user",user);
				//String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
				Map map=new HashMap();
				//	map.put("token",token);
				map.put("name",user.getName());//姓名
				return new Result(true,StatusCode.OK,"登陆成功",map);

			}else{
				return new Result(false,StatusCode.LOGINERROR,"用户名或密码错误");
			}
		}
	}


	/**
	 *  用户注销
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public void logout(HttpServletRequest request,HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("user", null);
        response.sendRedirect(request.getContextPath() + "/dist/view");

    }

}
