package com.example.zhangwb.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.zhangwb.R;
import com.example.zhangwb.model.LessonSchedule;
import com.example.zhangwb.util.Translate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class LessonScheduleView extends View
{
    private static final int NUM_SIZE = 90;
    private int textSize;
    private Paint titlePaint;
    private int winWidth;
    private int winHeight;
    private Paint imagePaint, textPaint;
    private int widthSize;
    private int heightSize;
    private Paint linePaint;
    private List<Date> dates;
    private Date date;
    private List<LessonSchedule> times;
    private List<ClassLocation> locationList;

    public LessonScheduleView(Context context)
    {
        super(context);
    }

    public LessonScheduleView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        imagePaint = new Paint();

        //画矩形标题的笔
        titlePaint = new Paint();
        titlePaint.setColor(Color.argb(127, 164, 159, 155));

        //画分割线的笔
        linePaint = new Paint();
        linePaint.setColor(Color.argb(127, 95, 87, 84));

        //得到当前一周的日期
        date = new Date();
        dates = dateToWeek(date);

    }

    public LessonScheduleView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        winWidth = measureWidth(widthMeasureSpec);
        winHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(winWidth, winHeight);

        widthSize = (winWidth - NUM_SIZE) / 7;
        heightSize = winHeight / 12;

        Log.i("main", "winWidth:"+winWidth+", winHeight:"+winHeight);

        //字体大小
        textSize = winWidth/36;

        //用于画文字的笔
        textPaint = new Paint();
        textPaint.setColor(Color.argb(255, 255, 255, 255));
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    private int measureWidth(int measureSpc)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpc);
        int specSize = MeasureSpec.getSize(measureSpc);

        //如果给的是精确值（具体数值或者match_parent）
        if (specMode == MeasureSpec.EXACTLY)
        {
            result = specSize;
        }
        else
        {
            //不是精确值的话就先给一个初始值
            result = 300;
            //如果是wrap_content的话，就让他为不超过最大值的一个默认值
            if (specMode == MeasureSpec.AT_MOST)
            {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpc)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpc);
        int specSize = MeasureSpec.getSize(measureSpc);

        //如果给的是精确值（具体数值或者match_parent）
        if (specMode == MeasureSpec.EXACTLY)
        {
            result = specSize;
        }
        else
        {
            //不是精确值的话就先给一个初始值
            result = 400;
            //如果是wrap_content的话，就让他为不超过最大值的一个默认值
            if (specMode == MeasureSpec.AT_MOST)
            {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if(locationList == null) return super.onTouchEvent(event);
        for(int i = 0;i<locationList.size();i++){
            if(locationList.get(i).isIn(x,y)){
//                Toast.makeText(getContext(),times.get(i).getClassName(),Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                Window win = alertDialog.getWindow();
//                WindowManager.LayoutParams lp = win.getAttributes();
//                lp.width = lp.width/2;
//                win.setAttributes(lp);

                // 设置弹出对话框的布局
                win.setContentView(R.layout.course_alert_dialog);
                TextView alertTextView = (TextView)win.findViewById(R.id.alertTextView);
                alertTextView.setGravity(Gravity.CENTER);
                alertTextView.setText(times.get(i).toString());
                alertTextView.setBackgroundColor(times.get(i).getColor());
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Paint paint = new Paint();
        //背景图画
        canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.pic9), winWidth, winHeight, true), 0, 0, new Paint());
        //绘制月份
        canvas.drawText(date.getMonth()+1 + "月", NUM_SIZE / 2, heightSize - 60, textPaint);

        //初始化日期
        for (int i = 0; i < 7; i++)
        {
            //画首行格子
            RectF rectF = new RectF((NUM_SIZE + widthSize * i),
                    0,
                    (NUM_SIZE + widthSize * (i + 1)),
                    heightSize);
            canvas.drawRect(rectF, titlePaint);
            //第一行的分割线
            canvas.drawLine((NUM_SIZE + widthSize * i),
                    0,
                    (NUM_SIZE + widthSize * i),
                    heightSize,
                    linePaint);
            //第一行的日期
            canvas.drawText(dates.get(i).getDate() + "",
                    (NUM_SIZE + widthSize / 2) + widthSize * i,
                    heightSize / 3,
                    textPaint);
            canvas.drawText(Translate.translateDateToChinese(i + 1),
                    (NUM_SIZE + widthSize / 2) + widthSize * i,
                    (heightSize / 3) * 2,
                    textPaint);
        }
        //初始化序号
        for (int i = 0; i < 12; i++)
        {
            //画首列格子
            RectF rectF = new RectF(0, heightSize * i, NUM_SIZE, heightSize * (i + 1));
            canvas.drawRect(rectF, titlePaint);
            //第一列的分割线
            canvas.drawLine(0, heightSize * i, NUM_SIZE, heightSize * i, linePaint);
            if (i > 0)
            {
                //第一列的序号
                canvas.drawText(i + "", NUM_SIZE / 2, (heightSize * i) + heightSize / 2, textPaint);
            }
        }
        //初始化具体数据
        if(times != null)
        {
            for (int i = 0; i < times.size(); i++)
            {
                LessonSchedule bean = times.get(i);

                int num = bean.getTo()-bean.getFrom();
                //画圆角矩形
                imagePaint.setColor(bean.getColor());
                RectF rectF = new RectF((bean.getDayOfWeek()-1)*widthSize+NUM_SIZE, bean.getFrom()*heightSize,
                        bean.getDayOfWeek()*widthSize+NUM_SIZE, (bean.getTo()+1)*heightSize);
                locationList.add(new ClassLocation((bean.getDayOfWeek()-1)*widthSize+NUM_SIZE,
                        bean.getDayOfWeek()*widthSize+NUM_SIZE,
                        bean.getFrom()*heightSize,
                        (bean.getTo()+1)*heightSize));
                canvas.drawRoundRect(rectF, 20, 20, imagePaint);

                textPaint.setTextAlign(Paint.Align.CENTER);

//                Paint.FontMetrics fontMetrics = paint.getFontMetrics();
//// 计算文字高度
//                float fontHeight = fontMetrics.bottom - fontMetrics.top;
                float nameH = paint.measureText(bean.getClassName())/(widthSize - 30);
                float locationH = paint.measureText(bean.getClassLocation())/(widthSize - 30);
                drawText(canvas, bean.getClassName(),bean.getClassLocation(),
                        (bean.getDayOfWeek()-1)*widthSize+widthSize/2+NUM_SIZE,
                        bean.getFrom()*heightSize+(num+1)*heightSize/2 - (nameH+locationH)*30,
                        widthSize, textPaint);
            }
        }
    }

    public void setData(List<LessonSchedule> times,Date date)
    {
        this.times = times;
        this.locationList = new ArrayList<>();
        this.dates = dateToWeek(date);
        this.date = date;
        System.out.println(date);
        invalidate();
    }

    private List<Date> dateToWeek(Date mDate)
    {
        int b = mDate.getDay();
        Date fDate;
        List<Date> list = new ArrayList();
        Long fTime = mDate.getTime() - b * 24 * 3600000;
        for (int a = 0; a < 8; a++)
        {
            fDate = new Date();
            fDate.setTime(fTime + (a * 24 * 3600000));

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(fDate);
            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
            fDate = calendar.getTime(); // 这个时间就是日期往后推一天的结果

            list.add(a, fDate);
        }

        return list;
    }

    private void drawText(Canvas canvas, String s, String s2,float x, float y, float width, Paint paint)
    {
        StringBuffer buffer = new StringBuffer();
        char c;
        for (int i = 0; i < s.length(); i++)
        {
            //一行写不下了，就输出
            if(paint.measureText(buffer.toString())+30 > width)
            {
                canvas.drawText(buffer.toString(), x, y, paint);
                y+=30;
                buffer.setLength(0);
            }
            //一行能写下，就再加字符
            c = s.charAt(i);
            buffer.append(c);
        }
        if(buffer.length()>0) {
            canvas.drawText(buffer.toString(), x, y, paint);
            y+=30;
            buffer.setLength(0);
        }
        y += 10;
        for (int i = 0; i < s2.length(); i++)
        {
            //一行写不下了，就输出
            if(paint.measureText(buffer.toString())+30 > width)
            {
                canvas.drawText(buffer.toString(), x, y, paint);
                y+=30;
                buffer.setLength(0);
            }
            //一行能写下，就再加字符
            c = s2.charAt(i);
            buffer.append(c);
        }
        canvas.drawText(buffer.toString(), x, y, paint);
    }
    class ClassLocation{
        float x1;
        float x2;
        float y1;
        float y2;

        public ClassLocation(float x1,float x2,float y1,float y2){
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        }

        public float getX1() {
            return x1;
        }

        public void setX1(float x1) {
            this.x1 = x1;
        }

        public float getX2() {
            return x2;
        }

        public void setX2(float x2) {
            this.x2 = x2;
        }

        public float getY1() {
            return y1;
        }

        public void setY1(float y1) {
            this.y1 = y1;
        }

        public float getY2() {
            return y2;
        }

        public void setY2(float y2) {
            this.y2 = y2;
        }

        public  boolean isIn(float x,float y){
            if(x>x1&&x<x2&&y>y1&&y<y2){
                return true;
            }
            return false;
        }
    }

}