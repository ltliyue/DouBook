package com.doubook.util;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.doubook.bean.BookInfoBean;

/**
 * �������ݽ���
 * 
 * @Copyright Copyright (c) 2012 - 2100
 * @author Administrator
 * @create at 2014��7��22��
 * @version 1.1.0
 */
public class JsoupGetInfo_NewBook {
	int count = 0;
	int start;
	int end;

	public ArrayList<BookInfoBean> getinfo(String url, int mark) {
		ArrayList<BookInfoBean> contacters = new ArrayList<BookInfoBean>();
		try {
			if (mark == 0) {
				start = 0;
				end = 25;
			} else {
				start = 25;
				end = 50;
			}
			Document doc = Jsoup.connect(url).get();

			Elements info_main = doc.select("ul.clearfix");

			Elements infoElements = info_main.select("li");
			for (int j = start; j < end; j++) {
				count++;
				if (count == 5) {
					count = 0;
					continue;
				}
				Element weatherTab = infoElements.get(j);
				BookInfoBean bookInfoBean = new BookInfoBean();

				String name = weatherTab.select("h2").text();// shuming
				String imgSrc = weatherTab.select("img").attr("src");// ͼƬ
				String info = weatherTab.select("p.color-gray").text();// xinxi
				String pl = weatherTab.select("p").get(1).text();// xinxi;
				String linkUrl = weatherTab.select("a").attr("href");// shuming

				bookInfoBean.setName(name);
				bookInfoBean.setImageUrl(imgSrc);
				bookInfoBean.setLinkUrl(linkUrl);
				bookInfoBean.setEvaluateNum(pl);
				bookInfoBean.setBookinfo(info);

				contacters.add(bookInfoBean);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contacters;
	}
}
