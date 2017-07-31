package com.example.administrator.quantaproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.tools.BitmapUtils;
import com.example.administrator.quantaproject.tools.MyCopView;

import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;


public class CropPicActivity extends AppCompatActivity {

    private static final String TAG = "CropPicActivity";
    private static final int RESULTFROM_ALBUM= 1;
    private static final int RESULTFROM_CAMERA= 2;

    private int resultFrom;

    @BindView(R.id.mytoolbar)
    private Toolbar toolbar;

    @BindView(R.id.my_image_view_crop)
    private MyCopView myCopView;


    private ImageButton complete= null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_croppic);

        initToolbar();
        initCropView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initToolbar(){

        toolbar = (Toolbar) findViewById(R.id.mytoolbar);


        Log.d(TAG,"Toolbar:"+toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
            toolbar.setNavigationIcon(R.mipmap.btn_back);
            toolbar.setTitle(R.string.crop_and_move);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.inflateMenu(R.menu.menu_crop);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                switch (menuItemId){
                    case R.id.crop_complete:
                        cropAndSaveImage();
                        Log.d(TAG,"finish");
                        finish();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 初始化裁剪View
     */
    private void initCropView() {
        myCopView = (MyCopView) findViewById(R.id.my_image_view_crop);
        final Intent intent = getIntent();
        switch (intent.getIntExtra("resultFrom",0)){
            case PingTai_Config.REQUEST_CODE_ASK_FOR_PIC_FROM_SDCARD:
                resultFrom = RESULTFROM_ALBUM;
                break;
            case PingTai_Config.REQUEST_CODE_ASK_FOR_PIC_FROM_CAMERA:
                resultFrom = RESULTFROM_CAMERA;
                break;
            default:
                break;
        }
        setImageData(intent);
    }

    private void setImageData(Intent intent){
        Log.d(TAG,"Uri:"+intent.getStringExtra("Uri"));
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(intent.getStringExtra("Uri")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myCopView.setImageBitmap(bitmap);
    }

    private void cropAndSaveImage() {
        Bitmap clipBitmap = myCopView.clipBitmap();
        if(clipBitmap != null){
            Uri uri = Uri.parse(BitmapUtils.saveImage(this,clipBitmap,PingTai_Config.getCachedPhoneNum(this)));
            Intent data = new Intent();
            data.setData(uri);
            setResult(Activity.RESULT_OK,data);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
//        if(resultCode == Activity.RESULT_OK) {
//            intent.putExtra("Uri", data.getData().toString());
//            setImageData(intent);
//        }else {
//            finish();
//        }

        switch (requestCode) {
            case PingTai_Config.REQUEST_CODE_ASK_FOR_PIC_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap photo = data.getParcelableExtra("data");
                    BitmapUtils.saveImage(this, photo, PingTai_Config.getCachedPhoneNum(this));
                    myCopView.setImageBitmap(photo);
                }
                break;

            case PingTai_Config.REQUEST_CODE_ASK_FOR_PIC_FROM_SDCARD:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(data.getData().toString()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myCopView.setImageBitmap(bitmap);
                }
                break;
        }

        switch (requestCode){
            case PingTai_Config.REQUEST_CODE_ASK_FOR_PIC_FROM_SDCARD:
                resultFrom = RESULTFROM_ALBUM;
                break;
            case PingTai_Config.REQUEST_CODE_ASK_FOR_PIC_FROM_CAMERA:
                resultFrom = RESULTFROM_CAMERA;
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG,"resultFrom:"+resultFrom);
        switch (resultFrom){
            case RESULTFROM_ALBUM:
                Intent picIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picIntent, PingTai_Config.REQUEST_CODE_ASK_FOR_PIC_FROM_SDCARD);
                break;
            case RESULTFROM_CAMERA:
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takeIntent, PingTai_Config.REQUEST_CODE_ASK_FOR_PIC_FROM_CAMERA);
                break;
            default:
                break;
        }
    }
}
