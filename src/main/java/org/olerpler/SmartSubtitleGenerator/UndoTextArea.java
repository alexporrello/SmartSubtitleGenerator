package org.olerpler.SmartSubtitleGenerator;

import java.awt.Font;

import text.JMTextArea;

/**
 * A label that contains a key.
 * @author Alexander Porrello
 */
public class UndoTextArea extends JMTextArea {
	private static final long serialVersionUID = -8921199230431829152L;

	public Double key;

	public UndoTextArea(String s, Double key) {
		super(s);

		this.key = key;
	}
	
	/**
	 * Changes the size of the font.
	 */
	public void setFontSize(int fontSize) {
		this.setFont(new Font(this.getFont().getName(), this.getFont().getStyle(), fontSize));
	}
	
	/** For storing a string for later reference in the undo stack **/
	public static class StringKey {
		
		public static final String TEXT_AREA  = "text_area";
		public static final String FORM_LABEL = "form_label";
		
		public String string;
		public String name;
		public Double key;
		public int    caretPosn;

		public StringKey(String string, String name, int caretPosn, Double key) {
			this.string    = string;
			this.caretPosn = caretPosn;
			this.key       = key;
			this.name      = name;
		}
	}

}
