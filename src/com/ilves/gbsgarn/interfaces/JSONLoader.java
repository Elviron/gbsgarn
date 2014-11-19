package com.ilves.gbsgarn.interfaces;

import java.util.List;

import com.ilves.gbsgarn.types.GbsFbPost;
import com.ilves.gbsgarn.types.GbsInfo;

public interface JSONLoader {
	public final int info = 1, feed = 0;
	public void loadedJSON(List<GbsFbPost> jsonlist);
	public void loadedINFO(GbsInfo jsonobj);
}
