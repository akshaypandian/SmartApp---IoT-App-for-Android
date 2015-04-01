/*
 * Appliance class to hold data corresponding to each appliance
 */
package com.example.smartapp;

public class ApplianceList {
	String applianceName , startTime, endTime , runTime, constraints, wattage;

	public ApplianceList(String applianceName, String startTime,
			String endTime, String runTime, String constraints, String wattage) {
		super();
		this.applianceName = applianceName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.runTime = runTime;
		this.constraints = constraints;
		this.wattage = wattage;
	}

	public String getApplianceName() {
		return applianceName;
	}

	public void setApplianceName(String applianceName) {
		this.applianceName = applianceName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getConstraints() {
		return constraints;
	}

	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}

	public String getWattage() {
		return wattage;
	}

	public void setWattage(String wattage) {
		this.wattage = wattage;
	}

	@Override
	public String toString() {
		return "ApplianceList [applianceName=" + applianceName + ", startTime="
				+ startTime + ", endTime=" + endTime + ", runTime=" + runTime
				+ ", constraints=" + constraints + ", wattage=" + wattage + "]";
	}
	

}
