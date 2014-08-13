package com.doubook.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import cn.trinea.android.common.view.DropDownListView;
import cn.trinea.android.common.view.DropDownListView.OnDropDownListener;

import com.doubook.BookInfoActivity;
import com.doubook.R;
import com.doubook.adapter.ContactListAdapter;
import com.doubook.bean.BookInfoBean;
import com.doubook.data.ContextData;
import com.doubook.util.JsoupGetInfo;
import com.umeng.analytics.MobclickAgent;

public class Top1Fragment extends BaseFragment {

    private DropDownListView contactList = null;
    private ContactListAdapter dataAdapter = null;
    private ArrayList<BookInfoBean> contacters = new ArrayList<BookInfoBean>();
    private boolean started = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    loadingHide();
                    inintListener();
                    dataAdapter = null;
                    dataAdapter = new ContactListAdapter(getActivity());
                    dataAdapter.setData(ContextData.contacters);
                    contactList.setAdapter(dataAdapter);
                    ContextData.contacters = null;
                    break;
                case 1:
                    loadingHide();
                    inintListener();
                    dataAdapter = null;
                    dataAdapter = new ContactListAdapter(getActivity());
                    dataAdapter.setData(contacters);
                    contactList.setAdapter(dataAdapter);
                    break;
                case 2:
                    Toast.makeText(getActivity(), "没有更多啦~", ContextData.toastTime).show();
                    contactList.onDropDownComplete();
                    break;
                case 3:
                    Toast.makeText(getActivity(), "没有更多啦~", ContextData.toastTime).show();
                    dataAdapter.notifyDataSetChanged();
                    contactList.onBottomComplete();
                    contactList.setSelection(contacters.size() - 2);
                    break;
                default:
                    break;
            }
        };
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = null;
        contentView = inflater.inflate(R.layout.list_layout, container, false);
        loadingShow();
        return contentView;
    }

    private void inintListener() {
        contactList.setOnDropDownListener(new OnDropDownListener() {
            @Override
            public void onDropDown() {
                mHandler.sendEmptyMessageDelayed(2, 1000);
            }
        });
        contactList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(getActivity(), BookInfoActivity.class);
                mIntent.putExtra("linkUrl", contacters.get(position - 1).getLinkUrl());
                startActivity(mIntent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (started) {
            return;
        }
        contactList = (DropDownListView) contentView.findViewById(R.id.list_of_contact);
        if (ContextData.contacters != null) {
            mHandler.sendEmptyMessage(0);
        } else {
            new Thread() {
                @Override
                public void run() {
                    contacters = null;
                    JsoupGetInfo jsoupTest = new JsoupGetInfo();
                    contacters = jsoupTest.getinfo(ContextData.best1);
                    mHandler.sendEmptyMessage(1);
                }
            }.start();
        }
        started = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); // 统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }
}
