package com.example.administrator.quantaproject.tools;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/29.
 */

public class Adapter_Category extends RecyclerView.Adapter implements View.OnClickListener {

    private List<Category> data = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener = null;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_category, parent, false);
        ItemCategory viewholder = new ItemCategory(view);
        view.setOnClickListener(this);

        return viewholder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Category category = data.get(position);

        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);

        ((ItemCategory) holder).imgCategory.setImageDrawable(category.getImage());
        ((ItemCategory) holder).nameCategory.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void addAll(List<Category> data){
        this.data =data;
        notifyDataSetChanged();
    }

    public void clear(){
        data.clear();
        notifyDataSetChanged();
    }

    class ItemCategory extends RecyclerView.ViewHolder{

        public ItemCategory(View itemView) {
            super(itemView);
            this.imgCategory = (ImageView) itemView.findViewById(R.id.iv_category_image);
            this.nameCategory = (TextView) itemView.findViewById(R.id.tv_category_name);
        }

        private ImageView imgCategory;
        private TextView nameCategory;

    }
}
