package com.doubook;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import cn.trinea.android.common.util.HttpUtils;
import cn.trinea.android.common.util.JSONUtils;
import cn.trinea.android.common.view.DropDownListView;
import cn.trinea.android.common.view.DropDownListView.OnDropDownListener;

import com.doubook.adapter.ContactListAdapter;
import com.doubook.bean.BookInfoBean;
import com.doubook.data.ContextData;
import com.doubook.util.JsoupSearchGetInfo;

public class SearchActivity extends Activity {

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
				dataAdapter = new ContactListAdapter(SearchActivity.this);
				dataAdapter.setData(contacters);
				contactList.setAdapter(dataAdapter);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_layout);
		initView();
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		contactList = (DropDownListView) findViewById(R.id.list_search_contact);
		text = getIntent().getStringExtra("text");
	}

	private void initListener() {
		// TODO Auto-generated method stub

	}

	private void inintListener() {
		contactList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
			}
		});

		// set on bottom listener
		contactList.setOnBottomListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		if (text != null) {
			new Thread() {
				@Override
				public void run() {
					contacters = null;
					JsoupSearchGetInfo jsoupSearchGetInfo = new JsoupSearchGetInfo();
					contacters = jsoupSearchGetInfo.getinfo(ContextData.Search, text);
					if (contacters == null) {
						onStart();
					} else {
						mHandler.sendEmptyMessage(1);
					}
				}
			}.start();
		}

	}

}
