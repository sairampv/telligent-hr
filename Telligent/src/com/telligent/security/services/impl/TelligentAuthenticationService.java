package com.telligent.security.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.telligent.common.user.TelligentUser;
import com.telligent.model.daos.impl.LoginDAO;
import com.telligent.model.dtos.User;
import com.telligent.util.BouncyCastleEncryptor;

public class TelligentAuthenticationService implements UserDetailsService{

	@Autowired
	BouncyCastleEncryptor bouncyCastleEncryptor;
	
	/*@Autowired
	public TelligentAuthenticationService(ILoginDAO loginDAO){
		this.loginDAO = loginDAO;
	}*/
	
	public TelligentAuthenticationService(){
	}
	
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		UserDetails userDetails =  null;
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		try {
			User user = new LoginDAO().authenticateUser(userName);
			if(user.getUserName() !=null && user.getUserName().equalsIgnoreCase(userName)){
				authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
				authorities.add(new GrantedAuthorityImpl(String.valueOf(user.getRole())));
				userDetails = new TelligentUser(user.getUserName(), bouncyCastleEncryptor.decryptString(user.getPassword()), true,true,true, true, authorities, true, "", user.getEmailId(),user.getEmployeeId(),"",user.getRole(),user.isChangePassword());				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		}
		return userDetails;
	}

}
