package com.aaron.runmer.objects;

import java.util.HashMap;

public class EventData {
    private String EventId;
    private String mMasterUid;
    private String mMasterName;
    private String mMasterPhoto;
    private HashMap<String,String> UserUid;
    private String mEventTitle;
    private String mEventPlace;
    private String mEventDate;
    private String mPeopleParticipate;
    private String mPeopleTotle;

    // EventId && UserUid 為 Firebase 節點處理問題，無法 refactor 為 mXxxXxx;

    public EventData() {
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getMasterUid() {
        return mMasterUid;
    }

    public void setMasterUid(String masterUid) {
        mMasterUid = masterUid;
    }

    public String getMasterName() {
        return mMasterName;
    }

    public void setMasterName(String masterName) {
        mMasterName = masterName;
    }

    public String getMasterPhoto() {
        return mMasterPhoto;
    }

    public void setMasterPhoto(String masterPhoto) {
        mMasterPhoto = masterPhoto;
    }

    public HashMap<String, String> getUserUid() {
        return UserUid;
    }

    public void setUserUid(HashMap<String, String> userUid) {
        this.UserUid = userUid;
    }

    public String getEventTitle() {
        return mEventTitle;
    }

    public void setEventTitle(String eventTitle) {
        mEventTitle = eventTitle;
    }

    public String getEventPlace() {
        return mEventPlace;
    }

    public void setEventPlace(String eventPlace) {
        mEventPlace = eventPlace;
    }

    public String getEventDate() {
        return mEventDate;
    }

    public void setEventDate(String eventDate) {
        mEventDate = eventDate;
    }

    public String getPeopleParticipate() {
        return mPeopleParticipate;
    }

    public void setPeopleParticipate(String peopleParticipate) {
        mPeopleParticipate = peopleParticipate;
    }

    public String getPeopleTotle() {
        return mPeopleTotle;
    }

    public void setPeopleTotle(String peopleTotle) {
        mPeopleTotle = peopleTotle;
    }
}
