package com.ilves.gbsgarn;

import java.util.List;

public interface JSONLoader {
	public final int info = 1, feed = 0;
	public void loadedJSON(List<GbsFbPost> jsonlist);
	public void loadedINFO(GbsInfo jsonobj);
}
