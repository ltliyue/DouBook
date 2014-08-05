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

public class New2Fragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newsLayout = inflater.inflate(R.layout.setting_layout, container, false);
        return newsLayout;
    }

}
