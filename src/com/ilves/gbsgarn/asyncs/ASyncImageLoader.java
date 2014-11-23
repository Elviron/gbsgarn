package com.ilves.gbsgarn.asyncs;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ilves.gbsgarn.adapters.GbsAdapter;
import com.ilves.gbsgarn.types.GbsFbPost;
import com.ilves.gbsgarn.utils.GlobalValues;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;

public class ASyncImageLoader extends AsyncTask<String, Void, Void> {

	private GbsAdapter parent;
	private List<GbsFbPost> mList;
	private int mWidth;

	public ASyncImageLoader(GbsAdapter context, List<GbsFbPost> list, int width) {
		parent = context;
		mList = list;
		mWidth = width;
	}
	@Override
	protected Void doInBackground(String... arg0) {
		Bitmap bmp = null;
		//URL url = new URL(arg0[0]);
		Log.i(GlobalValues.logTag, "Size: "+mList.size());
		
		for (int i = 0; i < mList.size(); i++) {
			String fromServer = readFromServer(mList.get(i).getUrl());
			String link = null;
			try {
				JSONObject jsonobj = new JSONObject(fromServer);
				JSONArray list = jsonobj.getJSONArray("images");
				JSONObject obj;
				for (int j = 0; j < list.length(); j++) {
					obj = list.getJSONObject(j);
					if (mWidth > obj.getInt("width")) {
						Log.i(GlobalValues.logTag, obj.getString("source"));
						link = obj.getString("source");
						break;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bmp = decodeSampledBitmapFromStream(link, mWidth);
			//Log.i(GlobalValues.logTag, "Bytes: "+bmp.getAllocationByteCount());
			mList.get(i).finishedLoadImage(bmp);
			bmp = null;
			// update view
			this.publishProgress();
		}
		//imageView.setImageBitmap(bmp);
		return null;
	}
	@Override
	protected void onProgressUpdate(Void... values) {
		//parent.update();
		Log.i(GlobalValues.logTag, "onProgressUpdate");
		super.onProgressUpdate(values);
	}
	@Override
	protected void onPostExecute(Void result) {
		parent.update();
		super.onPostExecute(result);
	}

	public static Bitmap decodeSampledBitmapFromStream(String url, int reqWidth) {
		Log.i(GlobalValues.logTag, "decodeSampledBitmapFromStream: "+url);
		Bitmap ret = null;
		Bitmap myBitmap = null;
		Rect outp = new Rect(-1,-1,-1,-1);

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		try {
			URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return myBitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
		// Raw height and width of image
		//final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			//final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = widthRatio;//heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	static class FlushedInputStream extends FilterInputStream {
		
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break;  // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
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
