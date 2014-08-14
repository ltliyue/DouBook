package com.doubook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * /登陆界面
 */
public class FirstActivity extends Activity {
    private TextView btn_login, btn_goin;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        initView();
        initListener();
    }

    private void initView() {
        btn_login = (TextView) findViewById(R.id.btn_login);
        btn_goin = (TextView) findViewById(R.id.btn_goin);
    }

    private void initListener() {
        btn_goin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mIntent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(mIntent);
            }
        });
        btn_login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mIntent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(mIntent);
            }
        });
    }
}
