package org.olerpler.SmartSubtitleGenerator.gui;

import icons.FancyIcon;

import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

import layout.GBC;

import org.olerpler.SmartSubtitleGenerator.UndoTextArea;
import org.olerpler.SmartSubtitleGenerator.subtitle.BonusSubtitle;
import org.olerpler.SmartSubtitleGenerator.subtitle.Subtitle;
import org.olerpler.SmartSubtitleGenerator.subtitle.SubtitleTime;

import text.FormLabel;
import text.JMLabel;
import text.JMTextField;
import text.Word;
import word.WordList;
import colors.JMColor;
import displays.JMDivider;
import displays.JMPanel;
import displays.JMUtils;
import displays.RoundedPanel;

public class SubtitleEditor extends RoundedPanel {
	private static final long serialVersionUID = 4547753120886416521L;

	public String contributor = "";

	public RoundedPanel numberPanel;

	private JMLabel number;

	private TimeGUI time;

	private Boolean wide = true;

	private JMDivider div_a = new JMDivider();

	public UndoTextArea text;

	private JMDivider div_b = new JMDivider();

	public SubtitleStateButton state;

	public SubtitleEditorButton export = new SubtitleEditorButton(FancyIcon.NEXT);

	public SubtitleEditorButton delete = new SubtitleEditorButton(FancyIcon.DELETE);

	public Boolean bonusSin;

	public Double key;

