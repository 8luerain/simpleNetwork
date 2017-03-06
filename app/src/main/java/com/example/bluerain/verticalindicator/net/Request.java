package com.example.bluerain.verticalindicator.net;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by bluerain on 17-3-5.
 */

public abstract class Request implements Comparable<Request> {
    private String url;
    private HashMap<String, String> mHeader;
    private HashMap<String, String> mParams;
    private int mPrority;
    private boolean isValid = true;

    private Listener mListener;


    public Request(String url, HashMap<String, String> mHeader, HashMap<String, String> mParams, int mPrority) {
        this.url = url;
        this.mHeader = mHeader;
        this.mParams = mParams;
        this.mPrority = mPrority;
    }

    public void setListner(Listener listner) {
        mListener = listner;
    }

    public int getmPrority() {
        return mPrority;
    }

    public void cancle() {
        isValid = false;
    }

    @Override
    public int compareTo(Request o) {
        return mPrority - o.getmPrority();
    }

    public String getUrl() {
        return url;
    }

    public HashMap<String, String> getmHeader() {
        return mHeader;
    }

    public HashMap<String, String> getmParams() {
        return mParams;
    }

    public boolean isValid() {
        return isValid;
    }

    public void parasResponse(InputStream inputStream) {
        Object response = onParasResponse(inputStream);
        if (null != response) {
            if (null != mListener) mListener.onSuccess(response);
        } else {
            if (null != mListener) mListener.onError("error");
        }
    }


    protected abstract Object onParasResponse(InputStream inputStream);


    public interface Listener {
        void onSuccess(Object response);

        void onError(String errInfo);
    }
}
