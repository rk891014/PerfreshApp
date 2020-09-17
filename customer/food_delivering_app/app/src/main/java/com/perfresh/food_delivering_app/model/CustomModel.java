package com.perfresh.food_delivering_app.model;

public class CustomModel {
    String url;
    String productId;
    String productname;
    String productprice;
    String productquantity;
    String quantity;
    String cuttedprice,discount;

    public CustomModel() {
    }

    public CustomModel(String url, String productId, String productname, String productprice, String productquantity, String quantity) {
        this.url = url;
        this.productId = productId;
        this.productname = productname;
        this.productprice = productprice;
        this.productquantity = productquantity;
        this.quantity = quantity;
    }
    public CustomModel(String url, String productId, String productname, String productprice, String productquantity,String cuttedprice,String discount,String quantity) {
        this.url = url;
        this.productId = productId;
        this.productname = productname;
        this.productprice = productprice;
        this.productquantity = productquantity;
        this.quantity = quantity;
        this.cuttedprice = cuttedprice;
        this.discount = discount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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
}
