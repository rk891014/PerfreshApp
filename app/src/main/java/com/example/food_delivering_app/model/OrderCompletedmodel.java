package com.example.food_delivering_app.model;

import java.util.ArrayList;
import java.util.List;

public class OrderCompletedmodel {
    String id,name,address,contactno,DeviceId,itemsordered,status,topay,date,time,completedtime;
    public OrderCompletedmodel() {
    }

    public OrderCompletedmodel(String deviceId, String itemsordered, String status, String topay, String date, String time, String completedtime) {
        this.DeviceId = deviceId;
        this.itemsordered = itemsordered;
        this.status = status;
        this.topay = topay;
        this.date = date;
        this.time = time;
        this.completedtime = completedtime;
    }

    public OrderCompletedmodel(String id,String deviceId, String name, String address, String contactno, String itemsordered, String status, String topay, String date, String time, String completedtime) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.DeviceId = deviceId;
        this.contactno = contactno;
        this.itemsordered = itemsordered;
        this.status = status;
        this.topay = topay;
        this.date = date;
        this.time = time;
        this.completedtime = completedtime;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
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
}
