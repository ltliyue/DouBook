package com.doubook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import u.aly.v;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.util.CacheManager;
import cn.trinea.android.common.util.HttpUtils;

import com.doubook.data.ContextData;
import com.doubook.util.JsoupGetInfo;

public class BookInfoActivity extends Activity {
    public static final ImageCache IMAGE_CACHE = CacheManager.getImageCache();
    public static final String TAG = "DianpuActivity";
    private ImageView image;
    private TextView name, point, writer, pub, pubdate, pagenum, price, isbn;
    private String imagesString, namesString, pointsString, writersString, pubsString, pubdatesString, pagenumsString,
        pricesString, isbnsString;
    private String linkUrl;
    ArrayList<String> arrayInfo;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        sharedPreferences = getSharedPreferences("AccessToken", 0);
        editor = sharedPreferences.edit();
        linkUrl = getIntent().getStringExtra("linkUrl");
        initView();
        initListener();
        getUserinfo();
    }

    public void initView() {
        image = (ImageView) findViewById(R.id.image);
        name = (TextView) findViewById(R.id.name);
        point = (TextView) findViewById(R.id.point);
        writer = (TextView) findViewById(R.id.writer);
        pub = (TextView) findViewById(R.id.pub);
        pubdate = (TextView) findViewById(R.id.pubdate);
        pagenum = (TextView) findViewById(R.id.pagenum);
        price = (TextView) findViewById(R.id.price);
        isbn = (TextView) findViewById(R.id.isbn);
    }

    private void initListener() {
    }

    public void getUserinfo() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(linkUrl).get();

                    Elements info_main = doc.select("div.subject");

                    Element weatherTab = info_main.get(0);

                    namesString = weatherTab.select("a").attr("title");// shuming
                    imagesString = weatherTab.select("img").attr("src");// ͼƬ

                    String info = weatherTab.getElementById("info").text();
                    String infos[] = new String[15];
                    arrayInfo = new ArrayList<String>();
                    infos = info.split(" ");
                    for (int i = 0; i < infos.length; i = i + 2) {
                        arrayInfo.add(infos[i] + infos[i + 1]);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Message message = handler.obtainMessage(0);
                handler.sendMessage(message);
            }
        }.start();
    }

    public void getUserinfo2() {
        new Thread() {
            @Override
            public void run() {
                Map<String, String> parasMap = new HashMap<String, String>();
                parasMap.put("alt", "json");
                String result = HttpUtils.httpPostString(ContextData.bookinfo, parasMap);
                try {
                    JSONObject data1 = new JSONObject(result);
                    // JSONObject shop_get_response =
                    // data1.getJSONObject("shop_get_response");
                    // JSONObject shop =
                    // shop_get_response.getJSONObject("shop");
                    // namesString, pointsString, writersString, pubsString,
                    // pubdatesString, pagenumsString, pricesString,
                    // isbnsString;
                    namesString = data1.getString("title");
                    // bulletin = shop.getString("bulletin");
                    // desc = shop.getString("desc");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = handler.obtainMessage(0);
                handler.sendMessage(message);
            }
        }.start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            name.setText(namesString);
            IMAGE_CACHE.get(imagesString, image);
            writer.setText(arrayInfo.get(0));
            pub.setText(arrayInfo.get(1));
            pubdate.setText(arrayInfo.get(2));
            pagenum.setText(arrayInfo.get(3));
            price.setText(arrayInfo.get(4) + " [" + arrayInfo.get(5).split(":")[1] + "]");
            isbn.setText(arrayInfo.get(6));
        }
    };

}
