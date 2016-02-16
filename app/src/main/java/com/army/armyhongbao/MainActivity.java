package com.army.armyhongbao;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.army.share.AndroidShare;


public class MainActivity extends BaseActivity {
    private Dialog mTipsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commitAllowingStateLoss();

        QHBApplication.activityStartMain(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(ArmyHongBaoService.isRunning()) {
            if(null != mTipsDialog) {
                mTipsDialog.dismiss();
            }
        } else {
            showOpenAccessibilityServiceDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTipsDialog = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item1 = menu.add(0, 0, 0, R.string.open_service_button);
        MenuItem item2 = menu.add(0, 1, 1, R.string.share_button);
        item1.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
        item2.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 0) {
            openAccessibilityServiceSettings();
            return true;
        }else if(item.getItemId() == 1) {
            AndroidShare as = new AndroidShare(
                    MainActivity.this,
                    "Army抢红包软件, 来自: 野狼",
                    "//assets/erweima.png");
//                    "http://app.army16.com/download/ErWeiMa.png");
            as.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** 显示未开启辅助服务的对话框*/
    private void showOpenAccessibilityServiceDialog() {
        if(mTipsDialog != null && mTipsDialog.isShowing()) {
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.dialog_tips_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibilityServiceSettings();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.open_service_title);
        builder.setView(view);
        builder.setPositiveButton(R.string.open_service_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAccessibilityServiceSettings();
            }
        });
        mTipsDialog = builder.show();
    }

    /** 打开辅助服务的设置*/
    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, R.string.tips, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class MainFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName(Config.PREFERENCE_NAME);
            addPreferencesFromResource(R.xml.main);

            //微信红包开关
            findPreference(Config.KEY_ENABLE_WECHAT).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if((Boolean) newValue && !ArmyHongBaoService.isRunning()) {
                        ((MainActivity)getActivity()).showOpenAccessibilityServiceDialog();
                    }
                    return true;
                }
            });

            //打开微信红包后
            final ListPreference wxAfterOpenPre = (ListPreference) findPreference(Config.KEY_WECHAT_AFTER_OPEN_HONGBAO);
            wxAfterOpenPre.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int value = Integer.parseInt(String.valueOf(newValue));
                    preference.setSummary(wxAfterOpenPre.getEntries()[value]);
                    return true;
                }
            });
            int value = Integer.parseInt(wxAfterOpenPre.getValue());
            wxAfterOpenPre.setSummary(wxAfterOpenPre.getEntries()[value]);

            final EditTextPreference delayEditTextPre = (EditTextPreference) findPreference(Config.KEY_WECHAT_DELAY_TIME);
            delayEditTextPre.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if("0".equals(String.valueOf(newValue))) {
                        preference.setSummary("");
                    } else {
                        preference.setSummary("已延时" + newValue + "毫秒");
                    }
                    return true;
                }
            });
            String delay = delayEditTextPre.getText();
            if("0".equals(String.valueOf(delay))) {
                delayEditTextPre.setSummary("");
            } else {
                delayEditTextPre.setSummary("已延时" + delay  + "毫秒");
            }
        }
    }
}
