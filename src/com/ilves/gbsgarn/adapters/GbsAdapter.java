package com.ilves.gbsgarn.adapters;

import java.util.ArrayList;
import java.util.List;

import com.ilves.gbsgarn.R;
import com.ilves.gbsgarn.R.id;
import com.ilves.gbsgarn.R.layout;
import com.ilves.gbsgarn.R.string;
import com.ilves.gbsgarn.asyncs.ASyncArrayUpdater;
import com.ilves.gbsgarn.asyncs.ASyncImageLoader;
import com.ilves.gbsgarn.asyncs.ASyncJSONLoader;
import com.ilves.gbsgarn.types.GbsFbPost;
import com.ilves.gbsgarn.types.GbsInfo;
import com.ilves.gbsgarn.utils.GlobalValues;
import com.ilves.gbsgarn.views.ResizableImageView;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GbsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private String[]		mDataset;

	private Context			mContext;
	private List<GbsFbPost>	mList;
	private GbsInfo			mInfo		= null;
	private String			url;
	private boolean			notLoading	= true;
	private int				width		= 0;

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class ViewHolderInfo extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public View					mParent;
		public TextView				mTextView;
		public TextView				closedView;
		public TextView				openView;
		public TextView				todayView;
		public TextView				tomorrowView;

		public ViewHolderInfo(View v) {
			super(v);
			mParent = v;
			// set open/closed sign
			closedView = (TextView) v.findViewById(R.id.tv_closed);
			openView = (TextView) v.findViewById(R.id.tv_open);
			// set new open hours (text)
			todayView = (TextView)v.findViewById(R.id.tv_today_hours);
			tomorrowView = (TextView)v.findViewById(R.id.tv_tomorrow_hours);
		}
	}
	public static class ViewHolderItem extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public View					mParent;
		public TextView				mTextView;
		public TextView				title;
		public TextView				content;
		public TextView				date;
		public ResizableImageView	image;

		public ViewHolderItem(View v) {
			super(v);
			mParent = v;
			title = (TextView) v.findViewById(R.id.row_title);
			image = (ResizableImageView) v.findViewById(R.id.row_image);
			date = (TextView) v.findViewById(R.id.row_date);
		}
	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public GbsAdapter() {
		mList = new ArrayList<GbsFbPost>();
	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public GbsAdapter(String[] myDataset) {
		mDataset = myDataset;
	}

	public GbsAdapter(Context mContext, List<GbsFbPost> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
		Log.d(GlobalValues.logTag, "width: " + width);
		// start new async to fetch the first posts
		// ASyncJSONLoader jsonLoaderInfo = new ASyncJSONLoader(this,
		// GlobalValues.json_loader_info);
		// jsonLoaderInfo.execute(new String[]{GlobalValues.url_gbs_info});
		Log.d(GlobalValues.logTag, "width: "+width);
		Log.d(GlobalValues.logTag, "Started jsonloader info");
		// ASyncJSONLoader jsonLoaderFeed = new ASyncJSONLoader(this,
		// GlobalValues.json_loader_feed, width);
		// jsonLoaderFeed.execute(new String[]{GlobalValues.url_gbs_feed});
		// Log.d(GlobalValues.logTag, "Started jsonloader feed");
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return (mInfo == null ? 0 : 1) + mList.size();// mList.size();
	}

	@Override
	public int getItemViewType(int position) {
		return (position == 0 ? 0 : 1);
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		switch (position) {
		case 0:
			ViewHolderInfo holderInfo = (ViewHolderInfo) holder;
			// set open/closed sign
			holderInfo.closedView.setVisibility(mInfo.closedView);
			holderInfo.openView.setVisibility(mInfo.openView);
			// set new open hours (text)
			holderInfo.todayView.setText(mInfo.getToday());
			holderInfo.tomorrowView.setText(mInfo.getTomorrow());
			break;
		default:
			GbsFbPost post = mList.get(position-1);
			ViewHolderItem holderItem = (ViewHolderItem) holder;

			if (post.hasTitle()) {
				holderItem.title.setVisibility(View.VISIBLE);
				holderItem.title.setText((post.getTitle()));
			} else {
				holderItem.title.setVisibility(View.GONE);
			}
			holderItem.date.setText(post.getDate());
			setWidthForImages(holderItem.image.getWidth());
			if (post.hasBitmap()) {
				holderItem.image.setImageURI(Uri.fromFile(post.getFile()));
				//ASyncImageViewLoader imageLoader = new ASyncImageViewLoader(holder.image);
				//imageLoader.execute(new File[]{s.getFile()});
				holderItem.image.setImageBitmap(post.loadBitmap());
			}
			break;
		}
	}

	private void setWidthForImages(int width) {
		// TODO Auto-generated method stub
		if (this.width == 0 && width != 0) {
			Log.i(GlobalValues.logTag, "setWidthForImages DONE: "+width);
			this.width = width;
			ASyncImageLoader a = new ASyncImageLoader(this, mList, width);
			a.execute();
		}
	}

	// Create new views (invoked by the layout manager)
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		ViewHolder vh = null;
		View v;
		switch (viewType) {
		case 0:
			// create a new view
			v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_info,
					parent,
					false);
			// set the view's size, margins, paddings and layout parameters
			vh = new ViewHolderInfo(v);
			break;
		case 1:
			// create a new view
			v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_row,
					parent,
					false);
			// set the view's size, margins, paddings and layout parameters
			vh = new ViewHolderItem(v);
			break;

		default:
			break;
		}
		return vh;
	}

	@SuppressWarnings("unchecked")
	public void loadedJSON(Pair<List<GbsFbPost>, String> pair) {
		url = pair.second;
		ASyncArrayUpdater updater = new ASyncArrayUpdater(this);
		updater.execute(new Pair<List<GbsFbPost>, List<GbsFbPost>>(mList, pair.first));
	}

	public String getActivityString(int id) {
		return mContext.getString(id);
	}

	public void setNewArray(List<GbsFbPost> jsonlist) {
		mList = jsonlist;
		this.notifyDataSetChanged();
		notLoading = true;
	}

	public void setInfo(GbsInfo info) {
		Log.i(GlobalValues.logTag, "setInfo");
		mInfo = info;
		this.notifyDataSetChanged();
		// fetch feed
		Log.i(GlobalValues.logTag, "Load feed.");
		ASyncJSONLoader jsonLoaderFeed = new ASyncJSONLoader(this, GlobalValues.json_loader_feed, width);
		jsonLoaderFeed.execute(new String[]{GlobalValues.url_gbs_feed+mContext.getString(R.string.fb_token)});
	}

	public void update() {
		this.notifyDataSetChanged();
	}

}
