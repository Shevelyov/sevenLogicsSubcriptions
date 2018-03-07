package org.dbProject.restfulcrud.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
 
@XmlRootElement(name = "CalendarEvent")
@XmlAccessorType(XmlAccessType.FIELD)
public class CalendarEvent {
	private String eventID;
	private String title;
	private String desc;
	private boolean notifyByEmail;
	private String eventUserSelectedDate;
	private String userID;
	
	public String getEventID() {
		return eventID;
	}
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public boolean isNotifyByEmail() {
		return notifyByEmail;
	}
	public void setNotifyByEmail(boolean notifyByEmail) {
		this.notifyByEmail = notifyByEmail;
	}
	public String getEventUserSelectedDate() {
		return eventUserSelectedDate;
	}
	public void setEventUserSelectedDate(String eventUserSelectedDate) {
		this.eventUserSelectedDate = eventUserSelectedDate;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	
}
