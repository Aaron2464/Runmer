package com.aaron.runmer.Objects;

import java.util.HashMap;

public class EventData {
    private String EventId;
    private String MasterUid;
    private String MasterName;
    private String MasterPhoto;
    private HashMap<String,String> UserUid;
    private String EventTitle;
    private String EventPlace;
    private String EventDate;
    private String PeopleParticipate;
    private String PeopleTotle;

    public EventData() {
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getMasterUid() {
        return MasterUid;
    }

    public void setMasterUid(String masterUid) {
        MasterUid = masterUid;
    }

    public String getMasterName() {
        return MasterName;
    }

    public void setMasterName(String masterName) {
        MasterName = masterName;
    }

    public String getMasterPhoto() {
        return MasterPhoto;
    }

    public void setMasterPhoto(String masterPhoto) {
        MasterPhoto = masterPhoto;
    }

    public HashMap<String, String> getUserUid() {
        return UserUid;
    }

    public void setUserUid(HashMap<String, String> userUid) {
        UserUid = userUid;
    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String eventTitle) {
        EventTitle = eventTitle;
    }

    public String getEventPlace() {
        return EventPlace;
    }

    public void setEventPlace(String eventPlace) {
        EventPlace = eventPlace;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getPeopleParticipate() {
        return PeopleParticipate;
    }

    public void setPeopleParticipate(String peopleParticipate) {
        PeopleParticipate = peopleParticipate;
    }

    public String getPeopleTotle() {
        return PeopleTotle;
    }

    public void setPeopleTotle(String peopleTotle) {
        PeopleTotle = peopleTotle;
    }
}
