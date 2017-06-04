package com.example.administrator.quantaproject.tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.administrator.quantaproject.fragment.PagerFragment_Mine_MyGroup;
import com.example.administrator.quantaproject.fragment.PagerFragment_Mine_MyMessages;
import com.example.administrator.quantaproject.fragment.PagerFragment_Mine_PersonalInfo;

/**
 * Created by Administrator on 2017/6/3.
 */

public class Adapter_PagerOption  extends FragmentPagerAdapter {
    private int currentOption;

    public Adapter_PagerOption(FragmentManager fm,int currentOption) {
        super(fm);
        this.currentOption = currentOption;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (currentOption){
            case 0:
                fragment = new PagerFragment_Mine_PersonalInfo();
                break;
            case 1:
                fragment = new PagerFragment_Mine_MyMessages();
                break;
            case 2:
                fragment = new PagerFragment_Mine_MyGroup();
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
