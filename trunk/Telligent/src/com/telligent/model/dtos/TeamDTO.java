package com.telligent.model.dtos;

public class TeamDTO {
	
	private String teamId;
	private String teamName;
	private String departmentId;
	private String supervisorEmpId;
	private String approvalStatus;
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
	 * @return the departmentId
	 */
	public String getDepartmentId() {
		return departmentId;
	}
	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * @return the supervisorEmpId
	 */
	public String getSupervisorEmpId() {
		return supervisorEmpId;
	}
	/**
	 * @param supervisorEmpId the supervisorEmpId to set
	 */
	public void setSupervisorEmpId(String supervisorEmpId) {
		this.supervisorEmpId = supervisorEmpId;
	}
	/**
	 * @return the approvalStatus
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}
	/**
	 * @param approvalStatus the approvalStatus to set
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	
}
