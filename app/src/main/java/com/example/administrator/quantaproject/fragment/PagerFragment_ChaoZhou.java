package com.example.administrator.quantaproject.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.activity.PostDetailActivity;
import com.example.administrator.quantaproject.data.Movements;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.Movement;
import com.example.administrator.quantaproject.tools.Adapter_ListMovement;

import java.util.List;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class PagerFragment_ChaoZhou extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private Adapter_ListMovement adapter_chaozhou = null;
    private String phoneNum;
    private RecyclerView post_chaozhou_list;
    private int page=1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        phoneNum = getActivity().getIntent().getStringExtra(PingTai_Config.KEY_PHONE_NUM);
        View page_chaozhou = inflater.inflate(R.layout.fragment_home_pager_chaohzou, container, false);

        post_chaozhou_list = (RecyclerView) page_chaozhou.findViewById(R.id.rv_post_chaozhou_list);
        adapter_chaozhou = new Adapter_ListMovement(getActivity());

        swipeRefreshLayout = (SwipeRefreshLayout) page_chaozhou.findViewById(R.id.refresh_layout_chaozhou);
        swipeRefreshLayout.setOnRefreshListener(this);
        //为下拉刷新的设置四种颜色
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
        );
        loadMovement();
        adapter_chaozhou.setOnItemClickListener(new Adapter_ListMovement.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent seeDetail = new Intent(getActivity(),PostDetailActivity.class);
                seeDetail.putExtra(PingTai_Config.KEY_PUBTITLE,((Movements)view.getTag(R.string.TAG_movement)).getTitle());
                seeDetail.putExtra(PingTai_Config.KEY_PUBUSER,((Movements)view.getTag(R.string.TAG_movement)).getpubUser());
                seeDetail.putExtra(PingTai_Config.KEY_PUBTIME,((Movements)view.getTag(R.string.TAG_movement)).getPubTime());
                seeDetail.putExtra(PingTai_Config.KEY_CATEGORY,((Movements)view.getTag(R.string.TAG_movement)).getCategory());
                seeDetail.putExtra(PingTai_Config.KEY_IMAGEURL,((Movements)view.getTag(R.string.TAG_movement)).getContent());
                seeDetail.putExtra(PingTai_Config.KEY_PUBCONTENT, String.valueOf(((Movements)view.getTag(R.string.TAG_movement)).getImageUrls()));
                seeDetail.putExtra(PingTai_Config.KEY_GROUPURLS,((Movements)view.getTag(R.string.TAG_movement)).getGroupUrls());
                seeDetail.putExtra(PingTai_Config.KEY_LIKETIMES,((Movements)view.getTag(R.string.TAG_movement)).getLikeTimes());
                seeDetail.putExtra(PingTai_Config.KEY_VIEWTIMES,((Movements)view.getTag(R.string.TAG_movement)).getViewTimes());
                seeDetail.putExtra(PingTai_Config.KEY_PHONE_NUM,((Movements)view.getTag(R.string.TAG_movement)).getPhoneNum());
                startActivity(seeDetail);
            }
        });

        post_chaozhou_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        post_chaozhou_list.setAdapter(adapter_chaozhou);
        post_chaozhou_list.setHasFixedSize(true);
        post_chaozhou_list.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        return page_chaozhou;
    }

    private void loadMovement(){
        new Movement(getActivity(),PingTai_Config.ACTION_MOVEMENT_CHAOZHOU,phoneNum, page, 10, new Movement.SuccessCallback() {
            @Override
            public void onSuccess(int page, int perpage, List<Movements> movements) {
                adapter_chaozhou.addAll(movements);
            }
        }, new Movement.FailCallback() {
            @Override
            public void onFail() {
                Toast.makeText(getActivity(), R.string.Fail_to_load_movement,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
//        adapter_chaozhou.clear();
        page++;
        loadMovement();
        post_chaozhou_list.setAdapter(adapter_chaozhou);
        adapter_chaozhou.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}
