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
import com.metro.model.User;
import com.metro.model.UserExample;
import com.metro.service.EmployeeService;
import com.metro.service.UserService;
import com.metro.util.BeanUtils;
import com.metro.vo.DataTransObj;
import com.metro.vo.EmployeeVO;
import com.metro.vo.UserVO;


@Controller
@RequestMapping(value="/")
public class LoginController  extends BaseController{
	
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private UserService userService;
	

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
	/**
	 * 跳转到员工登录页面
	 * 
	 */
	@RequestMapping(value = "tologinU", method = RequestMethod.GET)
	public String toLogin() {
		return "login.html";
	}
	
	/**
	 * 跳转到员工登录页面
	 * 
	 */
	@RequestMapping(value = "tologinM", method = RequestMethod.GET)
	public String toMngLogin() {
		return "syslogin.html";
	}
	
	/**
	 * 管理员登录验证
	 * 
	 * @param username
	 * @param password
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "sysLogin", method = RequestMethod.POST)
	public @ResponseBody DataTransObj sysLogin(String username,
			String password, HttpServletRequest request) {

		HttpSession session = request.getSession();
		username = StringUtils.trim(username);
		password = StringUtils.trim(password);

		UserExample example = new UserExample();
		UserExample.Criteria c = example.createCriteria();
		c.andUserNameEqualTo(username);
		c.andPassWordEqualTo(password);
		List<User> user = userService.selectByExample(example);
		if (user != null && user.size() > 0) {
			session.setAttribute(Constant.SESSION_LOGIN_MANAGER, user);
		} else {
			return DataTransObj.onFailure(null, "用户名或密码错误");
		}
		UserVO userVO = BeanUtils.transferB(user.get(0),UserVO.class);
		return DataTransObj.onSuccess(userVO, "登录成功");
	}

	/**
	 * 普通用户登录
	 * 
	 * @param username
	 * @param password
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public @ResponseBody DataTransObj login(String realName,
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
		if (user != null && user.size() > 0) {
			session.setAttribute(Constant.SESSION_LOGIN_USER, user);
		} else {
			return DataTransObj.onFailure(null, "姓名和身份证不匹配");
		}
		EmployeeVO employeeVO = BeanUtils.transferB(user.get(0),EmployeeVO.class);
		return DataTransObj.onSuccess(employeeVO, "登录成功");
	}
	
	
	/**
	 * 用户退出
	 * 
	 * @return 跳转到登录页面
	 */
	@RequestMapping("logout.u")
	public String logoutU(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/tologinU";
	}
	
	/**
	 * 管理严退出
	 * 
	 * @return 跳转到登录页面
	 */
	@RequestMapping("logout.m")
	public String logoutM(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/tologinM";
	}
	
}
