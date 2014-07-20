package com.doubook.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

import cn.trinea.android.common.view.DropDownListView;
import cn.trinea.android.common.view.DropDownListView.OnDropDownListener;

import com.doubook.R;
import com.doubook.adapter.ContactListAdapter;
import com.doubook.bean.BookInfoBean;
import com.doubook.data.ContextData;
import com.doubook.util.JsoupGetInfo;
import com.doubook.util.JsoupSearchGetInfo;

public class SettingFragment extends BaseFragment {
	public static String text = "";
	private DropDownListView contactList = null;
	private ContactListAdapter dataAdapter = null;
	private ArrayList<BookInfoBean> contacters = new ArrayList<BookInfoBean>();
	private OnItemClickListener itemListener = null;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			if (msg.what == 1) {
				dataAdapter = null;
				dataAdapter = new ContactListAdapter(getActivity());
				dataAdapter.setData(contacters);
				contactList.setAdapter(dataAdapter);
			}
		};
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		contentView = null;
		contentView = inflater.inflate(R.layout.setting_layout, container, false);
		setTitle(getString(R.string.main));
		return contentView;
	}

	private void inintListener() {
		contactList.setOnDropDownListener(new OnDropDownListener() {

			@Override
			public void onDropDown() {
				System.out.println("aaaaaaaaaaaaaa");
			}
		});

		// set on bottom listener
		contactList.setOnBottomListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("bbbbbbbbbbbbb");
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
				JsoupSearchGetInfo jsoupSearchGetInfo = new JsoupSearchGetInfo();
				contacters = jsoupSearchGetInfo.getinfo(ContextData.Search, text);
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
