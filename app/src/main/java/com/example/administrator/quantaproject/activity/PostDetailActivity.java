package com.example.administrator.quantaproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.Comment;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.DownloadUtil;
import com.example.administrator.quantaproject.net.HttpMethod;
import com.example.administrator.quantaproject.net.NetConnection;
import com.example.administrator.quantaproject.net.PutComment;
import com.example.administrator.quantaproject.tools.Adapter_CommentList;
import com.example.administrator.quantaproject.tools.BitmapUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostDetailActivity extends AppCompatActivity {

    private String postKind;
    private ArrayList<Bitmap> pubImages = new ArrayList<>();
    private String hostPhoneNum,token;
    private ArrayList<Comment> movements;
    private Adapter_CommentList adapter_commentList;
    private Handler contentPic_handler,head_Handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        hostPhoneNum = PingTai_Config.getCachedPhoneNum(PostDetailActivity.this);
        token = PingTai_Config.getCachedToken(PostDetailActivity.this);
        postKind = getIntent().getStringExtra(PingTai_Config.KEY_CATEGORY);

        Log.e("posyKINd", postKind);

        ViewGroup container = (ViewGroup) findViewById(R.id.post_detail_container);

        if (!postKind.equals("food")) {
            View fragment_NormalPost = getLayoutInflater().inflate(R.layout.fragment_normal_post, null);


            final Intent movementDetail = getIntent();
            final String movementId = movementDetail.getStringExtra(PingTai_Config.KEY_MOVEMENT_ID);
            String title = movementDetail.getStringExtra(PingTai_Config.KEY_PUBTITLE);
            String pubUser = movementDetail.getStringExtra(PingTai_Config.KEY_PUBUSER);
            String pubTime = movementDetail.getStringExtra(PingTai_Config.KEY_PUBTIME);
            final String category = movementDetail.getStringExtra(PingTai_Config.KEY_CATEGORY);
            final ArrayList<String> imageUrls = movementDetail.getStringArrayListExtra(PingTai_Config.KEY_IMAGEURL);
            Log.e("ImageUrL",imageUrls+"");
//            String groupUrls = movementDetail.getStringExtra(PingTai_Config.KEY_GROUPURLS);
            final String phoneNum = movementDetail.getStringExtra(PingTai_Config.KEY_PHONE_NUM);

            int categoryNamePos = -1;
            for(int i = 0;i < getResources().getStringArray(R.array.category_action).length;i++){
                if(category.equals(getResources().getStringArray(R.array.category_action)[i])){
                    categoryNamePos = i;
                }
            }
            TextView tvCategory = (TextView) fragment_NormalPost.findViewById(R.id.tv_normalPostCategory);
            String ctegoryName;

            Toolbar toolbar = (Toolbar) fragment_NormalPost.findViewById(R.id.title_toolbar);
            toolbar.setNavigationIcon(R.mipmap.btn_back);
            toolbar.setTitleTextColor(Color.WHITE);
            if(categoryNamePos!=-1) {
                ctegoryName = getResources().getStringArray(R.array.category_name)[categoryNamePos];
                toolbar.setTitle(ctegoryName);
                tvCategory.setText(ctegoryName);
            }
            setSupportActionBar(toolbar);
            toolbar.setTitleTextAppearance(this,R.style.Toolbar_TitleText);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            TextView tvTitle = (TextView) fragment_NormalPost.findViewById(R.id.tv_normalPostTitle);
            tvTitle.setText(title);

            TextView tvPubUser_up = (TextView) fragment_NormalPost.findViewById(R.id.tv_normalPostPubUser_upon);
            TextView tvPubUser_down = (TextView) fragment_NormalPost.findViewById(R.id.tv_normalPostPubUser_blow);
            tvPubUser_up.setText(pubUser);
            tvPubUser_down.setText(pubUser);

            TextView tvPubTime = (TextView) fragment_NormalPost.findViewById(R.id.tv_normalPostPubTime);
            if(pubTime.length()==15) {
                tvPubTime.setText(pubTime.substring(0, 10) + " " + pubTime.substring(11,12) + ":" + pubTime.substring(13,15));
            }else {
//                tvPubTime.setText(pubTime.substring(0, 10) + " " + pubTime.substring(12, 13) + ":" + pubTime.substring(15,16));
            }

            final TextView tvContent = (TextView) fragment_NormalPost.findViewById(R.id.tv_normalPostContent);
            contentPic_handler = new Handler(){
                @Override
                public void handleMessage(final Message msg) {
                    String content = movementDetail.getStringExtra(PingTai_Config.KEY_PUBCONTENT);
                    Log.e("Content",content);
                    int index = content.indexOf("#");
                    tvContent.setText(content.subSequence(0,index-1)+"\n\n");
                    for (int i = 0; i < ((ArrayList<Bitmap>)msg.obj).size(); i++) {
                        Log.e("图片数量",((ArrayList<Bitmap>)msg.obj).size()+"");
                        Drawable image = new BitmapDrawable(((ArrayList<Bitmap>)msg.obj).get(i));
                        image.setBounds(0,0,((ArrayList<Bitmap>)msg.obj).get(i).getWidth()/5,((ArrayList<Bitmap>)msg.obj).get(i).getHeight()/5);
                        index = content.indexOf("#");
                        SpannableString spanStr = new SpannableString(content.subSequence(index,index+1));
                        Log.i("spanStr",spanStr+"");
                        content = content.substring(index+1,content.length()-1);
                        Log.i("CONTENT",content);
                        Log.e("插入位置",index+"");
                        spanStr.setSpan(new ImageSpan(image), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        tvContent.append(spanStr);
                        tvContent.append("\n\n");
                        index = content.indexOf("#");
                        if(index !=-1) {
                            tvContent.append(content.substring(0, index - 1)+"\n\n");
                        }else {
                            tvContent.append(content+"\n\n");
                        }
                    }
                    super.handleMessage(msg);
                }
            };
            if (imageUrls != null) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < imageUrls.size(); i++) {
                            Log.e("第"+i+"张",imageUrls.get(i));
                            final Bitmap bitmap = DownloadUtil.getHttpBitmap(imageUrls.get(i));
                            pubImages.add(bitmap);
                        }
                        if(pubImages.size()!=0) {
                            Message msg = new Message();
                            msg.obj = pubImages;
                            contentPic_handler.sendMessage(msg);
                        }
                    }
                }).start();
            }

