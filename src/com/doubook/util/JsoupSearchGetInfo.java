package com.doubook.util;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.doubook.bean.BookInfoBean;

/**
 * ËÑË÷ÐÅÏ¢½âÎö
 * 
 * @author Meyao
 */
public class JsoupSearchGetInfo {

    public ArrayList<BookInfoBean> getinfo(String url, String text) {
        ArrayList<BookInfoBean> contacters = new ArrayList<BookInfoBean>();
        url = url + text;
        try {
            Document doc = Jsoup.connect(url).get();

            Elements infoElements = doc.select("li.subject-item");
            for (int i = 0; i < infoElements.size(); i++) {
                BookInfoBean bookInfoBean = new BookInfoBean();
                Element weatherTab = infoElements.get(i);
                String name = weatherTab.select("a").attr("title");// shuming
                String imgSrc = weatherTab.select("img").attr("src");// Í¼Æ¬
                String linkUrl = weatherTab.select("a").attr("nbg");
                String info = weatherTab.select("div.pub").text();// xinxi
                String rating_nums = weatherTab.select("span.rating_nums").text(); // pingfen
                String pl = weatherTab.select("span.pl").text();//
                // String pricesw =
                // weatherTab.select("a").get(2).attr("href");// shuming
                // System.out.println(pricesw);
                // System.out.println(name + "____" + imgSrc + "____" + info +
                // "____" + rating_nums + "____" + pl);
                bookInfoBean.setName(name);
                bookInfoBean.setImageUrl(imgSrc);
                bookInfoBean.setLinkUrl(linkUrl);
                bookInfoBean.setStarpoint(rating_nums);
                bookInfoBean.setEvaluateNum(pl);
                bookInfoBean.setBookinfo(info);

                contacters.add(bookInfoBean);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contacters;
    }

}
