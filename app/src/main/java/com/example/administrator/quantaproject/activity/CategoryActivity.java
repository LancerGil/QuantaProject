package com.example.administrator.quantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.Movements;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.Movement;
import com.example.administrator.quantaproject.tools.Adapter_ListMovement;

import java.util.List;

public class CategoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private String phoneNum = null;
    private String currentCategory = null,category_action=null;
    private RecyclerView post_list = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Adapter_ListMovement adapter_listMovement;
    private ScrollView scrollView;
    private View content_list;
    private LinearLayout content_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentCategory = getIntent().getStringExtra(PingTai_Config.KEY_CATEGORY_NAME);
        scrollView = (ScrollView) findViewById(R.id.scrollview_category);
        content_list = getLayoutInflater().inflate(R.layout.content_category,null);
        content_container = (LinearLayout) findViewById(R.id.category_content_container);

        for(int i = 0; i < getResources().getStringArray(R.array.category_action).length;i++ ){
            if(currentCategory.equals(getResources().getStringArray(R.array.category_name)[i])){
                category_action = getResources().getStringArray(R.array.category_action)[i];
            }
        }

        getSupportActionBar().setTitle(currentCategory);
        toolbar.setNavigationIcon(R.mipmap.btn_back);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                onRefresh();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent turnToPubAty = new Intent(CategoryActivity.this, PubPostActivity.class);
                turnToPubAty.putExtra(PingTai_Config.KEY_PHONE_NUM,phoneNum);

                startActivityForResult(turnToPubAty,0);
            }
        });

        phoneNum = getIntent().getStringExtra(PingTai_Config.KEY_PHONE_NUM);
        post_list = (RecyclerView) content_list.findViewById(R.id.rv_category_content_list);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.rflsh_category_content);
        swipeRefreshLayout.setOnRefreshListener(this);
        //为下拉刷新的设置四种颜色
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
        );

        adapter_listMovement = new Adapter_ListMovement(this);

        loadMovement();
        adapter_listMovement.setOnItemClickListener(new Adapter_ListMovement.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent seeDetail = new Intent(CategoryActivity.this,PostDetailActivity.class);
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

        post_list.setLayoutManager(new LinearLayoutManager(this));
        post_list.setAdapter(adapter_listMovement);
        post_list.setHasFixedSize(true);
        post_list.addItemDecoration(new DividerItemDecoration(CategoryActivity.this,DividerItemDecoration.VERTICAL));

        content_container.removeAllViews();
        content_container.addView(content_list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode){
            case PingTai_Config.ACTIVITY_RESULT_NEED_REFRESH:
                onRefresh();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadMovement() {
        new Movement(this,category_action, phoneNum, 1, 2, new Movement.SuccessCallback() {
            @Override
            public void onSuccess(int page, int perpage, List<Movements> movements) {
                adapter_listMovement.addAll(movements);
            }
        }, new Movement.FailCallback() {
            @Override
            public void onFail() {
                Toast.makeText(CategoryActivity.this,R.string.Fail_to_load_movement,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        adapter_listMovement.clear();
        swipeRefreshLayout.setRefreshing(true);
        loadMovement();
        post_list.setAdapter(adapter_listMovement);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}
