package com.telligent.model.daos.impl;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;

import com.telligent.common.user.TelligentUser;
import com.telligent.core.system.annotation.SpringBean;
import com.telligent.model.db.AbstractDBManager;
import com.telligent.model.dtos.CityDTO;
import com.telligent.model.dtos.EmployeeDTO;
import com.telligent.model.dtos.MapDTO;
import com.telligent.model.dtos.StateDTO;
import com.telligent.model.dtos.TeamDTO;
import com.telligent.util.BASE64DecodedMultipartFile;

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
			ps = conn.prepareStatement("select emp_id from EMP_PERSONAL where emp_id=?");
			ps.setString(1, empId);
			rs = ps.executeQuery();
			if(rs.next())
				return true;
		}catch(Exception e){
			return false;
		}
		return false;
	}
	private int employeeHistoryUpdate(Connection conn,PreparedStatement ps,String empId){
		try{
			StringBuffer query = new StringBuffer();
			query.append("insert into EMP_PERSONAL_HIS(EMP_NO,EMP_ID,BADGE,EFFECTIVE_DATE,F_NAME,M_NAME,L_NAME,P_EMAIL,H_PHONE,M_PHONE,ADDRESS_L1,ADDRESS_L2,CITY,STATE,ZIP,");
			query.append("DATE_OF_BIRTH,IS_MINOR,WORK_PHONE,WORK_MOBILE_PHONE,WORK_EMAIL,EMC_L_NAME,EMC_F_NAME,EMC_REL,EMC_EMAIL,");
			query.append("EMC_H_PHONE,EMC_M_PHONE,DATE_UPDATED,UPDATED_BY,PICTURE,SOCIAL_SEC_NO,END_EFFECTIVE_DATE)");// Always keep PICTURE column as last one
			query.append("select EMP_NO,EMP_ID,BADGE,EFFECTIVE_DATE,F_NAME,M_NAME,L_NAME,P_EMAIL,H_PHONE,M_PHONE,ADDRESS_L1,ADDRESS_L2,CITY,STATE,ZIP,DATE_OF_BIRTH,IS_MINOR,WORK_PHONE,WORK_MOBILE_PHONE,WORK_EMAIL,EMC_L_NAME,EMC_F_NAME,EMC_REL,EMC_EMAIL,EMC_H_PHONE,EMC_M_PHONE,DATE_UPDATED,UPDATED_BY,PICTURE,SOCIAL_SEC_NO,END_EFFECTIVE_DATE from EMP_PERSONAL where EMP_ID='"+empId+"'");
			ps = conn.prepareStatement(query.toString());
			int i = ps.executeUpdate();
			ps.close();
			return i;
		}catch(Exception e){
			
		}
		return 0;
	}
	@SuppressWarnings("deprecation")
	public EmployeeDTO saveEmployeeDetails(EmployeeDTO employeeDTO,TelligentUser telligentUser) {
		logger.info("in saveEmployeeDetails DAO");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		DecimalFormat decimalFormat = new DecimalFormat("000000000");
		try {
			conn = this.getConnection();
			String empId = decimalFormat.format(Integer.parseInt(employeeDTO.getEmployeeId()));
			//String.format(format, args)
			if(employeeDTO.getOperation().equalsIgnoreCase("edit")){
				conn.setAutoCommit(false);
				int i = employeeHistoryUpdate(conn, ps, empId);
				if(i>0){
					query = new StringBuffer();
					if(!employeeDTO.getPicture().getOriginalFilename().equalsIgnoreCase("")){
						query.append("update EMP_PERSONAL set BADGE=?,EFFECTIVE_DATE=?,F_NAME=?,M_NAME=?,L_NAME=?,P_EMAIL=?,H_PHONE=?,M_PHONE=?,ADDRESS_L1=?,ADDRESS_L2=?,CITY=?,STATE=?,ZIP=?,");
						query.append("DATE_OF_BIRTH=?,IS_MINOR=?,WORK_PHONE=?,WORK_MOBILE_PHONE=?,WORK_EMAIL=?,EMC_L_NAME=?,EMC_F_NAME=?,EMC_REL=?,EMC_EMAIL=?,");
						query.append("EMC_H_PHONE=?,EMC_M_PHONE=?,DATE_UPDATED=sysdate(),UPDATED_BY=?,SOCIAL_SEC_NO=?,END_EFFECTIVE_DATE='"+new java.sql.Date(new Date(employeeDTO.getEffectiveDate()).getTime()-1)+"',PICTURE=? where EMP_ID='"+empId+"'");					
					}else{
						query.append("update EMP_PERSONAL set BADGE=?,EFFECTIVE_DATE=?,F_NAME=?,M_NAME=?,L_NAME=?,P_EMAIL=?,H_PHONE=?,M_PHONE=?,ADDRESS_L1=?,ADDRESS_L2=?,CITY=?,STATE=?,ZIP=?,");
						query.append("DATE_OF_BIRTH=?,IS_MINOR=?,WORK_PHONE=?,WORK_MOBILE_PHONE=?,WORK_EMAIL=?,EMC_L_NAME=?,EMC_F_NAME=?,EMC_REL=?,EMC_EMAIL=?,");
						query.append("EMC_H_PHONE=?,EMC_M_PHONE=?,DATE_UPDATED=sysdate(),UPDATED_BY=?,END_EFFECTIVE_DATE='"+new java.sql.Date(new Date(employeeDTO.getEffectiveDate()).getTime()-1)+"',SOCIAL_SEC_NO=? where EMP_ID='"+empId+"'");					
					}
					ps = conn.prepareStatement(query.toString());
					setPreparedStatementsForSave(ps, employeeDTO, telligentUser,"edit");
					i = ps.executeUpdate();
					if(i>0){
						String temp = employeeDTO.getEmployeeId();
						employeeDTO = new EmployeeDTO();
						employeeDTO.setEmployeeId(temp);
						temp = null;
						employeeDTO.setSuccessMessage("Employee Details Saved Successfully");
						conn.commit();
					}else{
						employeeDTO.setErrorMessage("Employee Details not Saved");
						conn.rollback();
					}
				}else{
					employeeDTO.setErrorMessage("Employee Details not Saved");
					conn.rollback();
				}
				
			}else{
				boolean flag = checkEmployeeId(conn, ps, rs, empId);
				if(flag){
					employeeDTO.setErrorMessage("Employee ID already Exists");
					return employeeDTO;
				}else{
					conn.setAutoCommit(false);
					query.append("insert into EMP_PERSONAL(EMP_ID,BADGE,EFFECTIVE_DATE,F_NAME,M_NAME,L_NAME,P_EMAIL,H_PHONE,M_PHONE,ADDRESS_L1,ADDRESS_L2,CITY,STATE,ZIP,");
					query.append("DATE_OF_BIRTH,IS_MINOR,WORK_PHONE,WORK_MOBILE_PHONE,WORK_EMAIL,EMC_L_NAME,EMC_F_NAME,EMC_REL,EMC_EMAIL,");
					query.append("EMC_H_PHONE,EMC_M_PHONE,DATE_UPDATED,UPDATED_BY,SOCIAL_SEC_NO,END_EFFECTIVE_DATE,PICTURE)");// Always keep PICTURE column as last one
					query.append("values ('"+empId+"',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),?,?,'"+new java.sql.Date(new Date("12/31/3000").getTime())+"',?)");
					ps = conn.prepareStatement(query.toString());
					setPreparedStatementsForSave(ps, employeeDTO, telligentUser,"save");
					int i = ps.executeUpdate();
					ps.close();
					if(i>0){
						conn.commit();
						String temp = employeeDTO.getEmployeeId();
						employeeDTO = new EmployeeDTO();
						employeeDTO.setEmployeeId(temp);
						temp = null;
					}else{
						conn.rollback();
						employeeDTO.setErrorMessage("Employee Details not Saved");
					}
				}
			}
			
		}catch (Exception ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {}
			ex.printStackTrace();
			employeeDTO.setErrorMessage("Employee Details not Saved :: "+ex.getMessage());
			logger.info("Excpetion in saveEmployeeDetails :: "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return employeeDTO;
	}

	@SuppressWarnings("deprecation")
	private void setPreparedStatementsForSave(PreparedStatement ps,EmployeeDTO employeeDTO,TelligentUser telligentUser,String operation) throws java.sql.SQLException,IOException{
		ps.setString(1, employeeDTO.getBadgeNo());
		ps.setDate(2, new java.sql.Date(new Date(employeeDTO.getEffectiveDate()).getTime()));
		ps.setString(3, employeeDTO.getFirstName());
		ps.setString(4, employeeDTO.getMiddleName());
		ps.setString(5, employeeDTO.getLastName());
		ps.setString(6, employeeDTO.getPersonalEmail());
		ps.setString(7, employeeDTO.getHomePhone());
		ps.setString(8, employeeDTO.getMobilePhone());
		ps.setString(9, employeeDTO.getAddressLine1());
		ps.setString(10, employeeDTO.getAddressLine2());
		ps.setString(11, employeeDTO.getCity());
		ps.setString(12, employeeDTO.getState());
		ps.setString(13, employeeDTO.getZipcode());
		ps.setDate(14,new java.sql.Date(new Date(employeeDTO.getDateOfBirth()).getTime()));
		ps.setInt(15,  employeeDTO.isMinor() ? 0:1);
		ps.setString(16, employeeDTO.getWorkPhone());
		ps.setString(17, employeeDTO.getWorkMobilePhone());
		ps.setString(18, employeeDTO.getWorkEmail());
		ps.setString(19, employeeDTO.getEmergencyLastName());
		ps.setString(20, employeeDTO.getEmergencyFirstName());
		ps.setString(21, employeeDTO.getEmergencyRelationShip());
		ps.setString(22, employeeDTO.getEmergencyEmail());
		ps.setString(23, employeeDTO.getEmergencyHomePhone());
		ps.setString(24, employeeDTO.getEmergencyMobilePhone());
		ps.setString(25, telligentUser.getEmployeeId());
		ps.setString(26, employeeDTO.getSocialSecNo());
		// Always keep Picture column as last one
		if(operation.equalsIgnoreCase("save")){
			if(employeeDTO.getPicture() !=null)
				ps.setBinaryStream(27, employeeDTO.getPicture().getInputStream(),(int)employeeDTO.getPicture().getBytes().length);	
			else
				ps.setBinaryStream(27, null);
		}else{
			if(!employeeDTO.getPicture().getOriginalFilename().equalsIgnoreCase(""))
				ps.setBinaryStream(27, employeeDTO.getPicture().getInputStream(),(int)employeeDTO.getPicture().getBytes().length);	
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
			query.append("select SEQ_NO,EMP_NO,EMP_ID,BADGE,DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,F_NAME,M_NAME,L_NAME,P_EMAIL,H_PHONE,M_PHONE,ADDRESS_L1,ADDRESS_L2,CITY,STATE,ZIP,");
			query.append("DATE_FORMAT(DATE_OF_BIRTH,'%m/%d/%Y') DATE_OF_BIRTH,IS_MINOR,WORK_PHONE,WORK_MOBILE_PHONE,WORK_EMAIL,EMC_L_NAME,EMC_F_NAME,EMC_REL,EMC_EMAIL,");
			query.append("EMC_H_PHONE,EMC_M_PHONE,PICTURE,DATE_FORMAT(DATE_UPDATED,'%m/%d/%Y  %H:%i:%S') DATE_UPDATED,UPDATED_BY,SOCIAL_SEC_NO from EMP_PERSONAL where EMP_ID=?");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, empId);
			rs = ps.executeQuery();
			if(rs.next()){
				return setEmployeeDetails(rs);
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeeDetails "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return dto;
	}
	public ArrayList<EmployeeDTO> getEmployeeDetailsHistory(String empId){
		logger.info("in getEmployeeDetailsHistory");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		ArrayList<EmployeeDTO> list = new ArrayList<EmployeeDTO>();
		try{
			query.append("select SEQ_NO,EMP_NO,EMP_ID,BADGE,DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,F_NAME,M_NAME,L_NAME,P_EMAIL,H_PHONE,M_PHONE,ADDRESS_L1,ADDRESS_L2,CITY,STATE,ZIP,");
			query.append("DATE_FORMAT(DATE_OF_BIRTH,'%m/%d/%Y') DATE_OF_BIRTH,IS_MINOR,WORK_PHONE,WORK_MOBILE_PHONE,WORK_EMAIL,EMC_L_NAME,EMC_F_NAME,EMC_REL,EMC_EMAIL,");
			query.append("EMC_H_PHONE,EMC_M_PHONE,PICTURE,DATE_FORMAT(DATE_UPDATED,'%m/%d/%Y %H:%i:%S') DATE_UPDATED,UPDATED_BY,SOCIAL_SEC_NO from EMP_PERSONAL_HIS where EMP_ID=? order by seq_no desc");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, empId);
			rs = ps.executeQuery();
			while(rs.next()){
				list.add(setEmployeeDetails(rs));
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeeDetailsHistory "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}
	
	public EmployeeDTO getEmployeeDetailsFromHistory(String seqNo){
		logger.info("in getEmployeeDetailsFromHistory");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			query.append("select SEQ_NO,EMP_NO,EMP_ID,BADGE,DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,F_NAME,M_NAME,L_NAME,P_EMAIL,H_PHONE,M_PHONE,ADDRESS_L1,ADDRESS_L2,CITY,STATE,ZIP,");
			query.append("DATE_FORMAT(DATE_OF_BIRTH,'%m/%d/%Y') DATE_OF_BIRTH,IS_MINOR,WORK_PHONE,WORK_MOBILE_PHONE,WORK_EMAIL,EMC_L_NAME,EMC_F_NAME,EMC_REL,EMC_EMAIL,");
			query.append("EMC_H_PHONE,EMC_M_PHONE,PICTURE,DATE_FORMAT(DATE_UPDATED,'%m/%d/%Y %H:%i:%S') DATE_UPDATED,UPDATED_BY,SOCIAL_SEC_NO from EMP_PERSONAL_HIS where SEQ_NO=? ");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, seqNo);
			rs = ps.executeQuery();
			if(rs.next()){
				return setEmployeeDetails(rs);
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeeDetailsFromHistory "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return null;
	}
	
	private EmployeeDTO setEmployeeDetails(ResultSet rs) throws java.sql.SQLException{
		EmployeeDTO dto = new EmployeeDTO();
		dto.setSeqNo(rs.getString("SEQ_NO"));
		dto.setEmployeeId(rs.getString("EMP_ID"));
		dto.setEmployeeNo(rs.getString("EMP_NO"));
		dto.setLastName(rs.getString("L_NAME"));
		dto.setMiddleName(rs.getString("M_NAME"));
		dto.setFirstName(rs.getString("F_NAME"));
		dto.setPersonalEmail(rs.getString("P_EMAIL"));
		dto.setBadgeNo(rs.getString("BADGE"));
		dto.setDateOfBirth(rs.getString("DATE_OF_BIRTH"));
		dto.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
		dto.setMinor(rs.getString("IS_MINOR").equalsIgnoreCase("0")?true:false);
		dto.setHomePhone(rs.getString("H_PHONE"));
		dto.setMobilePhone(rs.getString("M_PHONE"));
		dto.setAddressLine1(rs.getString("ADDRESS_L1"));
		dto.setAddressLine2(rs.getString("ADDRESS_L2"));
		dto.setCity(rs.getString("CITY"));
		dto.setState(rs.getString("STATE"));
		dto.setZipcode(rs.getString("ZIP"));
		dto.setWorkPhone(rs.getString("WORK_PHONE"));
		dto.setWorkEmail(rs.getString("WORK_EMAIL"));
		dto.setWorkMobilePhone(rs.getString("WORK_MOBILE_PHONE"));
		dto.setEmergencyMobilePhone(rs.getString("EMC_M_PHONE"));
		dto.setEmergencyEmail(rs.getString("EMC_EMAIL"));
		dto.setEmergencyFirstName(rs.getString("EMC_F_NAME"));
		dto.setEmergencyLastName(rs.getString("EMC_L_NAME"));
		dto.setEmergencyHomePhone(rs.getString("EMC_H_PHONE"));
		dto.setEmergencyRelationShip(rs.getString("EMC_REL"));
		dto.setSocialSecNo(rs.getString("SOCIAL_SEC_NO"));
		try {
			Blob blob = rs.getBlob("PICTURE");
			BASE64DecodedMultipartFile file = new BASE64DecodedMultipartFile(rs.getBlob("PICTURE").getBytes(1, (int)blob.length()));
			dto.setPicture(file);
			dto.setPictureBase64(Base64.toBase64String(blob.getBytes(1, (int)blob.length())));
		} catch (Exception e) {}
		dto.setUpdatedDate(rs.getString("DATE_UPDATED"));
		dto.setUpdatedBy(rs.getString("UPDATED_BY"));
		return dto;
	}
	
	public ArrayList<CityDTO> getCityDetails(String stateId){
		logger.info("in getStateDetails");
		ArrayList<CityDTO> list = new ArrayList<CityDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			query.append("select id,Name from City where state_id=?");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, stateId);
			rs = ps.executeQuery();
			while(rs.next()){
				CityDTO dto = new CityDTO();
				dto.setId(rs.getString("id"));
				dto.setCity(rs.getString("Name"));
				list.add(dto);
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getStateDetails "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}
	
	public ArrayList<StateDTO> getStateDetails(){
		logger.info("in getStateDetails");
		ArrayList<StateDTO> list = new ArrayList<StateDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			query.append("select id,Name from State");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				StateDTO dto = new StateDTO();
				dto.setId(rs.getString("id"));
				dto.setStateName(rs.getString("Name"));
				list.add(dto);
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getStateDetails "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}
}
