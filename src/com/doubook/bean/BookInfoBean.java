package com.doubook.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 联系人信息对象
 * 
 * @Copyright Copyright (c) 2012 - 2100
 * @Company 山东易网通信息科技有限公司
 * @author 虞贵涛
 * @create at 2013-11-04
 * @version 1.1.0
 */
public class BookInfoBean implements Serializable {

    private static final long serialVersionUID = 4047937294346060846L;
    private String id;
    private String name; // 书名
    private String imageUrl;// 图片地址
    private String bookinfo; // 书信息
    private String starpoint; // 评分
    private String EvaluateNum; // 评价数量

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBookinfo() {
        return bookinfo;
    }

    public void setBookinfo(String bookinfo) {
        this.bookinfo = bookinfo;
    }

    public String getStarpoint() {
        return starpoint;
    }

    public void setStarpoint(String starpoint) {
        this.starpoint = starpoint;
    }

    public String getEvaluateNum() {
        return EvaluateNum;
    }

    public void setEvaluateNum(String evaluateNum) {
        EvaluateNum = evaluateNum;
    }

}
