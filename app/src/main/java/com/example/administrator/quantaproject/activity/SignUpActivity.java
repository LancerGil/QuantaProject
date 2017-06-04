package com.example.administrator.quantaproject.activity;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.GetCode;
import com.example.administrator.quantaproject.net.SignUp;
import com.example.administrator.quantaproject.net.UploadUtil;
import com.example.administrator.quantaproject.tools.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    private EditText etPhoneNum = null;
    private EditText etVerificationCode = null;
    private EditText etRealName = null;
    private EditText etStudNum = null;
    private EditText etPassword = null;
    private Button btnSignUp = null;
    private Button btnCancelSignUp = null;
    private Spinner spSelectCity = null;
    private TextView tvGetVerificationCode = null;
    private ImageView ivSelectHeadPortrait = null;
    private ImageButton ibtnBack = null;

    private String citySelected = null;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etPhoneNum = (EditText) findViewById(R.id.etInputPhoneNum);
        etVerificationCode = (EditText) findViewById(R.id.etInputVerificationCode);
        etRealName = (EditText) findViewById(R.id.etInputRealName);
        etStudNum = (EditText) findViewById(R.id.etInputStudNum);
        etPassword = (EditText) findViewById(R.id.etInputPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnCancelSignUp = (Button) findViewById(R.id.btnCancelSignUp);
        spSelectCity = (Spinner) findViewById(R.id.spSelectCity);
        tvGetVerificationCode = (TextView) findViewById(R.id.tvGetVerificationCode);
        ivSelectHeadPortrait = (ImageView) findViewById(R.id.ivSelectHeadPortrait);
        ibtnBack = (ImageButton) findViewById(R.id.back) ;

        setupSpinner();

        tvGetVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etPhoneNum.getText())){
                    Toast.makeText(SignUpActivity.this, R.string.phoneNum_can_not_be_null,Toast.LENGTH_SHORT).show();
                }

                new GetCode(etPhoneNum.getText().toString(), new GetCode.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SignUpActivity.this,R.string.code_has_been_sent,Toast.LENGTH_SHORT).show();
                    }
                }, new GetCode.FailCallback() {
                    @Override
                    public void onFail() {
                        Toast.makeText(SignUpActivity.this,R.string.code_sent_fail,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(infoIsNotEmpty()) {
                    new SignUp(etVerificationCode.getText().toString(), etPhoneNum.getText().toString(),citySelected,etPassword.getText().toString(),
                            etStudNum.getText().toString(),etRealName.getText().toString(),
                            new SignUp.SuccessCallback() {
                                @Override
                                public void onSuccess(String token) {
                                    PingTai_Config.cachToken(SignUpActivity.this,token);
                                    PingTai_Config.cachPhoneNum(SignUpActivity.this,etPhoneNum.getText().toString());

                                    Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                                    File finalHeadpre = new File(file,"head.png");
                                    finalHeadpre.renameTo(new File(file,etPhoneNum.getText().toString() + ".png"));
                                    final File fileHeadFinal = new File(file,etPhoneNum.getText().toString() + ".png");
                                    Log.e("FILEnameCHange",fileHeadFinal.getName());
                                    final UploadUtil uploadUtil = new UploadUtil();
                                    new Thread(new Runnable(){
                                        @Override
                                        public void run() {
                                            uploadUtil.uploadFile(fileHeadFinal, PingTai_Config.SERVER_URL +"/addFace",etPhoneNum.getText().toString());
                                        }
                                    }).start();

                                    Intent turnToMovementAty = new Intent(SignUpActivity.this, MovementActivity.class);
                                    startActivity(turnToMovementAty);
                                }
                            }, new SignUp.FailCallback() {
                        @Override
                        public void onFail() {
                            Toast.makeText(SignUpActivity.this, "注册失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        ivSelectHeadPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        btnCancelSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean infoIsNotEmpty(){

        if(citySelected.equals("选择城市")){
            Toast.makeText(SignUpActivity.this, R.string.city_can_not_be_null,Toast.LENGTH_SHORT).show();
            return false;
        }else
        if(TextUtils.isEmpty(etPhoneNum.getText())){
            Toast.makeText(SignUpActivity.this, R.string.phoneNum_can_not_be_null,Toast.LENGTH_SHORT).show();
            return false;
        }else
        if(TextUtils.isEmpty(etVerificationCode.getText())){
            Toast.makeText(SignUpActivity.this, R.string.VerificationCode_can_not_be_null,Toast.LENGTH_SHORT).show();
            return false;
        }else
        if(TextUtils.isEmpty(etRealName.getText())){
            Toast.makeText(SignUpActivity.this, R.string.RealName_can_not_be_null,Toast.LENGTH_SHORT).show();
            return false;
        }else
        if(TextUtils.isEmpty(etStudNum.getText())){
            Toast.makeText(SignUpActivity.this, R.string.StudNum_can_not_be_null,Toast.LENGTH_SHORT).show();
            return false;
        }else
        if(TextUtils.isEmpty(etPassword.getText())){
            Toast.makeText(SignUpActivity.this, R.string.Password_can_not_be_null,Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    /**
     * 设置"选择城市"spinner
     * */
    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities, android.R.layout.simple_spinner_item);

        // 每行一个
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        //给spinner添加adapter
        spSelectCity.setAdapter(genderSpinnerAdapter);

        spSelectCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citySelected = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Dialog中的item
     * */
    private String[] items = new String[] { "选择本地图片", "拍照" };
    /**设置选择头像来源Dialog*/
    private void showDialog() {

        new AlertDialog.Builder(this)
                .setTitle("设置头像")
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
    /**选择头像后显示头像*/
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {//系统相册
            Uri imageData = data.getData();
            String path = getPath(imageData);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Bitmap bitmap1 = BitmapUtils.zoom(bitmap, ivSelectHeadPortrait.getWidth(), ivSelectHeadPortrait.getHeight());
            Bitmap bitmap2 = BitmapUtils.circleBitmap(bitmap1);
            //加载显示
            ivSelectHeadPortrait.setImageBitmap(bitmap2);
            saveImage(bitmap);
        } else if (requestCode == 200 && resultCode == RESULT_OK && data != null) {//系统相机
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            BitmapUtils.zoom(bitmap, ivSelectHeadPortrait.getWidth(), ivSelectHeadPortrait.getHeight());
            bitmap = BitmapUtils.circleBitmap(bitmap);

            //加载显示
            ivSelectHeadPortrait.setImageBitmap(bitmap);
            saveImage(bitmap);
        }
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
    private void saveImage(Bitmap bitmap) {
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
            final File files = new File(file,"head.png");
            fos = new FileOutputStream(files);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
            Log.i("SAVE_IMAGE","SUCCESS:"+etPhoneNum.getText().toString()+".png");
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


    /**如果本地有,就不需要再去联网去请求*/
    private boolean readImage() {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){/**判断sd卡是否挂载*/
            /**路径1：storage/sdcard/Android/data/包名/files*/
            filesDir = getExternalFilesDir("");

        }else{/**手机内部存储   路径：data/data/包名/files*/
            filesDir = getFilesDir();

        }
        File file = new File(filesDir,"icon.png");
        if(file.exists()){
            /**存储--->内存*/
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ivSelectHeadPortrait.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }

//    /**判断本地是否有该图片,没有则去联网请求*/
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(readImage()){
//            return;
//        }
//    }
}