package org.olerpler.SmartSubtitleGenerator.gui;

import icons.FancyIcon;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import org.olerpler.SmartSubtitleGenerator.sin.SinState;

public class SinStateButton extends SinEditorButton {
	private static final long serialVersionUID = 8053269937583104140L;

	private ImageIcon[] icons = {FancyIcon.ADD, FancyIcon.SUBTRACT, FancyIcon.NOTHING};

	private int selected = 0;
	
	public SinStateButton(SinState sinState) {
		super(FancyIcon.ADD);
		
		setState(sinState);
		
		setOpaque(false);
		addActionListener(e -> cycle());
		addKeyPressListener(e -> {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				cycle();
			}
		});
	}
	
	public void setState(SinState state) {
		selected = state.state;
		setIcon(icons[selected]);
	}
	
	private void cycle() {		
		if(selected <= 1) {
			selected++;
		} else {
			selected = 0;
		}
		
		setIcon(icons[selected]);
	}
	
	public SinState state() {		
		if(selected == 0) {
			return SinState.ADD;
		} else if(selected == 1) {
			return SinState.SUBTRACT;
		} else {
			return SinState.DO_NOTHING;
		}
	}
}