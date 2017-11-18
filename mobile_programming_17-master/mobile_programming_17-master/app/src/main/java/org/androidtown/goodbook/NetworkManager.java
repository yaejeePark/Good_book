package org.androidtown.goodbook;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 지영 on 2017-05-04.
 */

public class NetworkManager {

    private static NetworkManager instance;
    public static NetworkManager getInstance(){
        if(instance ==null){
            instance = new NetworkManager();
        }
        return instance;
    }

    ThreadPoolExecutor mExecutor;
    BlockingQueue<Runnable> mTaskQueue = new LinkedBlockingQueue<Runnable>();
    private NetworkManager(){
        mExecutor = new ThreadPoolExecutor(3,64,10, TimeUnit.SECONDS, mTaskQueue);

    }

    public interface OnResultListener<T>{
        public void onSuccess(NetworkRequest<T> request, T result);
        public void onFailure(NetworkRequest<T> request, int errorCode, int responseCode, String message, Throwable exception);

    }

    private static final int MESSAGE_SUCESS = 0;
    private static final int MESSAGE_FAILURE = 1;

    static class NetworkHandler extends Handler {
        NetworkManager manager;
        public NetworkHandler(NetworkManager manager){
            super();
            this.manager = manager;
        }

        public NetworkHandler(Looper looper, NetworkManager manager){
            super(looper);
            this.manager = manager;
        }

        @Override
        public void handleMessage(Message msg) {
            NetworkRequest r = (NetworkRequest)msg.obj;
            switch (msg.what){
                case MESSAGE_SUCESS:
                    r.sendSuccess();
                    break;
                case MESSAGE_FAILURE:
                    r.sendFailure();
                    break;
            }
            manager.mRequestList.remove(r);
        }
    }

    Handler mHandler = new NetworkHandler(Looper.getMainLooper(), this);

    public void sendSuccess(NetworkRequest request){
        Message msg = mHandler.obtainMessage(MESSAGE_SUCESS, request);
        mHandler.sendMessage(msg);
    }

    public void sendFailure(NetworkRequest request){
        Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, request);
        mHandler.sendMessage(msg);
    }

    List<NetworkRequest> mRequestList = new ArrayList<NetworkRequest>();

    public <T> void getNetworkData(NetworkRequest<T> request, OnResultListener<T> listener){
        mRequestList.add(request);

        request.setManager(this);
        request.setOnResultListener(listener);
        mExecutor.execute(request);
    }

    void postCancelProcess(NetworkRequest request) {
        mRequestList.remove(request);
    }


    public void cancelAll(Object tag) {
        List<NetworkRequest> removeList = new ArrayList<NetworkRequest>();
        for (NetworkRequest r : mRequestList) {
            if (tag == null || r.getTag().equals(tag)) {
                removeList.add(r);
            }
        }

        for (NetworkRequest r : removeList) {
            r.cancel();
        }
    }
}

