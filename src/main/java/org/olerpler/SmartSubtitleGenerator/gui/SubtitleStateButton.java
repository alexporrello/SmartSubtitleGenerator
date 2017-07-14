package org.olerpler.SmartSubtitleGenerator.gui;

import icons.FancyIcon;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import org.olerpler.SmartSubtitleGenerator.subtitle.SubtitleState;

public class SubtitleStateButton extends SubtitleEditorButton {
	private static final long serialVersionUID = 8053269937583104140L;

	private ImageIcon[] icons = {FancyIcon.ADD, FancyIcon.SUBTRACT, FancyIcon.NOTHING};

	private int selected = 0;
	
	public SubtitleStateButton(SubtitleState sinState) {
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
	
	public void setState(SubtitleState state) {
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
	
	public SubtitleState state() {		
		if(selected == 0) {
			return SubtitleState.ADD;
		} else if(selected == 1) {
			return SubtitleState.SUBTRACT;
		} else {
			return SubtitleState.DO_NOTHING;
		}
	}
}