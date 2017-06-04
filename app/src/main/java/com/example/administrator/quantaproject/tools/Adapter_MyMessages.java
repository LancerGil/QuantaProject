package com.example.administrator.quantaproject.tools;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.MyMessage;
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

public class Adapter_MyMessages extends RecyclerView.Adapter {
    private List<MyMessage> myMessageList = new ArrayList<>();
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item = layoutInflater.inflate(R.layout.item_mymessage,parent,false);
        ItemMyMessage viewHolder = new ItemMyMessage(item);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(myMessageList.get(position).getIvChatWinUrl()))
                .setResizeOptions(new ResizeOptions(50, 50))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(((ItemMyMessage)holder).ivChatWin.getController())
                .setImageRequest(request)
                .build();

        ((ItemMyMessage)holder).ivChatWin.setController(controller);
        ((ItemMyMessage)holder).tvChatWin.setText(myMessageList.get(position).getTvChatWindow());
        ((ItemMyMessage)holder).tvLastSender.setText(myMessageList.get(position).getLastSender()+":");
        ((ItemMyMessage)holder).tvMessageCon.setText(myMessageList.get(position).getMessageCon());
    }

    @Override
    public int getItemCount() {
        return myMessageList.size();
    }

    public void addAll(List<MyMessage> myMessageList){
        this.myMessageList.addAll(myMessageList);
        notifyDataSetChanged();
    }

    public void clear(){
        this.myMessageList.clear();
        notifyDataSetChanged();
    }

    class ItemMyMessage extends RecyclerView.ViewHolder{
        private TextView tvChatWin,tvLastSender,tvMessageCon;
        private SimpleDraweeView ivChatWin;

        public ItemMyMessage(View itemView) {
            super(itemView);
            tvChatWin = (TextView) itemView.findViewById(R.id.tv_message_chatWin);
            tvLastSender = (TextView) itemView.findViewById(R.id.tv_message_lastSender);
            tvMessageCon = (TextView) itemView.findViewById(R.id.tv_message_messageCon);
            ivChatWin = (SimpleDraweeView) itemView.findViewById(R.id.iv_message_chatWin);
        }
    }
}
