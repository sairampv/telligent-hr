package com.telligent.ui.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.telligent.common.handlers.MessageHandler;
import com.telligent.model.daos.impl.AppUserDAO;
import com.telligent.model.dtos.AppUserDTO;
import com.telligent.model.dtos.AppUserListDTO;
import com.telligent.util.TelligentUtility;

@Controller
public class AppUserController {

	
	public final Logger logger = Logger.getLogger(AppUserController.class); 

	@Autowired
	MessageHandler messageHandler;
	
	@Autowired
	TelligentUtility telligentUtility;
	
	private AppUserDAO appUserDAO;
	
	@Autowired
	public AppUserController(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}
	public AppUserController() {
	}
	
	@RequestMapping(value="/appUser.htm", method = RequestMethod.POST)
	public ModelAndView showAppUser(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		AppUserDTO dto =  new AppUserDTO();
		ArrayList<AppUserListDTO> appUserList = new ArrayList<AppUserListDTO>();
		for(int i=0;i<10;i++){
			appUserList.add(new AppUserListDTO());
		}
		dto.setAppUserList(appUserList);
		mav.addObject("appUser", dto);
		mav.setViewName("appUser");
		return mav;
	}
	@RequestMapping(value="/saveAppUserDetails.htm", method = RequestMethod.POST)
	public @ResponseBody String saveAppUserDetails(@ModelAttribute("appUser") AppUserDTO appUserDTO){
		logger.info("in saveAppUserDetails");
		return null;
	}
	
}
