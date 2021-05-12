package com.llj.netstatuslibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.llj.netstatuslibrary.annotation.NetWork;
import com.llj.netstatuslibrary.util.NetTypeUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetWorkReceiver extends BroadcastReceiver {
    private NetType netType;
    private Map<Object, List<MethodManager>> mMethodManager;

    public NetWorkReceiver() {
        netType = NetType.NONE;
        mMethodManager = new HashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e("NetWork", "--intent-- == null");
            return;
        }
        Log.e("NetWork", "网络发生变化--intent--");
        if (intent.getAction() == ConnectivityManager.CONNECTIVITY_ACTION) {
            netType = NetTypeUtil.checkNetType(NetWorkManager.getInstance().getApplication());
            post(netType);
        }
    }

    private void post(NetType netType) {
        Set<Object> key = mMethodManager.keySet();
        for (Object object : key) {
            List<MethodManager> methodManagers = mMethodManager.get(object);
            if (methodManagers != null) {
                for (MethodManager methodManager : methodManagers) {
                    methodManager.getType().isAssignableFrom(netType.getClass());
                    switch (netType) {
                        case AUTO:
                        case WIFI:
                        case CMNET:
                        case CMWAP:
                            invoke(methodManager, object, netType);
                            break;
                        case NONE:
                            Log.e("NetWorkReceiver", "---netWorkStatus---NONE");
                            break;
                        default:
                            Log.e("NetWorkReceiver", "---netWorkStatus---UNKNOW");
                            break;
                    }
                }
            }
        }

    }

    private void invoke(MethodManager methodManager, Object object, NetType netType) {
        Method method = methodManager.getMethod();
        try {
            method.invoke(object, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void regiestObserver(Object object) {
        List<MethodManager> managers = mMethodManager.get(object);
        if (managers == null) {
            managers = findAnnotation(object);
            mMethodManager.put(object, managers);
        }
    }

    private List<MethodManager> findAnnotation(Object object) {
        List<MethodManager> managers = new ArrayList<>();
        Class<?> aclass = object.getClass();
        Method[] methods = aclass.getMethods();
        for (Method method : methods) {
            NetWork netWork = method.getAnnotation(NetWork.class);
            if (netWork == null) {
                continue;
            }
            Type type = method.getGenericReturnType();
            if (!type.toString().equals("void")) {
                throw new RuntimeException(method.getName() + "the method must be void");
            }
            Class<?>[] parms = method.getParameterTypes();
            if (parms.length != 1) {
                throw new RuntimeException(method.getName() + "Method can only have one parameter");
            }
            MethodManager methodManager = new MethodManager(parms[0], netType, method);
            managers.add(methodManager);
        }
        return managers;
    }

    public void unRegiestObserver(Object object) {
        if (!mMethodManager.isEmpty()) {
            mMethodManager.remove(object);
        }
    }

}
