package com.ilves.gbsgarn;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Pair;

public class ASyndArrayUpdater extends AsyncTask<Pair<List<GbsFbPost>,List<GbsFbPost>>, Void, List<GbsFbPost>> {

	private GbsArrayAdapter parent;
	private List<GbsFbPost> mList;

	public ASyndArrayUpdater(GbsArrayAdapter context) {
		parent = context;
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
