package com.doubook;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.util.CacheManager;

public class BookInfoActivity extends Activity {
    public static final ImageCache IMAGE_CACHE = CacheManager.getImageCache();
    private ImageView image;
    private TextView Title, name, point, infos, txt_intro_jianjie, txt_intro_writer;
    private ImageView btn_back;
    private RatingBar ratingBar;
    private ProgressBar loading;
    private LinearLayout lin_context_jianjie, lin_context_writer;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private String linkUrl, namesString, imagesString, isbnString, ratingString, collectionString, txt_introString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_info);
        loading = (ProgressBar) findViewById(R.id.loading_book);
        linkUrl = getIntent().getStringExtra("linkUrl");
        loading.setVisibility(View.VISIBLE);
        getUserinfo();
        initView();
        initListener();
    }

    public void initView() {

        Title = (TextView) findViewById(R.id.Title);
        Title.setText("图书信息");
        image = (ImageView) findViewById(R.id.image);
        name = (TextView) findViewById(R.id.name);
        point = (TextView) findViewById(R.id.point);
        infos = (TextView) findViewById(R.id.infos);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        txt_intro_jianjie = (TextView) findViewById(R.id.txt_intro_jianjie);
        txt_intro_writer = (TextView) findViewById(R.id.txt_intro_writer);
        lin_context_jianjie = (LinearLayout) findViewById(R.id.lin_context_jianjie);
        lin_context_writer = (LinearLayout) findViewById(R.id.lin_context_writer);
    }

    private void initListener() {
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getUserinfo() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(linkUrl).get();
                    Elements info_main = doc.select("div.subject");
                    Element weatherTab = info_main.first();
                    Elements rating = doc.select("div.rating_wrap");

                    Elements intro = doc.select("div.intro");
                    for (int i = 0; i < intro.size(); i++) {
                        if (intro.get(i).html().indexOf("(展开全部)") != -1) {
                        } else {
                            arrayList.add(intro.get(i).html());
                        }
                    }

                    namesString = info_main.select("a").attr("title");// shuming
                    imagesString = weatherTab.select("img").attr("src");// ͼƬ
                    if (imagesString == null && namesString != null) {
                        handler.sendEmptyMessage(1);
                    }
                    isbnString = weatherTab.getElementById("info").html().replace("href", "src");

                    ratingString = rating.select("strong.rating_num").text();
                    collectionString = rating.select("p.font_normal").text();
                    if (isbnString == null) {
                        getUserinfo();
                    } else {
                        Message message = handler.obtainMessage(0);
                        handler.sendMessage(message);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 0) {

                loading.setVisibility(View.GONE);
                ratingBar.setVisibility(View.VISIBLE);
                lin_context_jianjie.setVisibility(View.VISIBLE);
                if (arrayList.size() >= 2) {
                    lin_context_writer.setVisibility(View.VISIBLE);
                    txt_intro_writer.setText(Html.fromHtml(arrayList.get(1)));
                }
                name.setText(namesString);
                IMAGE_CACHE.get(imagesString, image);
                if (ratingString != null && !ratingString.equalsIgnoreCase("")) {
                    ratingBar.setRating(Float.parseFloat(ratingString) / 2);
                }
                point.setText(ratingString + collectionString);
                infos.setText(Html.fromHtml(isbnString));
                txt_intro_jianjie.setText(Html.fromHtml(arrayList.get(0)));
            }
            if (message.what == 1) {
                Toast.makeText(BookInfoActivity.this, "此书来自豆瓣阅读，无法查看详细信息", 1000).show();
            }
        }
    };
}
