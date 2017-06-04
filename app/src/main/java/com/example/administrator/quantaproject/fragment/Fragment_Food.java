package com.example.administrator.quantaproject.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.Movements;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.Movement;
import com.example.administrator.quantaproject.tools.Adapter_ListMovement;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */

public class Fragment_Food extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private String phoneNum;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Adapter_ListMovement adapterAtyMovement;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        phoneNum = getActivity().getIntent().getStringExtra(PingTai_Config.KEY_PHONE_NUM);
        View food = inflater.inflate(R.layout.fragment_food, container, false);
        adapterAtyMovement = new Adapter_ListMovement(getActivity());

        swipeRefreshLayout = (SwipeRefreshLayout) food.findViewById(R.id.refresh_layout_food);
        swipeRefreshLayout.setOnRefreshListener(this);
        //为下拉刷新的设置四种颜色
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
        );
        RecyclerView rvFood = (RecyclerView) food.findViewById(R.id.rv_food);
        rvFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadMovement();
        rvFood.setAdapter(adapterAtyMovement);
        rvFood.setHasFixedSize(true);

        return food;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        adapterAtyMovement.clear();
        loadMovement();
        adapterAtyMovement.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void loadMovement() {
        new Movement(getActivity(),PingTai_Config.ACTION_MOVEMENT_FOOD, phoneNum, 1, 2, new Movement.SuccessCallback() {
            @Override
            public void onSuccess(int page, int perpage, List<Movements> movements) {
                adapterAtyMovement.addAll(movements);
            }
        }, new Movement.FailCallback() {
            @Override
            public void onFail() {
//                Toast.makeText(getActivity(), R.string.Fail_to_load_movement, Toast.LENGTH_SHORT).show();
            }
        });
    }
}