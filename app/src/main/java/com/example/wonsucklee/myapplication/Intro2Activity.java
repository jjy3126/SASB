package com.example.wonsucklee.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by wonsucklee on 2017. 11. 13..
 */

public class Intro2Activity extends Activity {
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mHandler=new Handler();

        setContentView(R.layout.frame_layout2);

        new Handler().postDelayed(mRunnable,1500);




    }
    private Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(Intro2Activity.this, LoginVIew.class));
            finish();
        }
    };


}
