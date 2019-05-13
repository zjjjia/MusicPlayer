package com.dxxyzhourong.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dxxyzhourong.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    /**
     * 初始化注册界面
     */
    private void initView() {
        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        registerBtn = findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameStr = userName.getText().toString();
                String passwordStr = password.getText().toString();

                saveAccount(userNameStr, passwordStr);
                userName.getText().clear();
                password.getText().clear();
            }
        });
    }

    /**
     * 将账号信息存储在SP中
     */
    private void saveAccount(String userName, String password) {
        SharedPreferences accountSp = getSharedPreferences("account", 0);
        SharedPreferences.Editor editor = accountSp.edit();
        editor.putString("user", userName);
        editor.putString("password", password);
        editor.commit();
        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();

        backToLogin();
    }

    /**
     * 注册成功,回到登录界面
     */
    private void backToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
