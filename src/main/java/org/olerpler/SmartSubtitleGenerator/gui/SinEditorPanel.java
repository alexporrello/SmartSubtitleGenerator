package org.olerpler.SmartSubtitleGenerator.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JFileChooser;

import layout.GBC;

import org.olerpler.SmartSubtitleGenerator.UndoTextArea;
import org.olerpler.SmartSubtitleGenerator.gui.UndoTimeString.UndoTime;
import org.olerpler.SmartSubtitleGenerator.sin.BonusSin;
import org.olerpler.SmartSubtitleGenerator.sin.Sin;
import org.olerpler.SmartSubtitleGenerator.sin.SinManager;
import org.olerpler.SmartSubtitleGenerator.subtitle.SubtitleExporter;

import word.WordList;
import displays.JMPanel;
import displays.JMScrollPane;

public class SinEditorPanel extends JMPanel {
	private static final long serialVersionUID = -1604052028678375849L;

	/** Contains all of the sins **/
	private SinManager sins;

	/** For spell-checks in the text editing files **/
	private WordList wordlist = new WordList();

	/** All of the sins as they are displayed to the user **/
	private HashMap<Double, SinEditor> sinEditors = new HashMap<Double, SinEditor>();

	/** The panel upon which all of the SinEditors are displayed **/
	private JMPanel content;

	/** This is the object by which a user enters a sin **/
	public SinContributor sinContributor;

	/** The opacity for the overlays, for use in {@link #addNewSin(Sin, boolean)} **/
	public float overlayOpacity;

	/** The size of the interface: large, medium, or small **/
	public int interfaceSize;

	/** True if the user is using as a sins editor, not a wins editor **/
	public boolean sinsEditor = true;

	/** True if search mode is enabled; else, false **/
	public boolean searchMode = false;
	
	/** The array in which results are stored when the user searches **/
	private ArrayList<Sin> searchResultArray = new ArrayList<Sin>();
	
	/** The panel responsible for displaying all of the sins **/
	EditorPanel editorPanel;

	public SinEditorPanel(JMPanel panel, String fileURL, int interfaceSize, float overlayOpacity, Boolean premiereTime) {
		editorPanel = new EditorPanel(panel, fileURL, interfaceSize, premiereTime);

		this.overlayOpacity = overlayOpacity;

		setLayout(new BorderLayout());

		add(editorPanel, BorderLayout.CENTER);
		add(sinContributor, BorderLayout.SOUTH);
	}

