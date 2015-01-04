package com.telligent.ui.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.telligent.common.handlers.MessageHandler;
import com.telligent.common.user.TelligentUser;
import com.telligent.model.daos.impl.EmployeeDAO;
import com.telligent.model.dtos.EmployeeDTO;
import com.telligent.model.dtos.MapDTO;
import com.telligent.model.dtos.TeamDTO;
import com.telligent.util.TelligentUtility;

@Controller
public class EmployeeController {

	
	public final Logger logger = Logger.getLogger(MeritAdministrationController.class); 

	@Autowired
	MessageHandler messageHandler;
	
	@Autowired
	TelligentUtility telligentUtility;
	
	private EmployeeDAO employeeDAO;
	
	@Autowired
	public EmployeeController(EmployeeDAO employeeDAO) {
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
	public ModelAndView saveEmployeeDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@ModelAttribute(value="employee") EmployeeDTO employeeDTO){
		logger.info("In saveEmployeeDetails");
		employeeDTO.setSuccessMessage("");
		employeeDTO.setErrorMessage("");
		employeeDTO = employeeDAO.saveEmployeeDetails(employeeDTO,telligentUtility.getTelligentUser());
		if(employeeDTO.getSuccessMessage()!=null && !employeeDTO.getSuccessMessage().equalsIgnoreCase(""))
			mav.setViewName("redirect:saveEmployeeDetails.htm?empId="+employeeDTO.getEmployeeId());
		else{
			mav.setViewName("employee");
			mav.addObject("employee", employeeDTO);
		}
		return mav;
	}
	
	@RequestMapping(value="/employee.htm", method = RequestMethod.GET)
	public ModelAndView showEmployeeScreen(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		logger.info("in showEmployeeScreen");
		mav.addObject("employee", new EmployeeDTO());
		mav.setViewName("employee");
		return mav;
	}
	@RequestMapping(value="/employee.htm", method = RequestMethod.POST)
	public ModelAndView showEmployeeScreenPost(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		logger.info("in showEmployeeScreen");
		mav.addObject("employee", new EmployeeDTO());
		mav.setViewName("employee");
		return mav;
	}
	@RequestMapping(value="/saveEmployeeDetails.htm", method = RequestMethod.GET)
	public ModelAndView saveEmployeeDetailsGet(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		logger.info("in showEmployeeScreen");
		EmployeeDTO dto = new EmployeeDTO();
		dto.setEmployeeId(req.getParameter("empId"));
		dto.setSuccessMessage("success");
		mav.addObject("employee",dto);
		mav.setViewName("employee");
		return mav;
	}
	@RequestMapping(value="/searchLastName.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray searchLastName(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		ArrayList<MapDTO> list = employeeDAO.searchList(null, req.getParameter("q"), null);
		JSONArray obj = (JSONArray) JSONSerializer.toJSON(list);
		return obj;
	}
	@RequestMapping(value="/searchFirstName.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray searchFirstName(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		ArrayList<MapDTO> list = employeeDAO.searchList(req.getParameter("q"), null, null);
		JSONArray obj = (JSONArray) JSONSerializer.toJSON(list);
		return obj;
	}
	@RequestMapping(value="/searchEmpId.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray searchEmpId(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		ArrayList<MapDTO> list = employeeDAO.searchList(null, null, req.getParameter("q"));
		JSONArray obj = (JSONArray) JSONSerializer.toJSON(list);
		return obj;
	}
	@RequestMapping(value="/searchTeamEmployees.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray searchTeamEmployees(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		ArrayList<MapDTO> list = employeeDAO.searchTeamEmployees(req.getParameter("q"));
		JSONArray obj = (JSONArray) JSONSerializer.toJSON(list);
		return obj;
	}
	@RequestMapping(value="/getEmployeeDetails.htm", method = RequestMethod.POST)
	public @ResponseBody JSONObject getEmployeeDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@RequestParam("empId") String empId){
		EmployeeDTO dto = employeeDAO.getEmployeeDetails(empId);
		dto.setOperation("edit");
		return (JSONObject) JSONSerializer.toJSON(dto);
	}
}
