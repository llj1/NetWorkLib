package com.llj.networklib;

import android.app.Application;

import com.llj.netstatuslibrary.NetWorkManager;

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkManager.getInstance().init(this,true,"DEBUG");
    }
}
