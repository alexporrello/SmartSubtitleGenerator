package org.olerpler.SmartSubtitleGenerator.subtitleOverlay;

import java.awt.Font;
import java.util.ArrayList;

import text.Word;

class SubtitleOverlay {

	/** The width of the subtitle square **/
	public static final int  SUBTITLE_WIDTH  = 1280;
	
	/** The height of the subtitle square **/
	public static final int  SUBTITLE_HEIGHT = 100;
	
	/** The standard subtitle font **/
	public static final Font FONT = 
			new Font("GothamXNarrow-Medium", Font.PLAIN, 27);

	/** The middle line for a single-line subtitle **/
	public String middle = "";
	
	/** The top string for a two-line subtitle **/
	public String top    = "";
	
	/** The bottom string for a two-line subtitle **/
	public String bottom = "";

	/** Set to true if the subtitle is a single line; else, false **/
	private boolean singleLine;
	
	/**
	 * For use with subtitles that have one line.
	 * @param middle the single middle line of this subtitle.
	 */
	public SubtitleOverlay(String middle) {
		this.middle     = middle;
		this.singleLine = true;
	}
	
	/**
	 * For use with subtitles that have two lines.
	 * @param top the top line of this subtitle.
	 * @param bottom the bottom line of this subtitle.
	 */
	public SubtitleOverlay(String top, String bottom) {
		this.top        = top;
		this.bottom     = bottom;
		this.singleLine = false;
	}

	/**
	 * Used to check where a subtitle has one or two lines.
	 * @return true if the subtitle has one line; else, false.
	 */
	public Boolean isSingleLine() {
		return singleLine;
	}

	/**
	 * Accepts a string, splits it up into subtitles, and returns them.
	 * @param text the string to be split up.
	 * @return an ArrayList of the processed subtitles.
	 */
	public static ArrayList<SubtitleOverlay> processSubtitle(String text) {

		ArrayList<SubtitleOverlay> toPaint = new ArrayList<SubtitleOverlay>();

		Word fullTitle = new Word(text, FONT);

		if(fullTitle.width < SUBTITLE_WIDTH-40) {
			toPaint.add(new SubtitleOverlay(fullTitle.word));
		} else {
			String[] split    = text.split(" ");
			String   thisLine = "";

			ArrayList<String> lines = new ArrayList<String>();

			for(String s : split) {
				Word temp = new Word(thisLine + s, FONT);

				if(thisLine.startsWith(" ")) {
					thisLine = thisLine.replaceFirst(" ", "");
				}

				if(temp.width < SUBTITLE_WIDTH - 100) {
					thisLine = thisLine + " " + s;
				} else {
					String toAdd = thisLine;
					lines.add(toAdd);

					thisLine = s;
				}
			}

			lines.add(thisLine);

			for(int i = 0; i < lines.size(); i++) {
				if(i+1 < lines.size()) {
					toPaint.add(new SubtitleOverlay(lines.get(i), lines.get(i+=1)));
				} else {
					toPaint.add(new SubtitleOverlay(lines.get(i)));
				}
			}
		}

		return toPaint;
	}
}
