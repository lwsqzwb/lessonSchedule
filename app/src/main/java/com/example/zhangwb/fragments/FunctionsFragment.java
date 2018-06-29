package com.example.zhangwb.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhangwb.MainActivity;
import com.example.zhangwb.R;

public class FunctionsFragment extends Fragment {

    private Button get;
    private TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View functionsLayout = inflater.inflate(R.layout.fragment_functions,
                container, false);
        get = functionsLayout.findViewById(R.id.get);
        text = functionsLayout.findViewById(R.id.text_6);
        initView();
        return functionsLayout;
    }

    private void initView() {
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
