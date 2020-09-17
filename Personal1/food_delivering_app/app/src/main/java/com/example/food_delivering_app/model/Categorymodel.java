package com.example.food_delivering_app.model;

public class Categorymodel {
    String availability;
    String productId;
    String url;
    String productname;
    String cuttedprice,discount,name;
    String productprice;
    String productquantity;
    public  Categorymodel(){}

    public Categorymodel(String availability, String productId, String url, String productname, String cuttedprice, String discount, String name, String productprice, String productquantity) {
        this.availability = availability;
        this.productId = productId;
        this.url = url;
        this.productname = productname;
        this.cuttedprice = cuttedprice;
        this.discount = discount;
        this.name = name;
        this.productprice = productprice;
        this.productquantity = productquantity;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getProductquantity() {
        return productquantity;
    }

    public void setProductquantity(String productquantity) {
        this.productquantity = productquantity;
    }

    public String getCuttedprice() {
        return cuttedprice;
    }

    public void setCuttedprice(String cuttedprice) {
        this.cuttedprice = cuttedprice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
