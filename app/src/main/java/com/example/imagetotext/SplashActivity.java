package com.example.imagetotext;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private ImageView iv1;
//    private ImageView iv2;
//    private ImageView iv3;
//    private ImageView iv4;
//    private ImageView iv5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv1=(ImageView)findViewById(R.id.iv1);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        iv1.startAnimation(myanim);


        final Intent i=new Intent(this,MainActivity.class);
        Thread timer=new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();
    }
}
