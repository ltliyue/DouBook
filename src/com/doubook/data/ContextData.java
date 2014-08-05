package com.doubook.data;

public class ContextData {

	// 这是我的APP
	public static final String APIKey = "0998613904a8f79a1cc923ab0dfae6ce";
	public static final String Secret = "63324041aaf91126";
	public static final String redirect_uri = "http://book.douban.com";
	public static String access_token = "";

	/**
	 * 登陆
	 */
	// public static String LoginURL = "http://www.baidu.com";
	public static String LoginURL = "https://www.douban.com/service/auth2/auth?client_id=" + APIKey + "&redirect_uri=" + redirect_uri + "&response_type=code&scope=shuo_basic_r,shuo_basic_w";
	// public static String LoginURL =
	// "https://oauth.taobao.com/authorize?response_type=code&client_id=21599349&redirect_uri=http://www.oauth.net/2/&state=1212&scope=item&view=wap";

	/**
	 * 最受关注图书榜 非虚构类作品
	 */
	public static String best1 = "http://book.douban.com/chart?subcat=I";
	/**
	 * 最受关注图书榜 虚构类作品
	 */
	public static String best2 = "http://book.douban.com/chart?subcat=F";
	/**
	 * http://book.douban.com/top250
	 */
	public static String top250 = "http://book.douban.com/top250";
	/**
	 * search
	 */
	public static String Search = "http://book.douban.com/subject_search?search_text=";

	/**
	 * GetAccessToken
	 */
	public static String GetAccessToken = "https://www.douban.com/service/auth2/token";

	public static int toastTime = 2600;

}
