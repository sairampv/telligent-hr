package com.telligent.model.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

	public boolean checkEmployeeId(Connection conn,PreparedStatement ps,ResultSet rs,String empId){
		try{
			ps = conn.prepareStatement("select employee_id from employee where employee_id=?");
			ps.setString(1, empId);
			rs = ps.executeQuery();
			if(rs.next())
				return true;
		}catch(Exception e){
			return false;
		}
		return false;
	}
	@SuppressWarnings("deprecation")
	public String saveEmployeeDetails(EmployeeDTO employeeDTO) {
		logger.info("in saveEmployeeDetails DAO");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		try {
			boolean flag = checkEmployeeId(conn, ps, rs, employeeDTO.getEmployeeId());
			if(!flag){
				query.append("Insert into employee(employee_id,employee_name,employee_no,last_name,email_id,socialSecNo,badgeNo,dateOfBirth,effectiveDate,citizenship, ");
				query.append("maritalStatus,veteranStatus,visaType,visaExpDate,isMinor,gender, ");
				query.append("ethinicity,disabled,disabledDesc,homePhone,cellPhone,personalemail,addressLine1,addressLine2,city,state,zipcode, ");
				query.append("workPhone,workExt,workEmail,workCellPhone,emergencyLastName,emergencyFirstName,emergencyRelationShip,emergencyHomePhone,emergencyCellPhone,emergencyEmail,createdDate  ");
				query.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate())");
				conn = this.getConnection();
				ps = conn.prepareStatement(query.toString());
				ps.setString(1, employeeDTO.getEmployeeId());
				ps.setString(2, employeeDTO.getEmployeeName());
				ps.setString(3, employeeDTO.getEmployeeNo());
				ps.setString(4, employeeDTO.getLastName());
				ps.setString(5, employeeDTO.getEmail());
				ps.setString(6, employeeDTO.getSocialSecNo());
				ps.setString(7, employeeDTO.getBadgeNo());
				ps.setDate(8, new java.sql.Date(new Date(employeeDTO.getDateOfBirth()).getTime()));
				ps.setDate(9, new java.sql.Date(new Date(employeeDTO.getEffectiveDate()).getTime()));
				//ps.setString(9, dateFormat.format(new Date(employeeDTO.getEffectiveDate())));
				ps.setString(10, employeeDTO.getCitizenship());
				ps.setString(11, employeeDTO.getMaritalStatus());
				ps.setString(12, employeeDTO.getVeteranStatus());
				ps.setString(13, employeeDTO.getVisaType());
				ps.setDate(14, new java.sql.Date(new Date(employeeDTO.getVisaExpDate()).getTime()));
				//ps.setString(14, dateFormat.format(new Date(employeeDTO.getVisaExpDate())));
				ps.setInt(15, employeeDTO.isMinor() ? 0:1);
				ps.setString(16, employeeDTO.getGender());
				ps.setString(17, employeeDTO.getEthinicity());
				ps.setString(18, employeeDTO.getDisabled());
				ps.setString(19, employeeDTO.getDisablityDesc());
				ps.setString(20, employeeDTO.getHomePhone());
				ps.setString(21, employeeDTO.getCellPhone());
				ps.setString(22, employeeDTO.getPersonalEmail());
				ps.setString(23, employeeDTO.getAddressLine1());
				ps.setString(24, employeeDTO.getAddressLine2());
				ps.setString(25, employeeDTO.getCity());
				ps.setString(26, employeeDTO.getState());
				ps.setString(27, employeeDTO.getZipcode());
				ps.setString(28, employeeDTO.getWorkPhone());
				ps.setString(29, employeeDTO.getWorkExt());
				ps.setString(30, employeeDTO.getWorkEmail());
				ps.setString(31, employeeDTO.getWorkCellPhone());
				ps.setString(32, employeeDTO.getEmergencyCellPhone());
				ps.setString(33, employeeDTO.getEmergencyEmail());
				ps.setString(34, employeeDTO.getEmergencyFirstName());
				ps.setString(35, employeeDTO.getEmergencyLastName());
				ps.setString(36, employeeDTO.getEmergencyHomePhone());
				ps.setString(37, employeeDTO.getEmergencyRelationShip());
				int i = ps.executeUpdate();
				if(i>0)
					return "Employee Details Saved Successfully";
				else
					return "Employee Details not Saved";	
			}else
				return "Employee Id exists";
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Excpetion in saveEmployeeDetails "+ex.getMessage());
			return "Employee Details not Saved";
		} finally {
			this.closeAll(conn, ps, rs);
		}
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
