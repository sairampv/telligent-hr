package com.telligent.model.dtos;

import java.sql.Timestamp;


public class SalarPlanningDTO {
	
	private int id;
	private String coworker_name;
	private String employeeId;
	private String supervisor;
	private String jobTitle;
	private String grade;
	private String type;
	private String rate;
	private String compaRatio;
	private String minimum;
	private String midpoint;
	private String maximum;
	private String quartile;
	private String perfGrade;
	private String incrementPercentage;
	private String newRate;
	private String lumsum;
	private String updatedDate;
	
	
	
	/**
	 * @return the updatedDate
	 */
	public String getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the coworker_name
	 */
	public String getCoworker_name() {
		return coworker_name;
	}
	/**
	 * @param coworker_name the coworker_name to set
	 */
	public void setCoworker_name(String coworker_name) {
		this.coworker_name = coworker_name;
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
	 * @return the supervisor
	 */
	public String getSupervisor() {
		return supervisor;
	}
	/**
	 * @param supervisor the supervisor to set
	 */
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}
	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}
	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the rate
	 */
	public String getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(String rate) {
		if (rate == null || rate.equals("")){
			rate = "0.0";
		}
		this.rate = rate;
	}
	/**
	 * @return the compaRatio
	 */
	public String getCompaRatio() {
		return compaRatio;
	}
	/**
	 * @param compaRatio the compaRatio to set
	 */
	public void setCompaRatio(String compaRatio) {
		
		this.compaRatio = compaRatio;
	}
	/**
	 * @return the minimum
	 */
	public String getMinimum() {
		return minimum;
	}
	/**
	 * @param minimum the minimum to set
	 */
	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}
	/**
	 * @return the midpoint
	 */
	public String getMidpoint() {
		return midpoint;
	}
	/**
	 * @param midpoint the midpoint to set
	 */
	public void setMidpoint(String midpoint) {
		this.midpoint = midpoint;
	}
	/**
	 * @return the maximum
	 */
	public String getMaximum() {
		return maximum;
	}
	/**
	 * @param maximum the maximum to set
	 */
	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}
	/**
	 * @return the quartile
	 */
	public String getQuartile() {
		return quartile;
	}
	/**
	 * @param quartile the quartile to set
	 */
	public void setQuartile(String quartile) {
		this.quartile = quartile;
	}
	/**
	 * @return the perfGrade
	 */
	public String getPerfGrade() {
		return perfGrade;
	}
	/**
	 * @param perfGrade the perfGrade to set
	 */
	public void setPerfGrade(String perfGrade) {
		this.perfGrade = perfGrade;
	}
	/**
	 * @return the incrementPercentage
	 */
	public String getIncrementPercentage() {
		return incrementPercentage;
	}
	/**
	 * @param incrementPercentage the incrementPercentage to set
	 */
	public void setIncrementPercentage(String incrementPercentage) {
		this.incrementPercentage = incrementPercentage;
	}
	/**
	 * @return the newRate
	 */
	public String getNewRate() {
		return newRate;
	}
	/**
	 * @param newRate the newRate to set
	 */
	public void setNewRate(String newRate) {
		if (newRate == null || newRate.equals("")){
			newRate = "0.0";
			
		}
		this.newRate = newRate;
	}
	/**
	 * @return the lumsum
	 */
	public String getLumsum() {
		return lumsum;
	}
	/**
	 * @param lumsum the lumsum to set
	 */
	public void setLumsum(String lumsum) {
		if (lumsum == null || lumsum.equals("")){
			lumsum = "0.0";
			
		}
		this.lumsum = lumsum;
	}
	
}
