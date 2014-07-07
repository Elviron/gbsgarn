package com.ilves.gbsgarn;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;

public class ASyncImageLoader extends AsyncTask<String, Void, Bitmap> {

	private GbsFbPost parent;
	private int width;

	public ASyncImageLoader(GbsFbPost context, int w) {
		parent = context;
		width = w;
	}
	@Override
	protected Bitmap doInBackground(String... arg0) {
		Bitmap bmp = null;
		try {
			URL url = new URL(arg0[0]);
			bmp = decodeSampledBitmapFromStream(url, width);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//imageView.setImageBitmap(bmp);
		// TODO Auto-generated method stub
		return bmp;
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		parent.finishedLoadImage(result);
		super.onPostExecute(result);
	}

	public static Bitmap decodeSampledBitmapFromStream(URL url, int reqWidth) {
		Bitmap ret = null;
		Rect outp = new Rect(-1,-1,-1,-1);

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		try {
			InputStream is = url.openConnection().getInputStream();
			BitmapFactory.decodeStream(is, outp, options);
			is.close();

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			is = url.openConnection().getInputStream();
			ret = BitmapFactory.decodeStream(is, outp, options);
			is.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ret;
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
}
