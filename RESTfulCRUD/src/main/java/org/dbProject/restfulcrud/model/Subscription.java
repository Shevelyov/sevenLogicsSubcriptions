package org.dbProject.restfulcrud.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
 
@XmlRootElement(name = "Subscription")
@XmlAccessorType(XmlAccessType.FIELD)
public class Subscription {
	private String subscriptionType;
	private String subscriptionID;
	private String amount;
	
	public String getSubscriptionType() {
		return subscriptionType;
	}
	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}
	public String getSubscriptionID() {
		return subscriptionID;
	}
	public void setSubscriptionID(String subscriptionID) {
		this.subscriptionID = subscriptionID;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
}
