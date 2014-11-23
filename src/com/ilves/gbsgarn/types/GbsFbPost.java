package com.ilves.gbsgarn.types;

import java.io.File;
import java.io.FileOutputStream;

import org.json.JSONObject;

import com.ilves.gbsgarn.R;
import com.ilves.gbsgarn.R.string;
import com.ilves.gbsgarn.adapters.GbsAdapter;
import com.ilves.gbsgarn.asyncs.ASyncImageUrlLoader;
import com.ilves.gbsgarn.interfaces.ImageLoader;
import com.ilves.gbsgarn.utils.GlobalValues;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class GbsFbPost implements ImageLoader{

	//================================================================================
	// Properties
	//================================================================================

	private GbsAdapter parent;

	private String title, id, date, url = null, imgName = null;
	private File imgFile = null, myDir;

	private boolean isNotEmpty, hasTitle;
	private int width = 0;

	//================================================================================
	// Constructors
	//================================================================================

	public GbsFbPost(GbsAdapter adapter, JSONObject jsonobj, int w) {
		parent = adapter;
		width = w;
		//Log.i(GlobalValues.logTag, "Object: "+jsonobj.toString());
		try {
			if (jsonobj.getString("type").equals("photo")) {
				isNotEmpty = true;
				if (jsonobj.has("message")) {
					setTitle(jsonobj.getString("message"));
					hasTitle = true;
				} else {
					setTitle("");
					hasTitle = false;
				}
				setId(jsonobj.getString("id"));
				// make filepath
				imgName = "Image-"+ id +".png";
				myDir = new File(Environment.getExternalStorageDirectory().toString() + "/saved_images");    
				myDir.mkdirs();
				imgFile = new File (myDir, imgName);
				// Date
				setDate(jsonobj.getString("created_time").substring(0, 10));
				// Url for picture
				String u = jsonobj.getString("picture");
				//int i = u.length();
				// Change one letter in url to get big image
				//u = u.substring(0, i-5) + "n" + u.substring(i-4,i);
				//setUrl(u);
				setImagesUrls("https://graph.facebook.com/"+jsonobj.getString("object_id")+"?access_token="+parent.getActivityString(R.string.fb_token));
			} else {
				isNotEmpty = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(GlobalValues.logTag, e.getMessage());
		}
		/*
		Pattern pattern = Pattern.compile("src=\\\"(.*?)\"");
		Matcher matcher = pattern.matcher(getContent());
		if (matcher.find())
		{
			String u = matcher.group(1);
			int i = u.length();
			// Change one letter in url to get big image
			u = u.substring(0, i-5) + "n" + u.substring(i-4,i);
			setUrl(u);
			//Log.i(GlobalValues.logTag, "Url: "+u);
		} else {
			setUrl("");
		}
		 */
	}

	//================================================================================
	// Accessors Getter and setters.
	//================================================================================

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean hasTitle() {
		return hasTitle;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		//getImage();
	}

	public void getImage() {
		if (!imgFile.exists()) {
			//ASyncImageLoader a = new ASyncImageLoader(this, width);
			//a.execute(new String[]{url});
		} else {
		}
	}

	//================================================================================
	// Others
	//================================================================================

	public boolean hasBitmap() {
		return imgFile.exists();
	}
	public Bitmap loadBitmap() {
		return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
	}

	public void finishedLoadImage(Bitmap img) {
		saveImage(img);
		//setImage(img);
		//parent.notifyDataSetChanged();
	}

	public void saveImage(Bitmap finalBitmap) {
		if (imgFile.exists()) imgFile.delete (); 
		try {
			FileOutputStream out = new FileOutputStream(imgFile);
			finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		return imgFile;
	}

	public boolean isNotEmpty() {
		return isNotEmpty;
	}

	private void setImagesUrls(String string) {
		// TODO Auto-generated method stub
		url = string;
	}

	private void getImagesUrls(String string) {
		// TODO Auto-generated method stub
		if (string != null) {
			ASyncImageUrlLoader a = new ASyncImageUrlLoader(this, width);
			a.execute(new String[]{string});
		}
	}

	public void imageUrlLoaded(String result) {
		// TODO Auto-generated method stub
		setUrl(result);
	}
}
