package com.example.administrator.quantaproject.data;


import android.support.annotation.Nullable;

import org.json.JSONArray;

/**
 * Created by Administrator on 2017/5/24.
 */

public class Movements {

    public Movements(String MovementID, String title, String Movement, String phoneNum, @Nullable JSONArray imageUrls, String pubUser,
                     String viewTimes, String commentTimes, String category, String pubTime, int likeTimes) {
        this.title = title;
        this.MovementID = MovementID;
        this.content = Movement;
        this.phoneNum = phoneNum;
        if (!(imageUrls.length()==0)) {
            this.imageUrls=imageUrls;
        }
        this.pubUser = pubUser;
        this.viewTimes = viewTimes;
        this.commentTimes = commentTimes;
        this.category = category;
        this.pubTime = pubTime;
//        this.groupUrls = groupUrls;
        this.likeTimes = likeTimes;
    }

    private String MovementID = null, content = null, phoneNum = null, pubUser = null, groupUrls = null,
            viewTimes = null, commentTimes = null, title = null, category = null, pubTime = null;
    private JSONArray imageUrls = new JSONArray();
    private int likeTimes;


    public String getContent() {
        return content;
    }

    public String getMovementID() {
        return MovementID;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public JSONArray getImageUrls() {
        return imageUrls;
    }

    public String getpubUser() {
        return pubUser;
    }

    public String getViewTimes() {
        return viewTimes;
    }

    public String getCommentTimes() {
        return commentTimes;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getPubTime() {
        return pubTime;
    }

    public String getGroupUrls() {
        return groupUrls;
    }

    public int getLikeTimes() {
        return likeTimes;
    }
}
