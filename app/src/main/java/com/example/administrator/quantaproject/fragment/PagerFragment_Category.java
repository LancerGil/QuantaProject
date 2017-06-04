package com.example.administrator.quantaproject.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.activity.CategoryActivity;
import com.example.administrator.quantaproject.data.Category;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.tools.Adapter_Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public class PagerFragment_Category extends Fragment {

    private List<Category> data = new ArrayList<>();
    private String[] category_name;
    private Drawable[] category_image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View page_Category = inflater.inflate(R.layout.fragment_home_pager_category, container, false);

        RecyclerView category_list = (RecyclerView) page_Category.findViewById(R.id.rv_category_list);
        category_list.addItemDecoration(new SpacesItemDecoration(35,35,25,10));

        Adapter_Category adapter_category = new Adapter_Category();

        category_name = getActivity().getResources().getStringArray(R.array.category_name);
        category_image = new Drawable[]{
                getActivity().getResources().getDrawable(R.mipmap.category_chat),
                getActivity().getResources().getDrawable(R.mipmap.category_sportlife),
                getActivity().getResources().getDrawable(R.mipmap.category_change_idle),
                getActivity().getResources().getDrawable(R.mipmap.category_study),
                getActivity().getResources().getDrawable(R.mipmap.category_raiders),
        };

        for(int i = 0; i < category_name.length ; i++ ){
            data.add(new Category(category_image[i],category_name[i]));
        }

        adapter_category.addAll(data);

        adapter_category.setOnItemClickListener(new Adapter_Category.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent turnToCategoryContent = new Intent(getActivity(), CategoryActivity.class);
                turnToCategoryContent.putExtra(PingTai_Config.KEY_CATEGORY_NAME,getResources().getStringArray(R.array.category_name)[position]);
                turnToCategoryContent.putExtra(PingTai_Config.KEY_PHONE_NUM,getActivity().getIntent().getStringExtra(PingTai_Config.KEY_PHONE_NUM));

                startActivity(turnToCategoryContent);
            }
        });

        category_list.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        category_list.setAdapter(adapter_category);

        return page_Category;
    }

    class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int left,right,top,bottom;

        public SpacesItemDecoration(int left,int right,int top,int bottom) {
            this.left=left;
            this.right=right;
            this.top=top;
            this.bottom=bottom;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = left;
            outRect.right = right;
            outRect.bottom = bottom;
            outRect.top = top;
        }
    }
}
