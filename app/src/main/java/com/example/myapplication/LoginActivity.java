package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private RSKeyboard mRSKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etPhone = findViewById(R.id.etPhone);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etName = findViewById(R.id.etName);

        View keyboardView = findViewById(R.id.keyboard_view_container);
        mRSKeyboard = new RSKeyboard(keyboardView);
        mRSKeyboard.attachTo(etPhone);
        mRSKeyboard.attachTo(etPassword);
        mRSKeyboard.attachTo(etEmail);
        mRSKeyboard.attachTo(etName);

        mRSKeyboard.setOnKeyboardDoneListener(new RSKeyboard.OnKeyboardDoneListener() {
            @Override
            public void onDone() {
                // 处理完成按钮点击事件
            }
        });

        mRSKeyboard.setOnKeyboardCancelListener(new RSKeyboard.OnKeyboardCancelListener() {
            @Override
            public void onCancel() {
                // 处理取消按钮点击事件
            }
        });
    }
}