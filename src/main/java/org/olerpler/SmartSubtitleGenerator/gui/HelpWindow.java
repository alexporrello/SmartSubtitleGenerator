package org.olerpler.SmartSubtitleGenerator.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import layout.GBC;
import text.JMLabel;
import text.JMTextArea;
import colors.JMColor;
import displays.JMFrame;
import displays.JMPanel;
import displays.JMScrollPane;

public class HelpWindow extends JMFrame {
	private static final long serialVersionUID = -7293964552178698454L;

	public HelpWindow() {
		setLocationByPlatform(true);
		setIconImages(Variables.imageIcon());
		setSize(new Dimension(320, 400));
		setTitle("Fancy Subtitle Editor Help");
		setResizable(false);
		
		JMPanel panel = new JMPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(JMColor.WHITE);

		int y = 0;

		GBC.addWithGBC(panel, createLabel("What does this app do?"), 
				1.0, 0.0, 0, y++, GBC.HORIZ, GBC.CENT, GBC.insets(0, 10, 0, 10), 3);
		GBC.addWithGBC(panel, createTextArea("This is a subtitle exporter app "
				+ "and script writing app, developed by Alexander Porrello "
				+ "(www.AlexanderPorrello.com) in Java with JModern Library "
				+ "for the YouTube channel GamingSins."), 
				1.0, 0.0, 0, y++, GBC.HORIZ, GBC.CENT, GBC.insets(0, 10, 0, 10), 3);
		GBC.addWithGBC(panel, createLabel("Getting Started"), 
				1.0, 0.0, 0, y++, GBC.HORIZ, GBC.CENT, GBC.insets(10, 10, 0, 10), 3);

		// 1
		GBC.addWithGBC(panel, createLabel("1."), 
				0.0, 0.0, 0, y, GBC.HORIZ, GBC.NORTHWEST, GBC.insets(0, 10, 0, 0), 1);
		GBC.addWithGBC(panel, createTextArea("Click File > New Project (or the "
				+ "shortcut 'ctrl + N'). Select the save location of the subtitle "
				+ "file. Once you have selected the project location and pressed "
				+ "the 'Save' button, the new project file will be loaded."), 
				1.0, 1.0, 1, y++, GBC.HORIZ, GBC.CENT, GBC.insets(0, 0, 0, 10), 2);

		// 1.1
		GBC.addWithGBC(panel, createLabel("1.1."), 
				0.0, 0.0, 1, y, GBC.NONE, GBC.NORTHWEST, GBC.insets(0, 10, 0, 0), 1);
		GBC.addWithGBC(panel, createTextArea("To load a file, click File > Open "
				+ "Project. A list of the six most recently opened files can be "
				+ "accessed by hovering over File > Open Recent and selecting the"
				+ " file you wish to open."),
				0.0, 0.0, 2, y++, GBC.BOTH, GBC.NORTH, GBC.insets(0, 0, 0, 10), 1);

		// 2
		GBC.addWithGBC(panel, createLabel("2."), 
				0.0, 0.0, 0, y, GBC.HORIZ, GBC.NORTHWEST, GBC.insets(0, 10, 0, 0), 1);
		GBC.addWithGBC(panel, createTextArea("The area where new subtitles are "
				+ "entered is at the bottom of the window. The subtitle’s "
				+ "timecode will be entered to the far left. The subtitle’s "
				+ "text will be entered in the middle. The button with the right"
				+ " arrow (>) icon adds the subtitle to the project. The button "
				+ "with the x icon quickly clears both the timecode and the text"
				+ " area. There are two methods for entering a timecode:"), 
				1.0, 1.0, 1, y++, GBC.HORIZ, GBC.CENT, GBC.insets(0, 0, 0, 10), 2);
		// 2.1
		GBC.addWithGBC(panel, createLabel("2.1."), 
				0.0, 0.0, 1, y, GBC.NONE, GBC.NORTHWEST, GBC.insets(0, 10, 0, 0), 1);
		GBC.addWithGBC(panel, createTextArea("Manually enter the "
				+ "timecode (hh:mm:ss) of the subtitle location "
				+ "in your video editing software."),
				0.0, 0.0, 2, y++, GBC.BOTH, GBC.NORTH, GBC.insets(0, 0, 0, 10), 1);
		// 2.2
		GBC.addWithGBC(panel, createLabel("2.2."), 
				0.0, 0.0, 1, y, GBC.NONE, GBC.NORTHWEST, GBC.insets(0, 10, 0, 0), 1);
		GBC.addWithGBC(panel, createTextArea("If you are editing with "
				+ "Adobe Premiere® Pro CC, click on Window > Paste time "
				+ "from Adobe Premiere® Pro CC. Once this option is "
				+ "selected, copy the timecode directly from Adobe "
				+ "Premiere® Pro CC and click on the field where you "
				+ "enter the subtitle’s text. Smart Subtitle Generator "
				+ "will auto-paste the timecode. (To revert to manual "
				+ "entry, click Window > Paste time from Adobe Premiere®"
				+ " Pro CC)."),
				0.0, 0.0, 2, y++, GBC.BOTH, GBC.NORTH, GBC.insets(0, 0, 0, 10), 1);

		// 3
		GBC.addWithGBC(panel, createLabel("3."), 
				0.0, 0.0, 0, y, GBC.HORIZ, GBC.NORTHWEST, GBC.insets(0, 10, 0, 0), 1);
		GBC.addWithGBC(panel, createTextArea("Once the subtitle’s time"
				+ " and text have been entered, press on the button "
				+ "with the right arrow (>) icon to load it into the "
				+ "project (or press your keyboard's 'Enter' key). "
				+ "The subtitle will appear in the above window, and"
				+ " the subtitle-entry area will be cleared. Repeat "
				+ "until all of the subtitles have been entered."), 
				1.0, 1.0, 1, y++, GBC.HORIZ, GBC.CENT, GBC.insets(0, 0, 0, 10), 2);
		// 3.1
		GBC.addWithGBC(panel, createLabel("3.1."), 
				0.0, 0.0, 1, y, GBC.NONE, GBC.NORTHWEST, GBC.insets(0, 10, 0, 0), 1);
		GBC.addWithGBC(panel, createTextArea("Please note that "
				+ "subtitles are automatically sorted by their "
				+ "timecode. If you would like to change an "
				+ "existing subtitle’s timecode, double click "
				+ "on its hours, minutes, or seconds field and "
				+ "enter a new number."),
				0.0, 0.0, 2, y++, GBC.BOTH, GBC.NORTH, GBC.insets(0, 0, 0, 10), 1);

		// 4
		GBC.addWithGBC(panel, createLabel("4."), 
				0.0, 0.0, 0, y, GBC.HORIZ, GBC.NORTHWEST, GBC.insets(0, 10, 0, 0), 1);
		GBC.addWithGBC(panel, createTextArea("To export subtitles that can be "
				+ "imported into your editing software, (1) select Subtitle > "
				+ "Export Subtitles, (2) select the location where you would "
				+ "like the subtitles to be exported, and (3) once the location "
				+ "has been selected, press the 'Open' button. "
				+ "All of the subtitles will auto-export."), 
				1.0, 1.0, 1, y++, GBC.HORIZ, GBC.CENT, GBC.insets(0, 0, 0, 10), 2);
		// 4.1
		GBC.addWithGBC(panel, createLabel("4.1."), 
				0.0, 0.0, 1, y, GBC.NONE, GBC.NORTHWEST, GBC.insets(0, 10, 0, 0), 1);
		GBC.addWithGBC(panel, createTextArea("Subtitles are by default placed "
				+ "on a black bar with a background opacity of .75. To change "
				+ "the black bar’s background opacity, hover over Subtitle > "
				+ "Overlay Opacity and click the desired value."),
				0.0, 0.0, 2, y++, GBC.BOTH, GBC.NORTH, GBC.insets(0, 0, 0, 10), 1);
		// 4.2
		GBC.addWithGBC(panel, createLabel("4.2."), 
				0.0, 0.0, 1, y, GBC.NONE, GBC.NORTHWEST, GBC.insets(0, 10, 0, 0), 1);
		GBC.addWithGBC(panel, createTextArea("Please note that exported "
				+ "subtitle files are named by their timecode. If you have "
				+ "changed a subtitle's timecode, you will have to re-import "
				+ "the overlay into your video editing software."),
				0.0, 0.0, 2, y++, GBC.BOTH, GBC.NORTH, GBC.insets(0, 0, 10, 10), 1);

		JMScrollPane scroll = new JMScrollPane(panel, false);
		scroll.setScrollBarWidth(8);
		add(scroll, BorderLayout.CENTER);
	}

	public JMLabel createLabel(String s) {
		JMLabel label = new JMLabel(s);
		label.setOpaque(false);
		return label;
	}

	public JTextPane createTextArea(String s) {
		JTextPane text = new JTextPane();
		text.setText(s);
		
		SimpleAttributeSet attribs = new SimpleAttributeSet();
		StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_JUSTIFIED);
		StyleConstants.setFontFamily(attribs, "Arial");
		StyleConstants.setFontSize(attribs, 15);
		text.setParagraphAttributes(attribs, true);
		
		text.setFont(new JMTextArea("").getFont());
		text.setEditable(false);
		text.setCaretColor(text.getBackground());
		text.setFocusable(false);
		text.setHighlighter(null);
		text.setOpaque(false);
		
		return text;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			HelpWindow editor = new HelpWindow();
			editor.setVisible(true);
			editor.setDefaultCloseOperation(JMFrame.EXIT_ON_CLOSE);
		});
	}
}
