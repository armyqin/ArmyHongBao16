package com.army.armyhongbao.job;

import android.content.Context;

import com.army.armyhongbao.ArmyHongBaoService;
import com.army.armyhongbao.Config;

/**
 * Created by army-lev-ne on 16/1/16.
 */
public abstract class BaseAccessbilityJob implements AccessbilityJob {

    private ArmyHongBaoService service;

    @Override
    public void onCreateJob(ArmyHongBaoService service) {
        this.service = service;
    }

    public Context getContext() {
        return service.getApplicationContext();
    }

    public Config getConfig() {
        return service.getConfig();
    }

    public ArmyHongBaoService getService() {
        return service;
    }
}
