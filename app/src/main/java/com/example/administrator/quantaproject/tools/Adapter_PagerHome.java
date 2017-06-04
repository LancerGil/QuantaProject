package com.example.administrator.quantaproject.tools;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.fragment.PagerFragment_Category;
import com.example.administrator.quantaproject.fragment.PagerFragment_ChaoZhou;
import com.example.administrator.quantaproject.fragment.PagerFragment_Hot;

/**
 * Created by Administrator on 2017/5/25.
 */

public class Adapter_PagerHome extends FragmentPagerAdapter {

    public final int NUM_PAGES = 3;

    private String[] titles;

    public Adapter_PagerHome(Context cont, FragmentManager fm) {
        super(fm);
        final Context context = cont;
        titles = context.getResources().getStringArray(R.array.Movement_Home_label);

    }

    @Override
    public Fragment getItem(int position) {
        Fragment pagerFragment;
        if (position == 0) {
            pagerFragment = new PagerFragment_Hot();
        } else if(position == 1){
            pagerFragment = new PagerFragment_ChaoZhou();
        } else {
            pagerFragment = new PagerFragment_Category();
        }
        return pagerFragment;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
