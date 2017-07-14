package org.olerpler.SmartSubtitleGenerator.gui;

public class UndoObject {

	public Object o;
	public Double key;
	public int    caretPosn = 0;
	
	public UndoObject(Object o, Double key) {
		this.o = o;
		this.key = key;
	}
	
	public UndoObject(Object o, int carretPosn, Double key) {
		this.o = o;
		this.key = key;
		this.caretPosn = carretPosn;
	}
	
}
