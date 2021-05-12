package com.llj.netstatuslibrary;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.llj.netstatuslibrary.log.AndroidLogAdapter;
import com.llj.netstatuslibrary.log.DiskLogAdapter;
import com.llj.netstatuslibrary.log.Logger;
import com.llj.netstatuslibrary.log.PrettyFormatStrategy;


public class NetWorkManager {
    public static volatile NetWorkManager instants;
    public Application application;
    public NetWorkReceiver receiver;

    public NetWorkManager() {
        receiver = new NetWorkReceiver();
    }

    public static NetWorkManager getInstance() {
        if (instants == null) {
            synchronized (NetWorkManager.class) {
                if (instants == null) {
                    instants = new NetWorkManager();
                }
            }
        }
        return instants;
    }

    public Application getApplication() {
        if (null == application) {
            throw new RuntimeException("please init method int you app");
        }
        return application;
    }

    /**
     * 是否初始化保存本地日志
     * @param application
     * @param initLog 是否开启Logger
     * @param tag  日志打印tag
     */
    public void init(Application application, boolean initLog, String tag) {
        this.application = application;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        application.registerReceiver(receiver, filter);
        if (initLog) {
            initLog(tag);
        }
    }

    private void initLog(String tag) {
        PrettyFormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  //（可选）是否显示线程信息。 默认值为true
                .methodCount(1)         // （可选）要显示的方法行数。 默认2
                .methodOffset(0)        // （可选）隐藏内部方法调用到偏移量。 默认5
                .tag(tag)//（可选）每个日志的全局标记。 默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));//根据上面的格式设置logger相应的适配器
        Logger.addLogAdapter(new DiskLogAdapter(application));//保存到文件
    }

    public void regiestObserver(Object object) {
        receiver.regiestObserver(object);
    }

    public void unRegiestObserver(Object object) {
        receiver.unRegiestObserver(object);
    }
}
