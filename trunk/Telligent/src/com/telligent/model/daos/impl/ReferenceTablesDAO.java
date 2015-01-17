package com.telligent.model.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.telligent.common.user.TelligentUser;
import com.telligent.core.system.annotation.SpringBean;
import com.telligent.model.db.AbstractDBManager;
import com.telligent.model.dtos.MapDTO;

/**
 * @author spothu
 *
 */
@SpringBean
public class ReferenceTablesDAO extends AbstractDBManager{

	public final Logger logger = Logger.getLogger(ReferenceTablesDAO.class);

	public String saveBaseRateFreq(MapDTO dto,TelligentUser user){
		logger.info("in saveBaseRateFreq");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			if(dto.getOperation().equalsIgnoreCase("update")){
				String query = "update Base_Rate_Frequency set value=?,description=?,isActive=?,updated_date=sysdate(),updated_by=? where id=?";
				return update(conn, ps, dto, user, query, dto.getId());
			}else{
				if(!checkRecordExistence(conn, ps, rs, "Base_Rate_Frequency", dto.getValue())){
					String query = "insert into Base_Rate_Frequency (value,description,isActive,updated_date,updated_by) values(?,?,?,sysdate(),?)";
					return save(conn,ps,dto,user,query);
				}else{
					return "error:;Base Rate Frequency already exists";
				}
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Exception in saveBaseRateFreq"+ex.getMessage());
			return "error :: "+ex.getMessage();
		} finally {
			this.closeAll(conn, ps, rs);
		}
	}
	private String update(Connection conn,PreparedStatement ps,MapDTO dto,TelligentUser user,String query,String id){
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, dto.getValue());
			ps.setString(2, dto.getDescription());
			ps.setBoolean(3, Boolean.parseBoolean(dto.getIsActive()));
			ps.setString(4, user.getEmployeeId());
			ps.setString(5, id);
			int i = ps.executeUpdate();
			if(i>0)
				return "success:;Details updated Successfully";
			else
				return "error:;Details not updated";
		} catch (SQLException ex) {
			logger.error("Exception in save");
			logger.info("Exception in save"+ex.getMessage());
			return "error :: "+ex.getMessage();
		}
	}
	private String save(Connection conn,PreparedStatement ps,MapDTO dto,TelligentUser user,String query){
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, dto.getValue());
			ps.setString(2, dto.getDescription());
			ps.setBoolean(3, Boolean.parseBoolean(dto.getIsActive()));
			ps.setString(4, user.getEmployeeId());
			int i = ps.executeUpdate();
			if(i>0)
				return "success:;Details Saved Successfully";
			else
				return "error:;Details not saved";
		} catch (SQLException ex) {
			logger.error("Exception in save");
			logger.info("Exception in save"+ex.getMessage());
			return "error :: "+ex.getMessage();
		}
	}
	public boolean checkRecordExistence(Connection conn, PreparedStatement ps,ResultSet rs,String tableName,String value){
		try {
			ps = conn.prepareStatement("select value from "+tableName+" where upper(value)=?");
			ps.setString(1, value.toUpperCase());
			rs = ps.executeQuery();
			if(rs.next())
				return true;
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Exception in saveBaseRateFreq"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return false;
	}
	public ArrayList<MapDTO> getDetails(String tableName){
		logger.info("in getDetails == "+tableName);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			return getDetails( conn, ps, rs, tableName);
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Exception in getDetails"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return new ArrayList<MapDTO>();
	}
	public ArrayList<MapDTO> getDetails(Connection conn,PreparedStatement ps,ResultSet rs,String tableName){
		ArrayList<MapDTO> list = new ArrayList<MapDTO>();
		try{
			ps = conn.prepareStatement("select id,value,description,if(isActive,'true','false') isActive,DATE_FORMAT(B.updated_date,'%m/%d/%Y') updated_date,concat(B.updated_by,',',L_NAME) updatedBy from "+tableName+" B left join EMP_PERSONAL E on B.updated_by = EMP_ID");
			rs = ps.executeQuery();
			while(rs.next()){
				MapDTO dto = new MapDTO();
				dto.setId(rs.getString("id"));
				dto.setValue(rs.getString("value"));
				dto.setDescription(rs.getString("description"));
				dto.setIsActive(rs.getString("isActive"));
				dto.setUpdatedDate(rs.getString("updated_date"));
				dto.setUpdatedBy(rs.getString("updatedBy"));
				list.add(dto);
			}
		}catch(Exception e){
			logger.error("Exception in getDetails "+e.getMessage());
		}
		return list;
	}
	public MapDTO getDetailsById(String tableName,String id){
		logger.info("in getDetailsById == "+tableName);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			return getDetailsById( conn, ps, rs, tableName,id);
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Exception in getDetailsById"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return new MapDTO();
	}
	public MapDTO getDetailsById(Connection conn,PreparedStatement ps,ResultSet rs,String tableName,String id){
		MapDTO dto = new MapDTO();
		try{
			ps = conn.prepareStatement("select id,value,description,if(isActive,'true','false') isActive from "+tableName+" where id = ?");
			ps.setString(1, id);
			rs = ps.executeQuery();
			while(rs.next()){
				dto.setId(rs.getString("id"));
				dto.setValue(rs.getString("value"));
				dto.setDescription(rs.getString("description"));
				dto.setIsActive(rs.getString("isActive"));
			}
		}catch(Exception e){
			logger.error("Exception in getDetailsById "+e.getMessage());
		}
		return dto;
	}
}
