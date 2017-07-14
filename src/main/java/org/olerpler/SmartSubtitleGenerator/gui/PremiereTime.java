package org.olerpler.SmartSubtitleGenerator.gui;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.function.Consumer;

import javax.swing.BorderFactory;

import layout.GBC;
import text.JMLabel;
import text.JMTextArea;
import text.JMTextField;
import text.JMTextUtils;
import text.Word;
import displays.JMPanel;
import displays.JMUtils;

public class PremiereTime extends JMPanel {
	private static final long serialVersionUID = 1061907685648994729L;

	JMTextField hours    = new JMTextField("00");
	JMTextField minutes  = new JMTextField("00");
	JMTextField seconds  = new JMTextField("00");
	JMTextField mSeconds = new JMTextField("00");

	JMLabel colonA = new JMLabel(":");
	JMLabel colonB = new JMLabel(":");
	JMLabel colonC = new JMLabel(":");

	public PremiereTime(JMTextArea moveFocusTo) {
		setLayout(new GridBagLayout());
		setBackground(Variables.FOREGROUND);

		setUpFancyLabel(colonA);
		setUpFancyLabel(colonB);
		setUpFancyLabel(colonC);

		setUpFancyTextField(hours,    20, 23);
		setUpFancyTextField(minutes,  20, 23);
		setUpFancyTextField(seconds,  20, 23);
		setUpFancyTextField(mSeconds, 20, 23);

		moveFocusToNext(hours, hours, minutes);
		moveFocusToNext(hours, minutes, seconds);
		moveFocusToNext(minutes, seconds, mSeconds);
		moveFocusToNext(seconds, mSeconds, mSeconds);

		setUpHours(moveFocusTo);

		makeGUI();
	}

	private void makeGUI() {
		removeAll();

		int x = 0;

		Insets insets = GBC.insets(0, 0, 0, 0);
		Insets lInset = GBC.insets(0, 0, 0, 2);

		GBC.addWithGBC(this, hours,    1.0, 1.0, x++, 0, 
				GBC.BOTH, GBC.WEST, insets, 1);
		GBC.addWithGBC(this, colonA,   1.0, 1.0, x++, 0, 
				GBC.BOTH, GBC.WEST, lInset, 1);
		GBC.addWithGBC(this, minutes,  1.0, 1.0, x++, 0, 
				GBC.BOTH, GBC.WEST, insets, 1);
		GBC.addWithGBC(this, colonB,   1.0, 1.0, x++, 0, 
				GBC.BOTH, GBC.WEST, lInset, 1);
		GBC.addWithGBC(this, seconds,  1.0, 1.0, x++, 0, 
				GBC.BOTH, GBC.WEST, insets, 1);
		GBC.addWithGBC(this, colonC,   1.0, 1.0, x++, 0, 
				GBC.BOTH, GBC.WEST, lInset, 1);
		GBC.addWithGBC(this, mSeconds, 1.0, 1.0, x++, 0, 
				GBC.BOTH, GBC.WEST, insets, 1);

		revalidate();
		repaint();
	}

	private void moveFocusToNext(JMTextField last, JMTextField current, JMTextField next) {
		current.addKeyPressListener(e -> {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if(!next.equals(current)) {
					if(current.getCaretPosition() == 2) {
						next.requestFocus();
						next.setCaretPosition(0);
					}
				}
			}

			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				if(!last.equals(current)) {
					if(current.getCaretPosition() == 0) {
						last.requestFocus();
						last.setCaretPosition(last.getText().length());
					}
				}
			}
		});
	}

	private void setUpHours(JMTextArea moveFocusTo) {		
		hours.addKeyPressListener(e -> pasteTime(moveFocusTo));
		hours.addFocusGainedListener(e -> pasteTime(moveFocusTo));
		moveFocusTo.addFocusGainedListener(e -> pasteTime(moveFocusTo));
	}

	private void pasteTime(JMTextArea moveFocusTo) {
		try {
			String [] split = ((String) Toolkit.getDefaultToolkit().
					getSystemClipboard().
					getData(DataFlavor.stringFlavor)).split(":");

			if(split.length == 4) {
				Boolean flag = true;

				for(int i = 0; i < 4; i++) {
					if(split[i].length() != 2) {
						flag = false;
					}
				}

				if(flag) {
					hours.setText(split[0]);
					minutes.setText(split[1]);
					seconds.setText(split[2]);
					mSeconds.setText(split[3]);

					moveFocusTo.requestFocus();
					
					StringSelection stringSelection = new StringSelection("");
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
					            stringSelection, null);
				}
			}
		} catch (UnsupportedFlavorException | IOException f) {
			f.printStackTrace();
		}
	}

	/** Sets all of the FancyTextFields to 00. **/
	public void reset() {
		resetTextField(hours);
		resetTextField(minutes);
		resetTextField(seconds);
		resetTextField(mSeconds);
	}

	/** Sets a FancyTextField field to 00. **/
	private void resetTextField(JMTextField fText) {
		fText.setText("00");
	}

	private void setUpFancyLabel(JMLabel fl) {
		fl.setOpaque(false);
		fl.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
	}

	private void setUpFancyTextField(JMTextField fl, int width, int height) {
		fl.acceptOnlyNumbers(true);
		fl.setOpaque(false);
		fl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		acceptOnlyTwoCharacters(fl);
		JMTextUtils.setFixedSize(fl, width, height);
	}

	private void acceptOnlyTwoCharacters(JMTextField ftf) {
		ftf.addKeyTypedListener(e -> {
			if(e.getKeyChar() >= 48 && e.getKeyChar() <= 57) {
				if(ftf.getText().length() == 2) {
					e.consume();
					java.awt.Toolkit.getDefaultToolkit().beep();
				}
			}
		});
	}

	/**
	 * Sets the font size of all the components displaying text.
	 * @param fontSize the new font size.
	 */
	public void setInterfaceSize(int fontSize) {		
		hours.setFontSize(fontSize);
		minutes.setFontSize(fontSize);
		seconds.setFontSize(fontSize);
		mSeconds.setFontSize(fontSize);
		colonA.setFontSize(fontSize);
		colonB.setFontSize(fontSize);
		colonC.setFontSize(fontSize);

		Word word  = new Word(hours.getText(), hours.getFont());
		Word word2 = new Word(colonA.getText(), colonA.getFont());

		int width  = word.width+(word.width/3);
		int width2 = word2.width+(word2.width/3);

		JMTextUtils.setFixedSize(hours, width, word.height);
		JMTextUtils.setFixedSize(minutes, width, word.height);
		JMTextUtils.setFixedSize(seconds, width, word.height);
		JMTextUtils.setFixedSize(mSeconds, width, word.height);
		JMUtils.setFixedSize(colonA, width2, word.height);
		JMUtils.setFixedSize(colonB, width2, word.height);
		JMUtils.setFixedSize(colonC, width2, word.height);

		JMUtils.setFixedSize(this, (width*4)+(width2*3), word.height);

		revalidate();
		repaint();
		makeGUI();
	}

	public void addKeyTypedListener(Consumer<KeyEvent> f) {
		hours.addKeyPressListener(e -> {
			f.accept(e);
		});
	}
}
