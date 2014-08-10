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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import cn.trinea.android.common.view.DropDownListView;
import cn.trinea.android.common.view.DropDownListView.OnDropDownListener;

import com.doubook.adapter.ContactListAdapter;
import com.doubook.bean.BookInfoBean;
import com.doubook.data.ContextData;
import com.doubook.util.JsoupSearchGetInfo;
import com.meyao.book.Zxing.CaptureActivity;

public class SearchActivity extends Activity {

    public static String text = "";
    private TextView txt_cancel;
    private ImageView image_sao;
    private EditText search_edittext;
    private DropDownListView contactList = null;
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
        // TODO Auto-generated method stub
        contactList = (DropDownListView) findViewById(R.id.list_search_contact);
        text = getIntent().getStringExtra("text");
        txt_cancel = (TextView) findViewById(R.id.txt_cancel);
        image_sao = (ImageView) findViewById(R.id.image_sao);
        search_edittext = (EditText) findViewById(R.id.search_edittext);
    }

    private void initListener() {
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
        txt_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
                text = v.getText().toString();
                findBookInfo();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        System.out.println("search..............");
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

}
