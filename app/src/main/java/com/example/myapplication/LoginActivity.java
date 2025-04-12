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

        View keyboardView = findViewById(R.id.keyboard_view_container);

        mRSKeyboard = new RSKeyboard(keyboardView);
        EditText etPhone = findViewById(R.id.etPhone);
        mRSKeyboard.attachTo(etPhone);

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