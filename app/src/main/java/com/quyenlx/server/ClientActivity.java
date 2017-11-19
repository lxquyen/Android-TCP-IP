package com.quyenlx.server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener, ClientAna.MessageCallback {
    private EditText editIp, editPort;
    private Button btnConnect;
    private ClientAna ana;

    private EditText editMessage;
    private Button btnSend;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        editIp = findViewById(R.id.edit_ip);
        editPort = findViewById(R.id.edit_port);
        btnConnect = findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(this);

        ListView listView = findViewById(R.id.list_view);
        editMessage = findViewById(R.id.edit_message);
        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view == btnConnect) {
            initClient();
        } else if (view == btnSend) {
            sendMessage();
        }

    }

    private void sendMessage() {
        String message = editMessage.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            ana.send(message);
            editMessage.setText("");
            arrayAdapter.add("Client : " + message);
        }


    }

    private void initClient() {
        String ip = editIp.getText().toString();
        Integer port = Integer.parseInt(editPort.getText().toString());
        ana = new ClientAna(ip, port, this);
        ana.execute();
    }

    @Override
    public void connected() {
        runOnUiThread(() -> {
            btnConnect.setText("Connected");
        });
    }

    @Override
    public void receiver(String message) {
        arrayAdapter.add("Server : " + message);
    }
}
