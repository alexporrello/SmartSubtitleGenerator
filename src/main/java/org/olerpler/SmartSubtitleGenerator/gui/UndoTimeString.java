package org.olerpler.SmartSubtitleGenerator.gui;


public class UndoTimeString {
	public String timeString;
	public int caretPosition;
	public Double key;
	public UndoTime time;

	public UndoTimeString(SinEditor se, UndoTime time) {
		this.time = time;
		this.key  = se.key;

		if(time.equals(UndoTime.HOUR)) {
			caretPosition = se.getHoursField().getCaretPosition();
			timeString = se.getHoursField().getText();
		} else if(time.equals(UndoTime.MINUTE)) {
			caretPosition = se.getMinutesField().getCaretPosition();
			timeString = se.getMinutesField().getText();
		} else {
			caretPosition = se.getMinutesField().getCaretPosition();
			timeString = se.getMinutesField().getText();
		}
	}
	
	public enum UndoTime {
		HOUR, MINUTE, SECOND;
	}
}
