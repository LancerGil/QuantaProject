package com.example.administrator.quantaproject.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

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
    private int hasWriteCalendarPermission;

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
                                    setResult(PingTai_Config.ACTIVITY_RESULT_FINISH);
                                    startActivity(turnToMovementAty);
                                    finish();
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
                BitmapUtils.showDialogToChoosePic(SignUpActivity.this,null,null);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission Granted
                    switch (permissions[0]){
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent,100);
                            break;
                        case Manifest.permission.CAMERA:
                            Intent intent2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent2,200);
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(SignUpActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**选择图片后显示出来*/
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"onActivityResult:running");
        final ProgressDialog connecting = ProgressDialog.show(SignUpActivity.this,getResources().getString(R.string.setHead),getResources().getString(R.string.setting));
        connecting.show();
        BitmapUtils.getImageBitmapFromResult(this,requestCode,resultCode,data,ivSelectHeadPortrait,etPhoneNum.getText().toString());
    }

}