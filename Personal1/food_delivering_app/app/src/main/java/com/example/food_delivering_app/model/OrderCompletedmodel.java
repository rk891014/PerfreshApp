package com.example.food_delivering_app.model;

import java.util.ArrayList;
import java.util.List;

public class OrderCompletedmodel {
    String DeviceId,itemsordered,status,topay,date,time,completedtime,saved,deliveryboy;

    public OrderCompletedmodel() {
    }

    public OrderCompletedmodel(String deviceId, String itemsordered, String status, String topay, String date, String time, String completedtime, String saved, String deliveryboy) {
        DeviceId = deviceId;
        this.itemsordered = itemsordered;
        this.status = status;
        this.topay = topay;
        this.date = date;
        this.time = time;
        this.completedtime = completedtime;
        this.saved = saved;
        this.deliveryboy = deliveryboy;
    }

    public String getDeliveryboy() {
        return deliveryboy;
    }

    public void setDeliveryboy(String deliveryboy) {
        this.deliveryboy = deliveryboy;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getItemsordered() {
        return itemsordered;
    }

    public void setItemsordered(String itemsordered) {
        this.itemsordered = itemsordered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTopay() {
        return topay;
    }

    public void setTopay(String topay) {
        this.topay = topay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCompletedtime() {
        return completedtime;
    }

    public void setCompletedtime(String completedtime) {
        this.completedtime = completedtime;
    }

    public String getSaved() {
        return saved;
    }

    public void setSaved(String saved) {
        this.saved = saved;
    }
}
