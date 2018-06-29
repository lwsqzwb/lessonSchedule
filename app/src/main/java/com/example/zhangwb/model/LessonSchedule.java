package com.example.zhangwb.model;


import com.example.zhangwb.util.Translate;

public class LessonSchedule {
    private int dayOfWeek;
    private int from;
    private int to;
    private String className;
    private String classTeacher;
    private String classLocation;
    private int color;
    private int beginWeek;
    private int endWeek;
    private int isDouble;   //-1为单双周都上，0为单周，1为双周

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(String classTeacher) {
        this.classTeacher = classTeacher;
    }

    public String getClassLocation() {
        return classLocation;
    }

    public void setClassLocation(String classLocation) {
        this.classLocation = classLocation;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBeginWeek() {
        return beginWeek;
    }

    public void setBeginWeek(int beginWeek) {
        this.beginWeek = beginWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getIsDouble() {
        return isDouble;
    }

    public void setIsDouble(int isDouble) {
        this.isDouble = isDouble;
    }

    @Override
    public String toString() {
        String str = "";
        if(isDouble == 0){
            str = "(单周)";
        }else if(isDouble == 1){
            str = "(双周)";
        }
        return className + '\n'
                + Translate.translateDateToChinese(dayOfWeek)+'\n'
                +'第'+beginWeek+'-'+endWeek+"周" +str+'\n'
                + '第'+from+'-'+to+"节"+'\n'
                +classLocation+'\n'
                +classTeacher;
    }

    public boolean isThisWeek(int week){
        int a = week%2;
        if(week>=beginWeek&&week<=endWeek&&isDouble!=a){
            return true;
        }
        return false;
    }

}
