package com.example.administrator.quantaproject.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.tools.Adapter_PagerOption;

public class OptionDetailActivity extends AppCompatActivity {
    private String[] optionName ;
    private String currentOption;
    private int currentOptionPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_detail);
        ViewPager optionDetailContainer = (ViewPager) findViewById(R.id.vp_option_detail_container);
        optionName = getResources().getStringArray(R.array.option_mine);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_option_detail);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     onBackPressed();
                                                 }
                                             });
        currentOption = getIntent().getStringExtra("optionName");
        for(int i = 0 ; i < optionName.length ;i++ ){
            if(optionName[i].equals(currentOption)){
                currentOptionPos = i;
                toolbar.setTitle(optionName[i]);
            }
        }
        optionDetailContainer.setAdapter(new Adapter_PagerOption(getSupportFragmentManager(),currentOptionPos));
    }
}
