package com.telligent.model.daos.impl;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.telligent.common.user.TelligentUser;
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
	public String saveEmployeeDetails(EmployeeDTO employeeDTO,TelligentUser telligentUser) {
		logger.info("in saveEmployeeDetails DAO");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		try {
			query.append("insert into EMP_PERSONAL(EMP_ID,BADGE,EFFECTIVE_DATE,F_NAME,M_NAME,L_NAME,P_EMAIL,H_PHONE,M_PHONE,ADDRESS_L1,ADDRESS_L2,CITY,STATE,ZIP");
			query.append("DATE_OF_BIRTH,IS_MINOR,WORK_PHONE,WORK_MOBILE_PHONE,WORK_EMAIL,EMC_L_NAME,EMC_F_NAME,EMC_REL,EMC_EMAIL,");
			query.append("EMC_H_PHONE,EMC_M_PHONE,PICTURE,DATE_UPDATED,UPDATED_BY)");
			query.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),?)");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeDTO.getEmployeeId());
			ps.setString(2, employeeDTO.getBadgeNo());
			ps.setDate(3, new java.sql.Date(new Date(employeeDTO.getEffectiveDate()).getTime()));
			ps.setString(4, employeeDTO.getFirstName());
			ps.setString(5, employeeDTO.getMiddleName());
			ps.setString(6, employeeDTO.getLastName());
			ps.setString(7, employeeDTO.getPersonalEmail());
			ps.setString(8, employeeDTO.getHomePhone());
			ps.setString(9, employeeDTO.getMobilePhone());
			ps.setString(10, employeeDTO.getAddressLine1());
			ps.setString(11, employeeDTO.getAddressLine2());
			ps.setString(12, employeeDTO.getCity());
			ps.setString(13, employeeDTO.getState());
			ps.setString(14, employeeDTO.getZipcode());
			ps.setDate(15,new java.sql.Date(new Date(employeeDTO.getDateOfBirth()).getTime()));
			ps.setInt(16,  employeeDTO.isMinor() ? 0:1);
			ps.setString(17, employeeDTO.getWorkPhone());
			ps.setString(18, employeeDTO.getWorkMobilePhone());
			ps.setString(19, employeeDTO.getWorkEmail());
			ps.setString(20, employeeDTO.getEmergencyLastName());
			ps.setString(21, employeeDTO.getEmergencyFirstName());
			ps.setString(22, employeeDTO.getEmergencyRelationShip());
			ps.setString(23, employeeDTO.getEmergencyEmail());
			ps.setString(24, employeeDTO.getEmergencyHomePhone());
			ps.setString(25, employeeDTO.getEmergencyMobilePhone());
			ps.setBlob(26, employeeDTO.getPicture()!=null ? employeeDTO.getPicture().getInputStream():null);
			ps.setString(27, telligentUser.getEmployeeId());
			int i = ps.executeUpdate();
			if(i>0)
				return "Employee Details Saved Successfully";
			else
				return "Employee Details not Saved";
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
			query.append("select EMP_ID,F_NAME,L_NAME from EMP_PERSONAL where ");
			if(lastName !=null && !lastName.trim().equalsIgnoreCase("")){
				query.append("L_NAME like '"+lastName+"%'");
			}else if(firstName !=null && !firstName.trim().equalsIgnoreCase("")){
				query.append("F_NAME like '"+firstName+"%'");
			}else if(empId !=null && !empId.trim().equalsIgnoreCase("")){
				query.append("EMP_ID like '"+empId+"%'");
			}
			ps = conn.prepareStatement(query.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				MapDTO dto = new MapDTO();
				dto.setId(rs.getString("EMP_ID"));
				dto.setValue(rs.getString("EMP_ID")+" "+rs.getString("L_NAME")+","+rs.getString("F_NAME"));
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
			
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeeDetails "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return dto;
	}
}
