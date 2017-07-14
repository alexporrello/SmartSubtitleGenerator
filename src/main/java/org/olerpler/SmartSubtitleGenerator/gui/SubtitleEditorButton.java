package org.olerpler.SmartSubtitleGenerator.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.ImageIcon;

import text.JMLabel;

public class SubtitleEditorButton extends JMLabel {
	private static final long serialVersionUID = 8053269937583104140L;

	private Color oval = Variables.MOUSE_OFF;
	
	public Color mouse_over = Variables.MOUSE_OVER;
	public Color mouse_off  = Variables.MOUSE_OFF;
	
	public SubtitleEditorButton(ImageIcon icon) {
		setOpaque(false);
		setIcon(icon);
		
		addMouseEnteredListener(e -> changeColor(mouse_over));
		addMouseExitedListener(e -> changeColor(mouse_off));
		addFocusGainedListener(e -> repaint());
		addFocusLostListener(e -> repaint());
	}

	private void changeColor(Color color) {
		oval = color;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D gg = (Graphics2D) g;

		gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					
		int width = 2;

		if(hasFocus()) {
			gg.setColor(mouse_over);
			//gg.setPaint(new GradientPaint(0, 0, Constants.MOUSE_OFF, 0, getWidth(), Constants.MOUSE_OVER));
		} else {
			gg.setColor(oval);
		}
		
		gg.fillOval(width, getHeight()/2-(getWidth()/2)+width, getWidth()-(width*2), getWidth()-(width*2));
		
		super.paintComponent(gg);
	}
	
	/**
	 * Use a lambda to write a kyPressed listener.
	 * @param listener is the lambda to be accepted.
	 */
	public void addMouseEnteredListener(Consumer<MouseEvent> listener) {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				listener.accept(e);
			}
		});
	}
	
	/**
	 * Use a lambda to write a kyPressed listener.
	 * @param listener is the lambda to be accepted.
	 */
	public void addMouseExitedListener(Consumer<MouseEvent> listener) {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				listener.accept(e);
			}
		});
	}
	
	public void addActionListener(Consumer<MouseEvent> listener) {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				listener.accept(e);
			}
		});
	}
}
