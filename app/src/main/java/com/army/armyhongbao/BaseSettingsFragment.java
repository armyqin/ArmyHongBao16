package com.army.armyhongbao;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.army.armyhongbao.Config;

/**
 * Created by army-lev-ne on 16/2/17.
 */
public class BaseSettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Config.PREFERENCE_NAME);
    }
}
