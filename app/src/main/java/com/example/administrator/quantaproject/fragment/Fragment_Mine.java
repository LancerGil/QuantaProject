package com.example.administrator.quantaproject.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.activity.CropPicActivity;
import com.example.administrator.quantaproject.activity.MovementActivity;
import com.example.administrator.quantaproject.activity.OptionDetailActivity;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.DownloadUtil;
import com.example.administrator.quantaproject.net.PersonalInfo;
import com.example.administrator.quantaproject.tools.Adapter_MineRecyclerView;
import com.example.administrator.quantaproject.tools.BitmapUtils;
import com.kevin.crop.UCrop;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/26.
 */

public class Fragment_Mine extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private SwipeRefreshLayout swipeRefreshLayout;
    private String phoneNum;
    private TextView myIntegral,myName;
    private Handler headHandler ;
    private ImageView myHead;
    private Adapter_MineRecyclerView mineRecylerViewAdpt;
    private LinearLayout mineRV_container;
    private View mineRV;

    // 剪切后图像文件
    private Uri mDestinationUri;

    private static String tempHeadName = "tempHeadPic";
    private static String TAG = "Fragment_Mine";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDestinationUri = Uri.fromFile(new File(getActivity().getCacheDir(),"cropedPhoto.jpeg"));

        final View mine = inflater.inflate(R.layout.fragment_mine,container,false);
        phoneNum = PingTai_Config.getCachedPhoneNum(getContext());
        mineRV_container = (LinearLayout) mine.findViewById(R.id.mineRV_container);
        mineRV = inflater.inflate(R.layout.post_list,container,false);
        myHead = (ImageView) mine.findViewById(R.id.iv_mine_userHead);
        myName = (TextView) mine.findViewById(R.id.tv_mine_user_id);
        myIntegral = (TextView) mine.findViewById(R.id.tv_mine_userIntegral);

        //Init Refresh
        swipeRefreshLayout = (SwipeRefreshLayout) mine.findViewById(R.id.refresh_layout_mine);
        swipeRefreshLayout.setOnRefreshListener(this);
        //为下拉刷新的设置四种颜色
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_purple,
                android.R.color.holo_orange_light
        );

        //init handler for setting headPicture
        headHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                myHead.setImageBitmap((Bitmap) msg.obj);
                Log.e("HEAD", "Set");
                super.handleMessage(msg);
            }
        };

        //init toolbar
        Toolbar toolbar_mine = (Toolbar) mine.findViewById(R.id.tb_mine);
        toolbar_mine.setTitleTextColor(Color.WHITE);
        toolbar_mine.setNavigationIcon(R.mipmap.btn_back);
        if (toolbar_mine!=null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_mine);
            toolbar_mine.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MovementActivity)getActivity()).getViewPager().setCurrentItem(0);
                }
            });
            toolbar_mine.setTitleTextAppearance(getActivity(),R.style.Toolbar_TitleText);
            Log.e("MineActionBar","SET");
            ActionBar actionBar =  ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setTitle("我的");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        //init RecylerView
        RecyclerView mRecyclerView = (RecyclerView) mineRV.findViewById(R.id.rv_movement);
        final String[] items = getActivity().getResources().getStringArray(R.array.option_mine);
        mineRecylerViewAdpt = new Adapter_MineRecyclerView(items,getActivity());
        mineRecylerViewAdpt.setOnItemClickListener(new Adapter_MineRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent turnToOptionDetail = new Intent(getActivity(),OptionDetailActivity.class);
                turnToOptionDetail.putExtra("optionName",items[position]);
                startActivity(turnToOptionDetail);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mineRecylerViewAdpt);
        mRecyclerView.setHasFixedSize(true);
        mineRV_container.removeAllViews();
        mineRV_container.addView(mineRV);

        //get personal information
        loadPersonalInfo();
        //sethead listener
        myHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapUtils.showDialogToChoosePic(getActivity(),Fragment_Mine.this,tempHeadName);
            }
        });
        return mine;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission Granted
                    switch (permissions[0]){
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent,PingTai_Config.REQUEST_CODE_ASK_FOR_PIC_FROM_SDCARD);
                            break;
                        case Manifest.permission.CAMERA:
                            Intent intent2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent2,PingTai_Config.REQUEST_CODE_ASK_FOR_PIC_FROM_CAMERA);
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"onActivityResult:running");
        Log.e(TAG,"data:"+data);
        switch (requestCode) {
            case PingTai_Config.REQUEST_CODE_ASK_FOR_PIC_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap  photo = data.getParcelableExtra("data");
                    String imagePath = BitmapUtils.saveImage(getContext(),photo,phoneNum);
                    startCropActivity(imagePath,requestCode);
                }
                break;

            case PingTai_Config.REQUEST_CODE_ASK_FOR_PIC_FROM_SDCARD:
                if (resultCode == Activity.RESULT_OK) {
                    startCropActivity(data.getData().toString(),requestCode);
                }
                break;

            case PingTai_Config.REQUEST_CODE_ASK_FOR_CROP:    // 裁剪图片结果
                if (resultCode == Activity.RESULT_OK) {
                    handleCropResult(data, myHead);
                }
                break;
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param imagePath
     */
    public void startCropActivity(String imagePath,int resultFrom) {
        Log.d(TAG,"FileUri:"+imagePath);
        Intent turnToCrop = new Intent(getActivity(), CropPicActivity.class);
        turnToCrop.putExtra("Uri",imagePath);
        turnToCrop.putExtra("resultFrom",resultFrom);
        startActivityForResult(turnToCrop,PingTai_Config.REQUEST_CODE_ASK_FOR_CROP);
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result,ImageView view) {
        deleteTempPhotoFile();
        final Uri resultUri = result.getData();
        if (null != resultUri ) {
            Log.d(TAG,"resultUri:"+resultUri);
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeFile(resultUri.getPath());
//                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
            view.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getContext(), "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除拍照临时文件
     */
    private void deleteTempPhotoFile() {
        File tempFile = new File(PingTai_Config.MY_TEMPPHOTO_PATH+tempHeadName+".jpeg");
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result) {
        deleteTempPhotoFile();
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(getContext(), cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
        loadPersonalInfo();
    }

    private void loadPersonalInfo() {
            new PersonalInfo(getActivity(), phoneNum, new PersonalInfo.SuccessCallback() {
                @Override
                public void onSuccess(final Map<String,String> result) {
                    myName.setText(result.get(PingTai_Config.KEY_NICKNAME));
                    myIntegral.setText(result.get(PingTai_Config.KEY_USERINTEGRAL));
                    if (!BitmapUtils.readImage(myHead, getActivity(), phoneNum)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap head = DownloadUtil.getHttpBitmap(result.get(PingTai_Config.KEY_HEAD_IMAGEURL));
                                Bitmap finalHead = BitmapUtils.circleBitmap(BitmapUtils.imageCrop(head, 1, 1, true));
                                Message msg = new Message();
                                msg.obj = finalHead;
                                headHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                }
            }, new PersonalInfo.FailCallback() {
                @Override
                public void onFail() {
                    Toast.makeText(getContext(),R.string.fail_to_get_personalInfo,Toast.LENGTH_SHORT).show();
                }
            });
    }

}
