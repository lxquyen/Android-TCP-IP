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
    private ArrayAdapter<String> arrayAdapter;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        editIp = findViewById(R.id.edit_ip);
        editPort = findViewById(R.id.edit_port);
        btnConnect = findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(this);

        ListView listView = findViewById(R.id.list_view);
        findViewById(R.id.btn_song).setOnClickListener(this);
        findViewById(R.id.btn_view).setOnClickListener(this);
        findViewById(R.id.btn_play_list).setOnClickListener(this);

        findViewById(R.id.btn_play_pause).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_repeat).setOnClickListener(this);
        findViewById(R.id.btn_up).setOnClickListener(this);
        findViewById(R.id.btn_down).setOnClickListener(this);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                initClient();
                break;
            case R.id.btn_song:
                requestSong();
                break;
            case R.id.btn_play_list:
                requestPlayList();
                break;
            case R.id.btn_view:
                requestView(!flag);
                break;
            case R.id.btn_play_pause:
                requestPlayPause();
                break;
            case R.id.btn_next:
                requestNext();
                break;
            case R.id.btn_repeat:
                requestRepeat();
                break;
            case R.id.btn_up:
                requestUp();
                break;
            case R.id.btn_down:
                requestDown();
                break;
        }

    }

    private void requestDown() {
        String text = "{\"type\":\"action\",\"value\":5}";
        updateAdapter(text);
    }

    private void requestUp() {
        String text = "{\"type\":\"action\",\"value\":4}";
        updateAdapter(text);
    }

    private void requestRepeat() {
        String text = "{\"type\":\"action\",\"value\":3}";
        updateAdapter(text);
    }

    private void requestNext() {
        String text = "{\"type\":\"action\",\"value\":2}";
        updateAdapter(text);
    }

    private void requestPlayPause() {
        String text = "{\"type\":\"action\",\"value\":1}";
        updateAdapter(text);
    }

    private void requestView(boolean flag) {
        this.flag = flag;
        String text;
        if (flag) {
            text = "{\"type\":\"view\",\"value\":1}";
        } else {
            text = "{\"type\":\"view\",\"value\":0}";
        }
        updateAdapter(text);
    }

    private void requestSong() {
        String text = "{\"type\":\"song\",\"value\":1}";
        updateAdapter(text);
    }

    private void requestPlayList() {
        String text = "{\"type\":\"play-list\",\"value\":1}";
        updateAdapter(text);
    }

    private void updateAdapter(String text) {
        ana.send(text);
        arrayAdapter.add("--------->Client : \n" + text);
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
            btnConnect.setClickable(false);
        });
    }

    @Override
    public void receiver(String message) {
        arrayAdapter.add("--------->Server : \n" + message);
    }
}
