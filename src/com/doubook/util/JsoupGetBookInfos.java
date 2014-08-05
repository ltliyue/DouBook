package com.doubook.util;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.doubook.bean.BookInfoBean;

/**
 * 书籍详情
 * 
 * @Copyright Copyright (c) 2012 - 2100
 * @author Administrator
 * @create at 2014年7月22日
 * @version 1.1.0
 */
public class JsoupGetBookInfos {

    public ArrayList<BookInfoBean> getinfo(String url) {
        ArrayList<BookInfoBean> contacters = new ArrayList<BookInfoBean>();

        try {
            Document doc = Jsoup.connect(url).get();

            Elements infoElements = doc.select("li.clearfix");
            for (int i = 0; i < infoElements.size(); i++) {
                BookInfoBean bookInfoBean = new BookInfoBean();
                Element weatherTab = infoElements.get(i);
                String name = weatherTab.select("a.fleft").text();// shuming
                String imgSrc = weatherTab.select("img").attr("src");// 图片
                String info = weatherTab.select("p.color-gray").text();// xinxi
                String rating_nums = weatherTab.select("span.font-small").text(); // pingfen
                String pl = weatherTab.select("span.fleft").text();//
                // String pricesw =
                // weatherTab.select("a").get(2).attr("href");// shuming
                // System.out.println(pricesw);
                // System.out.println(name + "____" + imgSrc + "____" + info +
                // "____" + rating_nums + "____" + pl);
                bookInfoBean.setName(name);
                bookInfoBean.setImageUrl(imgSrc);
                bookInfoBean.setStarpoint(rating_nums);
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
