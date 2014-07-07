package com.ilves.gbsgarn;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class GbsGarnActivity extends Activity {

	private static final String	TAG	= "GbsGarnActivity";
    protected boolean mDebugLog = false;
    
	public GbsArrayAdapter adapter;
	private ListView listView;
	//private Session session;
	private GbsInfo info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView)findViewById(R.id.listview);
		listView.setSelector(android.R.color.transparent); 
		//session = new Session(this);
		// start Facebook Login
		//Session.openActiveSession(this, true, new MyStatusCallback());
	}

	/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	 */

	public GbsGarnActivity() {
		ASyncJSONLoader jsonLoaderInfo = new ASyncJSONLoader(this, GlobalValues.json_loader_info);
		jsonLoaderInfo.execute(new String[]{GlobalValues.url_gbs_info});
		Log.d(GlobalValues.logTag, "Started jsonloader info");
	}

	public void loadedINFO(GbsInfo result) {
		// update info in db or prefs
		info = result;
		// set open/closed sign
		((TextView)findViewById(R.id.tv_closed)).setVisibility(info.closedView);
		((TextView)findViewById(R.id.tv_open)).setVisibility(info.openView);
		// set new open hours (text)
		((TextView)findViewById(R.id.tv_today_hours)).setText(info.today);
		((TextView)findViewById(R.id.tv_tomorrow_hours)).setText(info.tomorrow);
		// get width (to get good size of pictures)
		int w = listView.getWidth();
		//Log.d(GlobalValues.logTag, "width2: "+w);
		// start fetching feed
		adapter = new GbsArrayAdapter(this, new ArrayList<GbsFbPost>(), w);
		listView.setAdapter(adapter);
	}

	/*
	private class MyStatusCallback implements Session.StatusCallback {

		public void call(Session arg0, SessionState arg1, Exception arg2) {
			// TODO Auto-generated method stub
			if (session.isOpened()) {
				Log.d(GlobalValues.logTag, "Session is open!");

				Request.executeGraphPathRequestAsync(session, "115442778477005/feed", new Callback() {

					public void onCompleted(Response arg0) {
						// TODO Auto-generated method stub
						Log.d(GlobalValues.logTag, arg0.toString());
					}
				});
			}
		}
	}
	 */

	public void debugLog(String message) {
		if (mDebugLog) {
			Log.i(TAG, message);
		}
	}
}
