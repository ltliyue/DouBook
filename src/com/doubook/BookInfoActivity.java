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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.util.CacheManager;
import cn.trinea.android.common.util.HttpUtils;

import com.doubook.data.ContextData;

public class BookInfoActivity extends Activity {
	public static final ImageCache IMAGE_CACHE = CacheManager.getImageCache();
	public static final String TAG = "DianpuActivity";
	private LinearLayout lin_infos;
	private LayoutInflater myLayoutInflater;
	private View myView;
	private ImageView image;
	private TextView name, point, writer, pub, pubdate, pagenum, price, isbn;
	private String linkUrl, namesString, imagesString, isbnString;
	// ArrayList<String> arrayInfo;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_info);
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
		lin_infos = (LinearLayout) findViewById(R.id.lin_infos);
		point = (TextView) findViewById(R.id.point);
		myLayoutInflater = getLayoutInflater();
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

					// namesString = weatherTab.select("a").attr("title");//
					// shuming
					imagesString = weatherTab.select("img").attr("src");// ͼƬ

					String info = weatherTab.getElementById("info").text();
					String infos[] = new String[30];
					// arrayInfo = new ArrayList<String>();
					infos = info.split(" ");
					isbnString = infos[infos.length - 1];
					getUserinfo2();
					// for (int i = 0; i < infos.length; i = i + 2) {
					// if ((i + 1) < infos.length) {
					// arrayInfo.add(infos[i] + " " + infos[i + 1]);
					// }
					// }
				} catch (IOException e) {
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
				String result = HttpUtils.httpGetString(ContextData.bookinfo + isbnString + "?alt=json");
				try {
					System.out.println(result);
					JSONObject data1 = new JSONObject(result);
					JSONObject title = data1.getJSONObject("title");
					JSONObject attribute = data1.getJSONObject("db:attribute");

					String attributeString = attribute.getString("$t");
					System.out.println("111:" + attributeString);
					// JSONObject shop =
					// shop_get_response.getJSONObject("shop");
					// namesString, pointsString, writersString, pubsString,
					// pubdatesString, pagenumsString, pricesString,
					// isbnsString;
					namesString = title.getString("$t");
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
			// for (int i = 0; i < arrayInfo.size(); i++) {
			// myView =
			// myLayoutInflater.inflate(R.layout.activity_book_info_item, null);
			// TextView textView = (TextView)
			// myView.findViewById(R.id.txt_info);
			// textView.setText(arrayInfo.get(i));
			// lin_infos.addView(myView);
			// }
		}
	};

}
