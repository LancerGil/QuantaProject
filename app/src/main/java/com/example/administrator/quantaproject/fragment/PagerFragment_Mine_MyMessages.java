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
import com.example.administrator.quantaproject.data.MyMessage;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.MyMessages;
import com.example.administrator.quantaproject.tools.Adapter_MyMessages;

import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class PagerFragment_Mine_MyMessages extends Fragment {
    private Adapter_MyMessages adapter_myMessages;
    private String phoneNum,token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myMessagesView = inflater.inflate(R.layout.fragment_mine_mymessages,container,false);
        RecyclerView rvMyMessageList = (RecyclerView) myMessagesView.findViewById(R.id.rv_mine_mymessages);
        phoneNum = PingTai_Config.getCachedPhoneNum(getActivity());
        token = PingTai_Config.getCachedToken(getActivity());

        adapter_myMessages = new Adapter_MyMessages();

        loadMessages();
        rvMyMessageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMyMessageList.setAdapter(adapter_myMessages);
        rvMyMessageList.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));
        rvMyMessageList.setHasFixedSize(true);

        return myMessagesView;
    }

    private void loadMessages(){
        new MyMessages(getActivity(), phoneNum, token, new MyMessages.SuccessCallback() {
            @Override
            public void onSuccess(List<MyMessage> myMessageList) {
                adapter_myMessages.addAll(myMessageList);
            }
        }, new MyMessages.FailCallback() {
            @Override
            public void onFail() {

            }
        });
    }
}
