package com.example.zhangwb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangwb.model.User;
import com.example.zhangwb.util.BodyUtil;
import com.example.zhangwb.util.HttpUtil;
import com.example.zhangwb.util.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox remember_password;
    private EditText username_text;
    private EditText password_text;
    private TextView show_text;
    private User mUser;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1: {
                    HttpUtil.setCookies(mUser.getUserName());
                    HttpUtil.sendPostRequest("http://uims.jlu.edu.cn/ntms/j_spring_security_check"
                            , BodyUtil.getLoginBody(mUser)
                            , new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this,"登录失败",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Message message = new Message();
                                    message.what = 2;
                                    handler.sendMessage(message);
                                }
                            });
                    break;
                }
                case 2: {
                    HttpUtil.sendPostRequest("http://uims.jlu.edu.cn/ntms/action/getCurrentUserInfo.do"
                            , new FormBody.Builder().build()
                            , new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this,"登录失败",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().string());
                                        mUser.setNickName(jsonObject.getString("nickName"));
                                        mUser.setUserId(jsonObject.getString("userId"));
                                        JSONObject defRes = jsonObject.getJSONObject("defRes");
                                        mUser.setTeachingTerm(defRes.getString("teachingTerm"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Message message = new Message();
                                    message.what = 3;
                                    handler.sendMessage(message);
                                }
                            });
                    break;
                }
                case 3: {
                    if (mUser.getNickName() != null) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user",mUser);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"登录失败",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        initView();
    }

    private void initView() {
        mUser = new User();
        btn_login = findViewById(R.id.btn_login);
        username_text = findViewById(R.id.username_text);
        password_text = findViewById(R.id.password_text);
        show_text = findViewById(R.id.show_text);
        remember_password = findViewById(R.id.remember_password);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String userName = pref.getString("username","");
            String password = pref.getString("password","");
            username_text.setText(userName);
            password_text.setText(password);
            remember_password.setChecked(true);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = pref.edit();
                if(remember_password.isChecked()){
                    editor.putBoolean("remember_password",true);
                    editor.putString("username",username_text.getText().toString().trim());
                    editor.putString("password",password_text.getText().toString().trim());
                }else{
                    editor.clear();
                }
                editor.apply();
                HttpUtil.sendGetRequest("http://uims.jlu.edu.cn/ntms/", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"登录失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        mUser.setUserName(username_text.getText().toString().trim());
                        mUser.setPassword(MD5Util.encode("UIMS"+mUser.getUserName()+
                                password_text.getText().toString().trim()));
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                });
            }
        });
    }
}
