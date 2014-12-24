package com.telligent.model.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.telligent.core.system.annotation.SpringBean;
import com.telligent.model.daos.IEmployeeDAO;
import com.telligent.model.db.AbstractDBManager;
import com.telligent.model.dtos.EmployeeDTO;
import com.telligent.model.dtos.MapDTO;
import com.telligent.model.dtos.TeamDTO;

/**
 * @author spothu
 *
 */
@SpringBean
public class EmployeeDAO extends AbstractDBManager implements IEmployeeDAO{

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
	@Override
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

	@Override
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

	@Override
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

	@Override
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

}
