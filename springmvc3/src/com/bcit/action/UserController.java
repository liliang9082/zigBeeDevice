package com.bcit.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bcit.entity.Users;
import com.bcit.service.UserService;

@Controller
public class UserController {

	@Resource
	private UserService userService;
	
	@RequestMapping("index.html")
	public ModelAndView index(){
		ModelAndView mv = new ModelAndView("index");
		return mv;
	}
	
	@RequestMapping("login.html")
	public ModelAndView login(String uname1,String password){
		ModelAndView mv = new ModelAndView();
		Users u = new Users();u.setUname(uname1);u.setPassword(password);
		Users ru = userService.queryOne(u);
		if(ru != null){
			mv.addObject("msg", "欢迎用户：" + uname1 +"进入系统！");
			mv.setViewName("welcome");
		}else{
			mv.addObject("error", "用户或密码错误！");
			mv.setViewName("index");
		}
		return mv;
	}
}