	/**
	 * Adds a new sin to {@link #sinEditors}.
	 * @param s the sin to be added
	 */
	public void addNewSin(Sin s, boolean addedLater) {
		SinEditor toAdd = new SinEditor(s);

		toAdd.enableSpellCheck(wordlist);

		toAdd.getHoursField().addFocusLostListener(e -> {
			editorPanel.updateTimeAction(toAdd);});
		toAdd.getHoursField().addKeyPressListener(e -> {
			editorPanel.undoStack.add(new UndoTimeString(toAdd, UndoTime.HOUR));});

		toAdd.getMinutesField().addFocusLostListener(e -> {
			editorPanel.updateTimeAction(toAdd);});
		toAdd.getMinutesField().addKeyPressListener(e -> {
			editorPanel.undoStack.add(new UndoTimeString(toAdd, UndoTime.MINUTE));});

		toAdd.getSecondsField().addFocusLostListener(e -> {
			editorPanel.updateTimeAction(toAdd);});		
		toAdd.getSecondsField().addKeyPressListener(e -> {
			editorPanel.undoStack.add(new UndoTimeString(toAdd, UndoTime.SECOND));});

		toAdd.text.addKeyPressListener(e -> {
			char c = e.getKeyChar();
			boolean lowercase = (c >= 'a' && c <= 'z');
			boolean uppercase = (c >= 'A' && c <= 'Z');

			if(!lowercase && !uppercase && !e.isControlDown()) {
				editorPanel.undoStack.add(new UndoString(toAdd.text.getText(), 
						toAdd.key, toAdd.text.getCaretPosition()));
			}
		});
		toAdd.text.addKeyPressListener(e -> {
			if(e.getKeyCode() == KeyEvent.VK_TAB) {
				e.consume();
				toAdd.state.requestFocus();
			}
		});

		toAdd.state.setFocusable(true);
		toAdd.state.setFocusTraversalKeysEnabled(false);
		toAdd.state.addActionListener(e -> editorPanel.updateSinState(toAdd));
		toAdd.state.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent k) {
				if(k.getKeyCode() == KeyEvent.VK_ENTER) {
					editorPanel.updateSinState(toAdd);
				}

				if(k.getKeyCode() == KeyEvent.VK_TAB) {
					sinEditors.get(sins.getNextSin(toAdd.key).key).text.requestFocus();
				}
			}
		});

		toAdd.export.addActionListener(e -> {
			exportSingleSinOverlay(toAdd);
		});

		toAdd.delete.addActionListener(e -> editorPanel.deleteAction(toAdd));

		toAdd.setDisplaySize(interfaceSize);

		if(s.key == Sin.PLACEHOLDER_KEY) {
			toAdd.setGUIVisible(false);
		}

		sinEditors.put(s.key, toAdd);

		if(addedLater) {
			sins.add(s);
			editorPanel.updateSinState(toAdd);
		}

		refreshGUI();

		if(editorPanel != null)
			scrollToComponent(getSinEditor(s.key).text);
	}

	/**
	 * Given a query, searches through all of the sins in {@link #sins}.
	 * @param query the term to be searched for.
	 * @return the number of results discovered.
	 */
	public int search(String query) {
		int numMatches = 0;

		searchResultArray.clear();
		
		for(Sin s : sins) {
			if(sinEditors.get(s.key).search(query)) {
				searchResultArray.add(s);
				numMatches++;
			}
		}

		Collections.sort(searchResultArray);
		refreshGUI();
		
		return numMatches;
	}

	/**
	 * Updates the sins from the user's changes and writes the changes.
	 * @return true if the save is successful; else, false.
	 */
	public Boolean save() {
		updateSinsFromGUI();

		return sins.writeSinsToFile();
	}

	/**
	 * Updates {@link #sins} to match the user's input.
	 */
	public void updateSinsFromGUI() {
		ArrayList<Sin> toRemove = new ArrayList<Sin>();

		for(Sin s : sins) {
			if(sinEditors.containsKey(s.key)) {
				if(!sinEditors.get(s.key).getSin().equals(s)) {
					s.update(sinEditors.get(s.key).getSin());
				}
			} else {
				toRemove.add(s);
			}
		}

		for(Sin s : toRemove) {
			sins.remove(s);
		}
	}

	/**
	 * Sets the sin's starting number.
	 * @param startingNumber is the number of the first sin.
	 */
	public void setSinStartingNumber(int startingNumber) {
		sins.startingNumber = startingNumber;
		sins.reNumber();
		editorPanel.updateDisplayNumbers();
		refreshGUI();
	}

	/**
	 * Changes the interface size given a font size.
	 * @param fontSize the size of the font to be changed to.
	 */
	public void setInterfaceSize(int fontSize) {
		this.interfaceSize = fontSize;

		sinContributor.setInterfaceSize(fontSize);

		for(Sin s : sins) {
			if(sinEditors.containsKey(s.key)) {
				sinEditors.get(s.key).setDisplaySize(fontSize);
			}
		}
	}

	/**
	 * Requests focus for and scrolls to a SinEditor.
	 * @param key the key of the SinEditor
	 */
	public void scrollToSinEditor(Double key) {
		if(sinEditors.containsKey(key)) {
			sinEditors.get(key).text.requestFocus();
			scrollToComponent(sinEditors.get(key).text);
		}
	}

	/**
	 * Checks whether any of the sins have been modified.
	 * @return true if modified; else, false.
	 */
	public boolean modified() {		
		SinManager tempSM = new SinManager(sins.url);

		if(sinEditors.size() == tempSM.size()) {
			for(Double key : sinEditors.keySet()) {				
				if(!sinEditors.get(key).getSin().equals(tempSM.get(key))) {
					return true;
				}
			}
		} else {
			return true;
		}

		return false;
	}

	/**
	 * Removes all the sins from {@link #content} and re-populates.
	 */
	public void refreshGUI() {
		content.removeAll();

		int y = 0;
		int i = 0;

		if(searchMode) {
			for(Sin s : searchResultArray) {
				addSinToGUI(s, i, y);
				i++;
				y++;
			}
		} else {
			for(Sin s : sins) {
				addSinToGUI(s, i, y);
				i++;
				y++;
			}
		}

		revalidate();
		repaint();
	}

	private void addSinToGUI(Sin s, int i, int y) {
		if(sinEditors.containsKey(s.key)) {
			int a = -1 * sinEditors.get(s.key).borderWidth;

			if(i == sins.size()-1) {
				a = sinEditors.get(s.key).borderWidth;
			}

			GBC.addWithGBC(content, sinEditors.get(s.key).number(),
					0.0, 0.0, 0, y,   GBC.BOTH, GBC.WEST, 
					GBC.insets(0, 0, a, 0), 1);
			GBC.addWithGBC(content, sinEditors.get(s.key),
					1.0, 1.0, 1, y, GBC.BOTH, GBC.WEST, 
					GBC.insets(0, -3, a, 0), 1);
		}
	}
	
	/**
	 * Changes whether time is inserted manually or pasted from Premiere.
	 * @param time true if to be pasted from premiere; else, false.
	 */
	public void pasteFromPremiere(Boolean time) {
		sinContributor.pasteFromPremiere(time);
	}

	public void scrollToComponent(Component c2) {
		((UndoTextArea) c2).scrollRectToVisible(c2.getBounds());
	}

	/**
	 * Exports a single sin's overlay.
	 * @param se the single sin to be exported
	 */
	public void exportSingleSinOverlay(SinEditor se) {
		String exportPath = editorPanel.selectSubtitleExportPath();

		if(!exportPath.equals("")) {
			Sin    s    = se.getSin();
			String time = exportPath + "\\" + editorPanel.make2Digit(s.time.hour) + "-" + 
					editorPanel.make2Digit(s.time.minute) + "-" + editorPanel.make2Digit(s.time.second);

			new SubtitleExporter(" "   , s.number, true,  time, sinsEditor);
			new SubtitleExporter(s.text, s.number, false, time, sinsEditor);
		}
	}

	/**
	 * Exports the overlays of all the sins.
	 */
	public void exportAllSinOverlays() {
		String exportPath = editorPanel.selectSubtitleExportPath();

		if(!exportPath.equals("")) {			
			int BonusSinNumber = 0;

			String destination = new File(exportPath).getAbsolutePath();

			for(Sin s : sins) {
				String time = destination + "\\" + editorPanel.make2Digit(s.time.hour) + "-" + 
						editorPanel.make2Digit(s.time.minute) + "-" + editorPanel.make2Digit(s.time.second);

				if(s instanceof BonusSin) {
					new SubtitleExporter(s.text, s.number, true, time + BonusSinNumber++ + ".png", sinsEditor);
				} else {
					new SubtitleExporter(" "   , s.number, true,  time, sinsEditor);
					new SubtitleExporter(s.text, s.number, false, time, sinsEditor);
				}

				try {
					Desktop.getDesktop().open(new File(destination));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sets the export path for sins.
	 * @param path is the export path.
	 */
	public void setExportPath(String path) {
		sins.exportPath = path;
	}

	/**
	 * @return the export path for the sins.
	 */
	public String getExportPath() {
		return sins.exportPath;
	}

	/**
	 * @return the filename of the project file.
	 */
	public String getProjectName() {
		return new File(sins.url).getName();
	}

	/**
	 * Given a key, returns a SinEditor.
	 * @param key is the key of the SinEditor to be returned.
	 * @return the sin editor.
	 */
	public SinEditor getSinEditor(Double key) {
		if(sinEditors.containsKey(key)) {
			return sinEditors.get(key);
		} else {
			return null;
		}
	}
	
	public void undo() {
		editorPanel.undo();
	}
	
	public void redo() {
		editorPanel.redo();
	}

	public class EditorPanel extends JMScrollPane {
		private static final long serialVersionUID = 6036028097747709842L;

		/** The stack in which is held all of the redo objects **/
		private FancyStack<Object> undoStack = new FancyStack<Object>();

		/** The stack in which is held all of the undo objects **/
		private FancyStack<Object> redoStack = new FancyStack<Object>();

		public EditorPanel(JMPanel panel, String fileURL, int interfaceSize, Boolean premiereTime) {
			super(panel);

			setScrollBarWidth(15);

			sins = new SinManager(fileURL);

			content = panel;
			content.setLayout(new GridBagLayout());

			loadSinsIntoGUI();

			createSinContributor(premiereTime);
			setInterfaceSize(interfaceSize);
		}

		/**
		 * Clears the SinEditor components and replaces them with sins from 
		 * the file.
		 */
		private void loadSinsIntoGUI() {
			sinEditors.clear();	

			for(Sin s : sins) {			
				addNewSin(s, false);
			}

			refreshGUI();
		}

		/**
		 * Initializes the SinContributor.
		 * @param premiereTime true if the user is to paste time from Premiere.
		 */
		private void createSinContributor(Boolean premiereTime) {
			sinContributor = new SinContributor(premiereTime);

			sinContributor.text.enableSpellCheck(wordlist);
			sinContributor.text.addKeyPressListener(e -> {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					Sin sin = sinContributor.getSin();
					addNewSin(sin, true);
				}
			});

			sinContributor.add.addActionListener(e -> {
				Sin sin = sinContributor.getSin();
				addNewSin(sin, true);
			});
		}

		private void updateDisplayNumbers() {
			for(Sin s2 : sins) {
				if(sinEditors.containsKey(s2.key)) {
					sinEditors.get(s2.key).setNumber(s2.number);
				}
			}
		}

		private void updateTimeAction(SinEditor toAdd) {
			sins.get(toAdd.key).time = toAdd.getTime();
			updateSinState(toAdd);

			refreshGUI();

			toAdd.text.requestFocus();
			scrollToComponent(toAdd.text);
		}

		/**
		 * Updates the state of a given SinEditor and renumbers accordingly.
		 * @param toAdd the SinEditor whose sin is to be updated
		 */
		private void updateSinState(SinEditor toAdd) {
			if(sins.contains(toAdd.key)) {
				sins.get(toAdd.key).state = toAdd.state.state();
			}

			sins.reNumber();

			editorPanel.updateDisplayNumbers();
		}

		/**
		 * The method by which a SinEditor is deleted.
		 * @param toDelete the sinEditor to be deleted
		 */
		private void deleteAction(SinEditor toDelete) {
			undoStack.add(toDelete);

			sinEditors.remove(toDelete.key);
			sins.remove(toDelete.key);

			content.remove(toDelete);
			content.remove(toDelete.numberPanel);

			updateSinState(toDelete);
		}

		/**
		 * Creates a JFileChooser whereby the user selects overlay export path.
		 * @return the selected path if valid; else, an empty string.
		 */
		private String selectSubtitleExportPath() {
			JFileChooser fc;

			if(new File(getExportPath()).exists()) {
				fc = new JFileChooser(getExportPath());
			} else {
				fc = new JFileChooser();
			}

			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setDialogTitle("Please select the output destination for the overlays.");

			if(fc.showOpenDialog(this.getParent()) == JFileChooser.APPROVE_OPTION) {				
				setExportPath(fc.getSelectedFile().getAbsolutePath());
				return fc.getSelectedFile().getAbsolutePath();
			} else {
				fc.cancelSelection();
				return "";
			}
		}

		/**
		 * Converts a single digit number into a printable two digit number.
		 * @param i is the number to be processed.
		 * @return a two digit number (i.e. 9 would return as 09).
		 */
		private String make2Digit(int i) {
			if(i <= 9) {
				return "0" + i;
			}

			return i + "";
		}

		/**
		 * Kept on reserve for changes in the font.
		 * @param destination
		 */
		void exportTimeString(String destination) {
			for(int x = 0; x <= 9; x++) {
				String time2 = destination + "\\" + make2Digit(x) + "-" + 
						make2Digit(x);
				new SubtitleExporter(x + "", time2);
			}
		}
		
		/**
		 * The method by which an action is undone.
		 */
		public void undo() {
			if(editorPanel.undoStack.size() > 0) {
				if(editorPanel.undoStack.peek() instanceof SinEditor) {
					Sin s = ((SinEditor) editorPanel.undoStack.pop()).getSin();
					addNewSin(s, true);
					sinEditors.get(s.key).text.requestFocus();	

					editorPanel.redoStack.add(s);
				} else if(editorPanel.undoStack.peek() instanceof Sin) {
					Sin s = ((Sin) editorPanel.undoStack.pop());
					addNewSin(s, true);
					sinEditors.get(s.key).text.requestFocus();	

					editorPanel.redoStack.add(s);
				} else if(editorPanel.undoStack.peek() instanceof UndoString) {
					UndoString undo = ((UndoString) editorPanel.undoStack.pop());
					sinEditors.get(undo.key).text.setText(undo.text);
					sinEditors.get(undo.key).text.setCaretPosition(undo.caretPosition);
					sinEditors.get(undo.key).text.requestFocus();

					editorPanel.redoStack.add(undo);
				} else if(editorPanel.undoStack.peek() instanceof UndoTimeString) {
					UndoTimeString undo = ((UndoTimeString) editorPanel.undoStack.pop());

					if(undo.time == UndoTime.HOUR) {
						editorPanel.updateTimeAction(sinEditors.get(undo.key));
					} else if(undo.time == UndoTime.MINUTE) {
						editorPanel.updateTimeAction(sinEditors.get(undo.key));
					} else {
						editorPanel.updateTimeAction(sinEditors.get(undo.key));
					}

					editorPanel.redoStack.add(undo);
				}
			}
		}

		/**
		 * Method by which an action is re-done.
		 */
		public void redo() {
			if(redoStack.size() > 0) {
				if(redoStack.peek() instanceof Sin) {
					Sin s = ((Sin) redoStack.pop());				

					sins.remove(s.key);

					refreshGUI();
					updateDisplayNumbers();

					undoStack.add(s);
				} else if(redoStack.peek() instanceof UndoString) {
					UndoString undo = ((UndoString) redoStack.pop());
					sinEditors.get(undo.key).text.setText(undo.text);
					sinEditors.get(undo.key).text.setCaretPosition(undo.caretPosition);
					sinEditors.get(undo.key).text.requestFocus();

					undoStack.add(undo);
				} else if(redoStack.peek() instanceof UndoTimeString) {
					UndoTimeString undo = ((UndoTimeString) redoStack.pop());

					if(undo.time == UndoTime.HOUR) {
						updateTimeAction(sinEditors.get(undo.key));
					} else if(undo.time == UndoTime.MINUTE) {
						updateTimeAction(sinEditors.get(undo.key));
					} else {
						updateTimeAction(sinEditors.get(undo.key));
					}
				}
			}
		}
	}
}