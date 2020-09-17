package com.perfresh.food_delivering_app.model;

public class Model {
    String url;
    String name;
    public  Model(){}
    public Model( String url, String name) {
        this.url = url;
        this.name = name;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
