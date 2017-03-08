package com.example.bluerain.verticalindicator.net;

import android.util.LruCache;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by bluerain on 17-3-8.
 */

public class CacheDelivery extends Thread {
    private final NetworkDelivery mNetworkDelivery;
    private PriorityBlockingQueue<Request> mCacheQueue;
    private ThreadPoolExecutor mThreadPoolExecutor;
    private LruCache<String, Response> mLruCache;

    public CacheDelivery(ThreadPoolExecutor executor, NetworkDelivery networkDelivery) {
        mNetworkDelivery = networkDelivery;
        mLruCache = new CustomCache(30);
        mThreadPoolExecutor = executor;
        mCacheQueue = new PriorityBlockingQueue<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Request request = mCacheQueue.take();
                String key = request.getTag();
                Response response = mLruCache.get(key);
                if (null != response) {
                    // TODO: 17-3-8
                    request.parasResponse(null);
                } else {
                    mNetworkDelivery.addNetworkRequest(request);
                    // TODO: 17-3-8 解析完了以后放到缓存里
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public PriorityBlockingQueue<Request> getQueue() {
        return mCacheQueue;
    }

    public void addCacheRequest(Request request) {
        mCacheQueue.put(request);
    }


    class CustomCache extends LruCache<String, Response> {
        public CustomCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, Response value) {
            return super.sizeOf(key, value);
        }
    }
}
