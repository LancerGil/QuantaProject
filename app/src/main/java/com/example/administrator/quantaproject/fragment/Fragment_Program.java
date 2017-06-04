package com.example.administrator.quantaproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
 * Created by Administrator on 2017/5/26.
 */

public class Fragment_Program extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private String phoneNum;
    private Adapter_ListMovement adapter_program;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LayoutInflater programLayoutInflater;
    private View programView;
    private RecyclerView program_list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        programLayoutInflater = LayoutInflater.from(getActivity());

        programView = programLayoutInflater.inflate(R.layout.fragment_program,container,false);
        program_list = (RecyclerView) programView.findViewById(R.id.rv_program_list);

        phoneNum = getActivity().getIntent().getStringExtra(PingTai_Config.KEY_PHONE_NUM);
        adapter_program = new Adapter_ListMovement(getActivity());

        swipeRefreshLayout = (SwipeRefreshLayout) programView.findViewById(R.id.refresh_layout_program);
        swipeRefreshLayout.setOnRefreshListener(this);
        //为下拉刷新的设置四种颜色
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
        );

        loadMovement();
        adapter_program.setOnItemClickListener(new Adapter_ListMovement.OnItemClickListener() {
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

        program_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        program_list.setAdapter(adapter_program);
        program_list.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        return programView;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        adapter_program.clear();
        loadMovement();
        program_list.setAdapter(adapter_program);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void loadMovement() {
        new Movement(getActivity(),PingTai_Config.ACTION_MOVEMENT_PROGRAM, phoneNum, 1, 10, new Movement.SuccessCallback() {
            @Override
            public void onSuccess(int page, int perpage, List<Movements> movements) {
                adapter_program.addAll(movements);
            }
        }, new Movement.FailCallback() {
            @Override
            public void onFail() {
                Toast.makeText(getActivity(), R.string.Fail_to_load_movement, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
