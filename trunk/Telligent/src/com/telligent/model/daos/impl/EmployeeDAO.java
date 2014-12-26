package com.telligent.model.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.telligent.core.system.annotation.SpringBean;
import com.telligent.model.db.AbstractDBManager;
import com.telligent.model.dtos.EmployeeDTO;
import com.telligent.model.dtos.MapDTO;
import com.telligent.model.dtos.TeamDTO;

/**
 * @author spothu
 *
 */
@SpringBean
public class EmployeeDAO extends AbstractDBManager{

	public final Logger logger = Logger.getLogger(EmployeeDAO.class);

	/**
	 *  Method to get respective teams of logged in employee
	 *  @param employeeId
	 * 
	 */
	public ArrayList<TeamDTO> getEmployeeTeams(String employeeId) {
		logger.info("in getEmployeeTeam DAO");
		ArrayList<TeamDTO> list = new ArrayList<TeamDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "SELECT team_id,team_name FROM team t where supervisor_employee_id=?";
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			while(rs.next()){
				TeamDTO dto = new TeamDTO();
				dto.setTeamId(rs.getString("team_id"));
				dto.setTeamName(rs.getString("team_name"));
				list.add(dto);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Excpetion in getEmployeeTeam"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}

	/**
	 *  Method to get respective team employees
	 *  @param teamId
	 * 
	 */
	public ArrayList<EmployeeDTO> getTeamEmployees(String teamId) {
		logger.info("in getTeamEmployees DAO");
		ArrayList<EmployeeDTO> list = new ArrayList<EmployeeDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "SELECT employee_id,employee_name,last_name,email_id,salary FROM employee e where team_id=?";
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, teamId);
			rs = ps.executeQuery();
			while(rs.next()){
				EmployeeDTO dto = new EmployeeDTO();
				dto.setEmployeeId(rs.getString("employee_id"));
				dto.setEmployeeName(rs.getString("employee_name"));
				dto.setLastName(rs.getString("last_name"));
				dto.setEmail(rs.getString("email_id"));
				dto.setSalary(rs.getString("salary"));
				list.add(dto);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Excpetion in getTeamEmployees "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}

	public boolean saveEmployeeDetails(EmployeeDTO employeeDTO) {
		logger.info("in saveEmployeeDetails DAO");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "Insert into employee(employee_name,last_name,email_id,team_id,salary,employee_id) values(?,?,?,?,?,?)";
		try {
			conn = this.getConnection();
			int maxId = GenericDAO.getInstance().getMaxId(conn, ps, rs, "SELECT ifnull(MAX(employee_id),0)+1 FROM employee", "");
			ps = conn.prepareStatement(query);
			ps.setString(1, employeeDTO.getEmployeeName());
			ps.setString(2, employeeDTO.getLastName());
			ps.setString(3, employeeDTO.getEmail());
			ps.setString(4, employeeDTO.getTeamId());
			ps.setString(5, employeeDTO.getSalary());
			ps.setInt(6, maxId);
			int i = ps.executeUpdate();
			if(i>0)
				return true;
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Excpetion in saveEmployeeDetails "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return false;
	}

	public ArrayList<MapDTO> searchList(String firstName, String lastName,String empId) {
		logger.info("in searchList DAO");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<MapDTO> list = new ArrayList<MapDTO>();
		StringBuffer query = new StringBuffer();
		try {
			conn = this.getConnection();
			query.append("select employee_id,employee_name,last_name from employee where ");
			if(lastName !=null && !lastName.trim().equalsIgnoreCase("")){
				query.append("last_name like '"+lastName+"%'");
			}else if(firstName !=null && !firstName.trim().equalsIgnoreCase("")){
				query.append("employee_name like '"+firstName+"%'");
			}else if(empId !=null && !empId.trim().equalsIgnoreCase("")){
				query.append("employee_id like '"+empId+"%'");
			}
			ps = conn.prepareStatement(query.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				MapDTO dto = new MapDTO();
				dto.setId(rs.getString("employee_id"));
				dto.setValue(rs.getString("employee_id")+" "+rs.getString("last_name")+","+rs.getString("employee_name"));
				list.add(dto);
			}
		}catch (Exception ex) {
			//ex.printStackTrace();
			logger.info("Excpetion in searchList "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}

	public ArrayList<MapDTO> searchTeamEmployees(String teamName) {
		logger.info("in searchList DAO");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<MapDTO> list = new ArrayList<MapDTO>();
		StringBuffer query = new StringBuffer();
		try {
			conn = this.getConnection();
			query.append("select employee_id,employee_name,last_name from employee e , team t where t.team_name like ? and t.team_id = e.team_id");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, teamName+"%");
			rs = ps.executeQuery();
			while(rs.next()){
				MapDTO dto = new MapDTO();
				dto.setId(rs.getString("employee_id"));
				dto.setValue(rs.getString("employee_id")+" "+rs.getString("last_name")+","+rs.getString("employee_name"));
				list.add(dto);
			}
		}catch (Exception ex) {
			//ex.printStackTrace();
			logger.info("Excpetion in searchList "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}
	public EmployeeDTO getEmployeeDetails(String empId){
		logger.info("in getEmployeeDetails");
		EmployeeDTO dto = new EmployeeDTO();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			conn = this.getConnection();
			query.append("select employee_name,last_name,email_id,employee_no,socialSecNo,badgeNo,DATE_FORMAT(dateOfBirth,'%y/%m/%d') dateOfBirth,DATE_FORMAT(effectiveDate,'%y/%m/%d') effectiveDate,citizenship, ");
			query.append("maritalStatus,veteranStatus,visaType,DATE_FORMAT(visaExpDate,'%y/%m/%d') visaExpDate,isMinor,gender, ");
			query.append("ethinicity,disabled,disabledDesc,homePhone,cellPhone,personalemail,addressLine1,addressLine2,city,state,zipcode, ");
			query.append("workPhone,workExt,workEmail,workCellPhone,emergencyLastName,emergencyFirstName,emergencyRelationShip,emergencyHomePhone,emergencyCellPhone,emergencyEmail  ");
			query.append("from employee e where employee_id=? ");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, empId);
			rs = ps.executeQuery();
			if(rs.next()){
				dto.setEmployeeId(empId);
				dto.setEmployeeName(rs.getString("employee_name"));
				dto.setEmployeeNo(rs.getString("employee_no"));
				dto.setLastName(rs.getString("last_name"));
				dto.setEmail(rs.getString("email_id"));
				dto.setSocialSecNo(rs.getString("socialSecNo"));
				dto.setBadgeNo(rs.getString("badgeNo"));
				dto.setDateOfBirth(rs.getString("dateOfBirth"));
				dto.setEffectiveDate(rs.getString("effectiveDate"));
				dto.setCitizenship(rs.getString("citizenship"));
				dto.setMaritalStatus(rs.getString("maritalStatus"));
				dto.setVeteranStatus(rs.getString("veteranStatus"));
				dto.setVisaType(rs.getString("visaType"));
				dto.setVisaExpDate(rs.getString("visaExpDate"));
				dto.setMinor(rs.getString("isMinor").equalsIgnoreCase("0")?true:false);
				dto.setGender(rs.getString("gender"));
				dto.setEthinicity(rs.getString("ethinicity"));
				dto.setDisabled(rs.getString("disabled"));
				dto.setDisablityDesc(rs.getString("disabledDesc"));
				dto.setHomePhone(rs.getString("homePhone"));
				dto.setCellPhone(rs.getString("cellPhone"));
				dto.setPersonalEmail(rs.getString("personalemail"));
				dto.setAddressLine1(rs.getString("addressLine1"));
				dto.setAddressLine2(rs.getString("addressLine2"));
				dto.setCity(rs.getString("city"));
				dto.setState(rs.getString("state"));
				dto.setZipcode(rs.getString("zipcode"));
				dto.setWorkPhone(rs.getString("workPhone"));
				dto.setWorkExt(rs.getString("workExt"));
				dto.setWorkEmail(rs.getString("workEmail"));
				dto.setWorkCellPhone(rs.getString("workCellPhone"));
				dto.setEmergencyCellPhone(rs.getString("emergencyCellPhone"));
				dto.setEmergencyEmail(rs.getString("emergencyEmail"));
				dto.setEmergencyFirstName(rs.getString("emergencyFirstName"));
				dto.setEmergencyLastName(rs.getString("emergencyLastName"));
				dto.setEmergencyHomePhone(rs.getString("emergencyHomePhone"));
				dto.setEmergencyRelationShip(rs.getString("emergencyRelationShip"));
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeeDetails "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return dto;
	}

}
