package com.example.jssbs.Model;

public class Meeting {

    private String meetingID;
    private String date;
    private String time;
    private String meetingPlace;
    private String itemID;
    private String buyerID;
    private String quantity;
    private String sellerID;

    public Meeting (){

    }

    public Meeting (String meetingID, String date, String time, String meetingPlace, String itemID, String buyerID,String quantity,String sellerID){
        this.meetingID = meetingID;
        this.date = date;
        this.time = time;
        this.meetingPlace = meetingPlace;
        this.itemID = itemID;
        this.buyerID = buyerID;
        this.quantity = quantity;
        this.sellerID = sellerID;
    }

    public String getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(String meetingID) {
        this.meetingID = meetingID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }


}
