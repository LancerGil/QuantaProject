package com.example.administrator.quantaproject.tools;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/7/4.
 */

public class MineHeaderAnimation {
    private String TAG = "MineHeaderAnimation";
    private int translateId = 0;

    public MineHeaderAnimation(int translateId){
        setTranslateId(translateId);
    }

    private void setTranslateId(int translateId){
        this.translateId = translateId;
    }

    public void setTranslation(ViewGroup viewGroup, float process){
        if (viewGroup == null) {
            return;
        }

        if(translateId!=0) {
            View translateView = viewGroup.findViewById(translateId);
            Log.d(TAG,"translateId:"+translateId);
            Log.d(TAG,"process:"+process);
            if(process>=0&&process<=100){
                translateView.setTranslationY(-translateView.getMeasuredHeight()*process/100.0f);
                translateView.setAlpha(1-process/100);
            }
        }
    }
}