	public SubtitleEditor(Subtitle s) {
		number  = new JMLabel(s.number + "");

		text    = new UndoTextArea(s.text, s.key);
		state   = new SubtitleStateButton(s.state);
		time    = new TimeGUI(s.time.hour, s.time.minute, s.time.second, s.time.msecond);
		key         = s.key;
		contributor = s.contributor;

		background = Variables.BACKGROUND;
		foreground = Variables.FOREGROUND;
		arcWidth   = 2;

		setLayout(new GridBagLayout());

		state.setToolTipText("Changes the sin's state.");
		export.setToolTipText("Exports the individual sin.");
		delete.setToolTipText("Deletes this sin.");
		delete.mouse_over = JMColor.DARK_RED;

		text.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		text.setOpaque(false);
		try {
			text.getJPopupMenu().add(new JMenuItem("Run!"));
		} catch(NoSuchElementException e) {
			
		}
		
		setUpNumberPanel();

		setUpFancyLabel(number);

		paintBorderOnFocus();
		setDisplaySize(15);

		if(s instanceof BonusSubtitle) {
			bonusSin = true;
		} else {
			bonusSin = false;
		}

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {								
				if(getWidth() >= 450) {
					if(!wide) {
						wide = true;
						makeGUI();
					}
				} else {
					if(wide) {
						wide = false;
						makeGUI();
					}
				}
			}			
		});

		makeWideGUI();
	}

	public void makeGUI() {	
		Boolean hasFocus = false;
		
		if(text.hasFocus()) {
			hasFocus = true;
		}
		
		removeAll();

		if(wide) {
			makeWideGUI();
		} else {
			makeNarrowGUI();
		}
		
		if(hasFocus) {
			text.requestFocus();
		}
	}

	public void makeNarrowGUI() {
		int a = 15;
		int b = -2;
		int d = borderWidth+1;
		int e = 15;

		int x = 0;
		int y = 0;

		JMPanel divA = new JMPanel();
		divA.setBackground(Variables.BACKGROUND_DARK);
		JMUtils.setFixedSize(divA, 1, 30);

		JMPanel divB = new JMPanel();
		divB.setBackground(Variables.BACKGROUND_DARK);
		JMUtils.setFixedSize(divB, 1, 1);

		int top = 15;

		GBC.addWithGBC(this, state,   0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(top, e, d, 0), 1);
		GBC.addWithGBC(this, export,  0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(top, 0, d, 0), 1);
		GBC.addWithGBC(this, delete,  0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(top, 0, d, a), 1);

		GBC.addWithGBC(this, divA,    0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(top, 0, d, a), 1);

		GBC.addWithGBC(this, time.hours,   0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(top, b, d, 0), 1);
		GBC.addWithGBC(this, time.colonA,  0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(top, b, d, 0), 1);
		GBC.addWithGBC(this, time.minutes, 0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(top, b, d, 0), 1);
		GBC.addWithGBC(this, time.colonB,  0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(top, b, d, 0), 1);
		GBC.addWithGBC(this, time.seconds, 0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(top, b, d, 0), 1);

		y++;
		x = 0;
		GBC.addWithGBC(this, divB, 1.0, 1.0, x, y, GBC.BOTH, GBC.WEST, GBC.insets(0, e, 0, e), 10);

		y++;
		GBC.addWithGBC(this, text, 1.0, 1.0, x, y, GBC.BOTH, GBC.WEST, GBC.insets(d, e, e, e), 10);
	}

	/**
	 * Makes a GUI that is over 
	 */
	public void makeWideGUI() {
		int a = 15;
		int b = -2;
		int c = 9;
		int d = borderWidth+1;
		int e = 15;

		int x = 0;
		int y = 0;
		
		if(!bonusSin){
			GBC.addWithGBC(this, time.hours,   0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, a, d, 0), 1);
			GBC.addWithGBC(this, time.colonA,  0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, b, d, 0), 1);
			GBC.addWithGBC(this, time.minutes, 0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, b, d, 0), 1);
			GBC.addWithGBC(this, time.colonB,  0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, b, d, 0), 1);
			GBC.addWithGBC(this, time.seconds, 0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, b, d, 0), 1);
			GBC.addWithGBC(this, div_a,        0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, c, d, c), 1);
			GBC.addWithGBC(this, text,         1.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(e, 0, e, 0), 1);
		} else {
			text.setEditable(false);
			GBC.addWithGBC(this, text, 1.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(e, a, e, 0), 1);
		}

		GBC.addWithGBC(this, div_b,   0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, c, d, c), 1);
		GBC.addWithGBC(this, state,   0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, 0, d, 0), 1);
		GBC.addWithGBC(this, export,  0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, 0, d, 0), 1);
		GBC.addWithGBC(this, delete,  0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, 0, d, a), 1);
	}
	
	/**
	 * Makes a GUI that is over 
	 */
	public void addToComponent(JComponent parent, int y) {
		int a = 15;
		int b = -2;
		int c = 9;
		int d = borderWidth+1;
		int e = 15;

		int x = 0;
		
		GBC.addWithGBC(parent, number(), 0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, a, d, 0), 1);

		if(!bonusSin){
			GBC.addWithGBC(parent, time.hours,   0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, a, d, 0), 1);
			GBC.addWithGBC(parent, time.colonA,  0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, b, d, 0), 1);
			GBC.addWithGBC(parent, time.minutes, 0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, b, d, 0), 1);
			GBC.addWithGBC(parent, time.colonB,  0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, b, d, 0), 1);
			GBC.addWithGBC(parent, time.seconds, 0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, b, d, 0), 1);
			GBC.addWithGBC(parent, div_a,        0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, c, d, c), 1);
			GBC.addWithGBC(parent, text,         1.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(e, 0, e, 0), 1);
		} else {
			text.setEditable(false);
			GBC.addWithGBC(parent, text, 1.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(e, a, e, 0), 1);
		}

		GBC.addWithGBC(parent, div_b,   0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, c, d, c), 1);
		GBC.addWithGBC(parent, state,   0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, 0, d, 0), 1);
		GBC.addWithGBC(parent, export,  0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, 0, d, 0), 1);
		GBC.addWithGBC(parent, delete,  0.0, 1.0, x++, y, GBC.BOTH, GBC.WEST, GBC.insets(d, 0, d, a), 1);
	}

	private void setUpFormLabel(FormLabel fl) {
		fl.field.acceptOnlyNumbers(true);
		fl.label.addKeyPressListener(e -> {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				text.requestFocus();
			}
		});
	}

	private void setUpFancyLabel(JMLabel fl) {
		fl.setOpaque(false);
		fl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	}

	private void setUpNumberPanel() {
		numberPanel = new RoundedPanel();//true, true, true, true);

		numberPanel.background = background;
		numberPanel.foreground = foreground;
		numberPanel.arcWidth   = borderWidth;

		numberPanel.setLayout(new GridBagLayout());
	}
	
	/**
	 * If any component is clicked or focused, component's border is painted.
	 */
	private void paintBorderOnFocus() {
		JComponent[] components = {time.hours, time.colonA, 
				time.minutes, time.colonB, time.seconds,
				div_a, text, div_b, state, export, delete};

		for(JComponent c : components) {
			c.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					setFocusPainted(false);

					numberPanel.setFocusPainted(false);
				}

				@Override
				public void focusGained(FocusEvent e) {
					setFocusPainted(true);

					numberPanel.setFocusPainted(true);
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

			if(c instanceof FormLabel) {
				((FormLabel) c).field.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						setFocusPainted(false);
					}

					@Override
					public void focusGained(FocusEvent e) {
						setFocusPainted(true);
					}
				});

				((FormLabel) c).label.addMouseClickedListener(e -> {
					if(e.getClickCount() == 1) {
						text.requestFocus();
					}
				});
			}

			//			if(c instanceof SinEditorButton) {
			//				((SinEditorButton) c).addMouseClickedListener(e -> {
			//					text.requestFocus();
			//				});
			//			}
		}
	}

	/**
	 * If any component is clicked or focused, component's border is painted.
	 */
	public void setTheme(SubtitleEditorTheme theme) {
		JComponent[] components = {time.hours, time.colonA, 
				time.minutes, time.colonB, time.seconds,
				div_a, text, div_b, state, export, delete};



		if(theme.equals(SubtitleEditorTheme.DARK)) {
			foreground = JMColor.WHITE;

			text.setForeground(JMColor.WHITE);
			

			this.foreground = JMColor.decode("#272727");
			this.background = JMColor.decode("#222222");
			this.border     = JMColor.decode("#252525");
		} else {
			foreground = JMColor.BLACK;
		}

		for(JComponent c : components) {
			c.addFocusListener(new FocusAdapter() {
				//TODO 
			});

			if(c instanceof JMLabel) {
				((JMLabel) c).setForeground(JMColor.PINK);
			}

			if(c instanceof JMDivider) {
				//TODO set FancyDivider color
			}

			if(c instanceof FormLabel) {
				//TODO Set FormLabel color
			}

			//			if(c instanceof SinEditorButton) {
			//				((SinEditorButton) c).addMouseClickedListener(e -> {
			//					text.requestFocus();
			//				});
			//			}
		}
	}

	/**
	 * Sets the font size of all the components displaying text.
	 * @param fontSize the new font size.
	 */
	public void setDisplaySize(int fontSize) {		
		number.setFontSize(fontSize);
		time.setFontSize(fontSize);
		text.setFontSize(fontSize);

		Word word = new Word(time.hours.getText(), time.hours.field.getFont());

		number.setAbsoluteSize(word.width+6, word.height);
		time.setSize(word);
	}

	/**
	 * Fetches information from all the fields.
	 * @return a sin obtained from all input.
	 */
	public Subtitle getSin() {
		return new Subtitle(text.key, Integer.parseInt(number.getText()), 
				time.getSinTime(), text.getText(),
				contributor, state.state());
	}

	/**
	 * Sets all the components on the GUI invisible.
	 * @param visible true if components visible; else, false.
	 */
	public void setGUIVisible(Boolean visible) {
		state.setVisible(false);
		export.setVisible(false);
		delete.setVisible(false);
		time.setVisible(false);
		text.setEditable(false);
		//text.setVisible(false);
		div_a.setVisible(false);
		div_b.setVisible(false);
	}

	/**
	 * @return this sin's number in a RoundedPanel for adding to the main GUI.
	 */
	public RoundedPanel number() {
		numberPanel.removeAll();

		GBC.addWithGBC(numberPanel, number, 1.0, 1.0, 0, 0, GBC.BOTH, GBC.NORTH, GBC.insets(5, 10, 5, 10), 1);

		return numberPanel;
	}

	public void setNumber(int i) {
		number.setText(i + "");
		number.revalidate();
		number.repaint();
	}

	public void enableSpellCheck(WordList wordlist) {
		text.enableSpellCheck(wordlist);
	}

	public Boolean search(String query) {
		return text.getText().contains(query);
//		if(text.getText().contains(query)) {
//			Highlighter highlighter = text.getHighlighter();
//			HighlightPainter painter = new DefaultHighlighter.
//					DefaultHighlightPainter(Variables.ACCENT);
//
//			int start = text.getText().indexOf(query);
//			int end   = text.getText().indexOf(query) + query.length();
//
//			try {
//				highlighter.addHighlight(start, end, painter);
//			} catch (BadLocationException e) {
//				e.printStackTrace();
//			}
//
//			return true;
//		}
//
//		return false;
	}

	public JMTextField getHoursField() {
		return time.hours.field;
	}

	public JMTextField getMinutesField() {
		return time.minutes.field;
	}

	public JMTextField getSecondsField() {
		return time.seconds.field;
	}

	public SubtitleTime getTime() {
		return time.getTime();
	}
	
	public class TimeGUI { 
		public FormLabel hours;

		public JMLabel colonA = new JMLabel(":");

		public FormLabel minutes;

		public JMLabel colonB = new JMLabel(":");

		public FormLabel seconds;
		
		public int mSecond = 0;

		TimeGUI(int hour, int minute, int second, int mSecond) {
			hours   = new FormLabel(SubtitleTime.twoDigits(hour), 23, 23);
			minutes = new FormLabel(SubtitleTime.twoDigits(minute), 23, 23);
			seconds = new FormLabel(SubtitleTime.twoDigits(second), 23, 23);
			
			this.mSecond = mSecond;

			hours.field.setOpaque(false);
			hours.label.setOpaque(false);
			hours.setOpaque(false);

			hours.setOpaque(false);
			minutes.setOpaque(false);
			seconds.setOpaque(false);

			setUpFancyLabel(colonA);
			setUpFancyLabel(colonB);

			setUpFormLabel(hours);
			setUpFormLabel(minutes);
			setUpFormLabel(seconds);
		}

		public SubtitleTime getTime() {
			return new SubtitleTime(SubtitleTime.twoDigits(hours.getText()), 
					SubtitleTime.twoDigits(minutes.getText()),
					SubtitleTime.twoDigits(seconds.getText()),
					SubtitleTime.twoDigits(mSecond));
		}

		public void setVisible(Boolean visible) {
			hours.setVisible(visible);
			minutes.setVisible(visible);
			seconds.setVisible(visible);
			colonA.setVisible(visible);
			colonB.setVisible(visible);
		}

		public SubtitleTime getSinTime() {
			return new SubtitleTime(hours.getText(), 
					minutes.getText(),
					seconds.getText(),
					mSecond + "");
		}
		
		public void setFontSize(int fontSize) {
			hours.setFontSize(fontSize);
			minutes.setFontSize(fontSize);
			seconds.setFontSize(fontSize);
		}
		
		public void setSize(Word word) {
			hours.setSize(word.width+(word.width/3), word.height);
			minutes.setSize(word.width+(word.width/3), word.height);
			seconds.setSize(word.width+(word.width/3), word.height);
		}
	}
}
