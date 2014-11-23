package com.ilves.gbsgarn.asyncs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ilves.gbsgarn.types.GbsFbPost;
import com.ilves.gbsgarn.utils.GlobalValues;

import android.os.AsyncTask;
import android.util.Log;

public class ASyncImageUrlLoader extends AsyncTask<String, Void, String> {

	private GbsFbPost parent;
	private int width;

	public ASyncImageUrlLoader(GbsFbPost context, int w) {
		parent = context;
		width = w;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String fromServer = readFromServer(params[0]);
		String link = null;
		try {
			JSONObject jsonobj = new JSONObject(fromServer);
			JSONArray list = jsonobj.getJSONArray("images");
			JSONObject obj;
			for (int i = 0; i < list.length(); i++) {
				obj = list.getJSONObject(i);
				if (width > obj.getInt("width")) {
					Log.i(GlobalValues.logTag, obj.getString("source"));
					link = obj.getString("source");
					break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return link;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if (result != null) {
			Log.i(GlobalValues.logTag,result);
			parent.imageUrlLoaded(result);
		}
		super.onPostExecute(result);
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
			Log.e(GlobalValues.logTag, "ASyncImageUrlLoader readFromServer, MalformedURLException:\n"+e.getMessage());
			e.printStackTrace();
		}catch (IOException e) {
			Log.e(GlobalValues.logTag, "ASyncImageUrlLoader readFromServer, IOException:\n"+e.getMessage());
			e.printStackTrace();
		}
		//Log.i(GlobalValues.logTag, "Read: "+builder.toString());
		return builder.toString();
	}

}
