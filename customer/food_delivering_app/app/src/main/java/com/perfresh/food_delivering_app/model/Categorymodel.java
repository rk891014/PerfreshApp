package com.perfresh.food_delivering_app.model;

public class Categorymodel {
    String productId;
    String url;
    String productname;
    String productprice;
    String productquantity;
    String discount,cuttedprice;
    public  Categorymodel(){}

    public Categorymodel(String productId, String url, String productname, String productprice, String productquantity) {
        this.productId = productId;
        this.url = url;
        this.productname = productname;
        this.productprice = productprice;
        this.productquantity = productquantity;
    }

    public Categorymodel(String discount,String cuttedprice,String productId, String url, String productname, String productprice, String productquantity) {
        this.productId = productId;
        this.url = url;
        this.productname = productname;
        this.productprice = productprice;
        this.productquantity = productquantity;
        this.discount = discount;
        this.cuttedprice = cuttedprice;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCuttedprice() {
        return cuttedprice;
    }

    public void setCuttedprice(String cuttedprice) {
        this.cuttedprice = cuttedprice;
    }
}
