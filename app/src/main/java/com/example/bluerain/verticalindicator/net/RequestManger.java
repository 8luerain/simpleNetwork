package com.example.bluerain.verticalindicator.net;

import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by bluerain on 17-3-5.
 */

public class RequestManger {

    public static final int DEFALT_THRAD_NUM = 5;
    private static RequestManger INSATANCE;
    private final CacheDelivery mCacheDelivery;


    private PriorityBlockingQueue<Request> mRequstQueue;

    private int threadNum;
    private ThreadPoolExecutor mThreadPoolExecutor;
    private HttpStack mHttpStack;
    private NetworkDelivery mNetworkDelivery;


    private RequestManger() {
        mHttpStack = new HttpUrlConnectionStack();
        mRequstQueue = new PriorityBlockingQueue<>();
        mThreadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFALT_THRAD_NUM);
        mNetworkDelivery = new NetworkDelivery(mThreadPoolExecutor, mHttpStack);
        mCacheDelivery = new CacheDelivery(mThreadPoolExecutor, mNetworkDelivery);
        mNetworkDelivery.start();
        mCacheDelivery.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Request request = mRequstQueue.take();
                        if (!request.isNeedCache()) mNetworkDelivery.addNetworkRequest(request);
                        else mCacheDelivery.addCacheRequest(request);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void setHttpStack(HttpStack httpStack) {
        mHttpStack = httpStack;
    }

    public synchronized static RequestManger getInstance() {
        if (null == INSATANCE)
            INSATANCE = new RequestManger();
        return INSATANCE;
    }

    public void addRequest(Request request, Request.Listener listener) {
        request.setListner(listener);
        mRequstQueue.put(request);
    }

    public void setThradNum(int num) {
        threadNum = num;
        mThreadPoolExecutor.setCorePoolSize(num);
    }

    public void cancleRequest(Request request) {
        if (mRequstQueue.contains(request))
            request.cancle();
    }

}
