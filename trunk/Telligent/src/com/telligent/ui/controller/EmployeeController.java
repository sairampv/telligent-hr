package com.telligent.ui.controller;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.telligent.model.dtos.EmployeeCompensationDTO;
import com.telligent.model.dtos.EmployeeDTO;
import com.telligent.model.dtos.EmployeeOtherDTO;
import com.telligent.model.dtos.EmployeePositionDTO;
import com.telligent.model.dtos.EmploymentDTO;
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
		mav.addObject("stateList",employeeDAO.getStateDetails());
		mav.setViewName("employee");
		return mav;
	}
	@RequestMapping(value="/employee.htm", method = RequestMethod.POST)
	public ModelAndView showEmployeeScreenPost(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		logger.info("in showEmployeeScreen");
		mav.addObject("employee", new EmployeeDTO());
		mav.addObject("stateList",employeeDAO.getStateDetails());
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
	@RequestMapping(value="/showEmployeeDetails.htm", method = RequestMethod.POST)
	public ModelAndView getEmployeeDetails1(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,EmployeeDTO dto){
		dto = employeeDAO.getEmployeeDetails(dto.getEmployeeId());
		mav.addObject("employee", dto);
		mav.addObject("stateList",employeeDAO.getStateDetails());
		mav.setViewName("employee");
		return mav;
	}
	@RequestMapping(value="/getEmployeeDetailsHistory.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray getEmployeeDetailsHistory(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@RequestParam("empId") String empId){
		return (JSONArray) JSONSerializer.toJSON(employeeDAO.getEmployeeDetailsHistory(empId));
	}
	@RequestMapping(value="/getEmployeeDetailsFromHistoryAjax.htm", method = RequestMethod.POST)
	public @ResponseBody JSONObject getEmployeeDetailsFromHistoryAjax(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@RequestParam("seqNo") String seqNo){
		EmployeeDTO dto = employeeDAO.getEmployeeDetailsFromHistory(seqNo);
		return (JSONObject) JSONSerializer.toJSON(dto);
	}
	@RequestMapping(value="/getStateListAjax.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray getStateListAjax(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		return (JSONArray) JSONSerializer.toJSON(employeeDAO.getStateDetails());
	}
	@RequestMapping(value="/getCityDetails.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray getCityDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@RequestParam("stateId") String stateId){
		return (JSONArray) JSONSerializer.toJSON(employeeDAO.getCityDetails(stateId));
	}
	@RequestMapping(value="/empCompensationPage.htm", method = RequestMethod.POST)
	public ModelAndView showEmpCompensationScreen(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,EmployeeCompensationDTO dto){
		logger.info("in showEmpCompensationScreen");
		EmployeeDTO dto2 = new EmployeeDTO();
		if(dto.getEmployeeId()!=null && ! dto.getEmployeeId().equalsIgnoreCase("")){
			dto2 = employeeDAO.getEmployeeDetails(dto.getEmployeeId());
			dto.setLastName(dto2.getLastName());
			dto.setFirstName(dto2.getFirstName());
			dto.setMiddleName(dto2.getMiddleName());
		}
		mav.addObject("employeeComp", dto);
		HashMap<String, ArrayList<MapDTO>> map = employeeDAO.getEmpCompensationLookup();
		mav.addObject("baseRateFreqList",map.get("Base_Rate_Frequency"));
		mav.addObject("bonusPlanList",map.get("Bonus_Plan"));
		mav.addObject("compActionList",map.get("Compensation_Action"));
		mav.addObject("compActionReasonList",map.get("Compensation_Action_Reason"));
		mav.addObject("defaultEarningCodeList",map.get("Default_Earning_Code"));
		mav.addObject("defaultHoursFreqList",map.get("Default_Hours_Frequency"));
		mav.addObject("gradeList",map.get("Grade"));
		mav.addObject("jobGroupList",map.get("Job_Group"));
		mav.addObject("payEntityList",map.get("pay_entity"));
		mav.addObject("payFreqList",map.get("pay_frequency"));
		mav.addObject("payGroupList",map.get("pay_group"));
		mav.addObject("perfPlanList",map.get("Performance_Plan"));
		mav.setViewName("employeeCompensation");
		return mav;
	}
	
	@RequestMapping(value="/saveEmployeeCompDetails.htm", method = RequestMethod.POST)
	public @ResponseBody String saveEmployeeCompDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@ModelAttribute(value="employeeComp") EmployeeCompensationDTO employeeCompensationDTO){
		logger.info("In saveEmployeeCompDetails");
		return "success";
	}

	@RequestMapping(value="/empEmployementPage.htm", method = RequestMethod.POST)
	public ModelAndView showEmpEmployementScreen(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,EmploymentDTO dto){
		logger.info("in showEmpEmployementScreen");
		EmployeeDTO dto2 = new EmployeeDTO();
		if(dto.getEmployeeId()!=null && ! dto.getEmployeeId().equalsIgnoreCase("")){
			dto2 = employeeDAO.getEmployeeDetails(dto.getEmployeeId());
			dto.setLastName(dto2.getLastName());
			dto.setFirstName(dto2.getFirstName());
			dto.setMiddleName(dto2.getMiddleName());
		}
		mav.addObject("employment", dto);
		HashMap<String, ArrayList<MapDTO>> map = employeeDAO.getEmpEmployementLookup();
		mav.addObject("statusCodeList",map.get("Status_Code"));
		mav.addObject("statusReasonList",map.get("Status_Reason"));
		mav.addObject("statusList",map.get("Status"));
		mav.addObject("FLSACategoryList",map.get("FLSA_Category"));
		mav.addObject("classificationList",map.get("Classification"));
		mav.addObject("employementCategoryList",map.get("Employement_Category"));
		mav.addObject("fullTimeEquivalencyList",map.get("Full_Time_Equivalency"));
		mav.addObject("leaveStatusCodeList",map.get("Leave_Status_code"));
		mav.addObject("leaveStatusReasonList",map.get("Leave_Status_Reason"));
		mav.setViewName("employement");
		return mav;
	}
	@RequestMapping(value="/saveEmployeeEmployement.htm", method = RequestMethod.POST)
	public @ResponseBody String saveEmployeeEmployement(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@ModelAttribute(value="employement") EmploymentDTO employmentDTO){
		logger.info("In saveEmployeeEmployement");
		return "success";
	}
	@RequestMapping(value="/empPosition.htm", method = RequestMethod.POST)
	public ModelAndView showEmpPositionScreen(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,EmployeePositionDTO dto){
		logger.info("in showEmpPositionScreen");
		EmployeeDTO dto2 = new EmployeeDTO();
		if(dto.getEmployeeId()!=null && ! dto.getEmployeeId().equalsIgnoreCase("")){
			dto2 = employeeDAO.getEmployeeDetails(dto.getEmployeeId());
			dto.setLastName(dto2.getLastName());
			dto.setFirstName(dto2.getFirstName());
			dto.setMiddleName(dto2.getMiddleName());
		}
		HashMap<String, ArrayList<MapDTO>> map = employeeDAO.getEmpPositionLookup();
		mav.addObject("statusCodeList",map.get("Status_Code"));
		mav.addObject("statusReasonList",map.get("Status_Reason"));
		mav.addObject("employeePosition", dto);
		mav.setViewName("employeePosition");
		return mav;
	}
	@RequestMapping(value="/saveEmployeePosition.htm", method = RequestMethod.POST)
	public @ResponseBody String saveEmployeePosition(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@ModelAttribute(value="employeePosition") EmployeePositionDTO employeePositionDTO){
		logger.info("In saveEmployeePosition");
		return "success";
	}
	
	@RequestMapping(value="/empOtherData.htm", method = RequestMethod.POST)
	public ModelAndView showEmpOtherDataScreen(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,EmployeeOtherDTO dto){
		logger.info("in showEmpOtherDataScreen");
		//dto.setEmployeeId(dto.getEmployeeId());
		EmployeeDTO dto2 = new EmployeeDTO();
		if(dto.getEmployeeId()!=null && ! dto.getEmployeeId().equalsIgnoreCase("")){
			dto2 = employeeDAO.getEmployeeDetails(dto.getEmployeeId());
			dto.setLastName(dto2.getLastName());
			dto.setFirstName(dto2.getFirstName());
			dto.setMiddleName(dto2.getMiddleName());
		}
		HashMap<String, ArrayList<MapDTO>> map = employeeDAO.getEmpOtherLookup();
		mav.addObject("ethinicityList",map.get("Ethinicity"));
		mav.addObject("maritalList",map.get("Marital_Status"));
		mav.addObject("citizenshipList",map.get("Citizenship_Status"));
		mav.addObject("visaTypeList",map.get("VISA_Type"));
		mav.addObject("militaryList",map.get("Military_Status"));
		mav.addObject("veteranList",map.get("Veteran_Status"));
		mav.addObject("employeeOther", dto);
		mav.setViewName("employeeOther");
		return mav;
	}
	
}
