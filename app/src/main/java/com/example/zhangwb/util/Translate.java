package com.example.zhangwb.util;


public class Translate
{
    // 只限于两位数
    public static String translateDateToChinese(int num)
    {
        String result = "";
        switch (num)
        {
            case 1:
                result = "周一";
                break;
            case 2:
                result = "周二";
                break;
            case 3:
                result = "周三";
                break;
            case 4:
                result = "周四";
                break;
            case 5:
                result = "周五";
                break;
            case 6:
                result = "周六";
                break;
            case 7:
                result = "周日";
                break;
            default:
                result = "错误";
                break;
        }
        return result;
    }

    public static int translateDateToNum(String Chinese)
    {
        int result;
        switch (Chinese)
        {
            case "周一":
                result = 1;
                break;
            case "周二":
                result = 2;
                break;
            case "周三":
                result = 3;
                break;
            case "周四":
                result = 4;
                break;
            case "周五":
                result = 5;
                break;
            case "周六":
                result = 6;
                break;
            case "周日":
                result = 7;
                break;
            default:
                result = 444;
                break;
        }
        return result;
    }
}
