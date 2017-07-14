package org.olerpler.SmartSubtitleGenerator.gui;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import colors.JMColor;

public class Variables {

	public static final Color FOREGROUND = JMColor.WHITE;
	public static final Color BACKGROUND = JMColor.decode("#f1f1f1");
	
	public static final Color FOREGROUND_DARK = JMColor.DARKER_GRAY;
	public static final Color BACKGROUND_DARK = JMColor.DARK_GRAY;
		
	public static final Color FOCUSED = JMColor.decode("#ecf4fb");
	
	public static final Color MOUSE_OFF  = JMColor.DARK_GRAY;//FancyColor.DARK_BLUE;
	public static final Color MOUSE_OVER = JMColor.decode("#1883d7");//FancyColor.LIGHT_BLUE;
	
	public static final Color ACCENT = JMColor.decode("#1883d7");
	
	public static final int INTERFACE_SIZE_SMALL  = 15;
	public static final int INTERFACE_SIZE_MEDIUM = 20;
	public static final int INTERFACE_SIZE_LARGE  = 25;
	
	public static final ArrayList<Image> imageIcon() {
		ArrayList<Image> icons = new ArrayList<Image>();

		icons.add(loadImage("/GamingSins_128x128.png"));
		icons.add(loadImage("/GamingSins_16x16.png"));
		icons.add(loadImage("/GamingSins_32x32.png"));
		icons.add(loadImage("/GamingSins_48x48.png"));
		
		return icons;
	}
	
	private static Image loadImage(String url) {
		return new ImageIcon(
				Variables.class.getClass().getResource(url)).getImage();
	}
}
