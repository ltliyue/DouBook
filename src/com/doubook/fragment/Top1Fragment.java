package com.doubook.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.trinea.android.common.view.DropDownListView;
import cn.trinea.android.common.view.DropDownListView.OnDropDownListener;

import com.doubook.MainActivity;
import com.doubook.R;
import com.doubook.WebActivity;
import com.doubook.adapter.ContactListAdapter;
import com.doubook.bean.BookInfoBean;
import com.doubook.data.ContextData;
import com.doubook.util.JsoupGetInfo;

public class Top1Fragment extends BaseFragment {

	private DropDownListView contactList = null;
	private ContactListAdapter dataAdapter = null;
	private ArrayList<BookInfoBean> contacters = new ArrayList<BookInfoBean>();
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
		setTitle(getString(R.string.top1));
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
		contactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				//				Intent mIntent = new Intent(getActivity(), WebActivity.class);
//				mIntent.putExtra("linkUrl", contacters.get(position - 1).getLinkUrl());
//				System.out.println("Tiop:" + contacters.get(position - 1).getLinkUrl());
//				startActivity(mIntent);
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
				contacters = jsoupTest.getinfo(ContextData.best1);
				mHandler.sendEmptyMessage(1);
			}
		}.start();
	}

}
