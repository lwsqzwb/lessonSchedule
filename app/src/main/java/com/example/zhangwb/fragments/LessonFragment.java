package com.example.zhangwb.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.zhangwb.R;
import com.example.zhangwb.model.LessonSchedule;
import com.example.zhangwb.view.LessonScheduleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LessonFragment extends Fragment {

    private LessonScheduleView mScheduleView;
    private Spinner mSpinner;
    private List<LessonSchedule> lessons;
    private int weeks;
    private Date startDate;
    private int thisWeek;
    private List<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View lessonLayout = inflater.inflate(R.layout.fragment_lesson,
                container, false);
        mScheduleView = lessonLayout.findViewById(R.id.lessonSchedule);
        mSpinner = lessonLayout.findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setData(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        newView();
        return lessonLayout;
    }

    public void newView(){
        initData();
        if(lessons!=null){
            long x = (new Date().getTime()) - (startDate.getTime());
            long c = 7*24*60*60*1000L;
            thisWeek = (int) (x/c) + 1;
            list = new ArrayList<>();
            for(int i = 0;i<weeks;i++){
                int w = i+1;
                String msg = "";
                if(w == thisWeek){
                    msg = "(本周)";
                }else{
                    msg = "(非本周)";
                }
                list.add("第"+w+"周"+msg);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.support_simple_spinner_dropdown_item,list);
            mSpinner.setAdapter(adapter);
            mSpinner.setSelection(thisWeek-1,true);
            setData(thisWeek);
        }
    }

    private void setData(int week) {
        List<LessonSchedule> ls = new ArrayList<>();
        for(LessonSchedule schedule:lessons){
            if(schedule.isThisWeek(week)){
                ls.add(schedule);
            }
        }
        Date date = new Date();
        long s = (thisWeek-week)*7*24*60*60*1000L;
        long t = date.getTime() - s;
        mScheduleView.setData(ls,new Date(t));
    }

    private void initData() {
        FileInputStream fis = null;
        BufferedReader reader = null;
        String json = null;
        File dir = getActivity().getFilesDir();
        File file = new File(dir,"courses.json");
        if(file.exists()){
            lessons = new ArrayList<>();
            try {
                fis = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(fis));
                StringBuilder builder = new StringBuilder();
                String s;
                while ((s=reader.readLine())!=null){
                    builder.append(s);
                }
                json = builder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(fis!=null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(json!=null){
                try {
                    JSONObject object = new JSONObject(json);
                    weeks = object.getInt("weeks");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    startDate = sdf.parse(object.getString("startDate"));
                    JSONArray array = object.getJSONArray("courses");
                    for(int i = 0;i<array.length();i++){
                        JSONObject course = array.getJSONObject(i);
                        LessonSchedule schedule = new LessonSchedule();
                        schedule.setDayOfWeek(course.getInt("dayOfWeek"));
                        schedule.setFrom(course.getInt("from"));
                        schedule.setTo(course.getInt("to"));
                        schedule.setClassName(course.getString("className"));
                        schedule.setClassTeacher(course.getString("classTeacher"));
                        schedule.setClassLocation(course.getString("classLocation"));
                        schedule.setColor(course.getInt("color"));
                        schedule.setBeginWeek(course.getInt("beginWeek"));
                        schedule.setEndWeek(course.getInt("endWeek"));
                        schedule.setIsDouble(course.getInt("isDouble"));
                        lessons.add(schedule);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{

        }
    }

}
