package com.metro.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metro.common.constant.Constant;
import com.metro.model.Employee;
import com.metro.model.EmployeeExample;
import com.metro.service.EmployeeService;


@Controller
@RequestMapping(value="/")
public class LoginController  extends BaseController{
	
	
	@Autowired
	private EmployeeService employeeService;

//	/**
//	 * 管理主页
//	 * 
//	 */
//	@RequestMapping(value="/")
//	public String toIndex(Model model, HttpServletRequest request) {
//		request.getSession().removeAttribute("code"); // 清除code
//		if( SysUserUtils.getLoginUser() == null){
//			return "redirect:/login";
//		}
//		return "WEB-INF/article.html";
//	}
//
//	/**
//	 * 跳转到登录页面
//	 * 
//	 */
//	@RequestMapping(value = "login", method = RequestMethod.GET)
//	public String toLogin() {
//		return "sys/login.html";
//	}
	
//	/**
//	 * 管理员登录验证
//	 * 
//	 * @param username
//	 * @param password
//	 * @param code
//	 * @return
//	 */
//	@RequestMapping(value = "sys/sysLogin", method = RequestMethod.POST)
//	public @ResponseBody Map<String, Object> sysLogin(String username,
//			String password, String code, HttpServletRequest request) {
//
//		Map<String, Object> msg = new HashMap<String, Object>();
//		HttpSession session = request.getSession();
//		code = StringUtils.trim(code);
//		username = StringUtils.trim(username);
//		password = StringUtils.trim(password);
//		Object scode = session.getAttribute("code");
//		String sessionCode = null;
//		if (scode != null)
//			sessionCode = scode.toString();
//		if (!StringUtils.equalsIgnoreCase(code, sessionCode)) {
//			msg.put("error", "验证码错误");
//			return msg;
//		}
//		SysUser user = sysUserService.checkUser(username, password);
//		if (null != user) {
//			session.setAttribute(Constant.SESSION_LOGIN_USER, user);
//		} else {
//			msg.put("error", "用户名或密码错误");
//		}
//		return msg;
//	}
//	
	/**
	 * 普通用户登录
	 * 
	 * @param username
	 * @param password
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(String realName,
			String usercard, HttpServletRequest request) {

		Map<String, Object> msg = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		realName = StringUtils.trim(realName);
		usercard = StringUtils.trim(usercard);

		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria c = example.createCriteria();
		c.andUserCardEqualTo(usercard);
		c.andRealNameEqualTo(realName);
		List<Employee> user = employeeService.selectByExample(example);
		if (null != user) {
			session.setAttribute(Constant.SESSION_LOGIN_USER, user);
		} else {
			msg.put("error", "用户名或密码错误");
		}
		return msg;
	}
	
	
	/**
	 * 用户退出
	 * 
	 * @return 跳转到登录页面
	 */
	@RequestMapping("logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/sys/login";
	}
	
}
