package com.example.whaththt.settings;

public class SettingsItem {

    private int imageResource;
    private String itemTitle;

    public SettingsItem(int imageResource, String itemTitle){
        this.imageResource = imageResource;
        this.itemTitle = itemTitle;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
}
