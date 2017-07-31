package com.example.administrator.quantaproject.tools;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.quantaproject.R;

/**
 * Created by Administrator on 2017/7/5.
 */

public class Adapter_PoPupWindowList extends RecyclerView.Adapter {
    private String[] data = null;

    public Adapter_PoPupWindowList(String[] data){
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item = layoutInflater.inflate(R.layout.item_popubwin_list, parent, false);
        ItemOption viewholder = new ItemOption(item);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);

        ((ItemOption)holder).option.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class ItemOption extends RecyclerView.ViewHolder{
        private TextView option = null;

        public ItemOption(View itemView) {
            super(itemView);
            this.option = (TextView) itemView.findViewById(R.id.tv_poPupContent);
        }

    }
}
