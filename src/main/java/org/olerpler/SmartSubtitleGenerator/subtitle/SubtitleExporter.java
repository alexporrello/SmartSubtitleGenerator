package org.olerpler.SmartSubtitleGenerator.subtitle;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import text.Word;
import displays.JMPanel;
import displays.JMUtils;

public class SubtitleExporter extends JMPanel {
	private static final long serialVersionUID = 389010027836038676L;

	/** The sin number for this subtitle **/
	private int sinNumber;

	/** The subtitle currently being painted **/
	private Subtitle toPaint;

	/** The subtitle's background color **/
	private Color background = Color.BLACK;

	/** The subtitle's foreground color **/
	private Color foreground = Color.WHITE;

	/** The opacity of the overlays **/
	private float overlayOpacity = 0.85f;

	/** The width of the sin counter and sin timer overlay **/
	int overlayWidth  = 69;

	/** The height of the sin counter and sin timer overlay **/
	int overlayHeight = 1051;

	/** For use in naming the subtitles **/
	private String[] letters = {"a", "b", "c", "d", "e", "f"};

	/** Usually an empty string. **/
	public String time = "";

	/** True if the user is using as a sins editor, not a wins editor **/
	public boolean sinsEditor = true;

	/**
	 * To be used when exporting a sin's subtitle.
	 * @param s the string to be drawn onto a subtitle.
	 * @param sinNumber the sin's number.
	 * @param empty used for naming; true for the first subtitle, 
	 * 		  false for the second.
	 * @param exportPath the export path of the subtitle. <br>
	 * 	      <i>NOTE:</i> please do not include file extension in path. This
	 *        will be added by the program.
	 * @param sinEditor if the user is using program to write a sins script
	 *        instead of a wins script.
	 */
	public SubtitleExporter(String s, int sinNumber, boolean empty, String exportPath,
			boolean sinsEditor) {

		this.sinsEditor = sinsEditor;

		JMUtils.setFixedSize(this, 1280, 720);

		this.sinNumber = sinNumber;

		int i = 0;

		if(!empty) i = 1;

		for(Subtitle subtitle : Subtitle.processSubtitle(s)) {
			toPaint = subtitle;

			revalidate();
			repaint();

			exportSubtitle(ScreenImage.createImage(this), 
					exportPath + "_" + letters[i++] + ".png");
		}
	}

	/**
	 * Kept on reserve if the fonts change.
	 * @param time the time to be exported.
	 * @param exportPath the location where the time is saved.
	 */
	public SubtitleExporter(String time, String exportPath) {

		JMUtils.setFixedSize(this, 1280, 720);

		this.sinNumber = 0;
		this.time = time;

		for(Subtitle subtitle : Subtitle.processSubtitle(" ")) {
			toPaint = subtitle;

			revalidate();
			repaint();

			exportSubtitle(ScreenImage.createImage(this), exportPath + ".png");
		}
	}

	public BufferedImage createImage() {
		BufferedImage image = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		this.paint(g);

		return image;
	}

	/**
	 * Saves the subtitles to an image at a specified path.
	 * @param subtitle the subtitle to export
	 * @param exportPath the path to which the subtitle will be exported
	 */
	public void exportSubtitle(BufferedImage subtitle, String exportPath) {
		try {
			ScreenImage.writeImage(subtitle, exportPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D gg = (Graphics2D) g;

		gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		gg.setFont(Subtitle.FONT);

		int sub = 720-Subtitle.SUBTITLE_HEIGHT;		

		gg.setColor(background);
		gg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, overlayOpacity));
		gg.fillRect(5, 5 + sub, Subtitle.SUBTITLE_WIDTH-10, Subtitle.SUBTITLE_HEIGHT-10);

		gg.setComposite(AlphaComposite.Src);
		gg.setColor(foreground);

		if(toPaint.isSingleLine()) {
			Word w = new Word(toPaint.middle, Subtitle.FONT);

			int x = (Subtitle.SUBTITLE_WIDTH - w.width) / 2;
			int y = (Subtitle.SUBTITLE_HEIGHT) /2 + sub;

			gg.drawString(w.word, x, y+5);
		} else {
			Word top = new Word(toPaint.top, Subtitle.FONT);
			Word bot = new Word(toPaint.bottom, Subtitle.FONT);

			int tWidth = g.getFontMetrics(Subtitle.FONT).stringWidth(toPaint.top);
			int bWidth = g.getFontMetrics(Subtitle.FONT).stringWidth(toPaint.bottom);

			int topX = (Subtitle.SUBTITLE_WIDTH - tWidth) / 2;
			int botX = (Subtitle.SUBTITLE_WIDTH - bWidth) / 2;

			int topY = (Subtitle.SUBTITLE_HEIGHT - bot.height) / 2   + sub;
			int botY = (Subtitle.SUBTITLE_HEIGHT + (bot.height)) / 2 + sub + 5;

			gg.drawString(top.word, topX, topY+5);
			gg.drawString(bot.word, botX, botY+5);
		}

		if(sinsEditor) {
			drawOverlay("Gaming Sins Counter", sinNumber + "", 999, 5, gg);
			drawOverlay("Gaming Sins Timer",   time,           5,   5, gg);
		} else {
			drawOverlay("Gaming Wins Counter", sinNumber + "", 999, 5, gg);
			drawOverlay("Gaming Wins Timer",   time,           5,   5, gg);
		}
	}

	/**
	 * Responsible for drawing overlays onto the subtitle.
	 * @param overlayTitle the title of the overlay
	 * @param text the displayed text of the overlay
	 * @param overlayX the x position of the overlay
	 * @param overlayY the y position of the overlay
	 * @param gg the font with which the overlay will be drawn
	 */
	public void drawOverlay(String overlayTitle, String text, 
			int overlayX, int overlayY, Graphics2D gg) {

		// Fill the overlay area with translucent black
		gg.setColor(background);
		gg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, overlayOpacity));	
		gg.fillRect(overlayX, overlayY, 276, 79);

		// Set graphics to opaque and set the color to white/
		gg.setComposite(AlphaComposite.Src);
		gg.setColor(foreground);

		// Create the font for overlayText.
		Font overlayTextFont = new Font(Subtitle.FONT.getName(), Font.PLAIN, 35);

		// Position and draw the overlay text.
		gg.setFont(overlayTextFont);
		int overlayTextWidth = 
				gg.getFontMetrics(overlayTextFont).stringWidth(text);
		int textX = ((276 - overlayTextWidth)/2)+overlayX;
		gg.drawString(text, textX, 43);

		// Position and draw the overlay title.
		gg.setFont(Subtitle.FONT);
		int titleWidth = 
				gg.getFontMetrics(Subtitle.FONT).stringWidth(overlayTitle);
		int titleX = ((276 - titleWidth)/2)+overlayX;
		gg.drawString(overlayTitle, titleX, 70);
	}
}
