package com.example.food_delivering_app.model;

public class Categorymodel {
    String productId;
    String url;
    String productname;
    String productprice;
    String productquantity;
    public  Categorymodel(){}

    public Categorymodel(String productId, String url, String productname, String productprice, String productquantity) {
        this.productId = productId;
        this.url = url;
        this.productname = productname;
        this.productprice = productprice;
        this.productquantity = productquantity;
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
}
