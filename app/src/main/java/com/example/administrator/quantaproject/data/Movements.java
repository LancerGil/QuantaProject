package com.example.administrator.quantaproject.data;


import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/24.
 */

public class Movements {
//
//    public Movements(String movementId, String pubTitle, String Movement, String phoneNum, @Nullable JSONArray imageUrl, String pubUser,
//                     String viewTimes, String commentTimes, String category, String pubTime, int likeTimes) {
//        this.pubTitle = pubTitle;
//        this.movementId = movementId;
//        this.content = Movement;
//        this.phoneNum = phoneNum;
//        if (!(imageUrl.length()==0)) {
//            this.imageUrl=imageUrl;
//        }
//        this.pubUser = pubUser;
//        this.viewTimes = viewTimes;
//        this.commentTimes = commentTimes;
//        this.category = category;
//        this.pubTime = pubTime;
////        this.groupUrls = groupUrls;
//        this.likeTimes = likeTimes;
//    }

    private String movementId = null, content = null, phoneNum = null, pubUser = null, groupUrls = null,
            viewTimes = null, commentTimes = null, pubTitle = null, category = null, pubTime = null;
    private ArrayList imageUrl = null;
    private int likeTimes;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }

    public String getMovementId() {
        return movementId;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setImageUrl(ArrayList imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList getImageUrl() {
        return imageUrl;
    }

    public void setPubUser(String pubUser) {
        this.pubUser = pubUser;
    }

    public String getpubUser() {
        return pubUser;
    }

    public void setViewTimes(String viewTimes) {
        this.viewTimes = viewTimes;
    }

    public String getViewTimes() {
        return viewTimes;
    }

    public void setCommentTimes(String commentTimes) {
        this.commentTimes = commentTimes;
    }

    public String getCommentTimes() {
        return commentTimes;
    }

    public void setPubTitle(String pubTitle) {
        this.pubTitle = pubTitle;
    }

    public String getPubTitle() {
        return pubTitle;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setGroupUrls(String groupUrls) {
        this.groupUrls = groupUrls;
    }

    public String getGroupUrls() {
        return groupUrls;
    }

    public void setLikeTimes(int likeTimes) {
        this.likeTimes = likeTimes;
    }

    public int getLikeTimes() {
        return likeTimes;
    }
}
