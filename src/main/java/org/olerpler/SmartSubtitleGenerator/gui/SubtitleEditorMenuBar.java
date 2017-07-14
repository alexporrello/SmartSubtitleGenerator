package org.olerpler.SmartSubtitleGenerator.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import text.JMLabel;
import text.Unicode;
import displays.JMPanel;

public class SubtitleEditorMenuBar extends JMenuBar {
	private static final long serialVersionUID = -8334027312726918658L;

	GamingSinsEditor gse;

	public SubtitleEditorMenuBar(GamingSinsEditor gse) {
		this.gse = gse;

		add(new FileMenu());
		add(new EditMenu());
		add(new WindowMenu());
		add(new SubtitleMenu());
		
		setBorder(BorderFactory.createEmptyBorder(2, 2, 4, 2));
	}

	private class FileMenu extends JMenu {
		private static final long serialVersionUID = 7975462796772264052L;

		public FileMenu() {
			super("File");

			add(newProject());
			add(openProject());
			add(recentProject());

			addSeparator();

			add(save());
			add(saveAs());
		}

		public JMenuItem newProject() {
			JMenuItem newProject = new JMenuItem("New Project");

			newProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
					(Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));

			newProject.addActionListener(e -> {
				JFileChooser fc;

				if(new File(gse.prefs.fileSaveLocation).exists()) {
					fc = new JFileChooser(gse.prefs.fileSaveLocation);
				} else {
					fc = new JFileChooser();
				}

				fc.setFileFilter(new FileNameExtensionFilter("Smart Subtitle Editor (*.gse)", "gse"));				
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setDialogTitle("Set Project Save Location");

				if(fc.showSaveDialog(this.getParent().getParent()) == JFileChooser.APPROVE_OPTION) {

					gse.prefs.fileSaveLocation = "";
					String[] split = fc.getSelectedFile().getAbsolutePath().
							replace("\\", "\\\\").split("\\\\");
					for(int i = 0; i < split.length-1; i++) {
						gse.prefs.fileSaveLocation = gse.prefs.fileSaveLocation + split[i] + "\\";
					}

					String path = fc.getSelectedFile().getAbsolutePath();
					if(!path.endsWith(".gse")) {
						path = path + ".gse";
					}

					gse.prefs.recentFiles.add(new DateString(path));					
					gse.loadFile(path);
				} else {
					fc.cancelSelection();
				}
			});

			return newProject;
		}

		public JMenuItem openProject() {
			JMenuItem openProject = new JMenuItem("Open Project...");

			openProject.addActionListener(e -> {
				JFileChooser fc;

				if(new File(gse.prefs.fileSaveLocation).exists()) {
					fc = new JFileChooser(gse.prefs.fileSaveLocation);
				} else {
					fc = new JFileChooser();
				}

				fc.setFileFilter(new FileNameExtensionFilter("Smart Subtitle Editor (*.gse)", "gse"));				
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setDialogTitle("Open Project");

				if(fc.showOpenDialog(this.getParent().getParent()) == JFileChooser.APPROVE_OPTION) {
					gse.prefs.fileSaveLocation = "";
					String[] split = fc.getSelectedFile().getAbsolutePath().
							replace("\\", "\\\\").split("\\\\");
					for(int i = 0; i < split.length-1; i++) {
						gse.prefs.fileSaveLocation = gse.prefs.fileSaveLocation + split[i] + "\\";
					}

					gse.prefs.recentFiles.add(new DateString(fc.getSelectedFile().getAbsolutePath()));
					gse.loadFile(fc.getSelectedFile().getAbsolutePath());
				} else {
					fc.cancelSelection();
				}
			});

			return openProject;
		}

		public JMenu recentProject() {
			JMenu recentProject = new JMenu("Open Recent...");

			for(DateString s : gse.prefs.recentFiles) {
				JMenuItem add = new JMenuItem(s.string);
				
				add.addActionListener(e -> {
					s.updateTimeStamp();
					gse.loadFile(s.string);
				});

				recentProject.add(add);
			}

			return recentProject;
		}

