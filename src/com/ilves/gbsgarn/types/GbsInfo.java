package com.ilves.gbsgarn.types;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import com.ilves.gbsgarn.GbsGarnActivity;
import com.ilves.gbsgarn.GlobalValues;
import com.ilves.gbsgarn.R;
import com.ilves.gbsgarn.R.string;

import android.util.Log;
import android.view.View;

public class GbsInfo {

	public int weekday_open = 9;
	public int weekday_close = 20;
	public int sat_open = 9;
	public int sat_close = 19;

	public int today_open, today_close;
	public int tomorrow_open, tomorrow_close;

	private String today = "", tomorrow = "";

	public int closedView = View.VISIBLE;
	public int openView = View.GONE;
	
	private GbsGarnActivity context;
	private boolean	mDebugLog = true;

	public GbsInfo() {
		// TODO Auto-generated constructor stub
	}

	public GbsInfo(GbsGarnActivity context, JSONObject obj) {
		this.context = context;
		try {
			// fetch open hours and convert to int
			JSONObject hours = obj.getJSONObject("hours");
			Log.i(GlobalValues.logTag, hours.toString());
			String msg = hours.getString("mon_1_open");
			weekday_open = convertToInt(msg);
			debugLog(""+weekday_open);
			msg = hours.getString("mon_1_close");
			weekday_close = convertToInt(msg);
			debugLog(""+weekday_close);
			msg = hours.getString("sat_1_open");
			sat_open = convertToInt(msg);
			debugLog(""+sat_open);
			msg = hours.getString("sat_1_close");
			sat_close = convertToInt(msg);
			debugLog(""+sat_close);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkTime();
	}

	public void checkTime() {
		// get todays day
		//DateTime cal = new DateTime();
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+01")); 
		SimpleTimeZone tz = getTimeZone(cal.get(Calendar.YEAR));
		cal = new GregorianCalendar(tz);
		//Calendar.getInstance(TimeZone.getTimeZone("GMT+02"));
		// sun-sat, 1-7
		int day = cal.get(Calendar.DAY_OF_WEEK);
		Log.d(GlobalValues.logTag, "Day: "+day);
		int time = cal.get(Calendar.HOUR_OF_DAY);
		Log.d(GlobalValues.logTag, "Time: "+time);
		// compare the time to set open or closed
		if (day >= 7) { // saturday
			checkOpenHours(time, sat_open, sat_close);
			today = sat_open+".00-"+sat_close+".00";
			tomorrow = context.getString(R.string.closed);
		} else if (day == 6) { // friday, tomorrow saturday
			checkOpenHours(time, weekday_open, weekday_close);
			today = weekday_open+".00-"+weekday_close+".00";
			tomorrow = sat_open+".00-"+sat_close+".00";
		} else if (day >= 2) { // weekday
			checkOpenHours(time, weekday_open, weekday_close);
			today = weekday_open+".00-"+weekday_close+".00";
			tomorrow = weekday_open+".00-"+weekday_close+".00";
		} else { // sunday, today closed, tomorrow open
			closedView = View.VISIBLE;
			openView = View.GONE;
			today = context.getString(R.string.closed);
			tomorrow = weekday_open+".00-"+weekday_close+".00";
		}
	}

	private void checkOpenHours(int time, int open, int close) {
		Log.d(GlobalValues.logTag, "open: "+open+"-"+close);
		if (time >= open && time < close) {
			Log.d(GlobalValues.logTag, "Open.");
			closedView=View.GONE;
			openView=View.VISIBLE;
		} else {
			Log.d(GlobalValues.logTag, "Closed.");
			closedView=View.VISIBLE;
			openView=View.GONE;
		}

	}

	private int convertToInt(String time) {
		if (time.length() > 3) {
			try {
				time = time.substring(0, 2);
			} catch (IndexOutOfBoundsException e) {
				Log.e(GlobalValues.logTag, e.getMessage());
				e.printStackTrace();
			}
			return (int)Integer.parseInt(time);
		}
		return 0;
	}
	
	private SimpleTimeZone getTimeZone(int year) {
		SimpleTimeZone ret;
		int day_of_month_start = 0;
		int day_of_month_end = 0;
		switch (year) {
		case 2013:
			day_of_month_start = 31;
			day_of_month_end = 27;
			break;
		case 2014:
			day_of_month_start = 30;
			day_of_month_end = 26;
			break;
		case 2015:
			day_of_month_start = 29;
			day_of_month_end = 25;
			break;
		case 2016:
			day_of_month_start = 27;
			day_of_month_end = 30;
			break;
		case 2017:
			day_of_month_start = 26;
			day_of_month_end = 29;
			break;
		case 2018:
			day_of_month_start = 25;
			day_of_month_end = 28;
			break;
		case 2019:
			day_of_month_start = 31;
			day_of_month_end = 27;
			break;
		default:
			ret = null;
			break;
		}
		ret = new SimpleTimeZone(3600000, TimeZone.getAvailableIDs(3600000)[0], Calendar.MARCH, day_of_month_start, 0, 2, Calendar.OCTOBER, day_of_month_end, 0, 3);
		return ret;
	}

	/*
	 * Stockholm daylight savings
	Year	Start Date	End Date
	2013	söndag den 31 mars, 02:00	söndag den 27 oktober, 03:00
	2014	söndag den 30 mars, 02:00	söndag den 26 oktober, 03:00
	2015	söndag den 29 mars, 02:00	söndag den 25 oktober, 03:00
	2016	söndag den 27 mars, 02:00	söndag den 30 oktober, 03:00
	2017	söndag den 26 mars, 02:00	söndag den 29 oktober, 03:00
	2018	söndag den 25 mars, 02:00	söndag den 28 oktober, 03:00
	2019	söndag den 31 mars, 02:00	söndag den 27 oktober, 03:00
	*/
	
	/*
	 * GETTER AND SETTERS
	 */
	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public String getTomorrow() {
		return tomorrow;
	}

	public void setTomorrow(String tomorrow) {
		this.tomorrow = tomorrow;
	}

	public void debugLog(String message) {
		if (mDebugLog ) {
			Log.i(GlobalValues.logTag, message);
		}
	}
}
