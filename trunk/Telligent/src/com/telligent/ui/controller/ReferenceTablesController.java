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

	
	public final Logger logger = Logger.getLogger(MeritAdministrationController.class); 

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
}
