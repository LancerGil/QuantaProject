package com.example.administrator.quantaproject.tools;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.Movements;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */

public class Adapter_ListMovement extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private List<Movements> data = new ArrayList<>();
    private Context context = null;
    private int width, height;
    private SimpleDraweeView programImage;
    private ViewGroup.LayoutParams pubImageLP,imageContainerLP;
    private OnItemClickListener mOnItemClickListener = null;

    public Adapter_ListMovement(Context context) {
        this.context = context;
    }

    private static final String TAG = "Adapter_ListMovement";


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item = layoutInflater.inflate(R.layout.item_movement_normal, parent, false);
        ItemMovement viewholder = new ItemMovement(item);

        item.setOnClickListener(this);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(R.string.TAG_position,position);

        Log.e("POSITION", position + "");
        Movements movements = data.get(position);
//        Log.e("TAG_movement:",movements.toString());
        holder.itemView.setTag(R.string.TAG_movement,movements);

        ((ItemMovement) holder).title.setText(movements.getPubTitle());

        if(!movements.getCategory().equals(PingTai_Config.ACTION_MOVEMENT_PROGRAM)) {

            Log.e("Category:",movements.getCategory());
            width = 192;
            height = 108;
            pubImageLP = ((ItemMovement) holder).pubImage.getLayoutParams();
            pubImageLP.height = height;
            pubImageLP.width = width;
            ((ItemMovement) holder).pubImage.setLayoutParams(pubImageLP);
        }else if(!(movements.getImageUrl().size()==0)){

            ViewGroup.LayoutParams itemContainerLP = ((ItemMovement) holder).imageContainer.getLayoutParams();
            itemContainerLP.height = 267;
            ((ItemMovement) holder).imageContainer.setLayoutParams(itemContainerLP);

            Log.e("Category:",movements.getCategory());
            width = 456;
            height = 169;

            imageContainerLP = ((ItemMovement) holder).imageContainer.getLayoutParams();
            imageContainerLP.height = height;
            imageContainerLP.width = width;
            ((ItemMovement) holder).imageContainer.setLayoutParams(imageContainerLP);

            programImage = new SimpleDraweeView(context);
            programImage.setLayoutParams(new ViewGroup.LayoutParams(width, height));
            ((ItemMovement) holder).imageContainer.addView(programImage);
        }

        if(movements.getImageUrl().size()==0){
            Log.e("ImageUrlIs:","null");
            Log.e("ImageUrlIs:", movements.getImageUrl()+"");
            ((ItemMovement) holder).preContent.setMaxWidth(456);

        }else {
            Log.e("ImageUrlIs:","notNull");
            Log.e("ImageUrlIs:", movements.getImageUrl()+"");
            ((ItemMovement) holder).preContent.setMaxWidth(288);

            Log.e("WIDTH:",width+"");
            Log.e("WIDTH:",height+"");
            ImageRequest request = null;
            request = ImageRequestBuilder.newBuilderWithSource(Uri.parse((String) movements.getImageUrl().get(0)))
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(((ItemMovement) holder).pubImage.getController())
                    .setImageRequest(request)
                    .build();

            if(movements.getCategory().equals(PingTai_Config.ACTION_MOVEMENT_PROGRAM)){
                final DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                        .setOldController(programImage.getController())
                        .setImageRequest(request)
                        .build();

                programImage.setController(controller1);
                Log.e("added_ImageView:","setController");
            }else {
                Log.e("pubImageView:","setController");
                ((ItemMovement) holder).pubImage.setController(controller);
                ((ItemMovement) holder).preContent.setText(movements.getContent().replace("#",""));
            }
        }


        ((ItemMovement) holder).pubUser.setText(movements.getpubUser());

        int viewCount = Integer.parseInt(movements.getViewTimes());
        if (viewCount > 10000) {
            viewCount = viewCount / 10000;
            ((ItemMovement) holder).viewTimes.setText(" " + viewCount + "万");
        } else {
            ((ItemMovement) holder).viewTimes.setText(""+movements.getViewTimes());
        }

        int commentCount = Integer.parseInt(movements.getCommentTimes());
        if (commentCount > 10000) {
            commentCount = commentCount / 10000;
            ((ItemMovement) holder).commentTimes.setText(" " + commentCount + "万");
        } else {
            ((ItemMovement) holder).commentTimes.setText(""+movements.getCommentTimes());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public Context getContext() {
        return context;
    }

    public void addAll(List<Movements> data) {
        data.addAll(this.data);
        this.data.clear();
        this.data.addAll(data);
        Log.d(TAG,"dataSize:"+data.size());
        notifyDataSetChanged();
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag(R.string.TAG_position));
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class ItemMovement extends RecyclerView.ViewHolder {
        public ItemMovement(View movement) {
            super(movement);
            title = (TextView) movement.findViewById(R.id.tv_title);
            imageContainer = (LinearLayout) movement.findViewById(R.id.ll_imageView_container);
            preContent = (TextView) movement.findViewById(R.id.tv_preContent);
            pubUser = (TextView) movement.findViewById(R.id.tv_pubUser);
            viewTimes = (TextView) movement.findViewById(R.id.tv_viewNum);
            commentTimes = (TextView) movement.findViewById(R.id.tv_commentNum);
            pubImage = (SimpleDraweeView) movement.findViewById(R.id.iv_pubImage);
            itemContainer = (LinearLayout) movement.findViewById(R.id.item_container);
        }

        private TextView title, preContent, pubUser, viewTimes, commentTimes;
        private SimpleDraweeView pubImage;
        private LinearLayout imageContainer,itemContainer;
    }

    class ItemFood extends RecyclerView.ViewHolder{
        private SimpleDraweeView imageFood;
        private ImageView foodLoca,foodBtnlike,foodBtnUnlike;
        private TextView tvFoodName,tvFoodPrice,tvFoodLoca,tvFoodLikeTimes,tvFoodUnlikeTimes,
        tvFoodPubTime,tvFoodPubUser,tvFoodCategory;
        public ItemFood(View itemView) {
            super(itemView);
            imageFood = (SimpleDraweeView) itemView.findViewById(R.id.sdv_FoodImage);
            foodLoca = (ImageView) itemView.findViewById(R.id.rv_food);
//            foodBtnlike = (ImageView) itemView.findViewById(R.i);
        }
    }
}