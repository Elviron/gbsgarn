package com.ilves.gbsgarn.asyncs;

import java.util.ArrayList;
import java.util.List;

import com.ilves.gbsgarn.adapters.GbsAdapter;
import com.ilves.gbsgarn.types.GbsFbPost;

import android.os.AsyncTask;
import android.util.Pair;

public class ASyncArrayUpdater extends AsyncTask<Pair<List<GbsFbPost>,List<GbsFbPost>>, Void, List<GbsFbPost>> {

	private GbsAdapter parent;
	private List<GbsFbPost> mList;

	public ASyncArrayUpdater(GbsAdapter gbsAdapter) {
		parent = gbsAdapter;
	}
	@Override
	protected List<GbsFbPost> doInBackground(Pair<List<GbsFbPost>,List<GbsFbPost>>... arg0) {
		// TODO Auto-generated method stub
		mList = new ArrayList<GbsFbPost>(arg0[0].first);
		mList.addAll(arg0[0].second);
		return mList;
	}

	@Override
	protected void onPostExecute(List<GbsFbPost> result) {
		// TODO Auto-generated method stub
		parent.setNewArray(result);
		super.onPostExecute(result);
	}
}
