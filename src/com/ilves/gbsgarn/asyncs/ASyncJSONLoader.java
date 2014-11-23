package com.ilves.gbsgarn.asyncs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ilves.gbsgarn.GbsGarnActivity;
import com.ilves.gbsgarn.adapters.GbsAdapter;
import com.ilves.gbsgarn.types.GbsFbPost;
import com.ilves.gbsgarn.types.GbsInfo;
import com.ilves.gbsgarn.utils.GlobalValues;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

public class ASyncJSONLoader extends AsyncTask<String, Void, Object> {

	private GbsGarnActivity activity;
	private GbsAdapter adapter;
	private boolean type;
	private String pagingNext;
	private int width;

	public ASyncJSONLoader(GbsAdapter gbsAdapter, boolean t, int w) {
		adapter = gbsAdapter;
		type = t;
		width = w;
	}

	public ASyncJSONLoader(GbsGarnActivity gbsGarnActivity, boolean t) {
		activity = gbsGarnActivity;
		type = t;
	}

	@Override
	protected Object doInBackground(String... arg0) {
		String fromServer = readFromServer(arg0[0]);
		if (type) { // INFO
			GbsInfo info = null;
			try {
				JSONObject jsonobj = new JSONObject(fromServer);
				info = new GbsInfo(activity, jsonobj);
			} catch (JSONException e) {
				Log.e(GlobalValues.logTag, "ASync Info: "+e.getMessage());
				e.printStackTrace();
			}
			return info;
		} else { // FEED
			List<GbsFbPost> fbPostList = new ArrayList<GbsFbPost>();
			try {
				// get data fields of feed
				JSONObject jsonObject = new JSONObject(fromServer);
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					GbsFbPost post = new GbsFbPost(adapter, jsonArray.getJSONObject(i), width);
					// check if it is a photo, then add
					if (post.isNotEmpty()) {
						fbPostList.add(post);
					}
				}
				// Now get paging link
				JSONObject paging = jsonObject.getJSONObject("paging");
				pagingNext = paging.getString("next");
				//Log.i(GlobalValues.logTag, "Paging next: "+pagingNext);
			} catch (JSONException e) {
				Log.e(GlobalValues.logTag, "ASync Feed: "+e.getMessage());
				e.printStackTrace();
			}
			return fbPostList;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecute(Object result) {
		if (type) { // info loaded
			activity.loadedINFO((GbsInfo)result);
		} else { // feed loaded
			adapter.loadedJSON(new Pair<List<GbsFbPost>, String>((List<GbsFbPost>) result, pagingNext));
		}

	}

	public String readFromServer(String url) {
		//Log.i(GlobalValues.logTag, "readFromServer start");
		StringBuilder builder = new StringBuilder();
		// http://graph.facebook.com/115442778477005
		try {
			URL httpurl = new URL(url);
			URLConnection conn = httpurl.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) { 
				builder.append(line);
			}
		} catch (MalformedURLException e) {
			Log.e(GlobalValues.logTag, "readFromServer, MalformedURLException:\n"+e.getMessage());
			e.printStackTrace();
		}catch (IOException e) {
			Log.e(GlobalValues.logTag, "readFromServer, IOException:\n"+e.getMessage());
			e.printStackTrace();
		}
		//Log.i(GlobalValues.logTag, "Read: "+builder.toString());
		return builder.toString();
	}
}
