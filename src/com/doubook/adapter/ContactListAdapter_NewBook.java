package com.doubook.adapter;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.util.CacheManager;
import com.doubook.R;
import com.doubook.bean.BookInfoBean;

/**
 * 展示联系人列表用的适配器类
 * 
 * @Copyright Copyright (c) 2012 - 2100
 * @create at 2013-11-4
 * @version 1.1.0
 */
public class ContactListAdapter_NewBook extends BaseAdapter {
	public static final ImageCache IMAGE_CACHE = CacheManager.getImageCache();
	private ArrayList<BookInfoBean> contacters = new ArrayList<BookInfoBean>();
	private LayoutInflater mInflater = null;

	public ContactListAdapter_NewBook(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(ArrayList<BookInfoBean> contacters) {
		this.contacters = contacters;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return contacters == null ? 0 : contacters.size();
	}

	@Override
	public BookInfoBean getItem(int position) {
		return contacters.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.contact_list_item_new, null);
		}
		BookInfoBean info = contacters.get(position);
		String name = info.getName();
		ImageView portrait = ((ImageView) convertView.findViewById(R.id.img_portrait));
		TextView point = ((TextView) convertView.findViewById(R.id.txt_point));
		TextView bookinfo = ((TextView) convertView.findViewById(R.id.txt_bookinfo));

		((TextView) convertView.findViewById(R.id.txt_name)).setText(name);
		point.setText(info.getBookinfo());
		bookinfo.setText(info.getEvaluateNum());
		IMAGE_CACHE.get(info.getImageUrl(), portrait);
		return convertView;
	}
}
