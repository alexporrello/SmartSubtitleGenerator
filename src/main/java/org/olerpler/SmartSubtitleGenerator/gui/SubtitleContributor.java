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

import org.olerpler.SmartSubtitleGenerator.UndoTextArea;
import org.olerpler.SmartSubtitleGenerator.subtitle.Subtitle;
import org.olerpler.SmartSubtitleGenerator.subtitle.SubtitleState;
import org.olerpler.SmartSubtitleGenerator.subtitle.SubtitleTime;

import text.JMLabel;
import text.JMTextField;
import text.JMTextUtils;
import text.Word;
import colors.JMColor;
import displays.JMDivider;
import displays.RoundedPanel;

public class SubtitleContributor extends RoundedPanel {
	private static final long serialVersionUID = 1622970777711975521L;

	private JMTextField hours   = new JMTextField("00");
	private JMTextField minutes = new JMTextField("00");
	private JMTextField seconds = new JMTextField("00");

	private JMLabel colonA = new JMLabel(":");
	private JMLabel colonB = new JMLabel(":");

	private JMDivider div_a = new JMDivider();
	private JMDivider div_b = new JMDivider();

	public UndoTextArea   text  = new UndoTextArea("", Subtitle.generateKey());
	public SubtitleStateButton state = new SubtitleStateButton(SubtitleState.ADD);

	private PremiereTime premiereTime = new PremiereTime(text);

	public Boolean pasteFromPremiere = true;
	public String  contributor       = "GamingSins";

	public SubtitleEditorButton add   = new SubtitleEditorButton(FancyIcon.NEXT);
	public SubtitleEditorButton clear = new SubtitleEditorButton(FancyIcon.DELETE);

	public SubtitleContributor(Boolean pasteFromPremiere) {
		background = Variables.BACKGROUND;
		foreground = Variables.FOREGROUND;
		arcWidth   = 2;

		this.pasteFromPremiere = pasteFromPremiere;

		setLayout(new GridBagLayout());

		makeClearButton();
		makeAddButton();
		setUpPremiereTime();
		setUpState();
		setUpText();

		moveFocusToNext(text, state);
		moveFocusToNext(state, add);
		moveFocusToNext(add, clear);
		moveFocusToNext(clear, text);

		setUpFancyLabel(colonA);
		setUpFancyLabel(colonB);

		setUpFancyTextField(hours, 23, 23);
		setUpFancyTextField(minutes, 23, 23);
		setUpFancyTextField(seconds, 23, 23);

		setUpFancyTextField(hours, 23, 23);
		setUpFancyTextField(minutes, 23, 23);
		setUpFancyTextField(seconds, 23, 23);

		paintBorderOnFocus();
		setInterfaceSize(15);

		makeGUI();
	}

