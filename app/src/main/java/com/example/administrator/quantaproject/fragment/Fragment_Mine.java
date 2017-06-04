package com.example.administrator.quantaproject.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.activity.MovementActivity;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.PersonalInfo;
import com.example.administrator.quantaproject.tools.BitmapUtils;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */

public class Fragment_Mine extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private MaterialViewPager materialViewPager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String[] page_mine_title;
    private String phoneNum;
    private TextView myIntegral,myName;
    private Handler headHandler ;
    private ImageView myHead;
    private List<String> personalInfodata = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        page_mine_title = new String[]{"我的"};
        View mine = inflater.inflate(R.layout.fragment_mine,container,false);
        View header = inflater.inflate(R.layout.mine_header,container,false);
        phoneNum = PingTai_Config.getCachedPhoneNum(getContext());
        myHead = (ImageView) header.findViewById(R.id.iv_mine_userHaed);
        myIntegral = (TextView) header.findViewById(R.id.tv_mine_userIntegral);

        swipeRefreshLayout = (SwipeRefreshLayout) mine.findViewById(R.id.refresh_layout_mine);
        swipeRefreshLayout.setOnRefreshListener(this);
        //为下拉刷新的设置四种颜色
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_purple,
                android.R.color.holo_orange_light
        );
        materialViewPager = (MaterialViewPager) mine.findViewById(R.id.materialViewPager);

        materialViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                return HeaderDesign.fromColorResAndDrawable(R.color.loginTitlebar, getActivity().getDrawable(R.drawable.mine_backgroud));
            }
        });

        Toolbar toolbar_mine = materialViewPager.getToolbar();
        toolbar_mine.setTitleTextColor(Color.WHITE);
        toolbar_mine.setNavigationIcon(R.mipmap.btn_back);

        materialViewPager.setToolbar(toolbar_mine);
        if (toolbar_mine!=null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_mine);
            toolbar_mine.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MovementActivity)getActivity()).getViewPager().setCurrentItem(0);
                }
            });
            toolbar_mine.setTitleTextAppearance(getActivity(),R.style.Toolbar_TitleText);
            Log.e("MineActionBar","SET");
            ActionBar actionBar =  ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setTitle("我的");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        ViewPager viewPager_mine = materialViewPager.getViewPager();
        viewPager_mine.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return page_mine_title[position];
            }

            @Override
            public int getCount() {
                return page_mine_title.length;
            }

            @Override
            public Fragment getItem(int position) {
                return Fragment_RecyclerView_Mine.newInstance();
            }
        });
        viewPager_mine.setOffscreenPageLimit(materialViewPager.getViewPager().getAdapter().getCount());
        materialViewPager.getPagerTitleStrip().setViewPager(viewPager_mine);

        loadHead();
        return mine;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void loadHead() {
        if (!BitmapUtils.readImage(myHead, getActivity(), "head")) {
            new PersonalInfo(getActivity(), phoneNum, new PersonalInfo.SuccessCallback() {
                @Override
                public void onSuccess(final List<String> result) {

                    myHead.setImageURI(Uri.parse(result.get(0)));
                    myIntegral.setText(result.get(4));
//                    personalInfodata.addAll(result);
//                    headHandler = new Handler() {
//                        @Override
//                        public void handleMessage(Message msg) {
//                            myHead.setImageBitmap((Bitmap) msg.obj);
//                            materialViewPager.notifyHeaderChanged();
//                            Log.e("HEAD", "UnSet");
//                            super.handleMessage(msg);
//                        }
//                    };
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Bitmap head = DownloadUtil.getHttpBitmap(personalInfodata.get(0));
//                            Bitmap finalHead = BitmapUtils.circleBitmap(BitmapUtils.imageCrop(head, 1, 1, true));
//                            Message msg = new Message();
//                            msg.obj = finalHead;
//                            headHandler.sendMessage(msg);
//                        }
//                    }).start();
                }
            }, new PersonalInfo.FailCallback() {
                @Override
                public void onFail() {

                }
            });
        }
    }
}
