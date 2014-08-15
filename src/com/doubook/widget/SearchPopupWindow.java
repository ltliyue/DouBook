package com.doubook.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.doubook.R;
import com.doubook.SearchActivity;
import com.meyao.book.Zxing.CaptureActivity;

public class SearchPopupWindow extends PopupWindow {
    private View mMenuView;
    private EditText search_edittext;
    private ImageView image_sao;
    private TextView txt_cancel;
    private final Activity mcontext;

    public SearchPopupWindow(Activity context, OnClickListener itemsOnClick) {
        super(context);
        this.mcontext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.search_popup_window, null);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 弹出PopupWindow
                                                                   // 显示软键盘
        inintView();
        inintListener();

        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.FILL_PARENT);
        this.setFocusable(true);

        // this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(0xb0000000);// 设置为半透明效果
        this.setBackgroundDrawable(dw);

    }

    private void inintView() {
        // TODO Auto-generated method stub
        search_edittext = (EditText) mMenuView.findViewById(R.id.search_edittext);
        txt_cancel = (TextView) mMenuView.findViewById(R.id.txt_cancel);
        image_sao = (ImageView) mMenuView.findViewById(R.id.image_sao);

    }

    private void inintListener() {
//        search_edittext.setOnKeyListener(new OnKeyListener() {// 监听键盘事件
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                        Toast.makeText(mcontext, "在这里执行搜索", Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//                    return false;
//                }
//            });
        search_edittext.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mcontext, SearchActivity.class);
                intent.putExtra("text", v.getText().toString());
                mcontext.startActivityForResult(intent, 0);
                dismiss();
                return true;
            }
        });

        image_sao.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(mcontext, CaptureActivity.class);
                mcontext.startActivityForResult(intent, 0);
                dismiss();
            }
        });
        txt_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        mMenuView.setOnTouchListener(new OnTouchListener() {// 搜索框下面区域
                // PopupWindow消失
                public boolean onTouch(View v, MotionEvent event) {
                    int height = mMenuView.findViewById(R.id.tel_popup_window_main).getBottom();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y > height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });
    }
}