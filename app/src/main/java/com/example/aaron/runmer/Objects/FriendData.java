package com.example.aaron.runmer.Objects;

public class FriendData {
    private String FriendRequest;
    private String UserName;
    private String UserEmail;
    private String UserPhoto;
    private String UserUid;
    private String UserBirth;
    private String UserHeight;
    private String UserWeight;
    private String UserGender;
    private Double lat;
    private Double lng;

    public FriendData() {

    }

    public String getFriendRequest() {
        return FriendRequest;
    }

    public void setFriendRequest(String friendRequest) {
        FriendRequest = friendRequest;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }

    public String getUserUid() {
        return UserUid;
    }

    public void setUserUid(String userUid) {
        UserUid = userUid;
    }

    public String getUserBirth() {
        return UserBirth;
    }

    public void setUserBirth(String userBirth) {
        UserBirth = userBirth;
    }

    public String getUserHeight() {
        return UserHeight;
    }

    public void setUserHeight(String userHeight) {
        UserHeight = userHeight;
    }

    public String getUserWeight() {
        return UserWeight;
    }

    public void setUserWeight(String userWeight) {
        UserWeight = userWeight;
    }

    public String getUserGender() {
        return UserGender;
    }

    public void setUserGender(String userGender) {
        UserGender = userGender;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
