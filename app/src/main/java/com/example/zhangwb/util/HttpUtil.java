package com.example.zhangwb.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    private static OkHttpClient client = new OkHttpClient.Builder()
            .dns(new Dns() {
                @Override
                public List<InetAddress> lookup(String hostname) throws UnknownHostException {
                    return Arrays.asList(InetAddress.getAllByName("10.60.65.8"));
                }
            })
            .cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                {
                    List<Cookie> cookieList = new ArrayList<>();
                    Cookie cookie2 = new Cookie.Builder().domain("uims.jlu.edu.cn").name("loginPage").value("userLogin.jsp").build();
                    cookieList.add(cookie2);
                    cookieStore.put("uims.jlu.edu.cn",cookieList);
                }
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    if(cookies.size()>0) {
                        cookieStore.get("uims.jlu.edu.cn").addAll(cookies);
                    }
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })
            .build();
    public static void setCookies(String username){
        HttpUrl url = HttpUrl.parse("uims.jlu.edu.cn");
        List<Cookie> cookieList = new ArrayList<>();
        Cookie cookie1 = new Cookie.Builder().domain("uims.jlu.edu.cn").name("alu").value(username).build();
        Cookie cookie3 = new Cookie.Builder().domain("uims.jlu.edu.cn").name("pwdStrength").value("1").build();
        cookieList.add(cookie1);
        cookieList.add(cookie3);
        client.cookieJar().saveFromResponse(url,cookieList);

    }

    public static void sendGetRequest(String address, Callback callback){
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendPostRequest(String address, RequestBody body, Callback callback){
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }


}
