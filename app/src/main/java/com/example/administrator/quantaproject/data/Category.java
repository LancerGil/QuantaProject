package com.example.administrator.quantaproject.data;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/5/29.
 */

public class Category {

    private Drawable image;
    private String name;

    public Category(Drawable image, String name){
        this.image = image;
        this.name = name;
    }

    public Drawable getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
