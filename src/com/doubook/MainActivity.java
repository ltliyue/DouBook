package com.doubook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.util.CacheManager;
import cn.trinea.android.common.util.HttpUtils;
import cn.trinea.android.common.util.JSONUtils;

import com.doubook.data.ContextData;
import com.doubook.fragment.New1Fragment;
import com.doubook.fragment.New2Fragment;
import com.doubook.fragment.Top1Fragment;
import com.doubook.fragment.Top2Fragment;
import com.doubook.widget.MySlideMenu;
import com.doubook.widget.SearchPopupWindow;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends FragmentActivity implements OnClickListener {
    public static final ImageCache IMAGE_CACHE = CacheManager.getImageCache();
    // 双向滑动菜单布局
    private MySlideMenu bidirSldingLayout;
    private FrameLayout frameLayout_content;
    private Button btn_exit, btn_login;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int selected = -1;
    private ProgressBar loading;
    private ImageView btn_back, user_photo;
    private TextView Title, username, desc, txt_wish, txt_reading, txt_read;
    private SearchPopupWindow mSearchPopupWindow;
    private ImageView btn_search;
    // 用于展示消息的Fragment
    private Top1Fragment top1Fragment;
    // *用于展示联系人的Fragment
    private Top2Fragment top2Fragment;
    // ** 用于展示动态的Fragment
    private New1Fragment new1Fragment;
    // **用于展示设置的Fragment
    private New2Fragment new2Fragment;
    // **消息界面布局
    private View top1Layout;
    // **联系人界面布局
    private View top2Layout;
    // **动态界面布局
    private View new1Layout;
    // **设置界面布局
    private View new2Layout;
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
    // ** 用于对Fragment进行管理
    private FragmentManager fragmentManager;
    private String access_token, douban_user_id;
    private String name, large_avatar, descString;
    int count = 0;
    private int wish, read, reading;
    private boolean started = false;
    private boolean isFirst = true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    IMAGE_CACHE.get(large_avatar, user_photo);
                    username.setText(name);
                    desc.setText(descString);
                    txt_read.setText("读过的书（" + read + "）");
                    txt_reading.setText("在读的书（" + reading + "）");
                    txt_wish.setText("想读的书（" + wish + "）");
                    btn_exit.setVisibility(View.VISIBLE);
                    btn_login.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        UmengUpdateAgent.update(this);
        // 初始化布局元素
        initViews();
        initListener();
        fragmentManager = getSupportFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);
        if (!started) {
            getUserInfo();
            started = true;
        }
    }

    private void getUserInfo() {
        sharedPreferences = getSharedPreferences("AccessToken", 0);
        editor = sharedPreferences.edit();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 4) {
                    if (!sharedPreferences.getString("access_token", "").equalsIgnoreCase("")) {
                        access_token = sharedPreferences.getString("access_token", "");
                        douban_user_id = sharedPreferences.getString("douban_user_id", "");

                        String userInfo = HttpUtils.httpGetString(ContextData.GetUserInfo + douban_user_id
                            + "?Authorization=" + access_token);
                        String bookSaveUrl = ContextData.UserBookSave + douban_user_id + "/collections";

                        name = JSONUtils.getString(userInfo, "name", "");
                        large_avatar = JSONUtils.getString(userInfo, "large_avatar", "");
                        descString = JSONUtils.getString(userInfo, "desc", "");

                        wish = JSONUtils.getInt(HttpUtils.httpGetString(bookSaveUrl + "?status=wish"), "total", 0);
                        read = JSONUtils.getInt(HttpUtils.httpGetString(bookSaveUrl + "?status=read"), "total", 0);
                        reading = JSONUtils.getInt(HttpUtils.httpGetString(bookSaveUrl + "?status=reading"), "total", 0);

                        mHandler.sendEmptyMessage(0);
                        break;
                    } else {
                        try {
                            count++;
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void initViews() {
        frameLayout_content = (FrameLayout) findViewById(R.id.content);
        bidirSldingLayout = (MySlideMenu) findViewById(R.id.bidir_sliding_layout);
        bidirSldingLayout.setScrollEvent(frameLayout_content);
        Title = (TextView) findViewById(R.id.Title);
        loading = (ProgressBar) findViewById(R.id.loading);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_search = (ImageView) findViewById(R.id.btn_search);

        top1Layout = findViewById(R.id.message_layout);
        top2Layout = findViewById(R.id.contacts_layout);
        new1Layout = findViewById(R.id.news_layout);
        new2Layout = findViewById(R.id.setting_layout);

        messageImage = (ImageView) findViewById(R.id.message_image);
        contactsImage = (ImageView) findViewById(R.id.contacts_image);
        newsImage = (ImageView) findViewById(R.id.news_image);
        settingImage = (ImageView) findViewById(R.id.setting_image);

        messageText = (TextView) findViewById(R.id.message_text);
        contactsText = (TextView) findViewById(R.id.contacts_text);
        newsText = (TextView) findViewById(R.id.news_text);
        settingText = (TextView) findViewById(R.id.setting_text);

        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_login = (Button) findViewById(R.id.btn_login);
        user_photo = (ImageView) findViewById(R.id.user_photo);
        username = (TextView) findViewById(R.id.username);
        desc = (TextView) findViewById(R.id.desc);
        txt_wish = (TextView) findViewById(R.id.txt_wish);
        txt_reading = (TextView) findViewById(R.id.txt_reading);
        txt_read = (TextView) findViewById(R.id.txt_read);

        top1Layout.setOnClickListener(this);
        top2Layout.setOnClickListener(this);
        new1Layout.setOnClickListener(this);
        new2Layout.setOnClickListener(this);

    }

    private void initListener() {
        btn_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchPopupWindow = new SearchPopupWindow(MainActivity.this, null);
                mSearchPopupWindow.showAsDropDown(MainActivity.this.findViewById(R.id.divider_view));
            }
        });

        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (bidirSldingLayout.isLeftLayoutVisible()) {
                    bidirSldingLayout.scrollToContentFromLeftMenu();
                } else {
                    bidirSldingLayout.scrollToLeftMenu();
                }
            }
        });
        btn_exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                editor.putString("access_token", "").commit();
                editor.putString("refresh_token", "").commit();
                Intent mIntent = new Intent(MainActivity.this, FirstActivity.class);
                finish();
                startActivity(mIntent);
            }
        });
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent mIntent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(mIntent);
            }
        });
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
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String text = intent.getStringExtra("RESULT");
                Toast.makeText(this, text, ContextData.toastTime).show();
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
     *        每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
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
                Title.setText(R.string.top1);
                // 当点击了消息tab时，改变控件的图片和文字颜色
                messageImage.setImageResource(R.drawable.icon44);
                messageText.setTextColor(Color.parseColor("#7fb80e"));
                if (top1Fragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    top1Fragment = new Top1Fragment();
                    transaction.add(R.id.content, top1Fragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(top1Fragment);
                }
                break;
            case 1:
                Title.setText(R.string.top2);
                // 改变控件的图片和文字颜色
                contactsImage.setImageResource(R.drawable.icon33);
                contactsText.setTextColor(Color.parseColor("#7fb80e"));
                if (top2Fragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    top2Fragment = new Top2Fragment();
                    transaction.add(R.id.content, top2Fragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(top2Fragment);
                }
                break;
            case 2:
                // 改变控件的图片和文字颜色
                Title.setText(R.string.new1);
                newsImage.setImageResource(R.drawable.icon22);
                newsText.setTextColor(Color.parseColor("#7fb80e"));
                if (new1Fragment == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    new1Fragment = new New1Fragment();
                    transaction.add(R.id.content, new1Fragment);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    // transaction.replace(R.id.content, new1Fragment);
                    transaction.show(new1Fragment);
                }
                break;
            case 3:
            default:
                Title.setText(R.string.new2);
                // 改变控件的图片和文字颜色
                settingImage.setImageResource(R.drawable.icon11);
                settingText.setTextColor(Color.parseColor("#7fb80e"));
                if (new2Fragment == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    new2Fragment = new New2Fragment();
                    transaction.add(R.id.content, new2Fragment);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    // transaction.replace(R.id.content, new2Fragment);
                    transaction.show(new2Fragment);
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
     *        用于对Fragment执行操作的事务
     */
    private void removeFragments(FragmentTransaction transaction) {
        if (top1Fragment != null) {
            transaction.remove(top1Fragment);
        }
        if (top2Fragment != null) {
            transaction.remove(top2Fragment);
        }
        if (new1Fragment != null) {
            transaction.remove(new1Fragment);
        }
        if (new2Fragment != null) {
            transaction.remove(new2Fragment);
        }
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     * 
     * @param transaction
     *        用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (top1Fragment != null) {
            transaction.hide(top1Fragment);
        }
        if (top2Fragment != null) {
            transaction.hide(top2Fragment);
        }
        if (new1Fragment != null) {
            transaction.hide(new1Fragment);
        }
        if (new2Fragment != null) {
            transaction.hide(new2Fragment);
        }
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, float ratio) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, bitmap.getWidth() / ratio, bitmap.getHeight() / ratio, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
