package com.army.armyhongbao.job;

import android.view.accessibility.AccessibilityEvent;

import com.army.armyhongbao.ArmyHongBaoService;

/**
 * Created by army-lev-ne on 16/1/16.
 */
public interface AccessbilityJob {
    String getTargetPackageName();

    void onCreateJob(ArmyHongBaoService service);

    void onReceiveJob(AccessibilityEvent event);

    void onStopJob();

    boolean isEnable();
}
