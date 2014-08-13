package com.doubook;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.doubook.adapter.ContactListAdapter;
import com.doubook.bean.BookInfoBean;
import com.doubook.data.ContextData;
import com.doubook.util.JsoupSearchGetInfo;
import com.meyao.book.Zxing.CaptureActivity;
import com.umeng.analytics.MobclickAgent;

public class SearchActivity extends Activity {

    public static String text = "";
    private TextView txt_cancel;
    private ImageView image_sao;
    private EditText search_edittext;
    private ListView contactList = null;
    private ContactListAdapter dataAdapter = null;
    private ArrayList<BookInfoBean> contacters = new ArrayList<BookInfoBean>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            if (msg.what == 1) {
                dataAdapter = null;
                if (contacters.size() == 0) {
                    Toast.makeText(SearchActivity.this, "未找到该书籍", ContextData.toastTime).show();
                } else {
                    dataAdapter = new ContactListAdapter(SearchActivity.this);
                    dataAdapter.setData(contacters);
                    contactList.setAdapter(dataAdapter);
                }
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
        contactList = (ListView) findViewById(R.id.list_search_contact);
        text = getIntent().getStringExtra("text");
        txt_cancel = (TextView) findViewById(R.id.txt_cancel);
        image_sao = (ImageView) findViewById(R.id.image_sao);
        search_edittext = (EditText) findViewById(R.id.search_edittext);
    }

    private void initListener() {
        txt_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        image_sao.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        search_edittext.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                text = v.getText().toString();
                findBookInfo();
                return true;
            }
        });
        contactList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent mIntent = new Intent(SearchActivity.this, BookInfoActivity.class);
                mIntent.putExtra("linkUrl", contacters.get(position).getLinkUrl());
                startActivity(mIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                text = intent.getStringExtra("RESULT");
                Toast.makeText(this, text, ContextData.toastTime).show();
                findBookInfo();
            } else if (resultCode == RESULT_CANCELED) {
            }
        } else {
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        findBookInfo();
    }

    private void findBookInfo() {
        if (text != null) {
            new Thread() {
                @Override
                public void run() {
                    contacters = null;
                    JsoupSearchGetInfo jsoupSearchGetInfo = new JsoupSearchGetInfo();
                    contacters = jsoupSearchGetInfo.getinfo(ContextData.Search, text);
                    if (contacters == null) {
                        findBookInfo();
                    } else {
                        mHandler.sendEmptyMessage(1);
                    }
                }
            }.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen"); // 保证 onPageEnd 在onPause
        MobclickAgent.onPause(this);// 之前调用,因为 onPause 中会保存信息
    }
}
