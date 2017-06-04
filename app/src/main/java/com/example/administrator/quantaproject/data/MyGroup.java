package com.example.administrator.quantaproject.data;

/**
 * Created by Administrator on 2017/6/3.
 */

public class MyGroup {
    private String groupImageUrl,groupName;
    public MyGroup(String groupImageUrl,String groupName){
        this.groupImageUrl = groupImageUrl;
        this.groupName = groupName;
    }

    public String getGroupImageUrl() {
        return groupImageUrl;
    }

    public String getGroupName() {
        return groupName;
    }
}
