package com.doubook;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import cn.trinea.android.common.util.HttpUtils;
import cn.trinea.android.common.util.JSONUtils;

import com.doubook.data.ContextData;
import com.doubook.util.JsoupGetInfo;
import com.doubook.util.JsoupGetInfo_NewBook;

public class WelcomeActivity extends Activity {

    private RelativeLayout iv;
    AlphaAnimation alphaAnimation;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String refresh_token;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.welcome);
        openAThread();
        sharedPreferences = getSharedPreferences("AccessToken", 0);
        editor = sharedPreferences.edit();
        if (!sharedPreferences.getString("refresh_token", "").equalsIgnoreCase("")) {
            refresh_token = sharedPreferences.getString("refresh_token", "");
            initData();
        }
        initView();
        initListener();
    }

    private void openAThread() {
        new Thread() {
            @Override
            public void run() {
                ContextData.contacters_1 = null;
                JsoupGetInfo jsoupTest = new JsoupGetInfo();
                ContextData.contacters_1 = jsoupTest.getinfo(ContextData.best1);
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                ContextData.contacters_2 = null;
                JsoupGetInfo jsoupTest = new JsoupGetInfo();
                ContextData.contacters_2 = jsoupTest.getinfo(ContextData.best2);
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                ContextData.contacters_3 = null;
                JsoupGetInfo_NewBook jsoupTest = new JsoupGetInfo_NewBook();
                ContextData.contacters_3 = jsoupTest.getinfo(ContextData.newbook, 0);
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                ContextData.contacters_4 = null;
                JsoupGetInfo_NewBook jsoupTest = new JsoupGetInfo_NewBook();
                ContextData.contacters_4 = jsoupTest.getinfo(ContextData.newbook, 1);
            }
        }.start();
    }

    private void initView() {
        iv = (RelativeLayout) this.findViewById(R.id.weatherRLayout);
        alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(2000);
        iv.startAnimation(alphaAnimation);
    }

    private void initListener() {
        alphaAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                if (refresh_token != null) {
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(WelcomeActivity.this, FirstActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> parasMap = new HashMap<String, String>();
                String httpUrl = ContextData.GetAccessToken;
                parasMap.put("client_id", ContextData.APIKey);
                parasMap.put("client_secret", ContextData.Secret);
                parasMap.put("redirect_uri", ContextData.redirect_uri);
                parasMap.put("grant_type", "refresh_token");
                parasMap.put("refresh_token", refresh_token);
                String result = HttpUtils.httpPostString(httpUrl, parasMap);
                ContextData.access_token = JSONUtils.getString(result, "access_token", "");
                if (ContextData.access_token == null) {
                    refresh_token = "";
                } else {
                    editor.putString("access_token", JSONUtils.getString(result, "access_token", "")).commit();
                    editor.putString("refresh_token", JSONUtils.getString(result, "refresh_token", "")).commit();
                }
            }
        }).start();
    }

}
