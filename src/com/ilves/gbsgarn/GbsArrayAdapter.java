package com.ilves.gbsgarn;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 
 * @author Seb
 *
 */
public class GbsArrayAdapter extends ArrayAdapter<GbsFbPost> {

	private Context mContext;
	public List<GbsFbPost> mList;
	private String url;
	private boolean notLoading = true;
	private int width = 100;

	static class ViewHolder {
		public TextView title;
		public TextView content;
		public TextView date;
		public ResizableImageView image;
	}

	public GbsArrayAdapter(Context context, int resource) {
		super(context, resource);
	}

	public GbsArrayAdapter(Context context, List<GbsFbPost> objects, int w) {
		super(context, R.layout.list_item_row, objects);
		mContext = context;
		mList = objects;
		width = w;
		//LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View rowView = inflater.inflate(R.layout.list_item_row, null);
		//ResizableImageView imgv = (ResizableImageView) rowView.findViewById(R.id.row_image);
		//LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imgv.getWidth(); 
		//params.width = width;
		//imgv.setLayoutParams(params);
		//width = imgv.getWidth();
		Log.d(GlobalValues.logTag, "width: "+width);
		// start new async to fetch the first posts
		ASyncJSONLoader jsonLoaderFeed = new ASyncJSONLoader(this, GlobalValues.json_loader_feed, width);
		jsonLoaderFeed.execute(new String[]{GlobalValues.url_gbs_feed});
		Log.d(GlobalValues.logTag, "Started jsonloader feed");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_item_row, null);
			// create a viewholder to hold the views of the row
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.title = (TextView) rowView.findViewById(R.id.row_title);
			viewHolder.image = (ResizableImageView) rowView.findViewById(R.id.row_image);
			viewHolder.date = (TextView) rowView.findViewById(R.id.row_date);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		// get item to show
		// change to db
		GbsFbPost s = mList.get(position);

		if (s.hasTitle()) {
			holder.title.setVisibility(View.VISIBLE);
			holder.title.setText((s.getTitle()));
		} else {
			holder.title.setVisibility(View.GONE);
		}
		holder.date.setText(s.getDate());
		if (s.hasBitmap()) {
			//holder.image.setImageURI(Uri.fromFile(s.getFile()));
			//ASyncImageViewLoader imageLoader = new ASyncImageViewLoader(holder.image);
			//imageLoader.execute(new File[]{s.getFile()});
			holder.image.setImageBitmap(s.loadBitmap());
		}
		
		
		// if next last element, start new async to load more posts
		if (position >= getCount()-2 && notLoading) {
			// New async
			ASyncJSONLoader jsonLoaderFeed = new ASyncJSONLoader(this, GlobalValues.json_loader_feed, width);
			jsonLoaderFeed.execute(new String[]{url});
			Log.d(GlobalValues.logTag, "Started jsonloader feed update");
			notLoading = false;
		}
		return rowView;
	}
	
	public int getWidth() {
		return width;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	public void setNewArray(List<GbsFbPost> jsonlist) {
		mList = jsonlist;
		this.notifyDataSetChanged();
		notLoading = true;
	}

	@SuppressWarnings("unchecked")
	public void loadedJSON(Pair<List<GbsFbPost>, String> pair) {
		url = pair.second;
		ASyndArrayUpdater updater = new ASyndArrayUpdater(this);
		updater.execute(new Pair<List<GbsFbPost>,List<GbsFbPost>>(mList, pair.first));
	}
}
