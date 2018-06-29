package com.example.zhangwb;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhangwb.fragments.FunctionsFragment;
import com.example.zhangwb.fragments.LessonFragment;
import com.example.zhangwb.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LessonFragment mLessonFragment;
    private FunctionsFragment mFunctionsFragment;
    private LoginFragment mLoginFragment;
    private View schedule;
    private View small_functions;
    private View login;
    private ImageView schedule_img;
    private ImageView small_functions_img;
    private ImageView login_img;
    private TextView schedule_text;
    private TextView small_functions_text;
    private TextView login_text;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
    }

    public void newLessonView(){
        if(mLessonFragment!=null) mLessonFragment.newView();
    }

    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index){
            case 0:{
                schedule_img.setImageResource(R.drawable.schedule_selected);
                schedule_text.setTextColor(Color.parseColor("#1296db"));
                if(mLessonFragment == null){
                    mLessonFragment = new LessonFragment();
                    transaction.add(R.id.content,mLessonFragment);
                }else{
                    transaction.show(mLessonFragment);
                }
                onAttachFragment(mLessonFragment);
                break;
            }
            case 1:{
                small_functions_img.setImageResource(R.drawable.small_functions_selected);
                small_functions_text.setTextColor(Color.parseColor("#1296db"));
                if(mFunctionsFragment == null){
                    mFunctionsFragment = new FunctionsFragment();
                    transaction.add(R.id.content,mFunctionsFragment);
                }else{
                    transaction.show(mFunctionsFragment);
                }
                onAttachFragment(mFunctionsFragment);
                break;
            }
            case 2:
            default:
                login_img.setImageResource(R.drawable.login_selected);
                login_text.setTextColor(Color.parseColor("#1296db"));
                if(mLoginFragment == null){
                    mLoginFragment = new LoginFragment();
                    transaction.add(R.id.content,mLoginFragment);
                }else{
                    transaction.show(mLoginFragment);
                }
                onAttachFragment(mLoginFragment);
                break;

        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction){
        if(mLessonFragment!=null){
            transaction.hide(mLessonFragment);
        }
        if(mFunctionsFragment!=null){
            transaction.hide(mFunctionsFragment);
        }
        if(mLoginFragment!=null){
            transaction.hide(mLoginFragment);
        }
    }

    private void clearSelection(){
        schedule_img.setImageResource(R.drawable.schedule_noselect);
        schedule_text.setTextColor(Color.parseColor("#82858b"));
        small_functions_img.setImageResource(R.drawable.small_functions_noselect);
        small_functions_text.setTextColor(Color.parseColor("#82858b"));
        login_img.setImageResource(R.drawable.login_noselect);
        login_text.setTextColor(Color.parseColor("#82858b"));
   }

    private void initViews() {
        schedule = findViewById(R.id.schedule);
        small_functions = findViewById(R.id.small_functions);
        login = findViewById(R.id.login);
        schedule_img = findViewById(R.id.schedule_image);
        small_functions_img = findViewById(R.id.small_functions_image);
        login_img = findViewById(R.id.login_image);
        schedule_text = findViewById(R.id.schedule_text);
        small_functions_text = findViewById(R.id.small_functions_text);
        login_text = findViewById(R.id.login_text);
        schedule.setOnClickListener(this);
        small_functions.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.schedule:{
                setTabSelection(0);
                break;
            }
            case R.id.small_functions:{
                setTabSelection(1);
                break;
            }
            case R.id.login:{
                setTabSelection(2);
                break;
            }
            default:break;
        }
    }
}
