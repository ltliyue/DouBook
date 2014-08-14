package com.doubook.fragment;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doubook.MainActivity;
import com.doubook.R;
import com.doubook.util.GestureDoInterface;

/**
 * Fragment自定义基类，继承自Fragment提取各模块可能公用的部分方法
 * 
 * @Copyright Copyright (c) 2014w - 2100
 * @author 李岳
 * @create at 2014-7-9
 * @version 1.1.0
 */
public class BaseFragment extends Fragment implements GestureDoInterface {
    private ProgressBar loading;
    protected static BlockingQueue<BaseFragment> fragmentQueue = new LinkedBlockingQueue<BaseFragment>();
    protected MainActivity owner = null;
    protected View contentView = null;
    protected Bundle mBundle = null;
    protected boolean finished = false;
    private BaseFragment prev = null;
    protected boolean afterSaveInstance = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setOrientation(null);
        return null;
    }

    /**
     * 设置跳转至当前Fragment前的Fragment
     * 
     * @return
     */
    public BaseFragment getPrev() {
        return prev;
    }

    /**
     * 获取跳转至当前Fragment之前的Fragment
     * 
     * @param prev
     */
    public void setPrev(BaseFragment prev) {
        this.prev = prev;
    }

    public final void loadingShow() {
        ((ProgressBar) getActivity().findViewById(R.id.loading)).setVisibility(View.VISIBLE);
    }

    public final void loadingHide() {
        ((ProgressBar) getActivity().findViewById(R.id.loading)).setVisibility(View.GONE);
    }

    /**
     * 设置标题
     * 
     * @param title
     */
    public final void setTitle(String title) {
        ((TextView) getActivity().findViewById(R.id.Title)).setText(title);
    }

    /**
     * 显示或隐藏resId指向的按钮
     * 
     * @param resId
     * @param show true 显示 false 隐藏
     * @param clickListener
     */
    protected void showBtn(int resId, boolean show, OnClickListener clickListener) {
        showBtn(resId, show, clickListener, 0);
    }

    /**
     * 显示或隐藏resId指向的按钮,并指定背景资源ID bgResId
     * 
     * @param resId
     * @param show
     * @param clickListener
     * @param bgResid
     */
    protected void showBtn(int resId, boolean show, OnClickListener clickListener, int bgResid) {
        if (owner == null) {
            return;
        }
        View btn = (View) owner.findViewById(resId);
        if (btn == null) {
            return;
        }
        if (show) {
            if (bgResid > 0) {
                btn.setBackgroundResource(bgResid);
            }
            if (clickListener != null) {
                btn.setOnClickListener(clickListener);
            } else {
                btn.setOnClickListener(owner);
            }
            btn.setVisibility(View.VISIBLE);
        } else {
            btn.setVisibility(View.GONE);
        }
    }

    protected enum OrienType {
        LANDSCAPE(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE), // 横屏
        PORTRAIT(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT), // 竖屏
        UNSPECIFIED(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); // 未指定

        private final int m_type;

        private OrienType(int type) {
            m_type = type;
        }

        public int getType() {
            return m_type;
        }
    }

    /**
     * 设置当前Fragment是横屏还是竖屏，或者是不指定随系统而定
     * 
     * @param type
     */
    protected void setOrientation(OrienType type) {
        if (type == null) {
            type = OrienType.UNSPECIFIED;
        }
        if (owner != null) {
            owner.setRequestedOrientation(type.getType());
        }
    }

    /**
     * 是否是横竖屏布局不同
     * 
     * @return
     */
    public boolean hasMultiLayout() {
        return false;
    }

    /**
     * 在该模块内是否需要通过语音模块控制音乐，返回false则说明本模块自己控制，默认返回true可以被子类重写
     * 
     * @return
     */
    public boolean ctlMusic() {
        return true;
    }

    /**
     * 在该模块内是否禁播音乐，返回false则说明可以播放，true表示禁播音乐，默认返回false可以被子类重写
     * 
     * @return
     */
    public boolean forbidMusic() {
        return false;
    }

    /**
     * 类似Activity的finish，结束本Fragment
     */
    public void finish() {
        if (owner == null) {
            return;
        }
        finished = true;
    }

    /**
     * 返回该Fragment包含的扩展参数集
     * 
     * @return
     */
    public Bundle getExtraBundle() {
        return mBundle;
    }

    /**
     * 由Activity调用，处理按键事件
     * 
     * @param keyCode
     * @param event
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }

    /**
     * 当本Fragment处理当前Fragment被其它Fragment替换掉时调用该方法
     */
    public void onReplaced(boolean needAddToBackStack) {
        // 添加被替换时所需要做的处理
    }

    /**
     * 处于当前界面时来电话的处理
     */
    public void onRingIn() {
        // 添加来电话时当前界面的处理逻辑
    }

    /**
     * 当按下Home键时调用
     */
    public boolean onHomePressed() {
        return true;
    }

    /**
     * 媒体按键点击事件
     * 
     * @return false不处理该点击事件由MainActivity处理，true表示该Fragment已处理该事件
     */
    public boolean onMediaKeyClick(int keyCode, boolean isHintRun) {
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.detach(this);
        transaction.attach(this);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void gestureDo(GestureType type, float lineLength) {
        // TODO Auto-generated method stub
        System.out.println("hahaha");
    }
}
