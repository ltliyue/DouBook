package com.doubook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doubook.data.ContextData;
import com.doubook.fragment.Top2Fragment;
import com.doubook.fragment.Top1Fragment;
import com.doubook.fragment.New1Fragment;
import com.doubook.fragment.New2Fragment;
import com.doubook.widget.SearchPopupWindow;
import com.meyao.book.Zxing.CaptureActivity;

public class MainActivity extends FragmentActivity implements OnClickListener {

	int selected = -1;
	public ProgressBar loading;
	private SearchPopupWindow mSearchPopupWindow;
	private ImageView btn_search;
	// 用于展示消息的Fragment
	private Top1Fragment mainFragment;
	// *用于展示联系人的Fragment
	private Top2Fragment top2Fragment;
	// ** 用于展示动态的Fragment
	private New1Fragment newsFragment;
	// **用于展示设置的Fragment
	private New2Fragment settingFragment;

	// **消息界面布局
	private View messageLayout;
	// **联系人界面布局
	private View contactsLayout;
	// **动态界面布局
	private View newsLayout;
	// **设置界面布局
	private View settingLayout;

	// **在Tab布局上显示消息图标的控件
	private ImageView messageImage;
	// ** 在Tab布局上显示联系人图标的控件
	private ImageView contactsImage;
	// **在Tab布局上显示动态图标的控件
	private ImageView newsImage;
	// **在Tab布局上显示设置图标的控件
	private ImageView settingImage;

	// **在Tab布局上显示消息标题的控件
	private TextView messageText;
	// ** 在Tab布局上显示联系人标题的控件
	private TextView contactsText;
	// **在Tab布局上显示动态标题的控件
	private TextView newsText;
	// ** 在Tab布局上显示设置标题的控件
	private TextView settingText;

	private boolean isFirst = true;
	// ** 用于对Fragment进行管理
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 初始化布局元素
		initViews();

		fragmentManager = getSupportFragmentManager();
		// 第一次启动时选中第0个tab
		setTabSelection(0);
	}

	/**
	 * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
	 */
	private void initViews() {
		loading = (ProgressBar) findViewById(R.id.loading);
		btn_search = (ImageView) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearchPopupWindow = new SearchPopupWindow(MainActivity.this, null);
				mSearchPopupWindow.showAsDropDown(MainActivity.this.findViewById(R.id.divider_view));
			}
		});

		messageLayout = findViewById(R.id.message_layout);
		contactsLayout = findViewById(R.id.contacts_layout);
		newsLayout = findViewById(R.id.news_layout);
		settingLayout = findViewById(R.id.setting_layout);
		messageImage = (ImageView) findViewById(R.id.message_image);
		contactsImage = (ImageView) findViewById(R.id.contacts_image);
		newsImage = (ImageView) findViewById(R.id.news_image);
		settingImage = (ImageView) findViewById(R.id.setting_image);
		messageText = (TextView) findViewById(R.id.message_text);
		contactsText = (TextView) findViewById(R.id.contacts_text);
		newsText = (TextView) findViewById(R.id.news_text);
		settingText = (TextView) findViewById(R.id.setting_text);

		messageLayout.setOnClickListener(this);
		contactsLayout.setOnClickListener(this);
		newsLayout.setOnClickListener(this);
		settingLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message_layout:
			// 当点击了消息tab时，选中第1个tab
			setTabSelection(0);
			break;
		case R.id.contacts_layout:
			// 当点击了联系人tab时，选中第2个tab
			setTabSelection(1);
			break;
		case R.id.news_layout:
			// 当点击了动态tab时，选中第3个tab
			setTabSelection(2);
			break;
		case R.id.setting_layout:
			// 当点击了设置tab时，选中第4个tab
			setTabSelection(3);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		System.out.println("main..............");
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String text = intent.getStringExtra("RESULT");
				Intent mIntent = new Intent(MainActivity.this, SearchActivity.class);
				mIntent.putExtra("text", text);
				startActivity(mIntent);
			} else if (resultCode == RESULT_CANCELED) {
			}
		} else {
			return;
		}
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 *            每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
	 */
	private void setTabSelection(int index) {
		if (selected == index) {
			return;
		}
		selected = index;
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		if (isFirst) {
			removeFragments(transaction);
			isFirst = false;
		} else {
			hideFragments(transaction);
		}

		switch (index) {
		case 0:
			// 当点击了消息tab时，改变控件的图片和文字颜色
			messageImage.setImageResource(R.drawable.icon44);
			messageText.setTextColor(Color.parseColor("#7fb80e"));
			if (mainFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				mainFragment = new Top1Fragment();
				transaction.add(R.id.content, mainFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				// transaction.replace(R.id.content, mainFragment);
				transaction.show(mainFragment);
			}
			break;
		case 1:
			// 改变控件的图片和文字颜色
			contactsImage.setImageResource(R.drawable.icon33);
			contactsText.setTextColor(Color.parseColor("#7fb80e"));
			if (top2Fragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				top2Fragment = new Top2Fragment();
				transaction.add(R.id.content, top2Fragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				// transaction.replace(R.id.content, top2Fragment);
				transaction.show(top2Fragment);
			}
			break;
		case 2:
			// 改变控件的图片和文字颜色
			newsImage.setImageResource(R.drawable.icon22);
			newsText.setTextColor(Color.parseColor("#7fb80e"));
			if (newsFragment == null) {
				// 如果NewsFragment为空，则创建一个并添加到界面上
				newsFragment = new New1Fragment();
				transaction.add(R.id.content, newsFragment);
			} else {
				// 如果NewsFragment不为空，则直接将它显示出来
				// transaction.replace(R.id.content, newsFragment);
				transaction.show(newsFragment);
			}
			break;
		case 3:
		default:
			// 改变控件的图片和文字颜色
			settingImage.setImageResource(R.drawable.icon11);
			settingText.setTextColor(Color.parseColor("#7fb80e"));
			if (settingFragment == null) {
				// 如果SettingFragment为空，则创建一个并添加到界面上
				settingFragment = new New2Fragment();
				transaction.add(R.id.content, settingFragment);
			} else {
				// 如果SettingFragment不为空，则直接将它显示出来
				// transaction.replace(R.id.content, settingFragment);
				transaction.show(settingFragment);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
		messageImage.setImageResource(R.drawable.icon4);
		messageText.setTextColor(Color.parseColor("#82858b"));
		contactsImage.setImageResource(R.drawable.icon3);
		contactsText.setTextColor(Color.parseColor("#82858b"));
		newsImage.setImageResource(R.drawable.icon2);
		newsText.setTextColor(Color.parseColor("#82858b"));
		settingImage.setImageResource(R.drawable.icon1);
		settingText.setTextColor(Color.parseColor("#82858b"));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void removeFragments(FragmentTransaction transaction) {
		if (mainFragment != null) {
			transaction.remove(mainFragment);
		}
		if (top2Fragment != null) {
			transaction.remove(top2Fragment);
		}
		if (newsFragment != null) {
			transaction.remove(newsFragment);
		}
		if (settingFragment != null) {
			transaction.remove(settingFragment);
		}
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (mainFragment != null) {
			transaction.hide(mainFragment);
		}
		if (top2Fragment != null) {
			transaction.hide(top2Fragment);
		}
		if (newsFragment != null) {
			transaction.hide(newsFragment);
		}
		if (settingFragment != null) {
			transaction.hide(settingFragment);
		}
	}

	private void delFragments(FragmentTransaction transaction) {
		if (mainFragment != null) {
			for (int i = 0; i < transaction.hashCode(); i++) {

			}
		}
	}
}
