package com.example.zhangwb.util;

import com.example.zhangwb.model.User;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class BodyUtil {
    public static RequestBody getLoginBody(User user){
        RequestBody body = new FormBody.Builder()
                .add("j_username",user.getUserName())
                .add("j_password",user.getPassword())
                .add("mousePath","nJQABmHQCZnJwCqpLgC8qMwDOrNgDfrOADwrOwECrPgEUrQgE" +
                        "lrRwE3rSgFIrTAFaqTgFrpUQF9nVgGOnWQGgmXQGxlYQHDlZQHUlaQHmkbQH3kdQIJiggIbgj" +
                        "QIsjkwI/imAJPslgJislwJnrnAJydhgMKf/wPiP0gEn")
                .build();
        return body;
    }

}
