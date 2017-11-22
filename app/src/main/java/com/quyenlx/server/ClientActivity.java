package com.quyenlx.server;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.quyenlx.core.util.DeviceUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener, ClientAna.MessageCallback {
    private Button btnConnect;
    private ClientAna ana;
    private ArrayAdapter<String> arrayAdapter;
    boolean flag = false;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
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
        findViewById(R.id.btn_play).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
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
            case R.id.btn_play:
                requestPlay();
                break;
            case R.id.btn_add:
                requestAdd();
                break;
        }

    }

    private void requestAdd() {
        String text = "{\"type\":\"add\",\"value\":8119}";
        updateAdapter(text);
    }

    private void requestPlay() {
        String text = "{\"type\":\"play\",\"value\":8119}";
        updateAdapter(text);
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
        start = 2;
        autoConnect();
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

    private int start = 2;
    private CompositeDisposable disposable = new CompositeDisposable();

    private void autoConnect() {
        progressDialog.show();
        String ip = DeviceUtils.getIpAddress();
        String desc = ip.substring(0, ip.lastIndexOf(".") + 1);
        arrayAdapter.add("Start auto connect...");
        disposable.add(Observable
                .create((ObservableOnSubscribe<Integer>) e -> {
                    while (start <= 255) {
                        e.onNext(start);
                        start++;
                    }
                    e.onComplete();
                })
                .concatMap(integer -> {
                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(desc + integer, 54555), 100);
                        return Observable.just(desc + integer);
                    } catch (Exception ex) {
                        Timber.e("#error %s", integer);
                        return Observable.just(false);
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s instanceof String) {
                        arrayAdapter.add("Connected : " + s);
                        ana = new ClientAna(String.valueOf(s), 54555, this);
                        ana.execute();
                        disposable.dispose();
                        progressDialog.dismiss();
                    }
                }, throwable -> updateAdapter(throwable.getMessage()), () -> {
                    arrayAdapter.add("No port is open");
                    progressDialog.dismiss();
                }));
    }

}
