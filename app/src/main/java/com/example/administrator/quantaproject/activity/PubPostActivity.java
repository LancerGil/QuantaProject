package com.example.administrator.quantaproject.activity;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.PubPost;
import com.example.administrator.quantaproject.net.UploadUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PubPostActivity extends AppCompatActivity {

    private ImageView btnTurnBack,btnPublish,btn_pop_from_gallary,btn_pop_from_camera;
    private EditText etPostTitle,etPostContent;
    private String phoneNum,token,categorySelected;
    private LayoutInflater inflater;
    private File file;
    private ImageView uploadPic;
    private Date imageAddTime;
    private ArrayList<String> preimageName = new ArrayList<>(), finalImageNames = new ArrayList<>();
    private ArrayList<File> images = new ArrayList<>();
    private ArrayList<Integer> indexes = new ArrayList<>();
    private Spinner chooseCategory;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_post);

        inflater = getLayoutInflater();
        etPostTitle = (EditText) findViewById(R.id.etPubTitle);
        etPostContent = (EditText) findViewById(R.id.etPubContent);
        btnPublish = (ImageView) findViewById(R.id.iv_publish);
        btnTurnBack = (ImageView) findViewById(R.id.iv_turnBack);
        phoneNum = getIntent().getStringExtra(PingTai_Config.KEY_PHONE_NUM);
        token = PingTai_Config.getCachedToken(PubPostActivity.this);
        uploadPic = (ImageView) findViewById(R.id.uploadPic) ;
        chooseCategory  = (Spinner) findViewById(R.id.pub_chooseCate);
        setupSpinner();


        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentFocus() == etPostContent){
                    showInsertPicDialog();
                }else {
                    Toast.makeText(PubPostActivity.this, R.string.cant_insert_to_not_content,Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnTurnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPostTitle.getText().toString().isEmpty()&&etPostContent.getText().toString().isEmpty()) {
                    finish();
                }else {
                    showCencelDialog();
                }
            }
        });


        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPostTitle.getText().length()<=30&&etPostContent.getText().length()<=3000
                        &&etPostTitle.getText().length()>0&&etPostContent.getText().length()>0&&!categorySelected.equals("选择帖子类型")) {

                    imageAddTime = new Date();// 获取当前时间

//                    final ProgressDialog connecting = ProgressDialog.show(PubPostActivity.this,getResources().getString(R.string.connecting),getResources().getString(R.string.connecting_to_server));
//                    connecting.show();
                    new PubPost(PingTai_Config.ACTION_PUBLISH,
                            phoneNum,
                            token,
                            etPostTitle.getText().toString().replace(" ","").replace("\n","+"),
                            etPostContent.getText().toString().replace(" ","#").replace("\n","+"),
                            categorySelected,
                            new PubPost.SuccessCallback() {
                                @Override
                                public void onSuccess(final int movementId) {
                                    SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
                                    sdf.applyPattern("yyyy-MM-dd_HH-mm");//设置格式
                                    for (int i =0 ; i< preimageName.size();i++){
                                        File from = new File(file,phoneNum+"_"+preimageName.get(i)+"_"+indexes.get(i)+".png");
                                        File to = new File(file,movementId+"_"+sdf.format(imageAddTime)+"_"+indexes.get(i)+".png");
                                        from.renameTo(to);
                                        File Image = new File(file,movementId+"_"+sdf.format(imageAddTime)+"_"+indexes.get(i)+".png");
                                        Log.e("FileNAMECHANGE",Image.getName());
                                        finalImageNames.add(movementId+"_"+sdf.format(imageAddTime)+"_"+indexes.get(i));
                                        images.add(Image);
                                    }
                                    for (int i = 0; i < images.size() ; i++) {
                                                final int finalI = i;
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Log.e("UploadImage", finalI + "");
                                                        UploadUtil.uploadFile(images.get(finalI), PingTai_Config.SERVER_URL + "/upload", movementId + "");
                                                    }
                                                }).start();
                                            }
                                    setResult(PingTai_Config.ACTIVITY_RESULT_NEED_REFRESH);
                                    Toast.makeText(PubPostActivity.this, R.string.PubSuccess,Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }, new PubPost.FailCallback() {
                        @Override
                        public void onFail() {
                            Toast.makeText(PubPostActivity.this, R.string.PubFail,Toast.LENGTH_SHORT).show();
//                            connecting.cancel();
                        }
                    });
                }else {
                    Toast.makeText(PubPostActivity.this,"标题/内容/帖子类型太长或没有填写，请检查",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    //确定取消对话框按钮响应函数
    public void showCencelDialog() {
//使用创建器创建单选对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//设置对话框的图标
//        builder.setIcon(android.R.drawable.alert_light_frame);
//设置对话框的标题
        builder.setTitle(R.string.finish_eiditor_dialog_title);
//设置文本
        builder.setMessage(R.string.finish_editor_dialog_content);
//设置确定按钮
        builder.setPositiveButton(R.string.dialog_determine, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
//设置取消按钮
        builder.setNegativeButton(R.string.dialog_cencel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
//使用创建器生成一个对话框对象
        AlertDialog ad = builder.create();
        ad.show();
    }

    /**
     * Dialog中的item
     * */
    private String[] items = new String[] { "选择本地图片", "拍照" };
    private void showInsertPicDialog() {

        new AlertDialog.Builder(this)
                .setTitle("插入图片")
                .setItems(items, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent,100);

                                break;
                            case 1:

                                Intent intent2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent2,200);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageAddTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd_HH-mm");
        preimageName.add(sdf.format(imageAddTime));// 输出已经格式化的现在时间（24小时制）
        Bitmap bitmap = null;
        int index =etPostContent.getSelectionEnd();//获取光标所在位置
        indexes.add(index);
        SpannableString spanStr = new SpannableString(" ");

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {//系统相册
            Uri imageData = data.getData();
            String path = getPath(imageData);
            bitmap = BitmapFactory.decodeFile(path);
            saveImage(bitmap,phoneNum+"_"+sdf.format(imageAddTime)+"_"+index);
        } else if (requestCode == 200 && resultCode == RESULT_OK && data != null) {//系统相机
            bitmap = (Bitmap) data.getExtras().get("data");
            saveImage(bitmap,phoneNum+"_"+sdf.format(imageAddTime)+"_"+index);
        }

        final Bitmap bitmap1 =bitmap;
        DynamicDrawableSpan drawableSpan = new DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BOTTOM) {

            @Override
            public Drawable getDrawable() {
                Drawable d = new BitmapDrawable(bitmap1);
                d.setBounds(0, 0, bitmap1.getWidth()/5,bitmap1.getHeight()/5);
                return d;
            }
        };

        spanStr.setSpan(drawableSpan, 0 , 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        Log.e("SPANSTR","SET");
        Editable et = ((EditText) getCurrentFocus()).getText();// 获取Edittext中的内容
        et.insert(index, spanStr);// 设置spanStr要添加的位置
        ((EditText) getCurrentFocus()).setText(et);// 把et添加到Edittext中
        ((EditText) getCurrentFocus()).setSelection(index + spanStr.length());// 设置Edittext中光标在最后面显示
    }
    /**查询路径*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        //高于4.4.2的版本
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = this.managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    /**uri路径查询字段*/
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    //保存图片
    private void saveImage(Bitmap bitmap, String name ) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            Log.i("SAVE_IMAGE","SAVING_SDCARD");
            file = this.getExternalFilesDir("");
        }else{//手机内部存储
            //路径：data/data/包名/files
            Log.i("SAVE_IMAGE","SAVING_PHONE");
            file = this.getFilesDir();
        }
        FileOutputStream fos = null;
        try {
            File files = new File(file,name +".png");
            fos = new FileOutputStream(files);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
            Log.i("SAVE_IMAGE","SUCCESS");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.all_category, android.R.layout.simple_spinner_item);

        // 每行一个
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        //给spinner添加adapter
        chooseCategory.setAdapter(genderSpinnerAdapter);

        chooseCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelected = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
