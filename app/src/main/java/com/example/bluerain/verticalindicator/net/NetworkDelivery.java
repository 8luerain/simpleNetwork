package com.example.bluerain.verticalindicator.net;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by bluerain on 17-3-8.
 */

public class NetworkDelivery extends Thread {
    private PriorityBlockingQueue<Request> mNetworkQueue;
    private ThreadPoolExecutor mThreadPoolExecutor;
    private HttpStack mHttpStack;

    public NetworkDelivery(ThreadPoolExecutor executor, HttpStack stack) {
        mThreadPoolExecutor = executor;
        mHttpStack = stack;
        mNetworkQueue = new PriorityBlockingQueue<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Request request = mNetworkQueue.take();
                mThreadPoolExecutor.execute(new Engin(request, mHttpStack));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public PriorityBlockingQueue<Request> getQueue() {
        return mNetworkQueue;
    }

    public void addNetworkRequest(Request request) {
        mNetworkQueue.put(request);
    }
}
