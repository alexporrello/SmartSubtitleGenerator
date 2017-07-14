package org.olerpler.SmartSubtitleGenerator.subtitle;

import java.io.Serializable;

/**
 * Time was created to manage the time for sins.
 * @author Alexander Porrello
 */
public class SubtitleTime implements Serializable {
	private static final long serialVersionUID = -1829145722765291049L;

	public int hour;
	public int minute;
	public int second;
	public int msecond;

	/**
	 * For use if one wishes to input an hour, minute, and second.
	 * @param hour is the given hour.
	 * @param minute is the given minute.
	 * @param second is the given second.
	 */
//	public SinTime(int hour, int minute, int second) {
//		this.hour    = hour;
//		this.minute  = minute;
//		this.second  = second;
//		this.msecond = 00;
//	}

	/**
	 * For use if one wishes to input an hour, minute, and second.
	 * @param hour is the given hour.
	 * @param minute is the given minute.
	 * @param second is the given second.
	 */
//	public SinTime(String hour, String minute, String second) {
//		this.hour   = Integer.parseInt(hour);
//		this.minute = Integer.parseInt(minute);
//		this.second = Integer.parseInt(second);
//		this.msecond = 00;
//	}
	
	/**
	 * For use if one wishes to input an hour, minute, and second.
	 * @param hour is the given hour.
	 * @param minute is the given minute.
	 * @param second is the given second.
	 */
	public SubtitleTime(int hour, int minute, int second, int msecond) {
		this.hour    = hour;
		this.minute  = minute;
		this.second  = second;
		this.msecond = msecond;
	}

	/**
	 * For use if one wishes to input an hour, minute, and second.
	 * @param hour is the given hour.
	 * @param minute is the given minute.
	 * @param second is the given second.
	 */
	public SubtitleTime(String hour, String minute, String second, String msecond) {
		this.hour    = Integer.parseInt(hour);
		this.minute  = Integer.parseInt(minute);
		this.second  = Integer.parseInt(second);
		this.msecond = Integer.parseInt(msecond);
	}

	/**
	 * For use if one wishes to input a minute and second.
	 * @param minute is the given minute.
	 * @param second is the given second.
	 */
	public SubtitleTime(int minute, int second) {
		this.hour    = 00;
		this.minute  = minute;
		this.second  = second;
		this.msecond = 00;
	}

	/**
	 * For use if one wishes to input a second.
	 * @param second is the given second.
	 */
	public SubtitleTime(int second) {
		this.hour    = 00;
		this.minute  = 00;
		this.second  = second;
		this.msecond = 00;
	}

	/**
	 * For use if one wishes to simply instantiate a time.
	 */
	public SubtitleTime() {
		this.hour    = 00;
		this.minute  = 00;
		this.second  = 00;
		this.msecond = 00;
	}

	/**
	 * Changes a valid string into a time.
	 * @param timeString should be in the following format: hh.mm.ss
	 */
	public void parseTime(String timeString) {
		String[] time = timeString.split(".");

		this.hour   = Integer.parseInt(time[0]);
		this.minute = Integer.parseInt(time[1]);
		this.second = Integer.parseInt(time[2]);
		
		if(time.length == 4) {
			this.msecond = Integer.parseInt(time[3]);
		}
	}

	/**
	 * Transforms this time into a valid string and returns it.
	 */
	public String toString() {
		String hour;
		String minute;
		String second;
		String msecond;

		if((hour = this.hour + "").length() < 2) {
			hour = "0" + hour;
		}

		if((minute = this.minute + "").length() < 2) {
			minute = "0" + minute;
		}

		if((second = this.second + "").length() < 2) {
			second = "0" + second;
		}
		
		if((msecond = this.msecond + "").length() < 2) {
			msecond = "0" + msecond;
		}

		return hour + ":" + minute + ":" + second + ":" + msecond;
	}

	@Override
	public boolean equals(Object o) {

		if(o instanceof SubtitleTime) {
			SubtitleTime t = (SubtitleTime) o;

			return(hour == t.hour &&
					minute  == t.minute &&
					second  == t.second &&
					msecond == t.msecond);
		}

		return false;
	}

	/**
	 * Converts a unit of time into proper two digits.
	 * @param time is the unit to be converted.
	 * @return an int of two digits.
	 */
	public static String twoDigits(int time) {
		if((time + "").length() < 2) {
			return "0" + time;
		}

		return time + "";
	}
	
	/**
	 * Converts a unit of time into proper two digits.
	 * @param time is the unit to be converted.
	 * @return an int of two digits.
	 */
	public static String twoDigits(String time) {
		if((time).length() < 2) {
			return "0" + time;
		}

		return time + "";
	}

	/**
	 * Converts an hour, minute, or second string to a time.
	 * @param time is the string to be parsed. 
	 * @return the string converted to a time int.
	 */
	public static int stringToTime(String time) {		
		if(time.equals("")) {
			return 0;
		} else {
			return Integer.parseInt(time);
		}
	}
}