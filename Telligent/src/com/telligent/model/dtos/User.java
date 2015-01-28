package com.telligent.model.dtos;

public class User {

	private String userName;
	private String password;
	private String employeeId;
	private String role;
	private String emailId;
	private boolean changePassword;
	private String pictureBase64;
	
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}
	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	/**
	 * @return the changePassword
	 */
	public boolean isChangePassword() {
		return changePassword;
	}
	/**
	 * @param changePassword the changePassword to set
	 */
	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}
	/**
	 * @return the pictureBase64
	 */
	public String getPictureBase64() {
		return pictureBase64;
	}
	/**
	 * @param pictureBase64 the pictureBase64 to set
	 */
	public void setPictureBase64(String pictureBase64) {
		this.pictureBase64 = pictureBase64;
	}
	
}
