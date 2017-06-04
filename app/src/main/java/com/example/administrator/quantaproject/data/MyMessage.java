package com.example.administrator.quantaproject.data;

/**
 * Created by Administrator on 2017/6/3.
 */

public class MyMessage {
    private String tvChatWindow,lastSender, messageCon,ivChatWinUrl;
    public MyMessage(String ivChatWinUrl,String tvChatWindo,String lastSender,String messageCon){
        this.ivChatWinUrl = ivChatWinUrl;
        this.tvChatWindow = tvChatWindo;
        this.lastSender = lastSender;
        this.messageCon = messageCon;
    }

    public String getTvChatWindow() {
        return tvChatWindow;
    }

    public String getLastSender() {
        return lastSender;
    }

    public String getMessageCon() {
        return messageCon;
    }

    public String getIvChatWinUrl() {
        return ivChatWinUrl;
    }
}
