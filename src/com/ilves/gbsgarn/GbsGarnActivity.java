package com.ilves.gbsgarn;

import java.util.ArrayList;

import com.ilves.gbsgarn.adapters.GbsAdapter;
import com.ilves.gbsgarn.adapters.GbsArrayAdapter;
import com.ilves.gbsgarn.asyncs.ASyncJSONLoader;
import com.ilves.gbsgarn.types.GbsFbPost;
import com.ilves.gbsgarn.types.GbsInfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class GbsGarnActivity extends ActionBarActivity {

	private static final String	TAG	= GbsGarnActivity.class.getSimpleName();;
    protected boolean mDebugLog = true;
    
	public GbsArrayAdapter adapter;
	private ListView listView;
	//private Session session;
	private GbsInfo info;

    private RecyclerView mRecyclerView;
    private GbsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//listView = (ListView)findViewById(R.id.listview);
		//listView.setSelector(android.R.color.transparent);
		//session = new Session(this);
		// start Facebook Login
		//Session.openActiveSession(this, true, new MyStatusCallback());
		getSupportActionBar().setTitle(getString(R.string.name));
		//getSupportActionBar().setSubtitle(getString(R.string.name));
		getSupportActionBar().setHomeButtonEnabled(true);
		//Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		//toolbar.setTitle("Öppet");
		
		mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
		// use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new GbsAdapter(this, new ArrayList<GbsFbPost>());
        mRecyclerView.setAdapter(mAdapter);
	}

	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	

	public GbsGarnActivity() {
		ASyncJSONLoader jsonLoaderInfo = new ASyncJSONLoader(this, GlobalValues.json_loader_info);
		jsonLoaderInfo.execute(new String[]{GlobalValues.url_gbs_info});
		Log.d(GlobalValues.logTag, "Started jsonloader info");
	}

	public void loadedINFO(GbsInfo result) {
		debugLog("loadedINFO");
		// update info in db or prefs
		info = result;
		mAdapter.setInfo(info);
	}

	public void debugLog(String message) {
		if (mDebugLog) {
			Log.i(TAG, message);
		}
	}
}
