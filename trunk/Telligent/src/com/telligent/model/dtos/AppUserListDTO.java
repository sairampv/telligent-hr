package com.telligent.model.dtos;


public class AppUserListDTO{
	
	private String team;
	private String meritApprovalLevel;
	private String effectiveDate;
	private String endDate;
	/**
	 * @return the team
	 */
	public String getTeam() {
		return team;
	}
	/**
	 * @param team the team to set
	 */
	public void setTeam(String team) {
		this.team = team;
	}
	/**
	 * @return the meritApprovalLevel
	 */
	public String getMeritApprovalLevel() {
		return meritApprovalLevel;
	}
	/**
	 * @param meritApprovalLevel the meritApprovalLevel to set
	 */
	public void setMeritApprovalLevel(String meritApprovalLevel) {
		this.meritApprovalLevel = meritApprovalLevel;
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
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
