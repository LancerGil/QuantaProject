package com.example.administrator.quantaproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.MyGroup;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.MyGroups;
import com.example.administrator.quantaproject.tools.Adapter_MyGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class PagerFragment_Mine_MyGroup extends Fragment {
    private Adapter_MyGroup adapter_myGroup;
    private String phoneNum,token;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myMessagesView = inflater.inflate(R.layout.fragment_mine_mymessages,container,false);
        RecyclerView rvMyMessageList = (RecyclerView) myMessagesView.findViewById(R.id.rv_mine_mymessages);
        phoneNum = PingTai_Config.getCachedPhoneNum(getActivity());
        token = PingTai_Config.getCachedToken(getActivity());

        adapter_myGroup = new Adapter_MyGroup();

        loadGroups();
        rvMyMessageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMyMessageList.setAdapter(adapter_myGroup);
        rvMyMessageList.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));
        rvMyMessageList.setHasFixedSize(true);

        return myMessagesView;
    }

    private void loadGroups(){
        new MyGroups(getActivity(), phoneNum, token, new MyGroups.SuccessCallback() {
            @Override
            public void onSuccess(List<MyGroup> myGroupsList) {
                adapter_myGroup.addAll(myGroupsList);
            }
        }, new MyGroups.FailCallback() {
            @Override
            public void onFail() {

            }
        });
    }
}
