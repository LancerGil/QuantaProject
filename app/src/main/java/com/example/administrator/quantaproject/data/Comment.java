package com.example.administrator.quantaproject.data;

/**
 * Created by Administrator on 2017/6/1.
 */

public class Comment {

    private String commentUser=null,comment=null,timeOfComment=null,commentHeadUrl = null;
    private int likeTimes;

    public Comment (String commentUser,String comment, String timeOfComment, String commentHeadUrl,int likeTimes){
        this.commentUser=commentUser;
        this.comment=comment;
        this.timeOfComment=timeOfComment;
        this.commentHeadUrl=commentHeadUrl;
        this.likeTimes = likeTimes;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public String getTimeOfComment() {
        return timeOfComment;
    }

    public String getCommentHeadUrl() {
        return commentHeadUrl;
    }

    public int getLikeTimes() {
        return likeTimes;
    }
}
