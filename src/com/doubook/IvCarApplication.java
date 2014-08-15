package com.doubook;

import android.app.Application;

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
