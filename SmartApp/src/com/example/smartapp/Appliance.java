/*
 * Appliance class to hold data corresponding to each appliance
 */
package com.example.smartapp;

public class Appliance {
	String applianceName,wattage,startTime,endTime,runtime;

	public String getApplianceName() {
		return applianceName;
	}

	public void setApplianceName(String applianceName) {
		this.applianceName = applianceName;
	}

	public String getWattage() {
		return wattage;
	}

	public void setWattage(String wattage) {
		this.wattage = wattage;
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

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	@Override
	public String toString() {
		return "Appliance [applianceName=" + applianceName + ", wattage="
				+ wattage + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", runtime=" + runtime + "]";
	}

	public Appliance(String applianceName, String wattage, String startTime,
			String endTime, String runtime) {
		super();
		this.applianceName = applianceName;
		this.wattage = wattage;
		this.startTime = startTime;
		this.endTime = endTime;
		this.runtime = runtime;
	}

	public Appliance() {
	}
	

}
