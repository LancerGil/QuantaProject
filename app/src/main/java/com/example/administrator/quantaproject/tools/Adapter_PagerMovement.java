package com.example.administrator.quantaproject.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.astuetz.PagerSlidingTabStrip;
import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.fragment.Fragment_Food;
import com.example.administrator.quantaproject.fragment.Fragment_Home;
import com.example.administrator.quantaproject.fragment.Fragment_Mine;
import com.example.administrator.quantaproject.fragment.Fragment_Program;

/**
 * Created by Administrator on 2017/5/26.
 */

public class Adapter_PagerMovement extends FragmentPagerAdapter  implements PagerSlidingTabStrip.CustomTabProvider {

    private Context context = null;
    private int NUM_PAGES = 4;
    private String[] titles = new String[]{
            "首页","美食","活动","我的"
    };

    public Adapter_PagerMovement(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment pagerFragment;
        if (position == 0) {
            pagerFragment = new Fragment_Home();
        } else if(position == 1){
            pagerFragment = new Fragment_Food();
        } else if(position == 2){
            pagerFragment = new Fragment_Program();
        }else {
            pagerFragment = new Fragment_Mine();
        }
        return pagerFragment;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public View getCustomTabView(ViewGroup parent, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View tabView = layoutInflater.inflate(R.layout.tab_movement,parent,false);

//        ImageView icon_tab = (ImageView) tabView.findViewById(R.id.iv_tab_movement);
//        TextView tv_pageTitle = (TextView) tabView.findViewById(R.id.tv_tab_movement);
        RadioButton rbTab = (RadioButton) tabView.findViewById(R.id.rbTab);
        Drawable drawable;
        switch (position){
            case 0:
//                icon_tab.setImageResource(R.drawable.tab_selector_home);
//                tv_pageTitle.setText("首页");
                rbTab.setText("首页");
                drawable = context.getResources().getDrawable(R.drawable.tab_selector_home);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                rbTab.setCompoundDrawables(null,drawable,null,null);
                break;
            case 1:
//                icon_tab.setImageResource(R.drawable.tab_selector_food);
//                tv_pageTitle.setText("美食");
                rbTab.setText("美食");
                drawable = context.getResources().getDrawable(R.drawable.tab_selector_food);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                rbTab.setCompoundDrawables(null,drawable,null,null);break;
            case 2:
//                icon_tab.setImageResource(R.drawable.tab_selector_program);
//                tv_pageTitle.setText("活动");
                rbTab.setText("活动");
                drawable = context.getResources().getDrawable(R.drawable.tab_selector_program);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                rbTab.setCompoundDrawables(null,drawable,null,null);break;
            case 3:
//                icon_tab.setImageResource(R.drawable.tab_selector_mine);
//                tv_pageTitle.setText("我的");
                rbTab.setText("我的");
                drawable = context.getResources().getDrawable(R.drawable.tab_selector_mine);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                rbTab.setCompoundDrawables(null,drawable,null,null);break;
        }


        return tabView;
    }

    @Override
    public void tabSelected(View tab) {
    }

    @Override
    public void tabUnselected(View tab) {
        ((RadioButton)tab).setChecked(false);
//        ((TextView)tab.findViewById(R.id.tv_tab_movement)).setTextColor(context.getResources().getColor(R.color.tag_unSelected));
    }
}
