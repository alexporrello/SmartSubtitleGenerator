package org.olerpler.SmartSubtitleGenerator.subtitleOverlay;

/**
 * A SubtitleString is a string with an x and a y coordinate for drawing.
 * @author Alexander Porrello
 */
class SubtitleOverlayString {

	/** The x position of the string for drawing **/
	public int x;
	
	/** The y position of the string for drawing **/
	public int y;
	
	/** The string to draw **/
	public String string;
	
	SubtitleOverlayString(String string, int x, int y) {
		this.string = string;
		this.x      = x;
		this.y      = y;
	}
}
