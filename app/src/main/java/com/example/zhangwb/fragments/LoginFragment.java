package com.example.zhangwb.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.example.zhangwb.LoginActivity;
import com.example.zhangwb.MainActivity;
import com.example.zhangwb.R;
import com.example.zhangwb.model.User;
import com.example.zhangwb.util.HttpUtil;
import com.example.zhangwb.util.LessonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends Fragment {

    private Button login;
    private Button logout;
    private Button update;
    private String courses;
    private User user;
    private String weeks;
    private String startDate;
    private String coursejson;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:{
                    try {
                        JSONObject object = new JSONObject(courses);
                        JSONArray array = object.getJSONArray("value");
                        for(int i = 0;i<array.length();i++){
                            String termId = array.getJSONObject(i).getString("termId");
                            if(termId.equals(user.getTeachingTerm())){
                                weeks = array.getJSONObject(i).getString("weeks");
                                startDate = array.getJSONObject(i).getString("startDate");
                                String s = "{\"tag\":\"teachClassStud@schedule\",\"branch\":\"default\"," +
                                        "\"params\":{\"termId\":"+user.getTeachingTerm()+",\"studId\":"+user.getUserId()+"}}";
                                RequestBody body = RequestBody.create(JSON, s);
                                HttpUtil.sendPostRequest("http://uims.jlu.edu.cn/ntms/service/res.do",
                                        body, new okhttp3.Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {

                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                coursejson = response.body().string();
                                                Message message = new Message();
                                                message.what = 2;
                                                handler.sendMessage(message);
                                            }
                                        }
                                );
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 2:{
                    FileOutputStream fos = null;
                    BufferedWriter writer = null;
                    try {
                        fos = getActivity().openFileOutput("courses.json", Context.MODE_PRIVATE);
                        writer = new BufferedWriter(new OutputStreamWriter(fos));
                        writer.write(LessonUtil.writeToFile(weeks,startDate,coursejson));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        if(writer!=null){
                            try {
                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if(fos!=null){
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    ((MainActivity)getActivity()).newLessonView();
                    Toast.makeText(getActivity(),"更新成功",Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3:{
                    Toast.makeText(getActivity(),"成功退出",Toast.LENGTH_SHORT).show();
                    user = null;
                    break;
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View loginLayout = inflater.inflate(R.layout.fragment_login,
                container, false);
        login = loginLayout.findViewById(R.id.fragment_login);
        update = loginLayout.findViewById(R.id.fragment_update);
        logout = loginLayout.findViewById(R.id.fragment_logout);
        initView();
        return loginLayout;
    }

    private void initView() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null){
                    Toast.makeText(getActivity(),user.getNickName()+"，你在线上",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null){
                    Toast.makeText(getActivity(),"少年，你还没登录",Toast.LENGTH_SHORT).show();
                }else{
                    String s = "{\"tag\":\"search@teachingTerm\",\"branch\":\"default\",\"params\":{}}";
                    RequestBody body = RequestBody.create(JSON, s);
                    HttpUtil.sendPostRequest("http://uims.jlu.edu.cn/ntms/service/res.do",
                            body, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    courses = response.body().string();
                                    Message message = new Message();
                                    message.what = 1;
                                    handler.sendMessage(message);
                                }
                            });
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null){
                    Toast.makeText(getActivity(),"少年，你还没登录",Toast.LENGTH_SHORT).show();
                }else{
                    HttpUtil.sendGetRequest("http://uims.jlu.edu.cn/ntms/logout.do", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Message message = new Message();
                            message.what = 3;
                            handler.sendMessage(message);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:{
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    user = (User) bundle.get("user");
                    if(user!=null){
                        Toast.makeText(getActivity(),user.getNickName(),Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }

    public User getUser() {
        return user;
    }
}
