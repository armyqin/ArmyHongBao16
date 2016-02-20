package com.army.armyhongbao;

import android.app.Notification;

/**
 * Created by army-lev-ne on 16/2/17.
 */
public interface IStatusBarNotification {
    String getPackageName();

    Notification getNotification();
}
