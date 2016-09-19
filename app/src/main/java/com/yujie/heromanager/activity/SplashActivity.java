package com.yujie.heromanager.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.yujie.heromanager.HeroApplication;
import com.yujie.heromanager.R;
import com.yujie.heromanager.bean.SuperUser;

public class SplashActivity extends AppCompatActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();
    /** Go LoginActivity*/
    private static final int GO_LOGIN = 1001;
    /** Go MainActivity*/
    private static final int GO_MAIN = 1002;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GO_LOGIN:
                    goLogin();
                    break;
                case GO_MAIN:
                    goMain();
                    break;
            }
        }
    };

    /**
     * go MainActivity
     */
    private void goMain() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    /**
     * go LoginActivity
     */
    private void goLogin() {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        /** find the user which is signed in,if true,go main,else go login*/
        SharedPreferences user = getSharedPreferences("login_super_user", MODE_PRIVATE);
        String login_super_user = user.getString("login_super_user", null);
        if (login_super_user!=null){
            mHandler.sendEmptyMessageDelayed(GO_MAIN,3000);
            HeroApplication.getInstance().setCurrentSuperUser(login_super_user);
        }else {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN,3000);
        }
    }
}
