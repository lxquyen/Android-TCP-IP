package com.quyenlx.server;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.quyenlx.core.util.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ServerBoxListener, View.OnClickListener {
    private ServerBox serverBox;
    private ListView listView;
    private EditText editText;
    private Button button;
    private ArrayAdapter<String> arrayAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.tv_ip);
        textView.setText(DeviceUtils.getIpAddress() + " : 54555");
        serverBox = new ServerBox(54555);
        serverBox.runServer(this);

        listView = findViewById(R.id.list_view);
        editText = findViewById(R.id.edit_message);
        button = findViewById(R.id.btn_send);
        button.setOnClickListener(this);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serverBox.destroy();
    }

    @Override
    public void onConnected(boolean isConnected) {

    }

    @Override
    public void onReceiver(String action) {
        arrayAdapter.add("Client : " + action);
    }

    @Override
    public void onClick(View view) {
        String message = editText.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            arrayAdapter.add("Server : " + message);
            serverBox.sendMessageToAll(message);
            editText.setText("");
        }
    }
}
