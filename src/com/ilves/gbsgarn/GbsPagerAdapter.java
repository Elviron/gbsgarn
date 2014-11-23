package com.ilves.gbsgarn;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class GbsPagerAdapter extends FragmentPagerAdapter {

	private FeedFragment ff;
	private NotesFragment nf;

	public GbsPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return ff;
		case 1:
			return nf;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return ff.getTitle();
		case 1:
			return nf.getTitle();
		}
		return null;
	}

}
