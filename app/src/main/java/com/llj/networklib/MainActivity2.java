package com.llj.networklib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.llj.netstatuslibrary.NetType;
import com.llj.netstatuslibrary.annotation.NetWork;
import com.llj.netstatuslibrary.NetWorkManager;
import com.orhanobut.logger.Logger;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        NetWorkManager.getInstance().regiestObserver(this);
    }

    @NetWork(netType = NetType.AUTO)
    public void netType(NetType netType) {
        switch (netType) {
            case WIFI:
                Logger.d("wifi");
//                Log.e("MainActivity", "wifi");
                Toast.makeText(this, "WIFI", Toast.LENGTH_SHORT).show();
                break;
            case CMNET:
                Toast.makeText(this, "CMNET", Toast.LENGTH_SHORT).show();
                break;
            case CMWAP:
                Logger.d("4G");
                Log.e("MainActivity", "4G");
                Toast.makeText(this, "CMWAP", Toast.LENGTH_SHORT).show();
                break;
            case AUTO:
                Toast.makeText(this, "AUTO", Toast.LENGTH_SHORT).show();
                break;
            case NONE:
                Logger.d("无网络");
                Log.e("MainActivity", "无网络");
                Toast.makeText(this, "NONE", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetWorkManager.getInstance().unRegiestObserver(this);
    }
}