package com.example.administrator.quantaproject.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.MyMessage;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.MyMessages;
import com.example.administrator.quantaproject.tools.Adapter_MyMessages;
import com.example.administrator.quantaproject.tools.Adapter_PoPupWindowList;
import com.example.administrator.quantaproject.tools.RecyclerViewClickListener;

import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class PagerFragment_Mine_MyMessages extends Fragment {
    private Adapter_MyMessages adapter_myMessages;
    private String phoneNum,token;
    private PopupWindow popupWindow=null;
    private View poPupContentView;
    private RecyclerView poPupcontentList;
    private String TAG = "PagerFragment_Mine_MyMessages";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myMessagesView = inflater.inflate(R.layout.fragment_mine_mymessages,container,false);
        RecyclerView rvMyMessageList = (RecyclerView) myMessagesView.findViewById(R.id.rv_mine_mymessages);
        phoneNum = PingTai_Config.getCachedPhoneNum(getActivity());
        token = PingTai_Config.getCachedToken(getActivity());

        adapter_myMessages = new Adapter_MyMessages();
        initPoPupWindows();

        loadMessages();
        rvMyMessageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMyMessageList.setAdapter(adapter_myMessages);
        rvMyMessageList.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));
        rvMyMessageList.setHasFixedSize(true);
        rvMyMessageList.addOnItemTouchListener(new RecyclerViewClickListener(getContext(), rvMyMessageList, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(),"ClickPosition:"+position,Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onItemLongClick(final View view, int position, final MotionEvent e) {
                Log.d(TAG,"EventX:"+(int)e.getX()+"EventY:"+(int)e.getY());

                popupWindow.showAsDropDown(view,(int)e.getX(),-view.getBottom()+(int)e.getY()-poPupContentView.getHeight());
                Toast.makeText(getContext(),"LongClickPosition:"+position,Toast.LENGTH_SHORT).show();
            }
        }));

        return myMessagesView;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("LongLogTag")
    private void initPoPupWindows(){
        if(popupWindow==null) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            String[] data = getActivity().getResources().getStringArray(R.array.option_message);
            poPupContentView = layoutInflater.inflate(R.layout.list_popubwin,null);
            poPupcontentList = (RecyclerView) poPupContentView.findViewById(R.id.lv_poPupcontent);
            poPupcontentList.setLayoutManager(new LinearLayoutManager(getContext()));
            poPupcontentList.setAdapter(new Adapter_PoPupWindowList(data));

            //popupwindows中的view对象需要自定义xml文件
            popupWindow = new PopupWindow(poPupContentView,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT); //设置弹窗的宽高度

//            popupWindow.setBackgroundDrawable(background);
            popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);//设置弹出的动画效果（可以自定义效果）
            popupWindow.setOutsideTouchable(true);//设置点击边缘区域windows消失
            popupWindow.setElevation(10);
            popupWindow.setFocusable(true);
            popupWindow.setTouchable(true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);    //设置软键盘的弹出自适应

        }


        //获取屏幕尺寸
        DisplayMetrics dm =new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        //获取宽高
        int width = dm.widthPixels;
        int height = dm.heightPixels;
//
//       //第二种方式,获取当前activity的宽高度，如果当前的activity不是全屏显示，获取的数据可能会有误差
//      int width =  getWindowManager().getDefaultDisplay().getWidth();
//      int height = getWindowManager().getDefaultDisplay().getHeight();
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
