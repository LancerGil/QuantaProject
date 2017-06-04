package com.example.administrator.quantaproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.activity.OptionDetailActivity;
import com.example.administrator.quantaproject.tools.Adapter_MineRecyclerView;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by Administrator on 2017/5/31.
 */

public class Fragment_RecyclerView_Mine extends Fragment {

    @BindView(R.id.recyclerView_mine)
    RecyclerView mRecyclerView;
    private Adapter_MineRecyclerView mineRecylerViewAdpt;

    public static Fragment_RecyclerView_Mine newInstance() {
        return new Fragment_RecyclerView_Mine();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        final String[] items = getActivity().getResources().getStringArray(R.array.option_mine);
        mineRecylerViewAdpt = new Adapter_MineRecyclerView(items,getActivity());
        mineRecylerViewAdpt.setOnItemClickListener(new Adapter_MineRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent turnToOptionDetail = new Intent(getActivity(),OptionDetailActivity.class);
                turnToOptionDetail.putExtra("optionName",items[position]);
                startActivity(turnToOptionDetail);
            }
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(mineRecylerViewAdpt);
    }
}
