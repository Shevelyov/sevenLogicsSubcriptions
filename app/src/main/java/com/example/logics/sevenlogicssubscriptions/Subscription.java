package com.example.logics.sevenlogicssubscriptions;

public class Subscription {
    private String subscriptionId;
    private String subscriptionType;

    public Subscription(String subscriptionType, String subscriptionId) {
        this.subscriptionId = subscriptionId;
        this.subscriptionType = subscriptionType;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    @Override
    public String toString() {
        return subscriptionType;
    }
}
