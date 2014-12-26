package com.telligent.model.dtos;

import java.util.ArrayList;

public class EmployeeDTO {
	
	private String employeeId;
	private String employeeNo;
	private String employeeName; // First Name
	private String teamId;
	private String teamName;
	private String lastName;
	private String salary;
	private ArrayList<TeamDTO> teamsList = new ArrayList<TeamDTO>();
	private String socialSecNo;
	private String badgeNo;
	private String dateOfBirth;
	private String effectiveDate;
	private String citizenship;
	private String maritalStatus;
	private String veteranStatus;
	private String visaType;
	private String visaExpDate;
	private boolean minor;
	private String gender;
	private String ethinicity;
	private String disabled;
	private String disablityDesc;
	
	private String homePhone;
	private String cellPhone;
	private String email;
	private String personalEmail;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String zipcode;
	
	private String workPhone;
	private String workExt;
	private String workEmail;
	private String workCellPhone;
	
	private String emergencyLastName;
	private String emergencyFirstName;
	private String emergencyRelationShip;
	private String emergencyHomePhone;
	private String emergencyCellPhone;
	private String emergencyEmail;
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
	 * @return the employeeNo
	 */
	public String getEmployeeNo() {
		return employeeNo;
	}
	/**
	 * @param employeeNo the employeeNo to set
	 */
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	/**
	 * @return the teamId
	 */
	public String getTeamId() {
		return teamId;
	}
	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	/**
	 * @return the teamName
	 */
	public String getTeamName() {
		return teamName;
	}
	/**
	 * @param teamName the teamName to set
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
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
	/**
	 * @return the salary
	 */
	public String getSalary() {
		return salary;
	}
	/**
	 * @param salary the salary to set
	 */
	public void setSalary(String salary) {
		this.salary = salary;
	}
	/**
	 * @return the teamsList
	 */
	public ArrayList<TeamDTO> getTeamsList() {
		return teamsList;
	}
	/**
	 * @param teamsList the teamsList to set
	 */
	public void setTeamsList(ArrayList<TeamDTO> teamsList) {
		this.teamsList = teamsList;
	}
	/**
	 * @return the socialSecNo
	 */
	public String getSocialSecNo() {
		return socialSecNo;
	}
	/**
	 * @param socialSecNo the socialSecNo to set
	 */
	public void setSocialSecNo(String socialSecNo) {
		this.socialSecNo = socialSecNo;
	}
	/**
	 * @return the badgeNo
	 */
	public String getBadgeNo() {
		return badgeNo;
	}
	/**
	 * @param badgeNo the badgeNo to set
	 */
	public void setBadgeNo(String badgeNo) {
		this.badgeNo = badgeNo;
	}
	/**
	 * @return the effectiveDate
	 */
	public String getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	/**
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	/**
	 * @return the citizenship
	 */
	public String getCitizenship() {
		return citizenship;
	}
	/**
	 * @param citizenship the citizenship to set
	 */
	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}
	/**
	 * @return the maritalStatus
	 */
	public String getMaritalStatus() {
		return maritalStatus;
	}
	/**
	 * @param maritalStatus the maritalStatus to set
	 */
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	/**
	 * @return the veteranStatus
	 */
	public String getVeteranStatus() {
		return veteranStatus;
	}
	/**
	 * @param veteranStatus the veteranStatus to set
	 */
	public void setVeteranStatus(String veteranStatus) {
		this.veteranStatus = veteranStatus;
	}
	/**
	 * @return the visaType
	 */
	public String getVisaType() {
		return visaType;
	}
	/**
	 * @param visaType the visaType to set
	 */
	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}
	/**
	 * @return the visaExpDate
	 */
	public String getVisaExpDate() {
		return visaExpDate;
	}
	/**
	 * @param visaExpDate the visaExpDate to set
	 */
	public void setVisaExpDate(String visaExpDate) {
		this.visaExpDate = visaExpDate;
	}
	/**
	 * @return the isMinor
	 */
	public boolean isMinor() {
		return minor;
	}
	/**
	 * @param isMinor the isMinor to set
	 */
	public void setMinor(boolean minor) {
		this.minor = minor;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the ethinicity
	 */
	public String getEthinicity() {
		return ethinicity;
	}
	/**
	 * @param ethinicity the ethinicity to set
	 */
	public void setEthinicity(String ethinicity) {
		this.ethinicity = ethinicity;
	}
	/**
	 * @return the disabled
	 */
	public String getDisabled() {
		return disabled;
	}
	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	/**
	 * @return the disablityDesc
	 */
	public String getDisablityDesc() {
		return disablityDesc;
	}
	/**
	 * @param disablityDesc the disablityDesc to set
	 */
	public void setDisablityDesc(String disablityDesc) {
		this.disablityDesc = disablityDesc;
	}
	/**
	 * @return the homePhone
	 */
	public String getHomePhone() {
		return homePhone;
	}
	/**
	 * @param homePhone the homePhone to set
	 */
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	/**
	 * @return the cellPhone
	 */
	public String getCellPhone() {
		return cellPhone;
	}
	/**
	 * @param cellPhone the cellPhone to set
	 */
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
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
	 * @return the personalEmail
	 */
	public String getPersonalEmail() {
		return personalEmail;
	}
	/**
	 * @param personalEmail the personalEmail to set
	 */
	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}
	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}
	/**
	 * @param addressLine1 the addressLine1 to set
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}
	/**
	 * @param addressLine2 the addressLine2 to set
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}
	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	/**
	 * @return the workPhone
	 */
	public String getWorkPhone() {
		return workPhone;
	}
	/**
	 * @param workPhone the workPhone to set
	 */
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	/**
	 * @return the workExt
	 */
	public String getWorkExt() {
		return workExt;
	}
	/**
	 * @param workExt the workExt to set
	 */
	public void setWorkExt(String workExt) {
		this.workExt = workExt;
	}
	/**
	 * @return the workEmail
	 */
	public String getWorkEmail() {
		return workEmail;
	}
	/**
	 * @param workEmail the workEmail to set
	 */
	public void setWorkEmail(String workEmail) {
		this.workEmail = workEmail;
	}
	/**
	 * @return the workCellPhone
	 */
	public String getWorkCellPhone() {
		return workCellPhone;
	}
	/**
	 * @param workCellPhone the workCellPhone to set
	 */
	public void setWorkCellPhone(String workCellPhone) {
		this.workCellPhone = workCellPhone;
	}
	/**
	 * @return the emergencyLastName
	 */
	public String getEmergencyLastName() {
		return emergencyLastName;
	}
	/**
	 * @param emergencyLastName the emergencyLastName to set
	 */
	public void setEmergencyLastName(String emergencyLastName) {
		this.emergencyLastName = emergencyLastName;
	}
	/**
	 * @return the emergencyFirstName
	 */
	public String getEmergencyFirstName() {
		return emergencyFirstName;
	}
	/**
	 * @param emergencyFirstName the emergencyFirstName to set
	 */
	public void setEmergencyFirstName(String emergencyFirstName) {
		this.emergencyFirstName = emergencyFirstName;
	}
	/**
	 * @return the emergencyRelationShip
	 */
	public String getEmergencyRelationShip() {
		return emergencyRelationShip;
	}
	/**
	 * @param emergencyRelationShip the emergencyRelationShip to set
	 */
	public void setEmergencyRelationShip(String emergencyRelationShip) {
		this.emergencyRelationShip = emergencyRelationShip;
	}
	/**
	 * @return the emergencyHomePhone
	 */
	public String getEmergencyHomePhone() {
		return emergencyHomePhone;
	}
	/**
	 * @param emergencyHomePhone the emergencyHomePhone to set
	 */
	public void setEmergencyHomePhone(String emergencyHomePhone) {
		this.emergencyHomePhone = emergencyHomePhone;
	}
	/**
	 * @return the emergencyCellPhone
	 */
	public String getEmergencyCellPhone() {
		return emergencyCellPhone;
	}
	/**
	 * @param emergencyCellPhone the emergencyCellPhone to set
	 */
	public void setEmergencyCellPhone(String emergencyCellPhone) {
		this.emergencyCellPhone = emergencyCellPhone;
	}
	/**
	 * @return the emergencyEmail
	 */
	public String getEmergencyEmail() {
		return emergencyEmail;
	}
	/**
	 * @param emergencyEmail the emergencyEmail to set
	 */
	public void setEmergencyEmail(String emergencyEmail) {
		this.emergencyEmail = emergencyEmail;
	}

	
}
