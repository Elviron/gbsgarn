package com.ilves.gbsgarn;

public interface GlobalValues {

	public static String logTag = "GbsGarnActivity";
	public static String url_gbs_info = "https://graph.facebook.com/115442778477005";
	public static String url_gbs_feed = "https://graph.facebook.com/115442778477005/feed?fields=id,created_time,type,message,picture,full_picture,object_id&access_token=";
	// https://graph.facebook.com/115442778477005/feed?fields=id,created_time,type,message,picture&access_token=424240934363823|IzmKFSTX-mq0KBkJBIn3Z1X9ltk
	// https://developers.facebook.com/tools/explorer?method=GET&path=115442778477005%3Ffields%3Dfeed.fields(id%2Ccreated_time%2Ctype%2Cmessage%2Cpicture)
	// https://www.facebook.com/feeds/page.php?id=115442778477005&format=json
	public static boolean json_loader_info = true;
	public static boolean json_loader_feed = false;

}
