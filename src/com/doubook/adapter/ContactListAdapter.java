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
 * @Company 山东易网通信息科技有限公司
 * @author 虞贵涛
 * @create at 2013-11-4
 * @version 1.1.0
 */
public class ContactListAdapter extends BaseAdapter {
	public static final ImageCache IMAGE_CACHE = CacheManager.getImageCache();
	private ArrayList<BookInfoBean> contacters = new ArrayList<BookInfoBean>();
	private LayoutInflater mInflater = null;

	public ContactListAdapter(Context context) {
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
			convertView = mInflater.inflate(R.layout.contact_list_item, null);
		}
		BookInfoBean info = contacters.get(position);
		String name = info.getName();
		RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
		ImageView portrait = ((ImageView) convertView.findViewById(R.id.portrait));
		TextView point = ((TextView) convertView.findViewById(R.id.point));
		TextView bookinfo = ((TextView) convertView.findViewById(R.id.bookinfo));

		((TextView) convertView.findViewById(R.id.name)).setText(name);
		if (info.getEvaluateNum().contentEquals(".")) {
			point.setText(info.getEvaluateNum());
		} else {
			point.setText(info.getStarpoint() + info.getEvaluateNum());
		}
		bookinfo.setText(info.getBookinfo());
		ratingBar.setRating((float) 6.5);
		// ratingBar.setProgress(Integer.parseInt(info.getStarpoint()));
		// ratingBar.setStepSize(Float.parseFloat(info.getStarpoint()));
		IMAGE_CACHE.get(info.getImageUrl(), portrait);
		return convertView;
	}
}
