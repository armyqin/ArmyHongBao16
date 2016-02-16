package com.army.armyhongbao;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Created by army-lev-ne on 16/1/16.
 */
public class QHBApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /** 首个activity启动调用*/
    public static void activityStartMain(Activity activity) {

    }

    /** 每个activity生命周期里的onCreate*/
    public static void activityCreateStatistics(Activity activity) {

    }

    /** 每个activity生命周期里的onResume*/
    public static void activityResumeStatistics(Activity activity) {

    }

    /** 每个activity生命周期里的onPause*/
    public static void activityPauseStatistics(Activity activity) {

    }

    /** 事件统计*/
    public static void eventStatistics(Context context, String event) {

    }

    /** 事件统计*/
    public static void eventStatistics(Context context, String event, String tag) {

    }
}
