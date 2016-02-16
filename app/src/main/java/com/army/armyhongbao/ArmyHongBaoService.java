package com.army.armyhongbao;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.army.armyhongbao.job.AccessbilityJob;
import com.army.armyhongbao.job.WechatAccessbilityJob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by army-lev-ne on 16/1/16.
 */
public class ArmyHongBaoService extends AccessibilityService {
    private static final String TAG = "ArmyHongBao";

    private static final Class[] ACCESSBILITY_JOBS = {
            WechatAccessbilityJob.class
    };

    private static ArmyHongBaoService service;

    private Config mConfig;
    private List<AccessbilityJob> mAccessbilityJobs;

    @Override
    public void onCreate() {
        super.onCreate();

        mAccessbilityJobs = new ArrayList<>();
        mConfig = new Config(this);

        //初始化辅助插件工作
        for(Class clazz : ACCESSBILITY_JOBS) {
            try {
                Object object = clazz.newInstance();
                if(object instanceof AccessbilityJob) {
                    AccessbilityJob job = (AccessbilityJob) object;
                    job.onCreateJob(this);
                    mAccessbilityJobs.add(job);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mAccessbilityJobs != null && !mAccessbilityJobs.isEmpty()) {
            for (AccessbilityJob job : mAccessbilityJobs) {
                job.onStopJob();
            }
            mAccessbilityJobs.clear();
        }
        service = null;
        mAccessbilityJobs = null;
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, R.string.stop_hong_bao_service, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        service = this;
        Toast.makeText(this, R.string.contented_hong_bao_service, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "事件--->" + event);
        }
        String pkn = String.valueOf(event.getPackageName());
        if(mAccessbilityJobs != null && !mAccessbilityJobs.isEmpty()) {

            for (AccessbilityJob job : mAccessbilityJobs) {
                if(pkn.equals(job.getTargetPackageName()) && job.isEnable()) {
                    job.onReceiveJob(event);
                }
            }
        }
    }

    public Config getConfig() {
        return mConfig;
    }

    /**
     * 判断当前服务是否正在运行
     * */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isRunning() {
        if(service == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();
        if(info == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if(i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if(!isConnect) {
            return false;
        }
        return true;
    }
}
