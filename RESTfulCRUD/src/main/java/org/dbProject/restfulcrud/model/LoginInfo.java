package org.dbProject.restfulcrud.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
 
@XmlRootElement(name = "LoginInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginInfo {
	private String userID;
	private boolean loginStatus;
	private String deviceType;
	private String deviceIPAddress;
	private String operation;
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public boolean isLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(boolean loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceIPAddress() {
		return deviceIPAddress;
	}
	public void setDeviceIPAddress(String deviceIPAddress) {
		this.deviceIPAddress = deviceIPAddress;
	}

}
