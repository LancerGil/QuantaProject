package com.example.administrator.quantaproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.tools.Adapter_PagerHome;

/**
 * Created by Administrator on 2017/5/26.
 */

public class Fragment_Home extends Fragment {

    private PagerSlidingTabStrip pagerTab;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View home = inflater.inflate(R.layout.fragment_home,container,false);

        pagerTab = (PagerSlidingTabStrip) home.findViewById(R.id.pager_tabs);
        viewPager = (ViewPager) home.findViewById(R.id.viewPager);

        //设置ViewPager每次预加载2个pager
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new Adapter_PagerHome(getActivity(),getFragmentManager()));
        pagerTab.setViewPager(viewPager);

        return home;
    }
}
