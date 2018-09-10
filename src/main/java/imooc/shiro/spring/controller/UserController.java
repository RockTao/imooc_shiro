package imooc.shiro.spring.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import imooc.shiro.spring.vo.User;

@Controller
public class UserController {
	
	
	/**
	 * 授权的方式：1、注解的方式  2 编程的方式
	 * @param user
	 * @return
	 *	@date:   2018年9月10日 上午10:34:09
	 */
//	2 编程的方式
	@RequestMapping(value="/subLogin",method=RequestMethod.POST,produces = "application/json;charset=utf-8")
	@ResponseBody
	public  String subLogin(User user) {
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
		try {
			token.setRememberMe(user.getRememberMe());
			subject.login(token);
		} catch (Exception e) {
			return e.getMessage();
		}
		if(subject.hasRole("admin")) {
			return "有admin权限";
		}
		
		return "无admin权限";
	}
	
	//1、注解的方式 
//	@RequiresRoles("admin")
	@RequestMapping(value="/testRole",method = RequestMethod.GET)
	@ResponseBody
	public String testRole() {
		return "TestRole Success";
	}
	
//	@RequiresRoles("admin1")
//	@RequiresPermissions("admin1")
	@RequestMapping(value="/testRole1",method = RequestMethod.GET)
	@ResponseBody
	public String testRole1() {
		return "TestRole1 Success   请看下差别";
	}
	
	
	@RequestMapping(value="/testPerms",method = RequestMethod.GET)
	@ResponseBody
	public String testPerms() {
		return "testPerms Success";
	}
	@RequestMapping(value="/testPerms1",method = RequestMethod.GET)
	@ResponseBody
	public String testPerms1() {
		return "testPerms1 Success";
	}
	
}
