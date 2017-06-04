package com.example.administrator.quantaproject.tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.Comment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/1.
 */

public class Adapter_CommentList extends RecyclerView.Adapter {

    private ArrayList<Comment> data = new ArrayList<>();
    private Context context;

    public Adapter_CommentList(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemComment = inflater.inflate(R.layout.item_comment_list,parent,false);
        ItemComment viewHolder = new ItemComment(itemComment);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Comment comment = data.get(position);

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(comment.getCommentHeadUrl()))
                .setResizeOptions(new ResizeOptions(30, 30))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(((ItemComment) holder).commentHead.getController())
                .setImageRequest(request)
                .build();
        ((ItemComment) holder).commentHead.setController(controller);

        ((ItemComment) holder).commentHost.setText(comment.getCommentUser());
        ((ItemComment) holder).commentTime.setText(comment.getTimeOfComment());
        ((ItemComment) holder).commentContent.setText(comment.getComment());
        ((ItemComment) holder).likeTimes.setText(comment.getLikeTimes());

        ((ItemComment) holder).btnLike.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(v.getBackground().equals(context.getDrawable(R.mipmap.like_pre))) {
                    v.setBackground(context.getDrawable(R.mipmap.like_after));
                }else {
                    v.setBackground(context.getDrawable(R.mipmap.like_pre));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void addAll(ArrayList<Comment> data){
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear(ArrayList<Comment> data){
        this.data.clear();
        notifyDataSetChanged();
    }

    class ItemComment extends RecyclerView.ViewHolder{

        private TextView commentHost,commentTime,commentContent,likeTimes;
        private ImageView btnLike;
        private SimpleDraweeView commentHead;

        public ItemComment(View itemView) {
            super(itemView);
            commentHead = (SimpleDraweeView) itemView.findViewById(R.id.iv_commentHead);
            commentHost = (TextView) itemView.findViewById(R.id.tv_commentHost);
            commentContent = (TextView) itemView.findViewById(R.id.tv_normalPostContent);
            commentTime = (TextView) itemView.findViewById(R.id.tv_commentTime);
            btnLike = (ImageView) itemView.findViewById(R.id.iv_comment_btnLike);
            likeTimes = (TextView) itemView.findViewById(R.id.tv_likeTimes);
        }
    }

}
