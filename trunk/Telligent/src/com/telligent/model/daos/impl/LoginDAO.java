package com.telligent.model.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.telligent.core.system.annotation.SpringBean;
import com.telligent.model.daos.ILoginDAO;
import com.telligent.model.db.AbstractDBManager;
import com.telligent.model.dtos.User;

/**
 * @author spothu
 *
 */
@SpringBean
public class LoginDAO extends AbstractDBManager implements ILoginDAO{

	public final Logger logger = Logger.getLogger(LoginDAO.class);

	/**
	 * 
	 * Method to authenticate user
	 * @param String userName
	 * 
	 */
	public User authenticateUser(String userName) {
		logger.info("in authenticateUser");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "SELECT user_name,password,employee_id,role_name FROM users a,role_master b WHERE user_name=? and a.role_id=b.role_id";
		User user = new User();
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if(rs.next()){
				user.setUserName(rs.getString("user_name"));
				user.setPassword(rs.getString("password"));
				user.setEmployeeId(rs.getString("employee_id"));
				user.setRole(rs.getString("role_name"));
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Excpetion in authenticateUser"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return user;
	}

}
