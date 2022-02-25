package com.savonikyurii.beatifulkrivbas.helpers;

public class InfoWindowData {
    private String title;
    private String image;
    private String category;

    public InfoWindowData(String title, String image, String category) {
        this.title = title;
        this.image = image;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
