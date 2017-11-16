package com.quyenlx.server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener, ClientAna.MessageCallback {
    private EditText editIp, editPort;
    private TextView tvMessage;
    private Button button;
    private boolean isConnected;
    private ClientAna ana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        editIp = findViewById(R.id.edit_ip);
        editPort = findViewById(R.id.edit_port);
        tvMessage = findViewById(R.id.tv_message);
        button = findViewById(R.id.btn_connect);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (isConnected) {
            sendMessage();
        } else {
            initClient();
        }

    }

    private void sendMessage() {
        String message = editIp.getText().toString();
        ana.send(message);
    }

    private void initClient() {
        String ip = editIp.getText().toString();
        Integer port = Integer.parseInt(editPort.getText().toString());
        ana = new ClientAna(ip, port, this);
        ana.execute();
    }

    @Override
    public void connected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isConnected = true;
                button.setText("Send");
            }
        });
    }

    @Override
    public void receiver(String message) {
        tvMessage.append(message);
    }
}
