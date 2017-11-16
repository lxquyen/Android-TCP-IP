package com.quyenlx.server;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.quyenlx.core.util.DeviceUtils;

public class MainActivity extends AppCompatActivity {
    private ServerBox serverBox;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.tv_ip);
        textView.setText(DeviceUtils.getIpAddress() + " : 54555");
        serverBox = new ServerBox(54555);
        serverBox.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serverBox.stopBox();
    }
}