	private void makeClearButton() {
		clear.setToolTipText("Clears the sin.");
		//clear.setFocusTraversalKeysEnabled(false);
		clear.mouse_over = JMColor.DARK_RED;
		clear.addActionListener(e -> reset());
		clear.addKeyPressListener(e -> {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				reset();
			}
		});
	}

	private void makeAddButton() {
		add.setToolTipText("Adds the sin to the project.");
		//add.setFocusTraversalKeysEnabled(false);
		add.mouse_over = JMColor.DARK_GREEN;
		add.addKeyPressListener(e -> {

		});
	}

	private void setUpPremiereTime() {
		premiereTime.addKeyTypedListener(e -> {
			if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) { 
				text.requestFocus();
			}
		});
	}

	private void setUpState() {
		state.setToolTipText("Changes the sin's state.");
		//state.setFocusTraversalKeysEnabled(false);
	}

	private void setUpText() {
		text.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	}

	private void setUpFancyTextField(JMTextField fl, int width, int height) {
		fl.acceptOnlyNumbers(true);
		fl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		JMTextUtils.setFixedSize(fl, width, height);

		fl.addFocusGainedListener(e -> {
			fl.selectAll();
		});
	}

	private void setUpFancyLabel(JMLabel fl) {
		fl.setOpaque(false);
		fl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	}

	private void moveFocusToNext(Component current, Component next) {
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
		int a = 15;
		int b = -2;
		int c = 9;
		int d = borderWidth+1;
		int e = 15;

		int x = 0;

		if(!pasteFromPremiere) {
			GBC.addWithGBC(this, hours,   0.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(d, a, d, 0), 1);
			GBC.addWithGBC(this, colonA,  0.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(d, b, d, 0), 1);
			GBC.addWithGBC(this, minutes, 0.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(d, b, d, 0), 1);
			GBC.addWithGBC(this, colonB,  0.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(d, b, d, 0), 1);
			GBC.addWithGBC(this, seconds, 0.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(d, b, d, 0), 1);
		} else {
			GBC.addWithGBC(this, premiereTime, 1.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(d, a, d, 0), 1);
		}

		GBC.addWithGBC(this, div_a,   0.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(d, c, d, c), 1);
		GBC.addWithGBC(this, text,    1.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(e, 0, e, 0), 1);
		GBC.addWithGBC(this, div_b,   0.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(d, c, d, c), 1);
		GBC.addWithGBC(this, state,   0.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(d, 0, d, 0), 1);
		GBC.addWithGBC(this, add,     0.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(d, 0, d, 0), 1);
		GBC.addWithGBC(this, clear,  0.0, 0.0, x++, 0, GBC.BOTH, GBC.WEST, GBC.insets(d, 0, d, a), 1);

		revalidate();
		repaint();
	}

	/**
	 * If any component is clicked or focused, component's border is painted.
	 */
	private void paintBorderOnFocus() {
		JComponent[] components = {hours, colonA, minutes, colonB, seconds,
				div_a, text, div_b, state, add, clear, premiereTime.hours,
				premiereTime.minutes, premiereTime.seconds, 
				premiereTime.mSeconds, premiereTime.colonA, premiereTime.colonB,
				premiereTime.colonC};

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
	 * Sets all the fields to 0.
	 */
	private void reset() {
		hours.setText("00");
		minutes.setText("00");
		seconds.setText("00");

		premiereTime.reset();

		text.setText("");

		state.setState(SubtitleState.ADD);
	}

	/**
	 * Sets the font size of all the components displaying text.
	 * @param fontSize the new font size.
	 */
	public void setInterfaceSize(int fontSize) {		
		hours.setFontSize(fontSize);
		minutes.setFontSize(fontSize);
		seconds.setFontSize(fontSize);
		text.setFontSize(fontSize);

		Word word = new Word(hours.getText(), hours.getFont());

		JMTextUtils.setFixedSize(hours, word.width+(word.width/3), word.height);
		JMTextUtils.setFixedSize(minutes, word.width+(word.width/3), word.height);
		JMTextUtils.setFixedSize(seconds, word.width+(word.width/3), word.height);

		premiereTime.setInterfaceSize(fontSize);
	}

	public void pasteFromPremiere(Boolean premiere) {
		pasteFromPremiere = premiere;

		removeAll();
		makeGUI();
	}

	/**
	 * Fetches information from all the fields.
	 * @return a sin obtained from all input.
	 */
	public Subtitle getSin() {

		int hours    = 0;
		int minutes  = 0;
		int seconds  = 0;
		int mseconds = 0;

		if(pasteFromPremiere) {
			hours    = Integer.parseInt(SubtitleTime.twoDigits(
					premiereTime.hours.getText()));
			minutes  = Integer.parseInt(SubtitleTime.twoDigits(
					premiereTime.minutes.getText()));
			seconds  = Integer.parseInt(SubtitleTime.twoDigits(
					premiereTime.seconds.getText()));
			mseconds = Integer.parseInt(SubtitleTime.twoDigits(
					premiereTime.mSeconds.getText()));
		} else {
			hours = Integer.parseInt(SubtitleTime.twoDigits(
					Integer.parseInt(this.hours.getText())));
			minutes = Integer.parseInt(SubtitleTime.twoDigits(
					Integer.parseInt(this.minutes.getText())));
			seconds = Integer.parseInt(SubtitleTime.twoDigits(
					Integer.parseInt(this.seconds.getText())));
		}

		Subtitle toReturn = new Subtitle(Subtitle.generateKey(), 0, 
				new SubtitleTime(hours, minutes, seconds, mseconds), 
				text.getText(), contributor, state.state());

		reset();

		return toReturn;
	}
}
