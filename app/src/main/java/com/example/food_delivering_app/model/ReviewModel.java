package com.example.food_delivering_app.model;

public class ReviewModel {
    String DeviceId,comment;

    public ReviewModel() {
    }

    public ReviewModel(String deviceId, String comment) {
        DeviceId = deviceId;
        this.comment = comment;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
