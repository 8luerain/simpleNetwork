package com.example.bluerain.verticalindicator.net;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by bluerain on 17-3-8.
 */

public class HttpUrlConnectionStack implements HttpStack {
    @Override
    public void performRequest(Request mRequest) {
        HashMap<String, String> header = mRequest.getmHeader();
        InputStream inputStream = null;
        try {
            mRequest.setRequestTimeStamp(System.currentTimeMillis());
            URL u = new URL(mRequest.getUrl());
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            Set<String> headerSet = header.keySet();
            for (String h : headerSet) {
                con.setRequestProperty(h, header.get(h));
            }
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            if (200 == con.getResponseCode()) {
                inputStream = con.getInputStream();
                mRequest.parasResponse(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
