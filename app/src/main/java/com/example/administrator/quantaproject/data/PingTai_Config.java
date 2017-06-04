package com.example.administrator.quantaproject.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/4/16.
 */

public class PingTai_Config {

    public static final String SERVER_URL = "http://192.168.165.83/gadilan/home/user";
    public static final String SERVER_URL_LOCAL = "http://192.168.165.86:8080/testProject/NewFile.jsp";
    public static final String SERVER_URL_LOCAL_IMAGE = "http://192.168.165.86:8080/testProject/image/";
    public static final String SERVER_URL_UPLOAD = "http://192.168.165.83/gadilan/home/movement";
    public static final String APP_ID="com.example.administrator.pingtai";
    public static final String CHARSET = "UTF-8";


    public static final String ACTION_GET_CODE = "get_code";
    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_MOVEMENT = "movement";
    public static final String ACTION_GETPAGEVIEWCONTENT = "getPageViewContent";
    public static final String ACTION_SIGNUP = "signup";
    public static final String ACTION_UPLOADINFO = "uploadinfo";
    public static final String ACTION_MOVEMENT_HOT = "movement_hot";
    public static final String ACTION_MOVEMENT_FOOD = "movement_food";
    public static final String ACTION_MOVEMENT_PROGRAM = "movement_program";
    public static final String ACTION_MOVEMENT_CHAOZHOU = "movement_chaozhou";
    public static final String ACTION_MOVEMENT_HEADLINE = "movement_headline";
    public static final String ACTION_PUBLISH = "publish";
    public static final String ACTION_FOLLOW = "follow";
    public static final String ACTION_MYMESSAGE = "myMessage";
    public static final String ACTION_MYGROUPS = "myGroups";

    public static final String KEY_TOKEN = "token";
    public static final String KEY_ACTION = "action";
    public static final String KEY_PHONE_MD5 = "phoneMd5";
    public static final String KEY_STATUS = "status";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PAGE = "page";
    public static final String KEY_PERPAGE = "perpage";
    public static final String KEY_CODE = "code";
    public static final String KEY_REALNAME = "realname";
    public static final String KEY_STUDNUM = "studNum";
    public static final String KEY_CITY = "city";
    public static final String KEY_MOVEMENT = "movement";
    public static final String KEY_MOVEMENT_ID = "movementId";
    public static final String KEY_PUBTITLE = "pubTitle";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_PHONE_NUM = "phoneNum";
    public static final String KEY_IMAGEURL = "imageUrl";
    public static final String KEY_PUBUSER = "pubUser";
    public static final String KEY_VIEWTIMES = "viewTimes";
    public static final String KEY_COMMENTTIMES = "commentTimes";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_PUBTIME = "pubTime";
    public static final String KEY_GROUPURLS = "groupUrls";
    public static final String KEY_LIKETIMES = "likeTimes";
    public static final String KEY_PUBCONTENT = "pubContent";
    public static final String KEY_CATEGORY_NAME = "categoryName";
    public static final String KEY_IMAGENAME = "imageNames";
    public static final String KEY_TARGET_USER_PHONE_NUM = "targetUserPhoneNum";
    public static final String KEY_COMMENTLIST = "commentList";
    public static final String KEY_COMMENTHAED_URL = "commentHeadUrl";
    public static final String KEY_COMMENT_LIKETIMES = "commentLikeTimes";
    public static final String KEY_PERSONALINFO = "personalInfo";
    public static final String KEY_HEAD_IMAGEURL = "headImageUrl";
    public static final String KEY_SEX = "sex";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_MESSAGES = "messages";
    public static final String KEY_MESSAGE_CHATWIN = "messageChatWin";
    public static final String KEY_MESSAGE_LASTSENDER = "messageLastSender";
    public static final String KEY_MESSAGE_MESSAGECON = "messageCon";
    public static final String KEY_MESSAGE_CHATWIN_IMAGEURL = "messageChatWinImageUrl";
    public static final String KEY_MYGROUPS = "myGroups";
    public static final String KEY_MYGROUPS_IMAGEURL = "myGroupsImageUrl";
    public static final String KEY_MYGROUPS_NAME = "myGroupsName";

    public static final int RESULT_STATUS_SUCCESS = 1;
    public static final int RESULT_STATUS_FAIL = 0;
    public static final int RESULT_STATUS_INVALID_TOKEN = 2;

    public static final int ACTIVITY_RESULT_NEED_REFRESH = 10000;


    public static String getCachedToken(Context context){
        return context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).getString(KEY_TOKEN,null);
    }

    public static void cachToken(Context context,String token){
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).edit();
        editor.putString(KEY_TOKEN,token);
        editor.commit();
    }

    public static String getCachedPhoneNum(Context context){
        return context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).getString(KEY_PHONE_NUM,null);
    }

    public static void cachPhoneNum(Context context,String PhoneNum){
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).edit();
        editor.putString(KEY_PHONE_NUM,PhoneNum);
        editor.commit();
    }
}
