package com.example.wonsucklee.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wonsucklee on 2017. 11. 10..
 */

public class LoginVIew extends BaseActivity {

    public static final int REQUEST_CODE_TO_FRAG_MAIN = 1000;

    private AlertDialog dialog;
    Button loginButton;
    TextView registerButton;
    private String userID;
    private String userPassword;

    public static final int MULTIPLE_PERMISSIONS = 101;
    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        loginButton = (Button)findViewById(R.id.loginButton);
        registerButton = (TextView)findViewById(R.id.registerButton);

        final EditText idText=(EditText)findViewById(R.id.idText);
        final EditText passwordText=(EditText)findViewById(R.id.passwordText);

        checkPermissions();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String userID=idText.getText().toString();
                String userPassword=passwordText.getText().toString();

                if(userID.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(LoginVIew.this);
                    dialog=builder.setMessage("아이디를 적어주세요.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Log.i("LoginView", "on_Response : " + response);
                            if(response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                String user_id = jsonObject.getString("user_id");
                                String user_name = jsonObject.getString("name");
                                String user_major = jsonObject.getString("major");
                                String user_profile = jsonObject.getString("image_path");

                                Intent intent_login = new Intent(LoginVIew.this, Frag_MainActivity.class);
                                intent_login.putExtra("user_id", user_id);
                                intent_login.putExtra("user_name", user_name);
                                intent_login.putExtra("user_major", user_major);
                                intent_login.putExtra("user_profile", user_profile);
                                startActivityForResult(intent_login, REQUEST_CODE_TO_FRAG_MAIN);
                            } else {
                                AlertDialog.Builder builder=new AlertDialog.Builder(LoginVIew.this);
                                dialog=builder.setMessage("실패했습니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();

                                return;
                            }

                            /*
                            if('1' == response.charAt(0)){
                                startActivity(new Intent(LoginVIew.this, Frag_MainActivity.class));
                                finish();

                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder(LoginVIew.this);
                                dialog=builder.setMessage("실패했습니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();

                                return;

                            }
                            */
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest=new LoginRequest(userID, userPassword,  responseListener);
                RequestQueue queue= Volley.newRequestQueue(LoginVIew.this);
                queue.add(loginRequest);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginVIew.this, RegisterActivity.class));
            }
        });

    }


    private boolean checkPermissions() {
        int result;
        List<String> permissionsList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(pm);
            }
        }

        if (!permissionsList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[3])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }



}