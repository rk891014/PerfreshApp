package com.perfresh.food_delivering_app.model;

public class user {
    String name,address,mobileno,distance,orders,shares,coinsmoney;
    public user(){}

    public user(String name, String address, String mobileno, String distance, String orders, String shares, String coinsmoney) {
        this.name = name;
        this.address = address;
        this.mobileno = mobileno;
        this.distance = distance;
        this.orders = orders;
        this.shares = shares;
        this.coinsmoney = coinsmoney;
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

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getCoinsmoney() {
        return coinsmoney;
    }

    public void setCoinsmoney(String coinsmoney) {
        this.coinsmoney = coinsmoney;
    }
}