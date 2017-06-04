package com.example.administrator.quantaproject.tools;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.MyGroup;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class Adapter_MyGroup extends RecyclerView.Adapter {
    private List<MyGroup> myGroupList = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item = layoutInflater.inflate(R.layout.item_mine_mygroup,parent,false);
        ItemMyGroup viewHolder = new ItemMyGroup(item);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(myGroupList.get(position).getGroupImageUrl()))
                .setResizeOptions(new ResizeOptions(50, 50))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(((ItemMyGroup)holder).sdvGroup.getController())
                .setImageRequest(request)
                .build();

        ((ItemMyGroup)holder).sdvGroup.setController(controller);
        ((ItemMyGroup)holder).tvGroupName.setText(myGroupList.get(position).getGroupName());
    }

    @Override
    public int getItemCount() {
        return myGroupList.size();
    }

    public void addAll(List<MyGroup> myMessageList){
        this.myGroupList.addAll(myMessageList);
        notifyDataSetChanged();
    }

    public void clear(){
        this.myGroupList.clear();
        notifyDataSetChanged();
    }

    class ItemMyGroup extends RecyclerView.ViewHolder{
        private TextView tvGroupName;
        private SimpleDraweeView sdvGroup;

        public ItemMyGroup(View itemView) {
            super(itemView);
            tvGroupName = (TextView) itemView.findViewById(R.id.tv_myGroup_name);
            sdvGroup = (SimpleDraweeView) itemView.findViewById(R.id.sdv_myGroup_image);
        }
    }
}
