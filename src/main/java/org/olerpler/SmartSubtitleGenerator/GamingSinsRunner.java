package org.olerpler.SmartSubtitleGenerator;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.olerpler.SmartSubtitleGenerator.gui.GamingSinsEditor;
import org.olerpler.SmartSubtitleGenerator.gui.Variables;

public class GamingSinsRunner {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			GamingSinsEditor editor = new GamingSinsEditor();
			
			//editor.loadFile("../GamingSinsEditor/test/Test.gse");			
			
			editor.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			editor.setIconImages(Variables.imageIcon());
			editor.setLocationByPlatform(true);
			editor.setSize(new Dimension(900, 500));
			editor.setPreferredSize(new Dimension(900, 500));
			editor.setVisible(true);
		});
	}
}
