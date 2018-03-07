package com.example.logics.sevenlogicssubscriptions;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable{
    private String eventId;
    private String eventTitle;
    private String eventDescription;
    private String notifyByEmail;
    private String eventDate;
    private String userId;

    public Event(String eventId, String eventTitle, String eventDescription, String notifyByEmail, String eventDate, String userId) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.notifyByEmail = notifyByEmail;
        this.eventDate = eventDate;
        this.userId = userId;
    }

    protected Event(Parcel in) {
        eventId = in.readString();
        eventTitle = in.readString();
        eventDescription = in.readString();
        notifyByEmail = in.readString();
        eventDate = in.readString();
        userId = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getNotifyByEmail() {
        return notifyByEmail;
    }

    public void setNotifyByEmail(String notifyByEmail) {
        this.notifyByEmail = notifyByEmail;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return eventTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(eventTitle);
        dest.writeString(eventDescription);
        dest.writeString(notifyByEmail);
        dest.writeString(eventDate);
        dest.writeString(userId);
    }
}
