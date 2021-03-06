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
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;

import com.telligent.common.handlers.MessageHandler;
import com.telligent.common.user.TelligentUser;
import com.telligent.core.system.annotation.SpringBean;
import com.telligent.model.db.AbstractDBManager;
import com.telligent.model.dtos.CityDTO;
import com.telligent.model.dtos.EmployeeCompensationDTO;
import com.telligent.model.dtos.EmployeeDTO;
import com.telligent.model.dtos.EmployeeOtherDTO;
import com.telligent.model.dtos.EmployeePositionDTO;
import com.telligent.model.dtos.EmploymentDTO;
import com.telligent.model.dtos.MapDTO;
import com.telligent.model.dtos.StateDTO;
import com.telligent.model.dtos.TeamDTO;
import com.telligent.util.BASE64DecodedMultipartFile;
import com.telligent.util.DateUtility;

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
	public EmployeeDTO saveEmployeeDetails(EmployeeDTO employeeDTO,TelligentUser telligentUser,MessageHandler messageHandler) {
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
				//int i = employeeHistoryUpdate(conn, ps, empId); //Trigger created to insert data in History table
				//if(i>0){
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
					int i  = ps.executeUpdate();
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
				/*}else{
					employeeDTO.setErrorMessage("Employee Details not Saved");
					conn.rollback();
				}*/
				
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
					query.append("values ('"+empId+"',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),?,?,'"+new java.sql.Date(new Date(messageHandler.getMessage("effectiveEndDate")).getTime())+"',?)");
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
	public EmployeeCompensationDTO saveEmployeeCompensation( EmployeeCompensationDTO compDTO,TelligentUser telligentUser) {
 		logger.info("in saveEmployeeCompensation DAO");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		DecimalFormat decimalFormat = new DecimalFormat("000000000");
		try {
			conn = this.getConnection();
			String empId = decimalFormat.format(Integer.parseInt(compDTO.getEmployeeId()));
			//String.format(format, args)
			//compDTO.setOperation("");
			if(compDTO.getOperation().equalsIgnoreCase("edit")){
				conn.setAutoCommit(false);
				//int i = compensationHistoryUpdate(conn, ps, empId);
				//if(i>0){
					query = new StringBuffer();
						query.append("update EMP_COMPENSATION set EFFECTIVE_DATE=?,COMP_ACTION_TYPE=?,COMP_REASON=?,PAY_ENTITY=?,PAY_GROUP=?,PAY_FREQ=?,LAST_EVALUATION_DATE=?,GRADE=?,NEXT_EVAL_DUE_DATE=?,SCHEDULED_HOURS=?,HOURS_FREQUENCY=?");
						query.append(",PAY_PERIOD_HRS=?,WEEKLY_HOURS=?,BASE_RATE=?,BASE_RATE_FREQ=?,PERIOD_RATE=?,HOURLY_RATE=?,DEFAULT_EARNING_CODE=?,JOB_GROUP=?,USE_JOB_RATE=?,PERFORMACE_PLAN=?");
						query.append(",BONUS_PLAN=? WHERE EMP_ID='"+empId+"'");
					ps = conn.prepareStatement(query.toString());
					setPreparedStatementsForCompensation(ps, compDTO, telligentUser);
					int i = ps.executeUpdate();
					if(i>0){
						String temp = compDTO.getEmployeeId();
						compDTO = new EmployeeCompensationDTO();
						compDTO.setEmployeeId(temp);
						temp = null;
						compDTO.setSuccessMessage("Employee Compensation Details Saved Successfully");
						conn.commit();
					}else{
						compDTO.setErrorMessage("Employee Compensation Details not Saved");
						conn.rollback();
					}
				/*}else{
					compDTO.setErrorMessage("Employee Compensation Details not Saved");
					conn.rollback();
				}*/
				
			}else{
				//boolean flag = checkEmployeeId(conn, ps, rs, empId);
				//if(flag){
				//	compDTO.setErrorMessage("Employee ID already Exists");
				//	return compDTO;
				//}else{
					conn.setAutoCommit(false);
					query.append("INSERT INTO EMP_COMPENSATION(EMP_ID,EFFECTIVE_DATE,COMP_ACTION_TYPE,COMP_REASON,PAY_ENTITY,PAY_GROUP,PAY_FREQ,LAST_EVALUATION_DATE,GRADE,NEXT_EVAL_DUE_DATE,");
					query.append("SCHEDULED_HOURS,HOURS_FREQUENCY,PAY_PERIOD_HRS,WEEKLY_HOURS,BASE_RATE,BASE_RATE_FREQ,PERIOD_RATE,HOURLY_RATE,DEFAULT_EARNING_CODE,");
					query.append("JOB_GROUP,USE_JOB_RATE,PERFORMACE_PLAN,BONUS_PLAN )");
					query.append("VALUES ('"+empId+"',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					ps = conn.prepareStatement(query.toString());
					setPreparedStatementsForCompensation(ps, compDTO, telligentUser);
					int i = ps.executeUpdate();
					ps.close();
					if(i>0){
						conn.commit();
						String temp = compDTO.getEmployeeId();
						compDTO = new EmployeeCompensationDTO();
						compDTO.setEmployeeId(temp);
						temp = null;
					}else{
						conn.rollback();
						compDTO.setErrorMessage("Employee Details not Saved");
					}
				//}
			}
			
		}catch (Exception ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {}
			ex.printStackTrace();
			compDTO.setErrorMessage("Employee Details not Saved :: "+ex.getMessage());
			logger.info("Excpetion in saveEmployeeDetails :: "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return compDTO;
	}
	
	private int compensationHistoryUpdate(Connection conn,PreparedStatement ps,String empId){
		try{
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO EMP_COMPENSATION_HIS(EMP_ID,EFFECTIVE_DATE,COMP_ACTION_TYPE,COMP_REASON,PAY_ENTITY,PAY_GROUP,PAY_FREQ,LAST_EVALUATION_DATE,GRADE,NEXT_EVAL_DUE_DATE,SCHEDULED_HOURS,HOURS_FREQUENCY");
			query.append(",PAY_PERIOD_HRS,WEEKLY_HOURS,BASE_RATE,BASE_RATE_FREQ,PERIOD_RATE,HOURLY_RATE,DEFAULT_EARNING_CODE,JOB_GROUP,USE_JOB_RATE,PERFORMACE_PLAN,BONUS_PLAN)");
			query.append("SELECT EMP_ID,EFFECTIVE_DATE,COMP_ACTION_TYPE,COMP_REASON,PAY_ENTITY,PAY_GROUP,PAY_FREQ,LAST_EVALUATION_DATE,GRADE,NEXT_EVAL_DUE_DATE,SCHEDULED_HOURS,HOURS_FREQUENCY");
			query.append(",PAY_PERIOD_HRS,WEEKLY_HOURS,BASE_RATE,BASE_RATE_FREQ,PERIOD_RATE,HOURLY_RATE,DEFAULT_EARNING_CODE,JOB_GROUP,USE_JOB_RATE,PERFORMACE_PLAN,BONUS_PLAN");
			query.append("  FROM EMP_COMPENSATION where EMP_ID='"+empId+"'");
			ps = conn.prepareStatement(query.toString());
			int i = ps.executeUpdate();
			ps.close();
			return i;
		}catch(Exception e){
			
		}
		return 0;
	}
	
	
	@SuppressWarnings("deprecation")
	private void setPreparedStatementsForCompensation(PreparedStatement ps,EmployeeCompensationDTO compDTO,TelligentUser telligentUser) throws java.sql.SQLException,IOException{
		ps.setDate(1, new java.sql.Date(new Date(compDTO.getEffectiveDate()).getTime()));
		ps.setString(2, compDTO.getCompActionType());
		ps.setString(3, compDTO.getCompActionReason());
		ps.setString(4, compDTO.getPayEntity());
		ps.setString(5, compDTO.getPayGroup());
		ps.setString(6, compDTO.getPayFrequency());
		ps.setDate(7, new java.sql.Date(new Date(compDTO.getLastperfEvaluationDate()).getTime()));
		ps.setString(8, compDTO.getGrade());
		ps.setDate(9, new java.sql.Date(new Date(compDTO.getNextEvalDueDate()).getTime()));
		ps.setString(10, compDTO.getScheduledHours());
		ps.setString(11, compDTO.getHoursFrequency());
		ps.setString(12, compDTO.getPayPeriodHours());
		ps.setString(13, compDTO.getWeeklyHours());
		ps.setString(14,compDTO.getBaseRate());
		ps.setString(15,  compDTO.getBaseRateFrequency());
		ps.setString(16, compDTO.getPeriodRate());
		ps.setString(17, compDTO.getHourlyRate());
		ps.setString(18, compDTO.getDefaultEarningCode());
		ps.setString(19, compDTO.getEligibleJobGroup());
		ps.setString(20, compDTO.getUseJobRate());
		ps.setString(21, compDTO.getPerformacePlan());
		ps.setString(22, compDTO.getBonusPlan());
				
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
	
	public ArrayList<EmployeeCompensationDTO> getEmployeeCompensationHistory(String empId){
		logger.info("in getEmployeeCompensationHistory");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		ArrayList<EmployeeCompensationDTO> list = new ArrayList<EmployeeCompensationDTO>();
		try{
			query.append("SELECT SEQ_NO,EMP_ID,DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,COMP_ACTION_TYPE,COMP_REASON,PAY_ENTITY,PAY_GROUP,PAY_FREQ,DATE_FORMAT(LAST_EVALUATION_DATE,'%m/%d/%Y') LAST_EVALUATION_DATE,GRADE,DATE_FORMAT(NEXT_EVAL_DUE_DATE,'%m/%d/%Y') NEXT_EVAL_DUE_DATE,");
			query.append("SCHEDULED_HOURS,HOURS_FREQUENCY,PAY_PERIOD_HRS,WEEKLY_HOURS,BASE_RATE,BASE_RATE_FREQ,PERIOD_RATE,HOURLY_RATE,DEFAULT_EARNING_CODE,");
			query.append("JOB_GROUP,USE_JOB_RATE,PERFORMACE_PLAN,BONUS_PLAN FROM EMP_COMPENSATION_HIS WHERE EMP_ID=? ORDER BY SEQ_NO DESC ");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, empId);
			rs = ps.executeQuery();
			while(rs.next()){
				list.add(setEmployeeCompensationDetails(rs));
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeeCompensationHistory "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
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
	
	private EmployeeCompensationDTO setEmployeeCompensationDetails(ResultSet rs) throws java.sql.SQLException{
		

		EmployeeCompensationDTO dto = new EmployeeCompensationDTO();
		dto.setSeqNo(rs.getString("SEQ_NO"));
		dto.setEmployeeId(rs.getString("EMP_ID"));
		dto.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
		dto.setCompActionType(rs.getString("COMP_ACTION_TYPE"));
		dto.setCompActionReason(rs.getString("COMP_REASON"));
		dto.setPayEntity(rs.getString("PAY_ENTITY"));
		dto.setPayGroup(rs.getString("PAY_GROUP"));
		dto.setPayFrequency(rs.getString("PAY_FREQ"));
		dto.setLastperfEvaluationDate(rs.getString("LAST_EVALUATION_DATE"));
		dto.setGrade(rs.getString("GRADE"));
		dto.setNextEvalDueDate(rs.getString("NEXT_EVAL_DUE_DATE"));
		dto.setScheduledHours(rs.getString("SCHEDULED_HOURS"));
		dto.setHoursFrequency(rs.getString("HOURS_FREQUENCY"));
		dto.setPayPeriodHours(rs.getString("PAY_PERIOD_HRS"));
		dto.setWeeklyHours(rs.getString("WEEKLY_HOURS"));
	    dto.setBaseRate(rs.getString("BASE_RATE"));
	    dto.setBaseRateFrequency(rs.getString("BASE_RATE_FREQ"));
	    dto.setPeriodRate(rs.getString("PERIOD_RATE"));
	    dto.setHourlyRate(rs.getString("HOURLY_RATE"));
	    dto.setDefaultEarningCode(rs.getString("DEFAULT_EARNING_CODE"));
	    dto.setEligibleJobGroup(rs.getString("JOB_GROUP"));
	    dto.setUseJobRate(rs.getString("USE_JOB_RATE"));
	    dto.setPerformacePlan(rs.getString("PERFORMACE_PLAN"));
	    dto.setBonusPlan(rs.getString("BONUS_PLAN"));
	    EmployeeDTO dto1 = dummyBlob(new EmployeeDTO());
		dto.setPicture(dto1.getPicture());
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
	public ArrayList<CityDTO> getCityDetailsAll(){
		logger.info("in getStateDetails");
		ArrayList<CityDTO> list = new ArrayList<CityDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			query.append("select id,Name from City");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
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
	/**
	 *  Generic method to get id and value of lookup tables
	 * @return HashMap
	 */
	public HashMap<String, ArrayList<MapDTO>> getEmpCompensationLookup(){
		logger.info("in getEmpCompensationLookup");
		HashMap<String, ArrayList<MapDTO>> map = new HashMap<String, ArrayList<MapDTO>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = this.getConnection();
			map.put("Base_Rate_Frequency", getLookup(conn, ps, rs, "Base_Rate_Frequency"));
			map.put("Bonus_Plan", getLookup(conn, ps, rs, "Bonus_Plan"));
			map.put("Compensation_Action", getLookup(conn, ps, rs, "Compensation_Action"));
			map.put("Compensation_Action_Reason", getLookup(conn, ps, rs, "Compensation_Action_Reason"));
			map.put("Default_Earning_Code", getLookup(conn, ps, rs, "Default_Earning_Code"));
			map.put("Default_Hours_Frequency", getLookup(conn, ps, rs, "Default_Hours_Frequency"));
			map.put("Grade", getLookup(conn, ps, rs, "Grade"));
			map.put("Job_Group", getLookup(conn, ps, rs, "Job_Group"));
			map.put("pay_entity", getLookup(conn, ps, rs, "pay_entity"));
			map.put("pay_frequency", getLookup(conn, ps, rs, "pay_frequency"));
			map.put("pay_group", getLookup(conn, ps, rs, "pay_group"));
			map.put("Performance_Plan", getLookup(conn, ps, rs, "Performance_Plan"));
		}catch (Exception ex) {
			logger.info("Excpetion in getEmpCompensationLookup "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return map;
	}
	public ArrayList<MapDTO> getLookup(Connection conn,PreparedStatement ps,ResultSet rs,String tableName){
		ArrayList<MapDTO> list = new ArrayList<MapDTO>();
		try{
			ps = conn.prepareStatement("select id,value from "+tableName+" where isActive=1");
			rs = ps.executeQuery();
			while(rs.next()){
				MapDTO dto = new MapDTO();
				dto.setId(rs.getString("id"));
				dto.setValue(rs.getString("value"));
				list.add(dto);
			}
		}catch(Exception e){
			logger.error("Exception in getLookup "+e.getMessage());
		}
		return list;
	}
	
	/**
	 *  Generic method to get id and value of lookup tables for Employment
	 * @return HashMap
	 */
	public HashMap<String, ArrayList<MapDTO>> getEmpEmployementLookup(){
		logger.info("in getEmpEmployementLookup");
		HashMap<String, ArrayList<MapDTO>> map = new HashMap<String, ArrayList<MapDTO>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = this.getConnection();
			map.put("Status_Code", getLookup(conn, ps, rs, "Status_Code"));
			map.put("Status_Reason", getLookup(conn, ps, rs, "Status_Reason"));
			map.put("Status", getLookup(conn, ps, rs, "Status"));
			map.put("FLSA_Category", getLookup(conn, ps, rs, "FLSA_Category"));
			map.put("Classification", getLookup(conn, ps, rs, "Classification"));
			map.put("Employement_Category", getLookup(conn, ps, rs, "Employement_Category"));
			map.put("Full_Time_Equivalency", getLookup(conn, ps, rs, "Full_Time_Equivalency"));
			map.put("Leave_Status_code", getLookup(conn, ps, rs, "Leave_Status_code"));
			map.put("Leave_Status_Reason", getLookup(conn, ps, rs, "Leave_Status_Reason"));
		}catch (Exception ex) {
			logger.info("Excpetion in getEmpEmployementLookup "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return map;
	}
	public ArrayList<MapDTO> getSupervisorList(Connection conn,PreparedStatement ps, ResultSet rs){
		ArrayList<MapDTO> list = new ArrayList<MapDTO>();
		try{
			ps = conn.prepareStatement("select EMP_ID,F_NAME,L_NAME from EMP_PERSONAL");
			rs = ps.executeQuery();
			while(rs.next()){
				MapDTO dto = new MapDTO();
				dto.setId(rs.getString("EMP_ID"));
				dto.setValue(rs.getString("EMP_ID")+" "+rs.getString("L_NAME")+","+rs.getString("F_NAME"));
				list.add(dto);
			}
		}catch(Exception e){
			logger.error("Exception in getLookup "+e.getMessage());
		}
		return list;
	}
	public ArrayList<MapDTO> getTeams(Connection conn,PreparedStatement ps, ResultSet rs){
		ArrayList<MapDTO> list = new ArrayList<MapDTO>();
		try{
			ps = conn.prepareStatement("SELECT team_id,team_name FROM team t ");
			rs = ps.executeQuery();
			while(rs.next()){
				MapDTO dto = new MapDTO();
				dto.setId(rs.getString("team_id"));
				dto.setValue(rs.getString("team_name"));
				list.add(dto);
			}
		}catch(Exception e){
			logger.error("Exception in getLookup "+e.getMessage());
		}
		return list;
	}
	public HashMap<String, ArrayList<MapDTO>> getEmpPositionLookup(){
		logger.info("in getEmpPositionLookup");
		HashMap<String, ArrayList<MapDTO>> map = new HashMap<String, ArrayList<MapDTO>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = this.getConnection();
			map.put("supervisorList",getSupervisorList(conn, ps, rs));
			map.put("teamList",getTeams(conn, ps, rs));
			map.put("Status_Code", getLookup(conn, ps, rs, "Status_Code"));
			map.put("Status_Reason", getLookup(conn, ps, rs, "Status_Reason"));
			map.put("position_master", getLookup(conn, ps, rs, "position_master"));
			map.put("position_level", getLookup(conn, ps, rs, "position_level"));
			map.put("primary_job", getLookup(conn, ps, rs, "primary_job"));
			map.put("primary_job_leave", getLookup(conn, ps, rs, "primary_job_leave"));
			map.put("union_code", getLookup(conn, ps, rs, "union_code"));
			map.put("org1", getLookup(conn, ps, rs, "org1"));
			map.put("org2", getLookup(conn, ps, rs, "org2"));
			map.put("org3", getLookup(conn, ps, rs, "org3"));
			map.put("org4", getLookup(conn, ps, rs, "org4"));
			map.put("org5", getLookup(conn, ps, rs, "org5"));
			map.put("org6", getLookup(conn, ps, rs, "org6"));
			map.put("org7", getLookup(conn, ps, rs, "org7"));
			map.put("org8", getLookup(conn, ps, rs, "org8"));
			map.put("org9", getLookup(conn, ps, rs, "org9"));
			map.put("org10", getLookup(conn, ps, rs, "org10"));
		}catch (Exception ex) {
			logger.info("Excpetion in getEmpPositionLookup "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return map;
	}
	public HashMap<String, ArrayList<MapDTO>> getEmpOtherLookup(){
		logger.info("in getEmpOtherLookup");
		HashMap<String, ArrayList<MapDTO>> map = new HashMap<String, ArrayList<MapDTO>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = this.getConnection();
			map.put("Ethinicity", getLookup(conn, ps, rs, "Ethinicity"));
			map.put("Marital_Status", getLookup(conn, ps, rs, "Marital_Status"));
			//map.put("Citizenship_Status", getLookup(conn, ps, rs, "Citizenship_Status"));
			//map.put("Military_Status", getLookup(conn, ps, rs, "Military_Status"));
			map.put("VISA_Type", getLookup(conn, ps, rs, "VISA_Type"));
			map.put("Veteran_Status", getLookup(conn, ps, rs, "Veteran_Status"));
			map.put("Relationship", getLookup(conn, ps, rs, "Relationship"));
		}catch (Exception ex) {
			logger.info("Excpetion in getEmpOtherLookup "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return map;
	}
	@SuppressWarnings("deprecation")
	public String saveEmployeeOtherDetails(EmployeeOtherDTO employeeOtherDTO,TelligentUser telligentUser,MessageHandler messageHandler) {
		logger.info("in saveEmployeeOtherDetails DAO");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("select emp_id from EMP_OTHER_DATA where emp_id=?");
			ps.setString(1, employeeOtherDTO.getEmployeeId());
			rs = ps.executeQuery();
			if(rs.next()){
				String str = getEffectiveDate(conn,ps,rs,"EMP_OTHER_DATA",employeeOtherDTO.getEmployeeId());
				boolean flag = DateUtility.compareDates(employeeOtherDTO.getEffectiveDate(), str);
				ps.close();
				if(flag){
					conn.setAutoCommit(false);
					query.append("update EMP_OTHER_DATA set GENDER=?,ETHINICITY=?,MARITAL_STAT=?,CITIZENSHIP=?,VISA_TYPE=?,I9_EXP_DATE=?,VET_STAT=?,");
					query.append("IS_DISABLED=?,DISABILITY_DESC=?,CITY=?,EFFECTIVE_DATE=?,END_EFFECTIVE_DATE=?,EMC_L_NAME=?,EMC_F_NAME=?,EMC_REL=?,EMC_H_PHONE=?,EMC_M_PHONE=?,EMC_EMAIL=?,DATE_UPDATED=sysdate(),UPDATED_BY=? where  EMP_ID=?");
					ps = conn.prepareStatement(query.toString());
					setPSforEmpOther(employeeOtherDTO, ps, telligentUser, messageHandler,"update");
				}else{
					return "error:;Effective Date should be greater than current Effective Date";
				}
			}else{
				ps.close();
				conn.setAutoCommit(false);
				query.append("insert into EMP_OTHER_DATA (GENDER,ETHINICITY,MARITAL_STAT,CITIZENSHIP,VISA_TYPE,I9_EXP_DATE,VET_STAT,");
				query.append("IS_DISABLED,DISABILITY_DESC,CITY,EFFECTIVE_DATE,END_EFFECTIVE_DATE,EMC_L_NAME,EMC_F_NAME,EMC_REL,EMC_H_PHONE,EMC_M_PHONE,EMC_EMAIL,DATE_UPDATED,UPDATED_BY,EMP_ID) ");
				query.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),?,?)");
				ps = conn.prepareStatement(query.toString());
				setPSforEmpOther(employeeOtherDTO, ps, telligentUser, messageHandler,"save");
			}
			int i = ps.executeUpdate();
			if(i>0){
				conn.commit();
				return "Details Saved Succuessfully";
			}else{
				conn.rollback();
				return "error:;Details Not Saved";
			}
		}catch (Exception ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {}
			ex.printStackTrace();
			employeeOtherDTO.setErrorMessage("error ::"+ex.getMessage());
			logger.info("Excpetion in saveEmployeeOtherDetails :: "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return null;
	}
	@SuppressWarnings("deprecation")
	public void setPSforEmpOther(EmployeeOtherDTO employeeOtherDTO,PreparedStatement ps,TelligentUser telligentUser,MessageHandler messageHandler,String operation) throws SQLException{
		ps.setString(1, employeeOtherDTO.getGender());
		ps.setString(2, employeeOtherDTO.getEthinicity());
		ps.setString(3, employeeOtherDTO.getMaritalStatus());
		ps.setString(4, employeeOtherDTO.getCitizenShip());
		ps.setString(5, employeeOtherDTO.getVisaType());
		if(employeeOtherDTO.getI9ExpDate() !=null && !employeeOtherDTO.getI9ExpDate().equalsIgnoreCase(""))
			ps.setDate(6, new java.sql.Date(new Date(employeeOtherDTO.getI9ExpDate()).getTime()));
		else
			ps.setDate(6, null);
		ps.setString(7, employeeOtherDTO.getVeteranStatus());
		ps.setString(8, employeeOtherDTO.getDisability());
		ps.setString(9, employeeOtherDTO.getDisabilityDesc());
		ps.setString(10, employeeOtherDTO.getCity()!=null && !employeeOtherDTO.getCity().equalsIgnoreCase("") ? employeeOtherDTO.getCity():null);
		ps.setDate(11, new java.sql.Date(new Date(employeeOtherDTO.getEffectiveDate()).getTime()));
		if(operation.equalsIgnoreCase("save"))
			ps.setDate(12, new java.sql.Date(new Date(messageHandler.getMessage("effectiveEndDate")).getTime()));
		else
			ps.setDate(12, new java.sql.Date(new Date(employeeOtherDTO.getEffectiveDate()).getTime()-1));
		ps.setString(13, employeeOtherDTO.getEmergencyLastName());
		ps.setString(14, employeeOtherDTO.getEmergencyFirstName());
		ps.setString(15, employeeOtherDTO.getEmergencyRelationShip());
		ps.setString(16, employeeOtherDTO.getEmergencyHomePhone());
		ps.setString(17, employeeOtherDTO.getEmergencyMobilePhone());
		ps.setString(18, employeeOtherDTO.getEmergencyEmail());
		ps.setString(19, telligentUser.getEmployeeId());
		ps.setString(20, employeeOtherDTO.getEmployeeId());
	}
	public EmployeeOtherDTO getEmployeeOtherDetails(String empId){
		logger.info("in getEmployeeOtherDetails");
		EmployeeOtherDTO dto = new EmployeeOtherDTO();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			query.append("select SEQ_NO,GENDER,ETHINICITY,MARITAL_STAT,CITIZENSHIP,VISA_TYPE,DATE_FORMAT(I9_EXP_DATE,'%m/%d/%Y') I9_EXP_DATE,VET_STAT,");
			query.append("IS_DISABLED,DISABILITY_DESC,CITY,DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE, ");
			query.append("EMC_L_NAME,EMC_F_NAME,EMC_REL,EMC_H_PHONE,EMC_M_PHONE,EMC_EMAIL,DATE_UPDATED,UPDATED_BY from EMP_OTHER_DATA where EMP_ID=?");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, empId);
			rs = ps.executeQuery();
			if(rs.next()){
				EmployeeDTO dto1 = getEmployeeDetails(empId);
				dto = setEmployeeOtherDetails(rs);
				dto.setEmployeeId(dto1.getEmployeeId());
				dto.setLastName(dto1.getLastName());
				dto.setFirstName(dto1.getFirstName());
				dto.setMiddleName(dto1.getMiddleName());
			}else{
				EmployeeDTO dto1 = dummyBlob(new EmployeeDTO());
				dto.setPicture(dto1.getPicture());
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeeOtherDetails "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return dto;
	}
	public EmployeeDTO dummyBlob(EmployeeDTO dto){
		try {
			byte[] bytes = "".getBytes();
			BASE64DecodedMultipartFile file = new BASE64DecodedMultipartFile(bytes);
			dto.setPicture(file);
			dto.setPictureBase64(Base64.toBase64String(bytes));
			return dto;
		} catch (Exception e) {}
		return dto;
	}
	public ArrayList<EmployeeOtherDTO> getEmployeeOtherDetailsHistory(String empId){
		logger.info("in getEmployeeOtherDetailsHistory");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		ArrayList<EmployeeOtherDTO> list = new ArrayList<EmployeeOtherDTO>();
		try{
			/*query.append("select SEQ_NO,GENDER,e.value ETHINICITY,m.value MARITAL_STAT,CITIZENSHIP,v.value VISA_TYPE,DATE_FORMAT(I9_EXP_DATE,'%m/%d/%Y') I9_EXP_DATE,ve.value VET_STAT,");
			query.append("IS_DISABLED,DISABILITY_DESC,CITY,DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE, ");
			query.append("EMC_L_NAME,EMC_F_NAME,r.value EMC_REL,EMC_H_PHONE,EMC_M_PHONE,EMC_EMAIL,DATE_UPDATED,UPDATED_BY ");
			query.append("from EMP_OTHER_DATA_HIS o,Ethinicity e,Marital_Status m,VISA_Type v,Veteran_Status ve,Relationship r ");
			query.append("where EMP_ID=? and o.ETHINICITY = e.id and o.MARITAL_STAT = m.id and o.VISA_TYPE=v.id and o.VET_STAT=ve.id and o.EMC_REL=r.id order by seq_no desc");*/
//			query.append("select SEQ_NO,GENDER,ETHINICITY,MARITAL_STAT,CITIZENSHIP,VISA_TYPE,DATE_FORMAT(I9_EXP_DATE,'%m/%d/%Y') I9_EXP_DATE,VET_STAT,");
//			query.append("IS_DISABLED,DISABILITY_DESC,CITY,DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE, ");
//			query.append("EMC_L_NAME,EMC_F_NAME,EMC_REL,EMC_H_PHONE,EMC_M_PHONE,EMC_EMAIL,DATE_UPDATED,UPDATED_BY from EMP_OTHER_DATA_HIS where EMP_ID=? order by seq_no desc");
			
			query.append("select e.SEQ_NO,GENDER,et.value ETHINICITY,m.value MARITAL_STAT,CITIZENSHIP,v.value VISA_TYPE,DATE_FORMAT(I9_EXP_DATE,'%m/%d/%Y') I9_EXP_DATE,ve.value VET_STAT,");
			query.append("IS_DISABLED,DISABILITY_DESC,e.CITY CITY,DATE_FORMAT(e.EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE, ");
			query.append("e.EMC_L_NAME EMC_L_NAME,e.EMC_F_NAME EMC_F_NAME,r.value EMC_REL,e.EMC_H_PHONE EMC_H_PHONE,e.EMC_M_PHONE EMC_M_PHONE,e.EMC_EMAIL EMC_EMAIL,e.DATE_UPDATED DATE_UPDATED,e.UPDATED_BY UPDATED_BY ");
			query.append("from EMP_OTHER_DATA_HIS e ");
			query.append("left join Ethinicity et on et.id = e.ETHINICITY ");
			query.append("left join Marital_Status m on m.id = e.MARITAL_STAT ");
			//query.append("left join Citizenship_Status c on c.id = e.CITIZENSHIP ");
			query.append("left join VISA_Type v on v.id = e.VISA_TYPE ");
			query.append("left join Veteran_Status ve on ve.id = e.VET_STAT ");
			query.append("left join Relationship r on r.id = e.EMC_REL ");
			query.append("where e.EMP_ID=? order by seq_no desc");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, empId);
			rs = ps.executeQuery();
			while(rs.next()){
				list.add(setEmployeeOtherDetails(rs));
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeeOtherDetailsHistory "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}
	public EmployeeOtherDTO getEmployeeOtherDetailsFromHistoryAjax(String seqNo){
		logger.info("in getEmployeeOtherDetailsFromHistoryAjax");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			query.append("select EMP_ID,SEQ_NO,GENDER,ETHINICITY,MARITAL_STAT,CITIZENSHIP,VISA_TYPE,DATE_FORMAT(I9_EXP_DATE,'%m/%d/%Y') I9_EXP_DATE,VET_STAT,");
			query.append("IS_DISABLED,DISABILITY_DESC,CITY,DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE, ");
			query.append("EMC_L_NAME,EMC_F_NAME,EMC_REL,EMC_H_PHONE,EMC_M_PHONE,EMC_EMAIL,DATE_UPDATED,UPDATED_BY from EMP_OTHER_DATA_HIS where SEQ_NO=?");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, seqNo);
			rs = ps.executeQuery();
			if(rs.next()){
				EmployeeOtherDTO dto = setEmployeeOtherDetails(rs);
				dto.setEmployeeId(rs.getString("EMP_ID"));
				EmployeeDTO dto1 = getEmployeeDetails(rs.getString("EMP_ID"));
				dto.setFirstName(dto1.getFirstName());
				dto.setLastName(dto1.getLastName());
				dto.setMiddleName(dto1.getMiddleName());
				return dto;
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeeOtherDetailsFromHistoryAjax "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return null;
	}
	private EmployeeOtherDTO setEmployeeOtherDetails(ResultSet rs) throws java.sql.SQLException{
		EmployeeOtherDTO dto = new EmployeeOtherDTO();
		dto.setSeqNo(rs.getString("SEQ_NO"));
		dto.setGender(rs.getString("GENDER"));
		dto.setEthinicity(rs.getString("ETHINICITY"));
		dto.setMaritalStatus(rs.getString("MARITAL_STAT"));
		dto.setCitizenShip(rs.getString("CITIZENSHIP"));
		dto.setVisaType(rs.getString("VISA_TYPE"));
		dto.setI9ExpDate(rs.getString("I9_EXP_DATE"));
		dto.setVeteranStatus(rs.getString("VET_STAT"));
		dto.setDisability(rs.getString("IS_DISABLED"));
		dto.setDisabilityDesc(rs.getString("DISABILITY_DESC"));
		dto.setCity(rs.getString("CITY"));
		dto.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
		dto.setEmergencyLastName(rs.getString("EMC_L_NAME"));
		dto.setEmergencyFirstName(rs.getString("EMC_F_NAME"));
		dto.setEmergencyRelationShip(rs.getString("EMC_REL"));
		dto.setEmergencyMobilePhone(rs.getString("EMC_M_PHONE"));
		dto.setEmergencyHomePhone(rs.getString("EMC_H_PHONE"));
		dto.setEmergencyEmail(rs.getString("EMC_EMAIL"));
		EmployeeDTO dto1 = dummyBlob(new EmployeeDTO());
		dto.setPicture(dto1.getPicture());
		return dto;
	}
	public String getEffectiveDate(Connection conn,PreparedStatement ps,ResultSet rs,String tableName,String empId) throws SQLException{
		ps = conn.prepareStatement("select DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE from "+tableName+" where EMP_ID=?");
		ps.setString(1, empId);
		rs = ps.executeQuery();
		if(rs.next())
			return rs.getString("EFFECTIVE_DATE");
		else
			return"";
	}
	public EmployeeCompensationDTO getEmployeeCompensationDetails(String empId){
		logger.info("in getEmployeeCompensationDetails");
		EmployeeCompensationDTO dto = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			query.append("SELECT SEQ_NO,EMP_ID,DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,COMP_ACTION_TYPE,COMP_REASON,PAY_ENTITY,PAY_GROUP,PAY_FREQ,DATE_FORMAT(LAST_EVALUATION_DATE,'%m/%d/%Y') LAST_EVALUATION_DATE,GRADE,DATE_FORMAT(NEXT_EVAL_DUE_DATE,'%m/%d/%Y') NEXT_EVAL_DUE_DATE,");
			query.append("SCHEDULED_HOURS,HOURS_FREQUENCY,PAY_PERIOD_HRS,WEEKLY_HOURS,BASE_RATE,BASE_RATE_FREQ,PERIOD_RATE,HOURLY_RATE,DEFAULT_EARNING_CODE,");
			query.append("JOB_GROUP,USE_JOB_RATE,PERFORMACE_PLAN,BONUS_PLAN FROM EMP_COMPENSATION where EMP_ID=?");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, empId);
			rs = ps.executeQuery();
			if(rs.next()){
				return setEmployeeCompensationDetails(rs);
			}else{
				dto = new EmployeeCompensationDTO();
				dto.setEmployeeId(empId);
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeeDetails "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return dto;
	}

	@SuppressWarnings("deprecation")
	public String saveEmployeePosition(EmployeePositionDTO employeePositionDTO,TelligentUser telligentUser,MessageHandler messageHandler) {
		logger.info("in saveEmployeePosition DAO");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("select emp_id from EMP_POSITION_DATA where emp_id=?");
			ps.setString(1, employeePositionDTO.getEmployeeId());
			rs = ps.executeQuery();
			if(rs.next()){
				String str = getEffectiveDate(conn,ps,rs,"EMP_POSITION_DATA",employeePositionDTO.getEmployeeId());
				boolean flag = DateUtility.compareDates(employeePositionDTO.getEffectiveDate(), str);
				ps.close();
				if(flag){
					conn.setAutoCommit(false);
					query.append("update EMP_POSITION_DATA set STAT_CODE=?,STAT_CODE_REASON=?,SUPERVISOR=?,TEAM=?,POSITION=?,POSITION_LEVEL=?,PRIM_JOB=?,PRIM_JOB_LEVEL=?,UNION_CODE=?, ");
					query.append("ORG_LEVEL_1=?,ORG_LEVEL_2=?,ORG_LEVEL_3=?,ORG_LEVEL_4=?,ORG_LEVEL_5=?,ORG_LEVEL_6=?,ORG_LEVEL_7=?,ORG_LEVEL_8=?,ORG_LEVEL_9=?,ORG_LEVEL_10=?, ");
					query.append("EFFECTIVE_DATE=?,END_EFFECTIVE_DATE=?,DATE_UPDATED=sysdate(),UPDATED_BY=? where EMP_ID=?");
					ps = conn.prepareStatement(query.toString());
					setPSforEmpPosition(employeePositionDTO, ps, telligentUser, messageHandler,"update");
				}else{
					return "error:;Effective Date should be greater than current Effective Date";
				}
			}else{
				ps.close();
				conn.setAutoCommit(false);
				query.append("insert into EMP_POSITION_DATA(STAT_CODE,STAT_CODE_REASON,SUPERVISOR,TEAM,POSITION,POSITION_LEVEL,PRIM_JOB,PRIM_JOB_LEVEL,UNION_CODE, ");
				query.append("ORG_LEVEL_1,ORG_LEVEL_2,ORG_LEVEL_3,ORG_LEVEL_4,ORG_LEVEL_5,ORG_LEVEL_6,ORG_LEVEL_7,ORG_LEVEL_8,ORG_LEVEL_9,ORG_LEVEL_10, ");
				query.append("EFFECTIVE_DATE,END_EFFECTIVE_DATE,DATE_UPDATED,UPDATED_BY,EMP_ID) ");
				query.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),?,?)");
				ps = conn.prepareStatement(query.toString());
				setPSforEmpPosition(employeePositionDTO, ps, telligentUser, messageHandler,"save");
			}
			int i = ps.executeUpdate();
			if(i>0){
				conn.commit();
				return "Details Saved Succuessfully";
			}else{
				conn.rollback();
				return "error:;Details Not Saved";
			}
		}catch (Exception ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {}
			ex.printStackTrace();
			employeePositionDTO.setErrorMessage("error ::"+ex.getMessage());
			logger.info("Excpetion in saveEmployeePosition :: "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return null;
	}
	@SuppressWarnings("deprecation")
	public void setPSforEmpPosition(EmployeePositionDTO employeePositionDTO,PreparedStatement ps,TelligentUser telligentUser,MessageHandler messageHandler,String operation) throws SQLException{
		ps.setString(1, employeePositionDTO.getStatusCode());
		ps.setString(2, employeePositionDTO.getStatusReason());
		ps.setString(3, employeePositionDTO.getSupervisor());
		ps.setString(4, employeePositionDTO.getTeam());
		ps.setString(5, employeePositionDTO.getPosition());
		ps.setString(6, employeePositionDTO.getPositionLevel());
		ps.setString(7, employeePositionDTO.getPrimaryJob());
		ps.setString(8, employeePositionDTO.getPrimaryJobLeave());
		ps.setString(9, employeePositionDTO.getUnionCode());
		ps.setString(10, employeePositionDTO.getOrg1());
		ps.setString(11, employeePositionDTO.getOrg2());
		ps.setString(12, employeePositionDTO.getOrg3());
		ps.setString(13, employeePositionDTO.getOrg4());
		ps.setString(14, employeePositionDTO.getOrg5());
		ps.setString(15, employeePositionDTO.getOrg6());
		ps.setString(16, employeePositionDTO.getOrg7());
		ps.setString(17, employeePositionDTO.getOrg8());
		ps.setString(18, employeePositionDTO.getOrg9());
		ps.setString(19, employeePositionDTO.getOrg10());
		ps.setDate(20, new java.sql.Date(new Date(employeePositionDTO.getEffectiveDate()).getTime()));
		if(operation.equalsIgnoreCase("save"))
			ps.setDate(21, new java.sql.Date(new Date(messageHandler.getMessage("effectiveEndDate")).getTime()));
		else
			ps.setDate(21, new java.sql.Date(new Date(employeePositionDTO.getEffectiveDate()).getTime()-1));
		ps.setString(22, telligentUser.getEmployeeId());
		ps.setString(23, employeePositionDTO.getEmployeeId());
	}
	
	public EmployeePositionDTO getEmployeePositionDetails(String empId){
		logger.info("in getEmployeePositionDetails");
		EmployeePositionDTO dto = new EmployeePositionDTO();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			query.append("select SEQ_NO,STAT_CODE,STAT_CODE_REASON,SUPERVISOR,TEAM,POSITION,POSITION_LEVEL,PRIM_JOB,PRIM_JOB_LEVEL,UNION_CODE, ");
			query.append("ORG_LEVEL_1,ORG_LEVEL_2,ORG_LEVEL_3,ORG_LEVEL_4,ORG_LEVEL_5,ORG_LEVEL_6,ORG_LEVEL_7,ORG_LEVEL_8,ORG_LEVEL_9,ORG_LEVEL_10, ");
			query.append("DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,DATE_FORMAT(DATE_UPDATED,'%m/%d/%Y') DATE_UPDATED,UPDATED_BY from EMP_POSITION_DATA where EMP_ID=?");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, empId);
			rs = ps.executeQuery();
			if(rs.next()){
				EmployeeDTO dto1 = getEmployeeDetails(empId);
				dto = setEmployeePositionDetails(rs);
				dto.setEmployeeId(dto1.getEmployeeId());
				dto.setLastName(dto1.getLastName());
				dto.setFirstName(dto1.getFirstName());
				dto.setMiddleName(dto1.getMiddleName());
			}else{
				EmployeeDTO dto1 = dummyBlob(new EmployeeDTO());
				dto.setPicture(dto1.getPicture());
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeePositionDetails "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return dto;
	}
	public ArrayList<EmployeePositionDTO> getEmployeePositionDetailsHistory(String empId){
		logger.info("in getEmployeePositionDetailsHistory");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		ArrayList<EmployeePositionDTO> list = new ArrayList<EmployeePositionDTO>();
		try{
			//query.append("select SEQ_NO,STAT_CODE,STAT_CODE_REASON,SUPERVISOR,TEAM,POSITION,POSITION_LEVEL,PRIM_JOB,PRIM_JOB_LEVEL,UNION_CODE, ");
			//query.append("ORG_LEVEL_1,ORG_LEVEL_2,ORG_LEVEL_3,ORG_LEVEL_4,ORG_LEVEL_5,ORG_LEVEL_6,ORG_LEVEL_7,ORG_LEVEL_8,ORG_LEVEL_9,ORG_LEVEL_10, ");
			//query.append("DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,DATE_FORMAT(DATE_UPDATED,'%m/%d/%Y') DATE_UPDATED,UPDATED_BY from EMP_POSITION_DATA_HIS where EMP_ID=? order by seq_no desc");
			
			query.append("select e.SEQ_NO SEQ_NO,sr.value STAT_CODE,sr.value STAT_CODE_REASON,CONCAT(ep.EMP_ID,',',ep.L_NAME, ',', ep.F_NAME) SUPERVISOR,t.team_name TEAM,pm.value POSITION,pl.value POSITION_LEVEL,pj.value PRIM_JOB,pjl.value PRIM_JOB_LEVEL,uc.value UNION_CODE, "); 
			query.append("ORG_LEVEL_1,ORG_LEVEL_2,ORG_LEVEL_3,ORG_LEVEL_4,ORG_LEVEL_5,ORG_LEVEL_6,ORG_LEVEL_7,ORG_LEVEL_8,ORG_LEVEL_9,ORG_LEVEL_10,  ");
			query.append("DATE_FORMAT(e.EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,DATE_FORMAT(e.DATE_UPDATED,'%m/%d/%Y') DATE_UPDATED,e.UPDATED_BY "); 
			query.append("from EMP_POSITION_DATA_HIS e ");
			query.append("left join Status_Code sc on sc.id = e.STAT_CODE ");
			query.append("left join Status_Reason sr on sr.id = e.STAT_CODE_REASON ");
			query.append("left join position_master pm on pm.id = e.POSITION ");
			query.append("left join position_level pl on pl.id = e.POSITION_LEVEL ");
			query.append("left join primary_job pj on pj.id = e.PRIM_JOB ");
			query.append("left join primary_job_leave pjl on pjl.id = e.PRIM_JOB_LEVEL ");
			query.append("left join union_code uc on uc.id = e.UNION_CODE ");
			query.append("left join EMP_PERSONAL ep on ep.EMP_ID = e.SUPERVISOR ");
			query.append("left join team t on t.team_id = e.team ");
			query.append("where e.EMP_ID=? order by seq_no desc"); 
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, empId);
			rs = ps.executeQuery();
			while(rs.next()){
				list.add(setEmployeePositionDetails(rs));
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeePositionDetailsHistory "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}
	public EmployeePositionDTO getEmployeePositionDetailsFromHistoryAjax(String seqNo){
		logger.info("in getEmployeePositionDetailsFromHistoryAjax");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			query.append("select SEQ_NO,STAT_CODE,STAT_CODE_REASON,SUPERVISOR,TEAM,POSITION,POSITION_LEVEL,PRIM_JOB,PRIM_JOB_LEVEL,UNION_CODE, ");
			query.append("ORG_LEVEL_1,ORG_LEVEL_2,ORG_LEVEL_3,ORG_LEVEL_4,ORG_LEVEL_5,ORG_LEVEL_6,ORG_LEVEL_7,ORG_LEVEL_8,ORG_LEVEL_9,ORG_LEVEL_10, ");
			query.append("DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,DATE_FORMAT(DATE_UPDATED,'%m/%d/%Y') DATE_UPDATED,UPDATED_BY,EMP_ID from EMP_POSITION_DATA_HIS where SEQ_NO=?");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, seqNo);
			rs = ps.executeQuery();
			if(rs.next()){
				EmployeePositionDTO dto = setEmployeePositionDetails(rs);
				dto.setEmployeeId(rs.getString("EMP_ID"));
				EmployeeDTO dto1 = getEmployeeDetails(rs.getString("EMP_ID"));
				dto.setFirstName(dto1.getFirstName());
				dto.setLastName(dto1.getLastName());
				dto.setMiddleName(dto1.getMiddleName());
				return dto;
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeePositionDetailsFromHistoryAjax "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return null;
	}
	private EmployeePositionDTO setEmployeePositionDetails(ResultSet rs) throws java.sql.SQLException{
		EmployeePositionDTO dto = new EmployeePositionDTO();
		dto.setSeqNo(rs.getString("SEQ_NO"));
		dto.setStatusCode(rs.getString("STAT_CODE"));
		dto.setStatusReason(rs.getString("STAT_CODE_REASON"));
		dto.setSupervisor(rs.getString("SUPERVISOR"));
		dto.setTeam(rs.getString("TEAM"));
		dto.setPosition(rs.getString("POSITION"));
		dto.setPositionLevel(rs.getString("POSITION_LEVEL"));
		dto.setPrimaryJob(rs.getString("PRIM_JOB"));
		dto.setPrimaryJobLeave(rs.getString("PRIM_JOB_LEVEL"));
		dto.setUnionCode(rs.getString("UNION_CODE"));
		dto.setOrg1(rs.getString("ORG_LEVEL_1"));
		dto.setOrg2(rs.getString("ORG_LEVEL_2"));
		dto.setOrg3(rs.getString("ORG_LEVEL_3"));
		dto.setOrg4(rs.getString("ORG_LEVEL_4"));
		dto.setOrg5(rs.getString("ORG_LEVEL_5"));
		dto.setOrg6(rs.getString("ORG_LEVEL_6"));
		dto.setOrg7(rs.getString("ORG_LEVEL_7"));
		dto.setOrg8(rs.getString("ORG_LEVEL_8"));
		dto.setOrg9(rs.getString("ORG_LEVEL_9"));
		dto.setOrg10(rs.getString("ORG_LEVEL_10"));
		dto.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
		dto.setUpdatedBy(rs.getString("UPDATED_BY"));
		dto.setUpdatedDate(rs.getString("DATE_UPDATED"));
		EmployeeDTO dto1 = dummyBlob(new EmployeeDTO());
		dto.setPicture(dto1.getPicture());
		return dto;
	}
	public EmployeeCompensationDTO getEmployeeCompDetailsFromHistoryAjax(String seqNo){
		logger.info("in getEmployeeCompDetailsFromHistoryAjax");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			
			query.append("SELECT SEQ_NO,EMP_ID,DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,COMP_ACTION_TYPE,COMP_REASON,PAY_ENTITY,PAY_GROUP,PAY_FREQ,DATE_FORMAT(LAST_EVALUATION_DATE,'%m/%d/%Y') LAST_EVALUATION_DATE,GRADE,DATE_FORMAT(NEXT_EVAL_DUE_DATE,'%m/%d/%Y') NEXT_EVAL_DUE_DATE,");
			query.append("SCHEDULED_HOURS,HOURS_FREQUENCY,PAY_PERIOD_HRS,WEEKLY_HOURS,BASE_RATE,BASE_RATE_FREQ,PERIOD_RATE,HOURLY_RATE,DEFAULT_EARNING_CODE,");
			query.append("JOB_GROUP,USE_JOB_RATE,PERFORMACE_PLAN,BONUS_PLAN FROM EMP_COMPENSATION_HIS WHERE SEQ_NO=? ");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, seqNo);
			rs = ps.executeQuery();
			if(rs.next()){
				EmployeeCompensationDTO dto = setEmployeeCompensationDetails(rs);
				dto.setEmployeeId(rs.getString("EMP_ID"));
				EmployeeDTO dto1 = getEmployeeDetails(rs.getString("EMP_ID"));
				dto.setFirstName(dto1.getFirstName());
				dto.setLastName(dto1.getLastName());
				dto.setMiddleName(dto1.getMiddleName());
				return dto;
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployeeCompDetailsFromHistoryAjax "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return null;
	}
	@SuppressWarnings("deprecation")
	public String saveEmployement(EmploymentDTO employmentDTO,TelligentUser telligentUser,MessageHandler messageHandler) {
		logger.info("in saveEmployement DAO");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("select emp_id from EMP_EMPLOYEMENT where emp_id=?");
			ps.setString(1, employmentDTO.getEmployeeId());
			rs = ps.executeQuery();
			if(rs.next()){
				String str = getEffectiveDate(conn,ps,rs,"EMP_EMPLOYEMENT",employmentDTO.getEmployeeId());
				boolean flag = DateUtility.compareDates(employmentDTO.getEffectiveDate(), str);
				ps.close();
				if(flag){
					conn.setAutoCommit(false);
					query.append("update EMP_EMPLOYEMENT set STAT_CODE=?,STAT_CODE_REASON=?,STATUS=?,HIRE_DATE=?,LAST_HIRE_DATE=?,SENIORITY_DATE=?,BENEFIT_DATE=?,TERM_DATE=?,FLSA_CATEGORY=?, ");
					query.append("CLASSIFICATION=?,EMPL_CATEGORY=?,FTE=?,LEAV_STAT_CODE=?,LEAV_REASON=?,LEAV_START_DATE=?,LEAV_END_DATE=?, ");
					query.append("EFFECTIVE_DATE=?,END_EFFECTIVE_DATE=?,DATE_UPDATED=sysdate(),UPDATED_BY=? where EMP_ID=?");
					ps = conn.prepareStatement(query.toString());
					setPSforEmployment(employmentDTO, ps, telligentUser, messageHandler,"update");
				}else{
					return "error:;Effective Date should be greater than current Effective Date";
				}
			}else{
				ps.close();
				conn.setAutoCommit(false);
				query.append("insert into EMP_EMPLOYEMENT(STAT_CODE,STAT_CODE_REASON,STATUS,HIRE_DATE,LAST_HIRE_DATE,SENIORITY_DATE,BENEFIT_DATE,TERM_DATE,FLSA_CATEGORY, ");
				query.append("CLASSIFICATION,EMPL_CATEGORY,FTE,LEAV_STAT_CODE,LEAV_REASON,LEAV_START_DATE,LEAV_END_DATE, ");
				query.append("EFFECTIVE_DATE,END_EFFECTIVE_DATE,DATE_UPDATED,UPDATED_BY,EMP_ID) ");
				query.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate(),?,?)");
				ps = conn.prepareStatement(query.toString());
				setPSforEmployment(employmentDTO, ps, telligentUser, messageHandler,"save");
			}
			int i = ps.executeUpdate();
			if(i>0){
				conn.commit();
				return "Details Saved Succuessfully";
			}else{
				conn.rollback();
				return "error:;Details Not Saved";
			}
		}catch (Exception ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {}
			ex.printStackTrace();
			employmentDTO.setErrorMessage("error ::"+ex.getMessage());
			logger.info("Excpetion in saveEmployement :: "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return null;
	}
	@SuppressWarnings("deprecation")
	public void setPSforEmployment(EmploymentDTO dto,PreparedStatement ps,TelligentUser telligentUser,MessageHandler handler,String operation) throws java.sql.SQLException,IOException{
		ps.setString(1, dto.getStatusCode());
		ps.setString(2, dto.getStatusReason());
		ps.setString(3, dto.getStatus());
		
		if(dto.getMostRecentHireDate() !=null && !dto.getMostRecentHireDate().equalsIgnoreCase(""))
			ps.setDate(4, new java.sql.Date(new Date(dto.getMostRecentHireDate()).getTime()));
		else
			ps.setDate(4, null);
		if(dto.getLastHireDate() !=null && !dto.getLastHireDate().equalsIgnoreCase(""))
			ps.setDate(5, new java.sql.Date(new Date(dto.getLastHireDate()).getTime()));
		else
			ps.setDate(5, null);
		if(dto.getSeniorityDate() !=null && !dto.getSeniorityDate().equalsIgnoreCase(""))
			ps.setDate(6, new java.sql.Date(new Date(dto.getSeniorityDate()).getTime()));
		else
			ps.setDate(6, null);
		if(dto.getBenefitStartDate() !=null && !dto.getBenefitStartDate().equalsIgnoreCase(""))
			ps.setDate(7, new java.sql.Date(new Date(dto.getBenefitStartDate()).getTime()));
		else
			ps.setDate(7, null);
		if(dto.getTerminationDate() !=null && !dto.getTerminationDate().equalsIgnoreCase(""))
			ps.setDate(8, new java.sql.Date(new Date(dto.getTerminationDate()).getTime()));
		else
			ps.setDate(8, null);
		ps.setString(9, dto.getFLSACategory());
		ps.setString(10, dto.getClassification());
		ps.setString(11, dto.getEmploymentCategory());
		ps.setString(12, dto.getFullTimeEquivalency());
		ps.setString(13, dto.getLeaveStatusCode());
		ps.setString(14, dto.getLeaveReason());
		if(dto.getLeaveStartDate() !=null && !dto.getLeaveStartDate().equalsIgnoreCase(""))
			ps.setDate(15, new java.sql.Date(new Date(dto.getLeaveStartDate()).getTime()));
		else
			ps.setDate(15, null);
		if(dto.getExpectedLeaveEndDate() !=null && !dto.getExpectedLeaveEndDate().equalsIgnoreCase(""))
			ps.setDate(16, new java.sql.Date(new Date(dto.getExpectedLeaveEndDate()).getTime()));
		else
			ps.setDate(16, null);
		ps.setDate(17, new java.sql.Date(new Date(dto.getEffectiveDate()).getTime()));
		if(operation.equalsIgnoreCase("save"))
			ps.setDate(18, new java.sql.Date(new Date(handler.getMessage("effectiveEndDate")).getTime()));
		else
			ps.setDate(18, new java.sql.Date(new Date(dto.getEffectiveDate()).getTime()-1));
		ps.setString(19, telligentUser.getEmployeeId());
		ps.setString(20, dto.getEmployeeId());
	}
	
	public EmploymentDTO getEmployementDetails(String empId){
		logger.info("in getEmployementDetails");
		EmploymentDTO dto = new EmploymentDTO();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			query.append("select SEQ_NO,STAT_CODE,STAT_CODE_REASON,STATUS,DATE_FORMAT(HIRE_DATE,'%m/%d/%Y') HIRE_DATE,DATE_FORMAT(LAST_HIRE_DATE,'%m/%d/%Y') LAST_HIRE_DATE,DATE_FORMAT(SENIORITY_DATE,'%m/%d/%Y') SENIORITY_DATE,DATE_FORMAT(BENEFIT_DATE,'%m/%d/%Y') BENEFIT_DATE,DATE_FORMAT(TERM_DATE,'%m/%d/%Y') TERM_DATE,FLSA_CATEGORY, ");
			query.append("CLASSIFICATION,EMPL_CATEGORY,FTE,LEAV_STAT_CODE,LEAV_REASON,DATE_FORMAT(LEAV_START_DATE,'%m/%d/%Y') LEAV_START_DATE,DATE_FORMAT(LEAV_END_DATE,'%m/%d/%Y') LEAV_END_DATE, ");
			query.append("DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,DATE_FORMAT(DATE_UPDATED,'%m/%d/%Y') DATE_UPDATED,UPDATED_BY from EMP_EMPLOYEMENT where EMP_ID=?");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, empId);
			rs = ps.executeQuery();
			if(rs.next()){
				EmployeeDTO dto1 = getEmployeeDetails(empId);
				dto = setEmployementDetails(rs);
				dto.setEmployeeId(dto1.getEmployeeId());
				dto.setLastName(dto1.getLastName());
				dto.setFirstName(dto1.getFirstName());
				dto.setMiddleName(dto1.getMiddleName());
			}else{
				EmployeeDTO dto1 = dummyBlob(new EmployeeDTO());
				dto.setPicture(dto1.getPicture());
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployementDetails "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return dto;
	}
	public ArrayList<EmploymentDTO> getEmployementDetailsHistory(String empId){
		logger.info("in getEmployementDetailsHistory");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		ArrayList<EmploymentDTO> list = new ArrayList<EmploymentDTO>();
		try{
			query.append("select e.SEQ_NO,sc.value STAT_CODE,sr.value STAT_CODE_REASON,s.value STATUS,DATE_FORMAT(HIRE_DATE,'%m/%d/%Y') HIRE_DATE,DATE_FORMAT(LAST_HIRE_DATE,'%m/%d/%Y') LAST_HIRE_DATE,DATE_FORMAT(SENIORITY_DATE,'%m/%d/%Y') SENIORITY_DATE,DATE_FORMAT(BENEFIT_DATE,'%m/%d/%Y') BENEFIT_DATE,DATE_FORMAT(TERM_DATE,'%m/%d/%Y') TERM_DATE,flsa.value FLSA_CATEGORY, ");
			query.append("c.value CLASSIFICATION,ec.value EMPL_CATEGORY,fte.value FTE,lsc.value LEAV_STAT_CODE,lsr.value LEAV_REASON,DATE_FORMAT(LEAV_START_DATE,'%m/%d/%Y') LEAV_START_DATE,DATE_FORMAT(LEAV_END_DATE,'%m/%d/%Y') LEAV_END_DATE, ");
			query.append("DATE_FORMAT(e.EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,DATE_FORMAT(e.DATE_UPDATED,'%m/%d/%Y') DATE_UPDATED,e.UPDATED_BY UPDATED_BY,e.EMP_ID  EMP_ID ");
			query.append("from EMP_EMPLOYEMENT_HIS e ");
			query.append("left join Status_Code sc on sc.id = e.STAT_CODE ");
			query.append("left join Status_Reason sr on sr.id = e.STAT_CODE_REASON ");
			query.append("left join Status s on s.id = e.STATUS ");
			query.append("left join FLSA_Category flsa on flsa.id = e.FLSA_CATEGORY ");
			query.append("left join Classification c on c.id = e.CLASSIFICATION ");
			query.append("left join Employement_Category ec on ec.id = e.EMPL_CATEGORY ");
			query.append("left join Full_Time_Equivalency fte on fte.id = e.FTE ");
			query.append("left join Leave_Status_code lsc on lsc.id = e.LEAV_STAT_CODE ");
			query.append("left join Leave_Status_Reason lsr on lsr.id = e.LEAV_REASON ");
			query.append("where e.EMP_ID=? order by seq_no desc"); 
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, empId);
			rs = ps.executeQuery();
			while(rs.next()){
				EmploymentDTO dto = setEmployementDetails(rs);
				dto.setEmployeeId(empId);
				list.add(dto);
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployementDetailsHistory "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}
	public EmploymentDTO getEmployementDetailsFromHistoryAjax(String seqNo){
		logger.info("in getEmployementDetailsFromHistoryAjax");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		try{
			query.append("select SEQ_NO,STAT_CODE,STAT_CODE_REASON,STATUS,DATE_FORMAT(HIRE_DATE,'%m/%d/%Y') HIRE_DATE,DATE_FORMAT(LAST_HIRE_DATE,'%m/%d/%Y') LAST_HIRE_DATE,DATE_FORMAT(SENIORITY_DATE,'%m/%d/%Y') SENIORITY_DATE,DATE_FORMAT(BENEFIT_DATE,'%m/%d/%Y') BENEFIT_DATE,DATE_FORMAT(TERM_DATE,'%m/%d/%Y') TERM_DATE,FLSA_CATEGORY, ");
			query.append("CLASSIFICATION,EMPL_CATEGORY,FTE,LEAV_STAT_CODE,LEAV_REASON,DATE_FORMAT(LEAV_START_DATE,'%m/%d/%Y') LEAV_START_DATE,DATE_FORMAT(LEAV_END_DATE,'%m/%d/%Y') LEAV_END_DATE, ");
			query.append("DATE_FORMAT(EFFECTIVE_DATE,'%m/%d/%Y') EFFECTIVE_DATE,DATE_FORMAT(DATE_UPDATED,'%m/%d/%Y') DATE_UPDATED,UPDATED_BY,EMP_ID from EMP_EMPLOYEMENT_HIS where SEQ_NO=?");
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, seqNo);
			rs = ps.executeQuery();
			if(rs.next()){
				EmploymentDTO dto = setEmployementDetails(rs);
				dto.setEmployeeId(rs.getString("EMP_ID"));
				EmployeeDTO dto1 = getEmployeeDetails(rs.getString("EMP_ID"));
				dto.setFirstName(dto1.getFirstName());
				dto.setLastName(dto1.getLastName());
				dto.setMiddleName(dto1.getMiddleName());
				return dto;
			}
		}catch (Exception ex) {
			logger.info("Excpetion in getEmployementDetailsFromHistoryAjax "+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return null;
	}
	private EmploymentDTO setEmployementDetails(ResultSet rs) throws SQLException{
		EmploymentDTO dto = new EmploymentDTO();
		dto.setSeqNo(rs.getString("SEQ_NO"));
		dto.setStatusCode(rs.getString("STAT_CODE"));
		dto.setStatusReason(rs.getString("STAT_CODE_REASON"));
		dto.setStatus(rs.getString("STATUS"));
		dto.setMostRecentHireDate(rs.getString("HIRE_DATE"));
		dto.setLastHireDate(rs.getString("LAST_HIRE_DATE"));
		dto.setSeniorityDate(rs.getString("SENIORITY_DATE"));
		dto.setBenefitStartDate(rs.getString("BENEFIT_DATE"));
		dto.setTerminationDate(rs.getString("TERM_DATE"));
		dto.setFLSACategory(rs.getString("FLSA_CATEGORY"));
		dto.setClassification(rs.getString("CLASSIFICATION"));
		dto.setEmploymentCategory(rs.getString("EMPL_CATEGORY"));
		dto.setFullTimeEquivalency(rs.getString("FTE"));
		dto.setLeaveStatusCode(rs.getString("LEAV_STAT_CODE"));
		dto.setLeaveReason(rs.getString("LEAV_REASON"));
		dto.setLeaveStartDate(rs.getString("LEAV_START_DATE"));
		dto.setExpectedLeaveEndDate(rs.getString("LEAV_END_DATE"));
		dto.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
		dto.setUpdatedBy(rs.getString("UPDATED_BY"));
		dto.setUpdatedDate(rs.getString("DATE_UPDATED"));
		EmployeeDTO dto1 = dummyBlob(new EmployeeDTO());
		dto.setPicture(dto1.getPicture());
		return dto;
	}
}
