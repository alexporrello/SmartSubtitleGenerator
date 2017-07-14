package org.olerpler.SmartSubtitleGenerator.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateString implements Comparable<DateString> {

	public String string;
	public String timeStamp;
	
	public DateString(String string) {
		this.string = string;
		updateTimeStamp();
	}
	
	public DateString(String string, String timeStamp) {
		this.string    = string;
		this.timeStamp = timeStamp;
	}
	
	public void updateTimeStamp() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime     now = LocalDateTime.now();
		timeStamp = dtf.format(now);
	}
	
	@Override
	public int compareTo(DateString o) {
		return o.timeStamp.compareTo(timeStamp);
	}
	
	
}