		public JMenuItem save() {
			JMenuItem save = new JMenuItem("Save");
			save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					(Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
			save.addActionListener(e -> gse.save());

			return save;
		}

		public JMenuItem saveAs() {
			JMenuItem saveAs = new JMenuItem("Save As...");

			saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
					(java.awt.event.InputEvent.SHIFT_MASK | 
							(Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
			saveAs.addActionListener(e -> saveAs());

			return saveAs;
		}
	}

	private class EditMenu extends JMenu {
		private static final long serialVersionUID = 354495771215164592L;

		private JMenuItem find;
		private JMenuItem undo;
		private JMenuItem redo;
		
		private JRadioButtonMenuItem subtitles;

		private EditMenu() {
			super("Edit");

			findMenu();

			addSeparator();

			undoMenu();
			redoMenu();
			
			addSeparator();
			
			subtitlesMenu();
		}

		/**
		 * The method by which a user searches through the GUI.
		 */
		private void findMenu() {
			find = new JMenuItem("Find");
			find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, 
					(Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
			find.addActionListener(e -> {
				gse.search.setVisible(true);
				gse.search.text.requestFocus();
			});

			add(find);
		}

		/**
		 * The method by which a user undoes his changes.
		 */
		private void undoMenu() {
			undo = new JMenuItem("Undo");

			undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 
					(Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
			undo.addActionListener(e -> gse.undo());

			add(undo);
		}

		private void redoMenu() {
			redo = new JMenuItem("Redo");

			redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 
					(Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
			redo.addActionListener(e -> gse.redo());

			add(redo);
		}
		
		private void subtitlesMenu() {
			subtitles = new JRadioButtonMenuItem("Subtitle Editor");
			
			subtitles.addActionListener(e -> gse.setSubtitleEditorState(subtitles.isSelected()));
			subtitles.setSelected(true);
			
			add(subtitles);
		}
	}


	private class WindowMenu extends JMenu {
		private static final long serialVersionUID = -7424165552383806997L;

		private JMenu interfaceSize;

		private Boolean selected = false;

		private WindowMenu() {
			super("Window");

			interfaceSizeMenu();
			pasteFromPremiereMenu();
			
			addSeparator();
			
			JMenuItem help = new JMenuItem("Help");
			help.addActionListener(e -> {
				new HelpWindow().setVisible(true);
			});
			add(help);
		}

		/**
		 * Menu by which the user sets the size of the GUI.
		 */
		private void interfaceSizeMenu() {
			interfaceSize = new JMenu("Interface Size");

			JRadioButtonMenuItem a = new JRadioButtonMenuItem("Small");
			JRadioButtonMenuItem b = new JRadioButtonMenuItem("Medium");
			JRadioButtonMenuItem c = new JRadioButtonMenuItem("Large");

			JRadioButtonMenuItem[] buttons = {a, b, c};

			if(gse.interfaceSize() == Variables.INTERFACE_SIZE_SMALL) {
				selectRadioButton(buttons, a);
			} else if(gse.interfaceSize() == Variables.INTERFACE_SIZE_MEDIUM) {
				selectRadioButton(buttons, b);
			} else if(gse.interfaceSize() == Variables.INTERFACE_SIZE_LARGE) {
				selectRadioButton(buttons, c);
			}

			// TODO Check to make sure the open project isn't null
			
			a.addActionListener(e -> {
				gse.setInterfaceSize(Variables.INTERFACE_SIZE_SMALL);
				selectRadioButton(buttons, a);
			});

			b.addActionListener(e -> {
				gse.setInterfaceSize(Variables.INTERFACE_SIZE_MEDIUM);
				selectRadioButton(buttons, b);
			});

			c.addActionListener(e -> { 
				gse.setInterfaceSize(Variables.INTERFACE_SIZE_LARGE);
				selectRadioButton(buttons, c);
			});

			interfaceSize.add(a);
			interfaceSize.add(b);
			interfaceSize.add(c);

			add(interfaceSize);
		}

		public void pasteFromPremiereMenu() {
			JRadioButtonMenuItem b = new JRadioButtonMenuItem("Paste time from Adobe Premiere" + Unicode.registered + " Pro CC");

			b.addActionListener(e -> {
				b.setSelected(selected = !selected);
				gse.premiereTime(selected);
			});

			add(b);
		}
	}


	private class SubtitleMenu extends JMenu {
		private static final long serialVersionUID = -3517729174350181238L;

		private JMenuItem importSubtitle;
		private JMenuItem exportSubtitle;
		private JMenu     subtitleOpacity;
		private JMenuItem newBonusSubtitle;
		private JMenuItem startingSubtitleNumber;

		private SubtitleMenu() {
			super("Subtitle");

			importSubtitleMenu();

			exportSubtitleMenu();

			overlayOpacityMenu();		

			addSeparator();

			newBonusSubtitleMenu();

			addSeparator();

			startingSubtitleNumber();

		}

		private void importSubtitleMenu() {
			//editor.editors.updateSinsFromGUI();

			importSubtitle = new JMenuItem("Import Subtitle...");
			importSubtitle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
					(Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));

			importSubtitle.addActionListener(e -> {
				//SinSubmission ssm = new SinSubmission(editor);
				//ssm.setLocationRelativeTo(this.getParent());
				//ssm.setVisible(true);
				//editor.modified(false);
			});

			add(importSubtitle);
		}

		/**
		 * Creates the menu by which a user exports subtitles.
		 */
		private void exportSubtitleMenu() {
			exportSubtitle = new JMenuItem("Export Subtitles...");

			exportSubtitle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
					(Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
			exportSubtitle.addActionListener(e -> {
				gse.exportSinsOverlays();
			});

			add(exportSubtitle);
		}

		/**
		 * Creates the menu by which a user changes overlay opacity.
		 */
		private void overlayOpacityMenu() {
			subtitleOpacity = new JMenu("Overlay Opacity");

			JRadioButtonMenuItem one_hundred  = new JRadioButtonMenuItem("100\u0025");
			JRadioButtonMenuItem eighty_five  = new JRadioButtonMenuItem("85\u0025");
			JRadioButtonMenuItem seventy_five = new JRadioButtonMenuItem("75\u0025");
			JRadioButtonMenuItem fifty        = new JRadioButtonMenuItem("50\u0025");
			JRadioButtonMenuItem twenty_five  = new JRadioButtonMenuItem("25\u0025");
			JRadioButtonMenuItem zero         = new JRadioButtonMenuItem("0\u0025");

			JRadioButtonMenuItem[] buttons = {one_hundred, eighty_five, seventy_five, fifty, twenty_five, zero};
			
			if(gse.overlayOpacity() == 1.0f) {
				selectRadioButton(buttons, one_hundred);
				//gse.setOverlayOpacity(1.0f);
			} else if(gse.overlayOpacity() == .85f) {
				selectRadioButton(buttons, eighty_five);
				//gse.setOverlayOpacity(.85f);
			} else if(gse.overlayOpacity() == .75f) {
				selectRadioButton(buttons, seventy_five);
				//gse.setOverlayOpacity(.75f);
			} else if(gse.overlayOpacity() == .5f) {
				selectRadioButton(buttons, fifty);
				//gse.setOverlayOpacity(.5f);
			} else if(gse.overlayOpacity() == .25f) {
				selectRadioButton(buttons, twenty_five);
				//gse.setOverlayOpacity(.25f);
			} else if(gse.overlayOpacity() == 0.0f) {
				selectRadioButton(buttons, zero);
				//gse.setOverlayOpacity(0.0f);
			}

			one_hundred.addActionListener(e -> {
				selectRadioButton(buttons, one_hundred);
				gse.setOverlayOpacity(1.0f);
			});

			eighty_five.addActionListener(e -> {
				selectRadioButton(buttons, eighty_five);
				gse.setOverlayOpacity(.85f);
			});

			seventy_five.addActionListener(e -> {
				selectRadioButton(buttons, seventy_five);
				gse.setOverlayOpacity(.75f);
			});

			fifty.addActionListener(e -> {
				selectRadioButton(buttons, fifty);
				gse.setOverlayOpacity(.5f);
			});

			twenty_five.addActionListener (e -> {
				selectRadioButton(buttons, twenty_five);
				gse.setOverlayOpacity(.25f);
			});

			zero.addActionListener(e -> {
				selectRadioButton(buttons, zero);
				gse.setOverlayOpacity(0.0f);
			});

			subtitleOpacity.add(one_hundred);
			subtitleOpacity.add(eighty_five);
			subtitleOpacity.add(seventy_five);
			subtitleOpacity.add(fifty);
			subtitleOpacity.add(twenty_five);
			subtitleOpacity.add(zero);

			add(subtitleOpacity);
		}

		private void newBonusSubtitleMenu() {
			newBonusSubtitle = new JMenuItem("New Bonus Subtitle");
			newBonusSubtitle.addActionListener(e -> {
				gse.addNewBonusSin();
			});

			newBonusSubtitle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, 
					(java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));

			add(newBonusSubtitle);
		}

		private void startingSubtitleNumber() {
			startingSubtitleNumber = new JMenuItem();
			startingSubtitleNumber.setLayout(new BorderLayout());			
			
			JMPanel sinNumberPanel = new JMPanel();
			sinNumberPanel.setLayout(new BorderLayout());
			sinNumberPanel.setOpaque(false);
			
			JMLabel sinNumberLabel = new JMLabel("Subtitle Starting Number");
			sinNumberLabel.setOpaque(false);
			sinNumberLabel.setFont(new JMenuItem().getFont());
			sinNumberPanel.add(sinNumberLabel, BorderLayout.WEST);

			JTextField insert = new JTextField("0");
			sinNumberPanel.add(insert, BorderLayout.CENTER);
			
			startingSubtitleNumber.add(sinNumberPanel, BorderLayout.CENTER);
			
			
			insert.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					if(e.getKeyChar() < 48 || e.getKeyChar() > 57) {
						e.consume();
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if(insert.getText().length() > 0) {
						gse.setSinStartingNumber(
								Integer.parseInt(insert.getText()));
					}
				}
			});

			add(startingSubtitleNumber);
		}
	}

	/**
	 * Updates the sins from the GUI and displays to the user a 
	 * NewProjectDialog; then, it removes the asterisk from the 
	 * program title.
	 */
	//		private void saveAs() {
	//			//TODO Save As
	//			//			editor.editors.updateSinsFromGUI();
	//			//			NewProjectDialog npd = new NewProjectDialog("Save As", editor);
	//			//			npd.setLocationRelativeTo(this.getParent());
	//			//			npd.setVisible(true);
	//			//			editor.modified(false);
	//		}

	/**
	 * Instantiates the dialog by which the user selects the location 
	 * for a newly-created file.
	 */
	//		private void selectProjectLocationDialog() {
	//			//			NewProjectDialog spld = new NewProjectDialog("New Project", editor);
	//			//			spld.setLocationRelativeTo(this.getParent());
	//			//			spld.setVisible(true);
	//		}

	/**
	 * Selects the correct RadioButton and deselects the others.
	 * @param buttons are all the buttons that need deselecting.
	 * @param toSelect is the button that needs selecting.
	 */
	private void selectRadioButton(JRadioButtonMenuItem[] buttons, JRadioButtonMenuItem toSelect) {
		for(JRadioButtonMenuItem button : buttons) {
			button.setSelected(false);
		}

		toSelect.setSelected(true);
	}
}