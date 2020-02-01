package com.example.cam1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edit1T;
    private EditText edit2T;
    private EditText edit3T;
    private EditText edit4T;
    private EditText edit5T;
    private String edit1 = "0.4";
    private String edit2 = "40";
    private String edit3 = "160";
    private String edit4 = "chenlinlin.wang";
    private String edit5 = "52018";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        edit1T = findViewById(R.id.edit1);
        edit1T.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edit1 = "0.4";
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edit1 = s.toString();
                Log.d("edit1",edit1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit2T = findViewById(R.id.edit2);
        edit2T.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edit2 = "40";
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                edit2 = s.toString();
                Log.d("edit2",edit2);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit3T = findViewById(R.id.edit3);
        edit3T.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edit3 = "160";
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edit3 = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit4T = findViewById(R.id.edit4);
        edit4T.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edit4 = "chenlinlin.wang";
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edit4 = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit5T = findViewById(R.id.edit5);
        edit5T.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edit5 = "52018";
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edit5 = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void startCam(View view){
        Intent intent = new Intent(MainActivity.this,realCam.class);
        intent.putExtra("edit1",edit1);
        intent.putExtra("edit2",edit2);
        intent.putExtra("edit3",edit3);
        intent.putExtra("edit4",edit4);
        intent.putExtra("edit5",edit5);
        startActivity(intent);


    }

}
