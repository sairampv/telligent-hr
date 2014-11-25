/**
 * 
 */
package com.telligent.common.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;


/**
 * @author spothu
 *
 */
public class TelligentUser extends org.springframework.security.core.userdetails.User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean accountVerified=true;
	private String email;
	private String createDate;
	private String userName;
	private String employeeId;
	private String lastName;
	
	public TelligentUser(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		// TODO Auto-generated constructor stub
	}
	public TelligentUser(String userName, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<GrantedAuthority> authorities,boolean accountVerifier,String createDate,String email,String employeeId,String lastName)
			throws IllegalArgumentException {
		super(userName, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		this.email = email;
		this.accountVerified=accountVerifier;
		this.createDate=createDate;
		this.userName = userName;
		this.employeeId = employeeId;
		this.lastName = lastName;
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the accountVerified
	 */
	public boolean isAccountVerified() {
		return accountVerified;
	}
	/**
	 * @param accountVerified the accountVerified to set
	 */
	public void setAccountVerified(boolean accountVerified) {
		this.accountVerified = accountVerified;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}
	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	
}
