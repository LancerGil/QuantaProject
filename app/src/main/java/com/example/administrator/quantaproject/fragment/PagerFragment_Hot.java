package com.example.administrator.quantaproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.activity.PostDetailActivity;
import com.example.administrator.quantaproject.data.Movements;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.Movement;
import com.example.administrator.quantaproject.tools.Adapter_ListMovement;
import com.example.administrator.quantaproject.tools.PreferencesUtility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.lightsky.infiniteindicator.OnPageClickListener;
import cn.lightsky.infiniteindicator.Page;

/**
 * Created by Administrator on 2017/5/25.
 */

public class PagerFragment_Hot extends Fragment implements SwipeRefreshLayout.OnRefreshListener,OnPageClickListener,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private List<Movements> data = new ArrayList<>();
    private String phoneNum;
    private LinearLayoutManager mGridLayoutManager;
    private Adapter_ListMovement adapterAtyMovement;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private SliderLayout mDemoSlider;
    private ViewGroup hot;
    private LayoutInflater hotLayoutInflater;
    private LinearLayout post_layout;
    private View hot_pageView;
    private String mPosition;
    private View movement_list;
    private HashMap<String, View> mViewHashMap;
    private HashMap<String,String> url_maps;
    private Handler headlineHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hot = (ViewGroup) inflater.inflate(R.layout.fragment_hot_ii,container,false);
        hotLayoutInflater = LayoutInflater.from(getActivity());
        hot_pageView = hotLayoutInflater.inflate(R.layout.fragment_home_page_hot,container,false);
        movement_list = hotLayoutInflater.inflate(R.layout.post_list,container,false);
        post_layout = (LinearLayout) hot_pageView.findViewById(R.id.post_layout) ;
        phoneNum = getActivity().getIntent().getStringExtra(PingTai_Config.KEY_PHONE_NUM);


        adapterAtyMovement = new Adapter_ListMovement(getActivity());
        adapterAtyMovement.setOnItemClickListener(new Adapter_ListMovement.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent seeDetail = new Intent(getActivity(), PostDetailActivity.class);
                seeDetail.putExtra(PingTai_Config.KEY_PUBTITLE,((Movements)view.getTag(R.string.TAG_movement)).getTitle());
                seeDetail.putExtra(PingTai_Config.KEY_PUBUSER,((Movements)view.getTag(R.string.TAG_movement)).getpubUser());
                seeDetail.putExtra(PingTai_Config.KEY_PUBTIME,((Movements)view.getTag(R.string.TAG_movement)).getPubTime());
                seeDetail.putExtra(PingTai_Config.KEY_CATEGORY,((Movements)view.getTag(R.string.TAG_movement)).getCategory());
                seeDetail.putExtra(PingTai_Config.KEY_PUBCONTENT,((Movements)view.getTag(R.string.TAG_movement)).getContent());
                Log.e("IMAGEURL",String.valueOf(((Movements)view.getTag(R.string.TAG_movement)).getImageUrls()));

                ArrayList<String> imageStrs = new ArrayList<>();
                for (int i = 0 ; i <((Movements)view.getTag(R.string.TAG_movement)).getImageUrls().length();i++){
                    try {
                        Log.i("Image:"+i+"",String.valueOf(((Movements)view.getTag(R.string.TAG_movement)).getImageUrls().get(i)));
                        imageStrs.add(String.valueOf(((Movements)view.getTag(R.string.TAG_movement)).getImageUrls().get(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                seeDetail.putExtra(PingTai_Config.KEY_IMAGEURL, imageStrs);
//                seeDetail.putExtra(PingTai_Config.KEY_GROUPURLS,((Movements)view.getTag(R.string.TAG_movement)).getGroupUrls());
                seeDetail.putExtra(PingTai_Config.KEY_LIKETIMES,((Movements)view.getTag(R.string.TAG_movement)).getLikeTimes());
                seeDetail.putExtra(PingTai_Config.KEY_VIEWTIMES,((Movements)view.getTag(R.string.TAG_movement)).getViewTimes());
                seeDetail.putExtra(PingTai_Config.KEY_PHONE_NUM,((Movements)view.getTag(R.string.TAG_movement)).getPhoneNum());
                startActivity(seeDetail);
            }
        });




        swipeRefreshLayout = (SwipeRefreshLayout) hot_pageView.findViewById(R.id.refresh_layout_hot);
        swipeRefreshLayout.setOnRefreshListener(this);
        //为下拉刷新的设置四种颜色
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_purple,
                android.R.color.holo_orange_light
        );


//        mDefaultIndicator = (InfiniteIndicator) hot_pageView.findViewById(R.id.hot_headline);
//        testCircleIndicator();
//        Log.e("ViewGroupCount:",hot.getChildCount()+"");
        hot.removeAllViews();
        hot.addView(hot_pageView);

//ggeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
        mDemoSlider = (SliderLayout)hot_pageView.findViewById(R.id.slider);
        url_maps = new HashMap<String, String>();
        headlineHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                for(String name : ((HashMap<String, String>)msg.obj).keySet()){
                    TextSliderView textSliderView = new TextSliderView(getActivity());
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(((HashMap<String, String>)msg.obj).get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(PagerFragment_Hot.this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra",name);

                    mDemoSlider.addSlider(textSliderView);
                }
                super.handleMessage(msg);
            }
        };
        loadHeadline();
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);


        Log.i("loadMovement","start");
        loadMovement();


        recyclerView = (RecyclerView) movement_list.findViewById(R.id.rv_movement);
        mGridLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setAdapter(adapterAtyMovement);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        post_layout.removeAllViews();
        mViewHashMap = new HashMap<>();
        mViewHashMap.put("hot", movement_list);
        mPosition = PreferencesUtility.getInstance(getActivity()).getItemPosition();
        addViews();
        return hot;
    }

    public void addViews(){
        String[] strs = mPosition.split(" ");
        for (int i = 0; i < strs.length; i++) {
            post_layout.addView(mViewHashMap.get(strs[i]));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
//        mDefaultIndicator.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mDefaultIndicator.start();
        if (mPosition == null) {
            return;
        }
        String st = PreferencesUtility.getInstance(getActivity()).getItemPosition();
        if (!st.equals(mPosition)) {
            mPosition = st;
            post_layout.removeAllViews();
            addViews();
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        adapterAtyMovement.clear();
        loadMovement();
        recyclerView.setAdapter(adapterAtyMovement);
//        testCircleIndicator();
        loadHeadline();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }


    private void loadMovement() {
        Log.i("loadMovement", "running");
        new Movement(getActivity(),PingTai_Config.ACTION_MOVEMENT_HOT, phoneNum, 1, 10, new Movement.SuccessCallback() {
            @Override
            public void onSuccess(int page, int perpage, List<Movements> movements) {
                Log.i("loadMovement", "onSuccess");
                adapterAtyMovement.addAll(movements);
                Log.e("adapterAtyMovement", "addAll_done");
            }
        }, new Movement.FailCallback() {
            @Override
            public void onFail() {
                Log.i("loadMovement", "onFail");
                Toast.makeText(getActivity(), R.string.Fail_to_load_movement, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPageClick(int position, Page page) {
        Toast.makeText(getActivity(), " click page --- " + position, Toast.LENGTH_SHORT).show();
    }

    private void loadHeadline() {

        Log.i("loadMovement", "running");
        new Movement(getActivity(),PingTai_Config.ACTION_MOVEMENT_HEADLINE, phoneNum, 1, 3, new Movement.SuccessCallback() {
            @Override
            public void onSuccess(int page, int perpage, List<Movements> movements) {
                Log.i("loadMovement", "onSuccess");
                data.addAll(movements);
                Log.e("Data:","get");
                        for (int i = 0; i< data.size(); i++){
                            try {
//                                pageViews.add(new Page(data.get(i).getMovementID(),data.get(i).getImageUrls().get(0)));
                                Log.e("imageheadurl",data.get(i).getImageUrls().get(0)+"");
                                url_maps.put(data.get(i).getTitle(),(String) data.get(i).getImageUrls().get(0));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("PageViews_ADD", data.get(i).getMovementID());
                        }
                for(String name : url_maps.keySet()){
                    TextSliderView textSliderView = new TextSliderView(getActivity());
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(url_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(PagerFragment_Hot.this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra",name);

                    mDemoSlider.addSlider(textSliderView);
                }
//                        Message msg = new Message();
//                        msg.obj = url_maps;
//                        headlineHandler.sendMessage(msg);
//                mDefaultIndicator.notifyDataChange(pageViews);
//                mDefaultIndicator.setCurrentItem(0);
            }
        }, new Movement.FailCallback() {
            @Override
            public void onFail() {
                Log.i("loadMovement", "onFail");
                Toast.makeText(getActivity(), R.string.Fail_to_load_movement, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initSlider(){

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
