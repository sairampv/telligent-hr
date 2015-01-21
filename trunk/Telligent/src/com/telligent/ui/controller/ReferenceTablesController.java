package com.telligent.ui.controller;

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
import com.telligent.model.daos.impl.ReferenceTablesDAO;
import com.telligent.model.dtos.EmployeeCompensationDTO;
import com.telligent.model.dtos.MapDTO;
import com.telligent.util.TelligentUtility;

@Controller
public class ReferenceTablesController {

	
	public final Logger logger = Logger.getLogger(ReferenceTablesController.class); 

	@Autowired
	MessageHandler messageHandler;
	
	@Autowired
	TelligentUtility telligentUtility;
	
	private ReferenceTablesDAO referenceTablesDAO;
	
	@Autowired
	public ReferenceTablesController(ReferenceTablesDAO referenceTablesDAO) {
		this.referenceTablesDAO = referenceTablesDAO;
	}
	public ReferenceTablesController() {
	}
	
	@RequestMapping(value="/referenceTables.htm", method = RequestMethod.GET)
	public ModelAndView referenceTables(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		mav.setViewName("baseRateFreq");
		MapDTO dto = new MapDTO();
		dto.setOperation("save");
		mav.addObject("mapdto", dto);
		return mav;
	}
	
	@RequestMapping(value="/referenceTables.htm", method = RequestMethod.POST)
	public ModelAndView referenceTablesPost(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		return referenceTables(req, res, mav);
	}
	@RequestMapping(value="/saveBaseRateFreq.htm", method = RequestMethod.POST)
	public @ResponseBody String saveEmployeeCompDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@ModelAttribute(value="mapdto") MapDTO mapDTO){
		logger.info("In saveBaseRateFreq");
		TelligentUser user = telligentUtility.getTelligentUser();
		return referenceTablesDAO.saveBaseRateFreq(mapDTO,user);
	}
	@RequestMapping(value="/getBaseRateFrequencyDetails.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray getBaseRateFrequencyDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		return (JSONArray) JSONSerializer.toJSON(referenceTablesDAO.getDetails("Base_Rate_Frequency"));
	}
	@RequestMapping(value="/getBaseRateFrequencyDetailsById.htm", method = RequestMethod.POST)
	public @ResponseBody JSONObject getBaseRateFrequencyDetailsById(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@RequestParam("id") String id){
		return (JSONObject) JSONSerializer.toJSON(referenceTablesDAO.getDetailsById("Base_Rate_Frequency",id));
	}
	
	/* bonus plan*/
	@RequestMapping(value="/bonusPlan.htm", method = RequestMethod.POST)
	public ModelAndView bonusPlan(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		mav.setViewName("bonuPlan");
		MapDTO dto = new MapDTO();
		dto.setOperation("save");
		mav.addObject("mapdto", dto);
		return mav;
	}
	
	@RequestMapping(value="/saveBonusPlan.htm", method = RequestMethod.POST)
	public @ResponseBody String saveBonusPlanDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@ModelAttribute(value="mapdto") MapDTO mapDTO){
		logger.info("In saveBaseRateFreq");
		TelligentUser user = telligentUtility.getTelligentUser();
		return referenceTablesDAO.saveData(mapDTO,user,"Bonus_Plan","Bonus Plan");
	}
	
	@RequestMapping(value="/getBonusPlanDetails.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray getBonusPlanDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		return (JSONArray) JSONSerializer.toJSON(referenceTablesDAO.getDetails("Bonus_Plan"));
	}
	@RequestMapping(value="/getBonusPlanDetailsById.htm", method = RequestMethod.POST)
	public @ResponseBody JSONObject getBonusPlanDetailsById(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@RequestParam("id") String id){
		return (JSONObject) JSONSerializer.toJSON(referenceTablesDAO.getDetailsById("Bonus_Plan",id));
	}

	/* bonus plan end*/
	
	
	/* classification*/
	@RequestMapping(value="/classification.htm", method = RequestMethod.POST)
	public ModelAndView classification(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		mav.setViewName("classification");
		MapDTO dto = new MapDTO();
		dto.setOperation("save");
		mav.addObject("mapdto", dto);
		return mav;
	}
	
	@RequestMapping(value="/saveClassification.htm", method = RequestMethod.POST)
	public @ResponseBody String saveClassification(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@ModelAttribute(value="mapdto") MapDTO mapDTO){
		logger.info("In saveClassification");
		TelligentUser user = telligentUtility.getTelligentUser();
		return referenceTablesDAO.saveData(mapDTO,user,"Classification","classification");
	}
	
	@RequestMapping(value="/getClassificationDetails.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray getClassificationDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		return (JSONArray) JSONSerializer.toJSON(referenceTablesDAO.getDetails("Classification"));
	}
	@RequestMapping(value="/getClassificationDetailsById.htm", method = RequestMethod.POST)
	public @ResponseBody JSONObject getClassificationDetailsById(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@RequestParam("id") String id){
		return (JSONObject) JSONSerializer.toJSON(referenceTablesDAO.getDetailsById("Classification",id));
	}

	/* classification end*/
	
	
	/* CompensationAction*/
	@RequestMapping(value="/compensationAction.htm", method = RequestMethod.POST)
	public ModelAndView compensationAction(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		mav.setViewName("compensationAction");
		MapDTO dto = new MapDTO();
		dto.setOperation("save");
		mav.addObject("mapdto", dto);
		return mav;
	}
	
	@RequestMapping(value="/saveCompensationAction.htm", method = RequestMethod.POST)
	public @ResponseBody String saveCompensationAction(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@ModelAttribute(value="mapdto") MapDTO mapDTO){
		logger.info("In saveCompensationAction");
		TelligentUser user = telligentUtility.getTelligentUser();
		return referenceTablesDAO.saveData(mapDTO,user,"Compensation_Action","Compensation Action");
	}
	
	@RequestMapping(value="/getCompensationActionDetails.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray getCompensationActionDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		return (JSONArray) JSONSerializer.toJSON(referenceTablesDAO.getDetails("Compensation_Action"));
	}
	@RequestMapping(value="/getCompensationActionDetailsById.htm", method = RequestMethod.POST)
	public @ResponseBody JSONObject getgetCompensationActionDetailsById(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@RequestParam("id") String id){
		return (JSONObject) JSONSerializer.toJSON(referenceTablesDAO.getDetailsById("Compensation_Action",id));
	}

	/* CompensationAction end*/
	
	/* CompensationActionReason*/
	@RequestMapping(value="/compensationActionReason.htm", method = RequestMethod.POST)
	public ModelAndView compensationActionReason(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		mav.setViewName("compensationActionReason");
		MapDTO dto = new MapDTO();
		dto.setOperation("save");
		mav.addObject("mapdto", dto);
		return mav;
	}
	
	@RequestMapping(value="/saveCompensationActionReason.htm", method = RequestMethod.POST)
	public @ResponseBody String saveCompensationActionReason(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@ModelAttribute(value="mapdto") MapDTO mapDTO){
		logger.info("In saveCompensationActionReason");
		TelligentUser user = telligentUtility.getTelligentUser();
		return referenceTablesDAO.saveData(mapDTO,user,"Compensation_Action_Reason","Compensation Action Reason");
	}
	
	@RequestMapping(value="/getCompensationActionReasonDetails.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray getCompensationActionReasonDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		return (JSONArray) JSONSerializer.toJSON(referenceTablesDAO.getDetails("Compensation_Action_Reason"));
	}
	@RequestMapping(value="/getCompensationActionReasonDetailsById.htm", method = RequestMethod.POST)
	public @ResponseBody JSONObject getgetCompensationActionReasonDetailsById(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@RequestParam("id") String id){
		return (JSONObject) JSONSerializer.toJSON(referenceTablesDAO.getDetailsById("Compensation_Action_Reason",id));
	}

	/* CompensationActionReason end*/
	
	/* DefaultEarningCode*/
	@RequestMapping(value="/defaultEarningCode.htm", method = RequestMethod.POST)
	public ModelAndView defaultEarningCode(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		mav.setViewName("defaultEarningCode");
		MapDTO dto = new MapDTO();
		dto.setOperation("save");
		mav.addObject("mapdto", dto);
		return mav;
	}
	
	@RequestMapping(value="/saveDefaultEarningCode.htm", method = RequestMethod.POST)
	public @ResponseBody String saveDefaultEarningCode(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@ModelAttribute(value="mapdto") MapDTO mapDTO){
		logger.info("In saveDefaultEarningCode");
		TelligentUser user = telligentUtility.getTelligentUser();
		return referenceTablesDAO.saveData(mapDTO,user,"Default_Earning_Code","Default Earning Code");
	}
	
	@RequestMapping(value="/getDefaultEarningCodeDetails.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray getDefaultEarningCodeDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		return (JSONArray) JSONSerializer.toJSON(referenceTablesDAO.getDetails("Default_Earning_Code"));
	}
	@RequestMapping(value="/getDefaultEarningCodeDetailsById.htm", method = RequestMethod.POST)
	public @ResponseBody JSONObject getgetDefaultEarningCodeDetailsById(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@RequestParam("id") String id){
		return (JSONObject) JSONSerializer.toJSON(referenceTablesDAO.getDetailsById("Default_Earning_Code",id));
	}

	/* DefaultEarningCode end*/
	
	/* DefaultHoursFrequency*/
	@RequestMapping(value="/defaultHoursFrequency.htm", method = RequestMethod.POST)
	public ModelAndView defaultHoursFrequency(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		mav.setViewName("defaultHoursFrequency");
		MapDTO dto = new MapDTO();
		dto.setOperation("save");
		mav.addObject("mapdto", dto);
		return mav;
	}
	
	@RequestMapping(value="/saveDefaultHoursFrequency.htm", method = RequestMethod.POST)
	public @ResponseBody String saveDefaultHoursFrequency(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@ModelAttribute(value="mapdto") MapDTO mapDTO){
		logger.info("In saveDefaultHoursFrequency");
		TelligentUser user = telligentUtility.getTelligentUser();
		return referenceTablesDAO.saveData(mapDTO,user,"Default_Hours_Frequency","Default Hours Freaquency");
	}
	
	@RequestMapping(value="/getDefaultHoursFrequencyDetails.htm", method = RequestMethod.POST)
	public @ResponseBody JSONArray getDefaultHoursFrequencyDetails(HttpServletRequest req,HttpServletResponse res,ModelAndView mav){
		return (JSONArray) JSONSerializer.toJSON(referenceTablesDAO.getDetails("Default_Hours_Frequency"));
	}
	@RequestMapping(value="/getDefaultHoursFrequencyDetailsById.htm", method = RequestMethod.POST)
	public @ResponseBody JSONObject getgetDefaultHoursFrequencyDetailsById(HttpServletRequest req,HttpServletResponse res,ModelAndView mav,@RequestParam("id") String id){
		return (JSONObject) JSONSerializer.toJSON(referenceTablesDAO.getDetailsById("Default_Hours_Frequency",id));
	}

	/* DefaultHoursFrequency end*/
}
