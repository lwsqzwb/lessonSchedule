package com.example.zhangwb.util;

import android.graphics.Color;

import com.example.zhangwb.model.LessonSchedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LessonUtil {
    public static List<LessonSchedule> getFromJson(String json){
        List<LessonSchedule> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray value = jsonObject.getJSONArray("value");
            for(int i = 0;i<value.length();i++){
                JSONObject jsonObject1 = value.getJSONObject(i);
                JSONObject teachClassMaster = jsonObject1.getJSONObject("teachClassMaster");
                JSONArray lessonSchedules = teachClassMaster.getJSONArray("lessonSchedules");
                JSONArray lessonTeachers = teachClassMaster.getJSONArray("lessonTeachers");
                JSONObject lessonSegment = teachClassMaster.getJSONObject("lessonSegment");
                String jsonClassname = lessonSegment.getString("fullName");
                String jsonTeacher = "";
                for(int j = 0;j<lessonTeachers.length();j++){
                    if(j>1){ jsonTeacher+=",";}
                    jsonTeacher += lessonTeachers.getJSONObject(j).getJSONObject("teacher").getString("name");
                }
                for (int j = 0;j<lessonSchedules.length();j++){
                    LessonSchedule bean = new LessonSchedule();
                    bean.setClassTeacher(jsonTeacher);
                    bean.setClassName(jsonClassname);
                    JSONObject jsonObject2 = lessonSchedules.getJSONObject(j);
                    JSONObject classroom = jsonObject2.getJSONObject("classroom");
                    JSONObject timeBlock = jsonObject2.getJSONObject("timeBlock");
                    String name = timeBlock.getString("name");
                    if(name.contains("单周")){
                        bean.setIsDouble(0);
                    }else if(name.contains("双周")){
                        bean.setIsDouble(1);
                    }else{
                        bean.setIsDouble(-1);
                    }
                    bean.setClassLocation(classroom.getString("fullName"));
                    bean.setDayOfWeek(Integer.parseInt(timeBlock.getString("dayOfWeek")));
                    bean.setBeginWeek(Integer.parseInt(timeBlock.getString("beginWeek")));
                    bean.setEndWeek(Integer.parseInt(timeBlock.getString("endWeek")));
                    int classSet = Integer.parseInt(timeBlock.getString("classSet"));
                    for(int k = 11;k>=1;k--){
                        int set = 1<<k;
                        if(classSet>=set){
                            classSet -= set;
                            if(bean.getTo()==0){
                                bean.setTo(k);
                            }else{
                                bean.setFrom(k);
                            }
                        }
                    }
                    bean.setColor(Color.argb(127, 255, 0, 0));
                    list.add(bean);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String writeToFile(String weeks,String startDate,String coursejson){
        List<LessonSchedule> list = getFromJson(coursejson);
        JSONObject object = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            for (LessonSchedule schedule:list){
                JSONObject j = new JSONObject();
                j.put("dayOfWeek",schedule.getDayOfWeek());
                j.put("from",schedule.getFrom());
                j.put("to",schedule.getTo());
                j.put("className",schedule.getClassName());
                j.put("classTeacher",schedule.getClassTeacher());
                j.put("classLocation",schedule.getClassLocation());
                j.put("color",schedule.getColor());
                j.put("beginWeek",schedule.getBeginWeek());
                j.put("endWeek",schedule.getEndWeek());
                j.put("isDouble",schedule.getIsDouble());
                array.put(j);
            }
            object.put("weeks",weeks);
            object.put("startDate",startDate);
            object.put("courses",array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

}