//            if(groupUrls!=null){
//
//            }

            final ImageView iv_head = (ImageView) fragment_NormalPost.findViewById(R.id.iv_normalPostPubUserHead);
            head_Handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    iv_head.setImageBitmap((Bitmap)msg.obj);
                    super.handleMessage(msg);
                }
            };
            if(!readImage(iv_head,phoneNum)){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap pngBM = BitmapFactory.decodeStream(new URL(PingTai_Config.SERVER_URL_LOCAL_IMAGE+phoneNum+".jpg").openStream());

                            Message msg = new Message();
                            msg.obj = BitmapUtils.circleBitmap(pngBM);
                            head_Handler.sendMessage(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                Log.e("LOADING-HEAD",phoneNum+".jpg");
                iv_head.setImageURI(Uri.parse(PingTai_Config.SERVER_URL_LOCAL_IMAGE+phoneNum+".jpg"));
            }

            Button btn_follow = (Button) fragment_NormalPost.findViewById(R.id.btn_normalPostFollow);
            btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new NetConnection(PingTai_Config.SERVER_URL, HttpMethod.GET, new NetConnection.SuccessCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject result_json = new JSONObject(result);
                                switch (result_json.getInt(PingTai_Config.KEY_STATUS)) {
                                    case PingTai_Config.RESULT_STATUS_SUCCESS:
                                        Toast.makeText(PostDetailActivity.this,"关注成功！",Toast.LENGTH_SHORT).show();
                                        break;
                                    case PingTai_Config.RESULT_STATUS_FAIL:
                                        Toast.makeText(PostDetailActivity.this,"网络出问题啦，过一会再试吧",Toast.LENGTH_SHORT).show();
                                        break;
                                    case PingTai_Config.RESULT_STATUS_INVALID_TOKEN:
                                        Toast.makeText(PostDetailActivity.this,"登录已过期，请重新登录",Toast.LENGTH_SHORT).show();
                                        final Intent turnToLogin = new Intent(PostDetailActivity.this,LoginActivity.class);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(turnToLogin);

                                            }
                                        });
                                        finish();
                                        break;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new NetConnection.FailCallback() {
                        @Override
                        public void onFail() {

                        }
                    },PingTai_Config.ACTION_FOLLOW,
                            PingTai_Config.KEY_PHONE_NUM,hostPhoneNum,
                            PingTai_Config.KEY_TOKEN,token,
                            PingTai_Config.KEY_TARGET_USER_PHONE_NUM,phoneNum);
                }
            });

            Button btn_sendMessage = (Button) fragment_NormalPost.findViewById(R.id.btn_normalPostContact);
            btn_sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent turnToChat = new Intent(PostDetailActivity.this,MessageActivity.class);
                    startActivity(turnToChat);
                }
            });

            RecyclerView rvCommentList = (RecyclerView) fragment_NormalPost.findViewById(R.id.rv_normalPost_commentList);
            adapter_commentList = new Adapter_CommentList(PostDetailActivity.this);
            loadComments();
            rvCommentList.setLayoutManager(new LinearLayoutManager(this));
            rvCommentList.setAdapter(adapter_commentList);
            rvCommentList.setHasFixedSize(true);

            ImageView sendComment = (ImageView) fragment_NormalPost.findViewById(R.id.iv_commentSend);
            final EditText etComment = (EditText) fragment_NormalPost.findViewById(R.id.tv_etComment);
            sendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date putComTime = new Date();// 获取当前时间
                    SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
                    sdf.applyPattern("yyyy-MM-dd_HH-mm");//设置格式
                    if(!etComment.getText().toString().isEmpty()){
                        new PutComment(phoneNum, token, movementId, etComment.getText().toString(), sdf.format(putComTime), new PutComment.SuccessCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(PostDetailActivity.this,"评论成功！",Toast.LENGTH_SHORT);
                                etComment.setText("");
                            }
                        }, new PutComment.FailCallback() {
                            @Override
                            public void onFail() {
                                Toast.makeText(PostDetailActivity.this,"网络出问题了，稍后重试吧",Toast.LENGTH_SHORT);
                            }
                        });
                    }
                }
            });


            container.addView(fragment_NormalPost);
        }
    }
    private boolean readImage(ImageView view ,String name) {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){/**判断sd卡是否挂载*/
            /**路径1：storage/sdcard/Android/data/包名/files*/
            filesDir = getExternalFilesDir("");

        }else{/**手机内部存储   路径：data/data/包名/files*/
            filesDir = getFilesDir();

        }
        File file = new File(filesDir,name + ".png");
        if(file.exists()){
            /**存储--->内存*/
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            view.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }

    private void loadComments(){
        new NetConnection(PingTai_Config.SERVER_URL, HttpMethod.GET, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject result_json = new JSONObject(result);
                    switch (result_json.getInt(PingTai_Config.KEY_STATUS)) {
                        case PingTai_Config.RESULT_STATUS_SUCCESS:
                            movements = new ArrayList<>();
                            JSONArray commentJSONArray = result_json.getJSONArray(PingTai_Config.KEY_COMMENTLIST);

                            for (int i = 0; i < commentJSONArray.length(); i++) {
                                JSONObject movementObj = commentJSONArray.getJSONObject(i);
                                movements.add(new Comment(
                                        movementObj.getString(PingTai_Config.KEY_MOVEMENT_ID),
                                        movementObj.getString(PingTai_Config.KEY_PUBTITLE),
                                        movementObj.getString(PingTai_Config.KEY_CONTENT),
                                        movementObj.getString(PingTai_Config.KEY_COMMENTHAED_URL),
                                        movementObj.getInt(PingTai_Config.KEY_COMMENT_LIKETIMES)
                                ));
                            }
                            adapter_commentList.addAll(movements);
                            break;
                        case PingTai_Config.RESULT_STATUS_FAIL:
                            Toast.makeText(PostDetailActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                            break;
                        case PingTai_Config.RESULT_STATUS_INVALID_TOKEN:
                            Toast.makeText(PostDetailActivity.this, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                            final Intent turnToLogin = new Intent(PostDetailActivity.this, LoginActivity.class);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(turnToLogin);

                                }
                            });
                            finish();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {

            }
        },PingTai_Config.KEY_ACTION,PingTai_Config.ACTION_FOLLOW,
                PingTai_Config.KEY_PHONE_NUM,hostPhoneNum,
                PingTai_Config.KEY_TOKEN,token,
                PingTai_Config.KEY_TARGET_USER_PHONE_NUM,hostPhoneNum);
    };
}
