package com.erikiado.xchange.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.erikiado.xchange.R;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Animation anim = AnimationUtils.loadAnimation(this,R.anim.zoom_in);

        ImageView logo = (ImageView) findViewById(R.id.splash_logo);
        logo.setAnimation(anim);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ActivitySplash.this, ActivityLogin.class));
                finish();
            }
        },3000);
    }
}
