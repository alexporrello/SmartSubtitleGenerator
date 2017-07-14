package org.olerpler.SmartSubtitleGenerator.sin;

import java.io.Serializable;

/**
 * Has a key (for hashing) and holds all sin info.
 * @author Alexander Porrello
 */
public class Sin implements Serializable, Comparable<Sin> {
	private static final long serialVersionUID = -5400623367485790675L;

	/** The key that is assigned to the placeholder sin **/
	public static final double PLACEHOLDER_KEY = 0.0;

	/** The key will be used to identify sins **/
	public Double key;

	/** The number to be painted onto the subtitle **/
	public int number;

	/** contributor is the sin's contributor **/
	public String contributor = "";

	/** The sin's state; whether a sin will be added or subtracted **/
	public SinState state;

	/** The sin's text **/
	public String text;

	/** The sin's time. **/
	public SinTime time;
	
	/** If the sin will be displayed on the GUI **/
	public Boolean visible = true;

	/**
	 * For use in instantiating a sin from scratch.
	 */
	public Sin() {
		key         = generateKey();
		contributor = "";
		state       = SinState.ADD;
		text        = "";
		time        = new SinTime();
		number      = 0;
	}

	/**
	 * @param key is the key.
	 * @param number is the number to be painted on the subtitle.
	 * @param time is the sin's time.
	 * @param text is the sin's text.
	 * @param contributor is the name of the sin's contributor.
	 * @param state is the state of the sin.
	 */
	public Sin(Double key, int number, SinTime time, String text, String contributor, SinState state) {
		this.key         = key;
		this.contributor = contributor;
		this.state       = state;
		this.text        = text;
		this.time        = time;
		this.number      = number;
	}

	/**
	 * @param sinString is the string to be parsed if valid.
	 */
	public Sin(String sinString) {
		parseSinString(sinString);
	}

	/**
	 * Parses a valid sin string.
	 * @param sinString is a valid sin string. See documentation for more information on parseable strings.
	 */
	public void parseSinString(String sinString) {
		if(sinString.startsWith("<sin>") && sinString.endsWith("</sin>")) {			
			this.key = Double.parseDouble(
					(String) sinString.subSequence(sinString.indexOf("<k>") + 3, sinString.indexOf("</k>")));
			
			int mSecond = 0;
			if(sinString.contains("<ms>")) {
				mSecond = Integer.parseInt((String) sinString.subSequence(sinString.indexOf("<ms>") + 4, sinString.indexOf("</ms>")));
			}
			
			this.time = new SinTime(
					Integer.parseInt((String) sinString.subSequence(sinString.indexOf("<h>")  + 3, sinString.indexOf("</h>"))),
					Integer.parseInt((String) sinString.subSequence(sinString.indexOf("<m>")  + 3, sinString.indexOf("</m>"))),
					Integer.parseInt((String) sinString.subSequence(sinString.indexOf("<s>")  + 3, sinString.indexOf("</s>"))),
					mSecond);

			this.state = SinState.returnProperEnum(
					Integer.parseInt((String) sinString.subSequence(sinString.indexOf("<ss>") + 4, sinString.indexOf("</ss>"))));

			this.text = (String) sinString.subSequence(sinString.indexOf("<st>") + 4, sinString.indexOf("</st>"));
			this.contributor = (String) sinString.subSequence(sinString.indexOf("<c>") + 3, sinString.indexOf("</c>"));
			this.number = Integer.parseInt((String) sinString.subSequence(sinString.indexOf("<n>") + 3, sinString.indexOf("</n>")));
		} else {
			throw new IllegalArgumentException("The given sinString (" + sinString + ") is not valid.\n"
					+ "Please see documentation for more information on parseable strings.");
		}
	}

	/**
	 * Method by which a string is made that can be parsed.
	 * @return a parseable string.
	 */
	@Override
	public String toString() {
		return "<sin>" +
				"<k>"  + this.key            + "</k>" +
				"<n>"  + this.number         + "</n>" +
				"<h>"  + this.time.hour      + "</h>" +
				"<m>"  + this.time.minute    + "</m>" +
				"<s>"  + this.time.second    + "</s>" +
				"<ms>" + this.time.msecond   + "</ms>" +
				"<st>" + this.text           + "</st>" +
				"<c>"  + this.contributor    + "</c>" +
				"<ss>" + this.state.state    + "</ss>" +
				"</sin>";
	}

	/**
	 * Generates a key.
	 * @return the generated key.
	 */
	public static Double generateKey() {
		return Math.random() * Math.random();
	}

	/**
	 * Updates this sin to match a given sin.
	 * @param s the sin that will update this.
	 */
	public Boolean update(Sin s) {
		if(this.key.equals(s.key)) {
			this.number = s.number;
			this.time = s.time;
			this.text = s.text;
			this.contributor = s.contributor;
			this.state = s.state;

			return true;
		}

		return false;
	}

	@Override
	public boolean equals(Object o) {

		if(o instanceof Sin) {
			Sin s = (Sin) o;

			return (this.key.equals(s.key)  &&
					this.number == s.number &&
					this.time.equals(s.time) &&
					this.contributor.equals(s.contributor) &&
					this.state.state == s.state.state &&
					this.text.equals(s.text));
		}

		return false;
	}

	@Override
	public int compareTo(Sin o) {
		return this.time.toString().compareTo(o.time.toString());
	}
}