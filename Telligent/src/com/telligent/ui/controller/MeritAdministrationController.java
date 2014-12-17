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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.telligent.common.handlers.MessageHandler;
import com.telligent.common.user.TelligentUser;
import com.telligent.model.daos.IEmployeeDAO;
import com.telligent.model.daos.IMeritAdministrationDAO;
import com.telligent.model.dtos.BudgetSummaryDTO;
import com.telligent.model.dtos.RatingsAndIncreaseDTO;
import com.telligent.model.dtos.SalarPlanningDTO;
import com.telligent.model.dtos.TeamDTO;
import com.telligent.util.TelligentUtility;

@Controller
public class MeritAdministrationController {
	
	public final Logger logger = Logger.getLogger(MeritAdministrationController.class); 

	@Autowired
	MessageHandler messageHandler;
	
	@Autowired
	TelligentUtility telligentUtility;
	
	private IEmployeeDAO employeeDAO;
	
	private IMeritAdministrationDAO meritAdministrationDAO;
	
	@Autowired
	public MeritAdministrationController(IMeritAdministrationDAO meritAdministrationDAO,IEmployeeDAO employeeDAO) {
		this.meritAdministrationDAO = meritAdministrationDAO;
		this.employeeDAO = employeeDAO;
	}
	public MeritAdministrationController() {
	}
	
	@RequestMapping(value="/meritAdministration.htm", method = RequestMethod.GET)
	public ModelAndView showMeritAdministration(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		logger.info("In showMeritAdministration");
		TelligentUser user = telligentUtility.getTelligentUser();
		ArrayList<TeamDTO> teamList = employeeDAO.getEmployeeTeams(user.getEmployeeId());
		mav.addObject("teams", teamList);
		if(teamList.size() > 0){
			mav.addObject("teamName",teamList.get(0).getTeamName());
			mav.addObject("teamId",teamList.get(0).getTeamId());
		}else{
			mav.addObject("teamName","");
		}
		mav.addObject("gridViewMap", meritAdministrationDAO.getSalaryPlanningGridView(user.getEmployeeId()));
		mav.addObject("salaryPositionRangeDetails", meritAdministrationDAO.salaryPositionRangeDetails());
		mav.addObject("salaryPlanning", new SalarPlanningDTO());
		mav.setViewName("meritAdministration");
		return mav;
	}
	
	@RequestMapping(value="/getSalaryPlanningDetails.htm", method = RequestMethod.GET)
	public @ResponseBody JSONArray getSalaryPlanningDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,
											@RequestParam("teamName") String teamName,@RequestParam("teamId") String teamId){
		logger.info("In getSalaryPlanningDetails");
		TelligentUser user = telligentUtility.getTelligentUser();
		ArrayList<SalarPlanningDTO> salaryPlanningDetails = meritAdministrationDAO.getSalaryPlanningDetails(req.getParameter("sort")!=null ? req.getParameter("sort").toString():"" ,req.getParameter("order")!=null ? req.getParameter("order").toString():"",teamName,teamId,user.getEmployeeId());
		JSONArray obj = (JSONArray) JSONSerializer.toJSON(salaryPlanningDetails);
		return obj;
	}
    @RequestMapping(value="/updateEmployeeDetails.htm", method = RequestMethod.POST)
	public @ResponseBody String updateEmployeeDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,
					@RequestBody JSONArray list){
		logger.info("In updateEmployeeDetails");
		boolean flag =  meritAdministrationDAO.updateEmployeeDetails(list);
		if(flag)
			return messageHandler.getMessage("label.employeeSaveSuccess");
		else
			return messageHandler.getMessage("label.employeeSaveError");
	}
    
    @RequestMapping(value="/anualBudgetSummary.htm", method = RequestMethod.GET)
	public @ResponseBody JSONArray budgetSummary(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,
						@RequestParam("teamName") String teamName,@RequestParam("teamId") String teamId){
		logger.info("in budgetSummary");
		TelligentUser user = telligentUtility.getTelligentUser();
		String supervisorId = req.getParameter("supervisorId");
		ArrayList<BudgetSummaryDTO> budgetSummaryDetails =  meritAdministrationDAO.budgetSummary(supervisorId,teamId,teamName,user.getEmployeeId());
		JSONArray obj = (JSONArray) JSONSerializer.toJSON(budgetSummaryDetails);
		return obj;
	}
    @RequestMapping(value="/ratingsAndIncreaseSummary.htm", method = RequestMethod.GET)
   	public @ResponseBody JSONArray ratingsAndIncreaseSummary(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,
   			@RequestParam("teamName") String teamName,@RequestParam("teamId") String teamId){
   		logger.info("in ratingsAndIncreaseSummary");
   		TelligentUser user = telligentUtility.getTelligentUser();
   		ArrayList<RatingsAndIncreaseDTO> details =  meritAdministrationDAO.ratingsAndIncreaseSummary(teamId,teamName,user.getEmployeeId());
   		JSONArray obj = (JSONArray) JSONSerializer.toJSON(details);
   		return obj;
   	}
    @RequestMapping(value="/updateSalaryPlanningColumnWidth.htm", method = RequestMethod.GET)
   	public @ResponseBody void updateSalaryPlanningColumnWidth(@RequestParam("field") String field,@RequestParam("width") String width){
   		logger.info("in updateSalaryPlanningColumnWidth");
   		TelligentUser user = telligentUtility.getTelligentUser();
   		meritAdministrationDAO.updateSalaryPlanningColumnWidth(field,width,user.getEmployeeId());
   	}
}
