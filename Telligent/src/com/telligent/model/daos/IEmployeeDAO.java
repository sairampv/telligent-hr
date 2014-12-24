package com.telligent.model.daos;

import java.util.ArrayList;

import com.telligent.model.dtos.EmployeeDTO;
import com.telligent.model.dtos.MapDTO;
import com.telligent.model.dtos.TeamDTO;

public interface IEmployeeDAO{
	public ArrayList<TeamDTO> getEmployeeTeams(String employeeId);
	
	public ArrayList<EmployeeDTO> getTeamEmployees(String teamId);
	
	public boolean saveEmployeeDetails(EmployeeDTO employeeDTO);
	
	public ArrayList<MapDTO> searchList(String firstName,String lastName,String empId);
	
	public ArrayList<MapDTO> searchTeamEmployees(String teamName);
	
}
