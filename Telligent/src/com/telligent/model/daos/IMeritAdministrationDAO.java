package com.telligent.model.daos;

import java.util.ArrayList;

import com.telligent.model.dtos.BudgetSummaryDTO;
import com.telligent.model.dtos.RatingsAndIncreaseDTO;
import com.telligent.model.dtos.SalarPlanningDTO;
import com.telligent.model.dtos.SalaryPositionRangeDTO;


public interface IMeritAdministrationDAO{

	public ArrayList<SalarPlanningDTO> getSalaryPlanningDetails(String columnName,String order,String teamName,String teamId,String employeeId);
	public ArrayList<BudgetSummaryDTO> budgetSummary(String supervisorId);
    public boolean updateEmployeeDetails(SalarPlanningDTO salaryPlanDTO);
    public ArrayList<RatingsAndIncreaseDTO> ratingsAndIncreaseSummary();
    public ArrayList<SalaryPositionRangeDTO> salaryPositionRangeDetails();
}
