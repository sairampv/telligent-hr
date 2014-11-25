package com.telligent.ui.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.telligent.common.handlers.MessageHandler;
import com.telligent.common.user.TelligentUser;
import com.telligent.model.dtos.User;
import com.telligent.util.TelligentUtility;

@Controller
public class LoginController {

	@Autowired
	MessageHandler messageHandler;
	
	@Autowired
	TelligentUtility telligentUtility;
	
	public final Logger logger = Logger.getLogger(LoginController.class); 
	
	@RequestMapping(value="/login.htm", method = RequestMethod.GET)
	public ModelAndView showLoginForm(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		logger.info("In login form");
		if(req.getParameter("login_error")!=null && req.getParameter("login_error").toString().equalsIgnoreCase("1")){
			mav.addObject("message", messageHandler.getMessage("login.error"));
		}
		mav.setViewName("loginForm");
		return mav;
	}
	
	@RequestMapping(value="/dashboard.htm", method = RequestMethod.GET)
	public ModelAndView showDashBoard(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		logger.info("In showDashboard");
		TelligentUser user = telligentUtility.getTelligentUser();
		if(user.getAuthorities().contains(new GrantedAuthorityImpl("admin")))
			mav.setViewName("admindashboard");
		else if(user.getAuthorities().contains(new GrantedAuthorityImpl("user")))
			mav.setViewName("userdashboard");
		return mav;
	}
	
	@RequestMapping(value="/test.htm", method = RequestMethod.GET)
	public @ResponseBody JSONArray test(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		logger.info("In showDashboard");
		User user = new User();
		ArrayList<User> list = new ArrayList<User>();
		int[] retArr = getStartAndEndNo(Integer.parseInt(req.getParameter("page")), Integer.parseInt(req.getParameter("rows")));
		for(int i=retArr[0];i<=retArr[1];i++){
			user = new User();
			user.setEmployeeId(i+"");
			user.setPassword("test"+i);
			user.setRole("role"+i);
			user.setUserName("username"+i);
			list.add(user);
		}
		JSONArray obj = (JSONArray) JSONSerializer.toJSON(list);
		return obj;
	}
	public int[] getStartAndEndNo(int page,int noOfRecords){
		int[] retArr = new int[2];
		int start = 0,end=0;
		if(page == 1){
			start =1;
			end = noOfRecords;
		}else{
			start = (page*noOfRecords)+1;
			end = (page*noOfRecords)+10;
		}
		retArr[0] = start;
		retArr[1] = end;
		return retArr;
	}
}
