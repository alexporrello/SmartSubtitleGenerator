package org.olerpler.SmartSubtitleGenerator.gui;

import icons.FancyIcon;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import layout.GBC;
import text.JMLabel;
import text.JMTextField;
import colors.JMColor;
import displays.JMDivider;
import displays.RoundedPanel;

public class SearchPanel extends RoundedPanel {
	private static final long serialVersionUID = -5908127450981918010L;

	public JMTextField  text  = new JMTextField("");
	public SubtitleEditorButton removeFromView = new SubtitleEditorButton(FancyIcon.DELETE_16x16);

	public SearchPanel() {
		background = Variables.BACKGROUND;
		foreground = Variables.FOREGROUND;
		arcWidth   = 2;

		removeFromView.setToolTipText("Clears the sin.");
		removeFromView.setFocusTraversalKeysEnabled(false);
		removeFromView.mouse_over = JMColor.DARK_RED;
		removeFromView.addActionListener(e -> setVisible(false));
		removeFromView.addKeyPressListener(e -> {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				setVisible(false);
			}
		});

		text.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		text.addKeyPressListener(e -> {

		});

		setLayout(new GridBagLayout());

		moveFocusToNext(text, removeFromView);
		moveFocusToNext(removeFromView, text);

		paintBorderOnFocus();
		setFontSize(15);

		makeGUI();
		setVisible(false);
	}

	public void moveFocusToNext(Component current, Component next) {
		current.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_TAB) {
					e.consume();
					next.requestFocus();
				}
			}
		});
	}

	private void makeGUI() {
		int c = 9;
		int d = borderWidth;
		int e = 8;

		int x = 0;

		GBC.addWithGBC(this, text,           1.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(e, e, e, 0), 1);
		GBC.addWithGBC(this, removeFromView, 0.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(d, 0, d, c), 1);
	}

	public void setUpFancyLabel(JMLabel fl) {
		fl.setOpaque(false);
		fl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	}

	/**
	 * If any component is clicked or focused, component's border is painted.
	 */
	private void paintBorderOnFocus() {
		JComponent[] components = {text, removeFromView};

		for(JComponent c : components) {
			c.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					setFocusPainted(false);
				}

				@Override
				public void focusGained(FocusEvent e) {
					setFocusPainted(true);
				}
			});

			if(c instanceof JMLabel) {
				((JMLabel) c).addMouseClickedListener(e -> {
					text.requestFocus();
				});
			}

			if(c instanceof JMDivider) {
				((JMDivider) c).addMouseClickedListener(e -> text.requestFocus());
			}

			if(c instanceof SubtitleEditorButton) {
				((SubtitleEditorButton) c).addActionListener(e -> {
					text.requestFocus();
				});
			}
		}
	}

	/**
	 * Sets the font size of all the components displaying text.
	 * @param fontSize the new font size.
	 */
	public void setFontSize(int fontSize) {
		text.setFontSize(fontSize);
	}
}
