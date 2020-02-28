package com.ys100.cloudspacepreview;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ys100.yscloudpreview.PreViewFileActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View mView){
        PreViewFileActivity.startPreViewFileActivity(this,"","");
    }
}
