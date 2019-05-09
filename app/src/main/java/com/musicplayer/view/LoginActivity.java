package com.musicplayer.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.musicplayer.R;

import okhttp3.OkHttpClient;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText userName;
    private EditText password;
    private Button loginBtn;
    private TextView registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        initView();
    }

    /**
     * sqlite可视化工具初始化
     */
    private void init() {
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    /**
     * 初始化登录界面
     */
    private void initView() {
        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.step_into_register);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameStr = userName.getText().toString();
                String passwordStr = password.getText().toString();
                login(userNameStr, passwordStr);
            }
        });
    }

    /**
     * 登录验证
     */
    private void login(String userName, String password) {
        SharedPreferences accountSp = getSharedPreferences("account", 0);
        String spUser = accountSp.getString("user", null);
        String spPassword = accountSp.getString("password", null);

        Log.d(TAG, "username: " + userName + "; password: " + password);
        Log.d(TAG, "spUser: " + spUser + "; spPassword: " + spPassword);
        if (userName.equals(spUser) && password.equals(spPassword)) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
            stepIntoHomeView();
        } else {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 进入到主界面
     */
    private void stepIntoHomeView() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
