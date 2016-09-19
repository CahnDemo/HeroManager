package com.yujie.heromanager.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yujie.heromanager.HeroApplication;
import com.yujie.heromanager.I;
import com.yujie.heromanager.R;
import com.yujie.heromanager.bean.SuperUser;
import com.yujie.heromanager.utils.OkHttpUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();
    private Context mContext;
    @Bind(R.id.login_activity_EditText_inputPhone)
    EditText loginActivityEditTextInputPhone;
    @Bind(R.id.login_activity_EditText_inputPwd)
    EditText loginActivityEditTextInputPwd;
    @Bind(R.id.login_activity_Button_login)
    Button loginActivityButtonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.login_activity_Button_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_activity_Button_login:
                login();
                break;
        }
    }

    /**
     * execute login
     */
    private void login() {
        final String uid = loginActivityEditTextInputPhone.getText().toString();
        String pwd = loginActivityEditTextInputPwd.getText().toString();
        if (uid.isEmpty()){
            loginActivityEditTextInputPhone.setError("未输入内容,请重试");
            loginActivityEditTextInputPhone.requestFocus();
            return;
        }
        if (pwd.isEmpty()){
            loginActivityEditTextInputPwd.setError("未输入内容,请重试");
            loginActivityEditTextInputPwd.requestFocus();
            return;
        }
        OkHttpUtils<SuperUser> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_SUPER_USER_LOGIN)
                .addParam(I.SuperUser.USER_NAME,uid)
                .addParam(I.SuperUser.USER_PWD,pwd)
                .post()
                .targetClass(SuperUser.class)
                .execute(new OkHttpUtils.OnCompleteListener<SuperUser>() {
                    @Override
                    public void onSuccess(SuperUser result) {
                        if (result.getUserName()==null){
                            Toast.makeText(LoginActivity.this,"登陆失败，请确认您的账号或密码",Toast.LENGTH_LONG).show();
                        }else {
                            SharedPreferences user = getSharedPreferences("login_super_user", MODE_PRIVATE);
                            user.edit().putString("login_super_user",uid).commit();
                            startActivity(new Intent(mContext,MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(LoginActivity.this,"网络不通畅,请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
