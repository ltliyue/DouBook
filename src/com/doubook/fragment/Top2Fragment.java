package com.doubook.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.trinea.android.common.view.DropDownListView;
import cn.trinea.android.common.view.DropDownListView.OnDropDownListener;

import com.doubook.R;
import com.doubook.adapter.ContactListAdapter;
import com.doubook.bean.BookInfoBean;
import com.doubook.data.ContextData;
import com.doubook.util.JsoupGetInfo;

public class Top2Fragment extends BaseFragment {

    private DropDownListView contactList = null;
    private ContactListAdapter dataAdapter = null;
    private ArrayList<BookInfoBean> contacters = new ArrayList<BookInfoBean>();
    private OnItemClickListener itemListener = null;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    loadingHide();
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
        contentView = inflater.inflate(R.layout.message_layout, container, false);
        setTitle(getString(R.string.main));
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
        contactList.setOnBottomListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessageDelayed(3, 1000);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        contactList = (DropDownListView) contentView.findViewById(R.id.list_of_contact);
        inintListener();
        new Thread() {
            @Override
            public void run() {
                contacters = null;
                JsoupGetInfo jsoupTest = new JsoupGetInfo();
                contacters = jsoupTest.getinfo(ContextData.best2);
                mHandler.sendEmptyMessage(1);
            }
        }.start();

    }

    /**
     * 设置联系人选择监听
     * 
     * @param selectListener
     */
    public void setContactSelectListener(OnItemClickListener selectListener) {
        this.itemListener = selectListener;
    }
}
