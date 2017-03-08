package com.example.bluerain.verticalindicator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bluerain.verticalindicator.net.CachePolicy;
import com.example.bluerain.verticalindicator.net.Request;
import com.example.bluerain.verticalindicator.net.RequestManger;
import com.example.bluerain.verticalindicator.net.StringRequest;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView mTxv;
    private Button mButton;


    public static final String URL = "http://api.m.mi.com";
    public static final String PATH = "/v1/app/start";
    private Handler mUIHandler;

    public static final int ON_SUSSCESS_MESSAGE = 1;
    public static final int ON_ERROR_MESSAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariables();
        mTxv = (TextView) findViewById(R.id.content);
        mButton = (Button) findViewById(R.id.btn_add);
        final HashMap<String, String> header = new HashMap<>();
        header.put("User-Agent", "okhttp/3.3.1");
        final StringRequest stringRequest = new StringRequest(URL + PATH, header, null, 1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 10; i++) {
                    final StringRequest r = new StringRequest(URL + PATH + "/" + (9 - i), header, null, 9 - i);
                    if (i == 5) r.setCachePolicy(new CachePolicy() {
                        @Override
                        public boolean isNeedCache() {
                            return r.getRequestTimeStamp() - System.currentTimeMillis() <= 60 * 1000;
                        }
                    });
                    RequestManger.getInstance().addRequest(r, new Request.Listener() {
                        @Override
                        public void onSuccess(Object response) {

                        }

                        @Override
                        public void onError(String errInfo) {

                        }
                    });
                }
            }
        });

//        NetworkUtils.asyncPostRequest(URL + PATH, null, header, new NetworkUtils.PostStringListener() {
//            @Override
//            public void onSuccess(final String response) {
//                Message message = mUIHandler.obtainMessage();
//                message.what = ON_SUSSCESS_MESSAGE;
//                message.obj = response;
//                mUIHandler.sendMessage(message);
//            }
//
//            @Override
//            public void onError(String errInfo) {
//                Message message = mUIHandler.obtainMessage();
//                message.what = ON_ERROR_MESSAGE;
//                message.obj = errInfo;
//                mUIHandler.sendMessage(message);
//
//            }
//        });
    }


    private void initVariables() {
        mUIHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                switch (what) {
                    case ON_SUSSCESS_MESSAGE:
                        mTxv.setText((String) (msg.obj));
                        break;
                    case ON_ERROR_MESSAGE:
                        mTxv.setText((String) (msg.obj));
                        break;
                }
            }
        };
    }
}
