package com.doubook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.SyncStateContract.Constants;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.Display;
import cn.trinea.android.common.service.impl.ImageCache;

/**
 * 应用程序入口，负责创建应用程序缓存区，如图片缓冲区、请求缓冲区等
 * 
 * @author gzs
 */
public class IvCarApplication extends Application {
    /**
     * Tag used for DDMS logging
     */
    public static String TAG = "ivCar";

    /**
     * Singleton pattern
     */
    private static IvCarApplication instance = null;

    public static IvCarApplication getInstance() {
        return instance;
    }

    private static void setInstance(IvCarApplication app) {
        instance = app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 设置实例
        setInstance(this);
    }

}
