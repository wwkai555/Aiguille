package me.spencerwang.aiguille.common;

import android.app.Application;
import android.content.Context;

/**
 * Created by SpencerWang on 2015/3/10.
 */
public class App extends Application {
    private static Context mContext;

    private static final String HOST = "http://aiguille/content";

    public static final String SERVER = HOST + "/%1$s/%2$s";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return  mContext;
    }
}
