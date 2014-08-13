package com.doubook.util;

import android.content.Context;
import android.view.Display;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.doubook.IvCarApplication;
import com.doubook.util.GestureDoInterface.GestureType;

/**
 * 手势指令识别公用处理类
 * 
 * @company 山东易网通信息科技有限公司
 * @author 虞贵涛
 * @create at 2012-12-17
 */
@SuppressWarnings("deprecation")
public class YWTGestureDetector extends SimpleOnGestureListener {
    private static Display display = null;
    private static float FLING_MIN_DISTANCE = 0;

    static {
        display = ((WindowManager) IvCarApplication.getInstance().getSystemService(Context.WINDOW_SERVICE))
            .getDefaultDisplay();
        // Point windowSize = new Point();
        // display.getSize(windowSize);
        int width = display.getWidth(); // deprecated
        int height = display.getHeight(); // deprecated
        FLING_MIN_DISTANCE = width > height ? height / 4 : height / 4;
    }
    GestureDoInterface callback;

    public YWTGestureDetector(GestureDoInterface callback) {
        this.callback = callback;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        callback.gestureDo(GestureType.LONG_PRESS, 0);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        callback.gestureDo(GestureType.SINGLE_TAP_CONFIRMED, 0);
        return super.onSingleTapConfirmed(e);
    }

    /**
     * 识别上下左右滑动的手势指令
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null) {
            return false;
        }
        GestureType gtype = GestureType.NONE;
        /*
         * 参数解释： e1：第1个ACTION_DOWN MotionEvent e2：最后一个ACTION_MOVE MotionEvent
         * velocityX：X轴上的移动速度，像素/秒 velocityY：Y轴上的移动速度，像素/秒 触发条件 ：
         * X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
         */
        // Point p = new Point();
        // display.getSize(p);
        int width = display.getWidth(); // deprecated
        int height = display.getHeight(); // deprecated
        // X或者y轴上移动的距离(像素)
        float xLen = e1.getRawX() - e2.getRawX();
        float yLen = e1.getRawY() - e2.getRawY();
        float len = xLen / width;
        if (Math.abs(xLen) < Math.abs(yLen)) {
            if (yLen <= -FLING_MIN_DISTANCE) {
                gtype = GestureType.DOWN;
            } else if (yLen >= FLING_MIN_DISTANCE) {
                gtype = GestureType.UP;
            }
            len = yLen / height;
        } else {
            if (xLen > FLING_MIN_DISTANCE) {
                gtype = GestureType.LEFT; // 向左滑动
            } else if (xLen < -FLING_MIN_DISTANCE) {
                gtype = GestureType.RIGHT; // 向右滑动
            }
        }
        callback.gestureDo(gtype, len);
        return false;
    }
}
