package org.olerpler.SmartSubtitleGenerator.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.olerpler.SmartSubtitleGenerator.subtitle.Subtitle;
import org.olerpler.SmartSubtitleGenerator.subtitle.SubtitleTime;

import tabs.JMTab;
import tabs.JMTabPane;
import dialogs.JMDialog;
import displays.JMFrame;
import displays.JMPanel;

public class GamingSinsEditor extends JMFrame {
	private static final long serialVersionUID = -2941115389932488457L;

	SearchPanel       search  = new SearchPanel();
	JMTabPane         tabs    = new JMTabPane();
	Preferences       prefs   = new Preferences();
	SubtitleEditorMenuBar menu    = new SubtitleEditorMenuBar(this);

	public GamingSinsEditor() {
		setUpProgram();
	}

	public GamingSinsEditor(String url) {
		setUpProgram();
		loadFile(url);
	}

	private void setUpProgram() {
		setTitle("Smart Subtitle Generator");
		setJMenuBar(menu);
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(330, 200));
		setUpProgramCloseListener();
		setUpSearch();
		setUpDropTarget();
		
		add(tabs, BorderLayout.CENTER);
	}

	/**
	 * Watches for when the user closes the program. If some of the files have
	 * not been saved, the program prompts the user if they shoudl be saved.
	 */
	private void setUpProgramCloseListener() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Boolean modifiedCheck = false;

				for(JMTab tab : tabs.getTabArray()) {
					SubtitleEditorPanel current = (SubtitleEditorPanel) tab.content;

					if(current.modified()) {
						modifiedCheck = true;
						break;
					}
				}

				if(modifiedCheck) {
					confirmCloseWithoutSaveDialog();
				} else {
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Sets up the search feature.
	 */
	private void setUpSearch() {
		search.text.addKeyReleasedListener(e -> {
			try {
				getProjectInView().searchMode = true;
				getProjectInView().search(search.text.getText());
			} catch(NoSuchElementException f) {
				System.err.println("Cannot search because no project is loaded.");
			}
		});
		search.removeFromView.addMouseClickedListener(e -> {
			try {
				getProjectInView().searchMode = false;
				getProjectInView().refreshGUI();
			} catch(NoSuchElementException f) {
				System.err.println("Cannot search because no project is loaded.");
			}
		});
		add(search, BorderLayout.NORTH);
	}
	/**
	 * Makes it so users can open files by dragging and dropping
	 */
	private void setUpDropTarget() {
		this.getContentPane().setDropTarget(new DropTarget(this, new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent e) {				
				try {
					Transferable t = e.getTransferable();
					int d = e.getDropAction();
					e.acceptDrop(d);

					String url = t.getTransferData(DataFlavor.javaFileListFlavor).toString();
					url = url.substring(1, url.length()-1);

					e.dropComplete(true);

					String[] urls = url.split(", ");

					if(urls.length == 1) {
						loadFile(urls[0]);
					} else {
						if(urls[0].endsWith(".gse")) {
							loadFile(urls[0]);
						}

						for(int i = 1; i < urls.length; i++) {
							loadFile(urls[i]);
						}
					}
				} catch (UnsupportedFlavorException | IOException e1) {
					e1.printStackTrace();
				}
			}
		}));
	}

	/**
	 * @return The project that is currently in view.
	 */
	private SubtitleEditorPanel getProjectInView() {	
		if(tabs.selectedComponent() != null) {
			return ((SubtitleEditorPanel) tabs.selectedComponent());
		} else {
			throw new NoSuchElementException();
		}
	}

	/**
	 * A generic dialog that checks whether the user wants to save changes 
	 * to files before the program exits.
	 */
	private void confirmCloseWithoutSaveDialog() {
		JMDialog dialog = new JMDialog("GamingSins Editor", 
				"Would you like save changes to all modified files before closing?", 
				"Yes", "No",
				y -> {				
					for(JMTab tab : tabs.getTabArray()) {
						SubtitleEditorPanel current = (SubtitleEditorPanel) tab.content;
						current.save();
					}

					prefs.writePreferences();
					System.exit(0);
				},
				n -> {
					prefs.writePreferences();
					System.exit(0);
				});

		dialog.setBackground(Variables.BACKGROUND);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	/**
	 * @param url the file-to-be-loaded's URL
	 */
	public void loadFile(String url) {
		SubtitleEditorPanel seb = new SubtitleEditorPanel(new JMPanel(), url, 
				prefs.interfaceSize, prefs.overlayOpacity, prefs.premeireTime);

		String name = new File(url).getName();

		tabs.addTab(name, seb, e -> {
			JMDialog dialog = new JMDialog("GamingSins Editor", 
					"Would you like save changes to '" + ((SubtitleEditorPanel) 
							tabs.selectedComponent()).getProjectName() + 
							"' before closing?", "Yes", "No", 
							f -> {
								getProjectInView().save();
								prefs.writePreferences();
								tabs.closeTab(name);
							},
							g -> {
								setTitle("GamingSins Editor");
								tabs.closeTab(name);
							});

			dialog.setBackground(Variables.BACKGROUND);
			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);

			revalidate();
			repaint();
		}, f -> {/**TODO: Set the window's title **/});

		revalidate();
		repaint();
	}

	/** Sets the starting number for the project currently in view. **/
	public void setSinStartingNumber(int i) {
		try {
			getProjectInView().setSinStartingNumber(i);
		} catch(NoSuchElementException e) {
			System.err.println("Cannot set sin starting number because no "
					+ "project is loaded.");
		}
	}

	/**
	 * Sets the interface size for the overlays in {@link #prefs}.
	 * @param fontSize the size of the font to be set.
	 */
	public void setInterfaceSize(int fontSize) {
		prefs.interfaceSize = fontSize;

		try {
			getProjectInView().setInterfaceSize(fontSize);
		} catch(NoSuchElementException e) {
			System.err.println("The interface size cannot be set because "
					+ "there is no project loaded.");
		}
	}

	/**
	 * Sets the opacity for the overlays in {@link #prefs}
	 * @param overlayOpacity the new opacity of the overlays.
	 */
	public void setOverlayOpacity(float overlayOpacity) {
		try {
			getProjectInView().overlayOpacity = overlayOpacity;
			prefs.overlayOpacity = overlayOpacity;
		} catch(NoSuchElementException e) {
			System.err.println("Overlay opacity cannot be set because "
					+ "there is no project loaded.");
		}
	}

	/** Sets the sinEditorState for the project currently in view. **/
	public void setSubtitleEditorState(boolean sinsEditor) {
		try {
			getProjectInView().sinsEditor = sinsEditor;
		} catch(NoSuchElementException f) {
			System.err.println("Cannot set the SinEditorState because no "
					+ "project is loaded.");
		}
	}

	/**
	 * Sets whether the time should be pasted from Adobe Premier Pro.
	 * @param time true if the display should be from Premiere; else, false.
	 */
	public void premiereTime(Boolean time) {
		try {
			getProjectInView().pasteFromPremiere(time);
			prefs.premeireTime = time;
		} catch(NoSuchElementException e) {
			System.err.println("Cannot set Paste From Premiere because no "
					+ "project is loaded.");
		}
	}

	/** Saves the project currently in view. **/
	public boolean save() {
		try {
			return getProjectInView().save();
		} catch(NoSuchElementException e) {
			System.err.println("Cannot save because no project is loaded.");
			return false;
		}
	}

	/** Undoes an action for the project currently in view. **/
	public void undo() {
		try {
			getProjectInView().undo();
		} catch(NoSuchElementException e) {
			System.err.println("Cannot undo because no project is loaded.");
		}
	}

	/** Redoes an action for the project currently in view. **/
	public void redo() {
		try {
			getProjectInView().redo();
		} catch(NoSuchElementException e) {
			System.err.println("Cannot redo because no project is loaded.");
		}
	}

	/** Adds a new Bonus Sin to the project currently in view. **/
	public void addNewBonusSin() {
		try {
			Subtitle toAdd = new Subtitle();
			toAdd.time = new SubtitleTime(77, 77, 77, 77);

			getProjectInView().addNewSin(toAdd, true);
			getProjectInView().scrollToSinEditor(toAdd.key);
		} catch(NoSuchElementException e) {
			System.err.println("Cannot add a bonus sin because no "
					+ "project is loaded.");
		}
	}

	/**
	 * Exports all of the overlays for the selected Sin Editor.
	 */
	public void exportSinsOverlays() {
		try {
			getProjectInView().exportAllSinOverlays();
		} catch(NoSuchElementException e) {
			System.err.println("Cannot export sins overlays because no "
					+ "project is loaded.");
		}
	}

	/**
	 * @return the overlay opacity from {@link #prefs}
	 */
	public float overlayOpacity() {
		return prefs.overlayOpacity;
	}

	/** @return the interface size from {@link #prefs} **/
	public int interfaceSize() {
		return prefs.interfaceSize;
	}
}
