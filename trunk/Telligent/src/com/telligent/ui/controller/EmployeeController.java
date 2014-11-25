package com.telligent.ui.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.telligent.common.handlers.MessageHandler;
import com.telligent.common.user.TelligentUser;
import com.telligent.model.daos.IEmployeeDAO;
import com.telligent.model.dtos.EmployeeDTO;
import com.telligent.model.dtos.TeamDTO;
import com.telligent.util.TelligentUtility;

@Controller
public class EmployeeController {

	
	public final Logger logger = Logger.getLogger(MeritAdministrationController.class); 

	@Autowired
	MessageHandler messageHandler;
	
	@Autowired
	TelligentUtility telligentUtility;
	
	private IEmployeeDAO employeeDAO;
	
	@Autowired
	public EmployeeController(IEmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}
	public EmployeeController() {
	}
	
	@RequestMapping(value="/showTeams.htm", method = RequestMethod.GET)
	public ModelAndView showTeams(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		logger.info("In showTeams");
		TelligentUser user = telligentUtility.getTelligentUser();
		ArrayList<TeamDTO> teamList = employeeDAO.getEmployeeTeams(user.getEmployeeId());
		mav.addObject("teams", teamList);
		if(teamList.size() > 0){
			mav.addObject("teamName",teamList.get(0).getTeamName());
			mav.addObject("teamId",teamList.get(0).getTeamId());
			//employeeList = employeeDAO.getTeamEmployees(teamList.get(0).getTeamId());
		}else{
			mav.addObject("teamName","");
		}
		EmployeeDTO dto = new EmployeeDTO();
		dto.setTeamsList(teamList);
		mav.addObject("employeeForm", dto);
		mav.setViewName("showTeams");
		return mav;
	}
	
	@RequestMapping(value="/getTeamEmployees.htm", method = RequestMethod.GET)
	public @ResponseBody JSONArray getTeamEmployees(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@RequestParam("teamName") String teamName,@RequestParam("teamId") String teamId){
		logger.info("In getTeamEmployees");
		ArrayList<EmployeeDTO> employeeList = new ArrayList<EmployeeDTO>();
		mav.addObject("teamName",teamName);
		mav.addObject("teamId",teamId);
		employeeList = employeeDAO.getTeamEmployees(teamId);
		JSONArray obj = (JSONArray) JSONSerializer.toJSON(employeeList);
		return obj;
	}
	
	@RequestMapping(value="/saveEmployeeDetails.htm", method = RequestMethod.POST)
	public @ResponseBody String saveEmployeeDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,EmployeeDTO employeeDTO){
		logger.info("In saveEmployeeDetails");
		boolean flag =  employeeDAO.saveEmployeeDetails(employeeDTO);
		if(flag)
			return messageHandler.getMessage("label.employeeSaveSuccess");
		else
			return messageHandler.getMessage("label.employeeSaveError");
	}
	
}
