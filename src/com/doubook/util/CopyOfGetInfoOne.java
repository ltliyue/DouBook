package com.doubook.util;
import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CopyOfGetInfoOne {

    public static void main(String[] args) {

        String url = "http://book.douban.com/latest?icn=index-latestbook-all";
        try {
            Document doc = Jsoup.connect(url).get();

            Elements info_main = doc.select("ul.clearfix");
            for (int i = 0; i < info_main.size(); i++) {

                Elements infoElements = info_main.select("li");
                for (int j = 0; j < infoElements.size(); j++) {

                    if (j % 4 == 0) {
                        continue;
                    }
                    Element weatherTab = infoElements.get(j);

                    String name = weatherTab.select("h2").text();// shuming

                    String imgSrc = weatherTab.select("img").attr("src");// ͼƬ
                    String info = weatherTab.select("p.color-gray").text();// xinxi
                    String rating_nums = "";
                    String pl = "";
                    String pricesw = weatherTab.select("a").attr("href");// shuming
                    // System.out.println(pl + rating_nums);// ͼƬ);
                    System.out.println(name + "____" + imgSrc + "____" + info + "____" + rating_nums + "____" + pl
                        + "____" + pricesw);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
