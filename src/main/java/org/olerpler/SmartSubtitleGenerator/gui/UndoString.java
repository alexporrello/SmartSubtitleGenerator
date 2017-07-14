package org.olerpler.SmartSubtitleGenerator.gui;

public class UndoString {
	public String text;
	public int caretPosition;
	public Double key;

	public UndoString(String text, Double key, int caretPosition) {
		this.caretPosition = caretPosition;
		this.key           = key;
		this.text          = text;
	}
}
