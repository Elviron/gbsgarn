package com.ilves.gbsgarn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotesFragment extends Fragment implements FragmentInterface {

	public NotesFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
		return rootView;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return getActivity().getString(R.string.title_notes);
	}

}
