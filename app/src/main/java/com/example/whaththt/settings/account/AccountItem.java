package com.example.whaththt.settings.account;

public class AccountItem {
    private int imageResource;
    private String itemTitle;
    private  String itemDescription;

    public AccountItem( String itemTitle,String itemDescription){
        this.itemTitle = itemTitle;
        this.itemDescription = itemDescription;

    }



    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
