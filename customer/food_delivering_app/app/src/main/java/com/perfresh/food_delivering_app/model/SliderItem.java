package com.perfresh.food_delivering_app.model;

public class SliderItem {
    private String title;
    private String imageUrl;
    private String rec;

    public SliderItem() {}

    public SliderItem(String title, String imageUrl, String rec) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.rec = rec;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRec() {
        return rec;
    }

    public void setRec(String rec) {
        this.rec = rec;
    }
}
