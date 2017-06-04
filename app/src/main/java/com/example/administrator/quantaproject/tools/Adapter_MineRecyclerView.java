package com.example.administrator.quantaproject.tools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.quantaproject.R;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class Adapter_MineRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private String[] optionName;
    private OnItemClickListener mOnItemClickListener;


    public Adapter_MineRecyclerView(String[] optionName, Context context) {
        this.optionName= optionName;

    }


    @Override
    public int getItemCount() {
        return optionName.length;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_list__card_small, parent, false);
        view.setOnClickListener(this);
        ItemMine viewHolder = new ItemMine(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);

        ((ItemMine) holder).optionName.setText(optionName[position]);
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

    class ItemMine extends  RecyclerView.ViewHolder {

        private TextView optionName;
        private ImageView iv_seeDetail;

        public ItemMine(View itemMine) {
            super(itemMine);

            this.optionName = (TextView) itemMine.findViewById(R.id.tv_option_name);
            this.iv_seeDetail = (ImageView) itemMine.findViewById(R.id.iv_seeDetail);
        }
    }
}