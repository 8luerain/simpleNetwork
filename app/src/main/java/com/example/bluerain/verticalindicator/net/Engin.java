package com.example.bluerain.verticalindicator.net;

/**
 * Created by bluerain on 17-3-8.
 */

public class Engin implements Runnable, HttpStack {
    private HttpStack mHttpStack;
    private Request mRequest;


    public Engin(Request request, HttpStack stack) {
        this.mRequest = request;
        this.mHttpStack = stack;
    }


    @Override
    public void run() {
        performRequest(mRequest);
    }

    @Override
    public void performRequest(Request request) {
        mHttpStack.performRequest(request);
    }
}
