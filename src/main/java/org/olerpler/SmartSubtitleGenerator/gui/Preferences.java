package org.olerpler.SmartSubtitleGenerator.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Preferences {

	public static final String PREFS_URL = System.getenv("APPDATA") + "\\GSE_PREFS.txt";

	/** For use in {@link #openRecentProjectMenu()} **/
	public ArrayList<DateString> recentFiles = new ArrayList<DateString>();

	public String fileSaveLocation = "";

	/** The size of the interface: large, medium, or small **/
	public int interfaceSize = Variables.INTERFACE_SIZE_SMALL;

	/** The opacity of video overlays **/
	public float overlayOpacity = .85f;

	/** Whether the time will be entered in manually or pasted from Premiere **/
	public Boolean premeireTime = false;

	public Preferences() {
		readPreferences();
	}

	/**
	 * Reads the preferences from the application data folder 
	 * for the user's OS.
	 * @return true if the file could be read; else, false.
	 */
	private Boolean readPreferences() {
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			try {
				String url;

				if(!new File(PREFS_URL).exists()) {
					new File(PREFS_URL).createNewFile();
				}

				BufferedReader br = new BufferedReader(new FileReader(PREFS_URL));

				while ((url = br.readLine()) != null) {
					if(!url.equals("")) {
						if(url.contains("<SL>")) {
							fileSaveLocation = url.replace("<SL>", "");
						} else if(url.contains("<d>")) {
							String[] split = url.split("<d>");

							if(split.length == 2) {
								DateString ds = new DateString(split[0], split[1]);

								if(new File(ds.string).exists()) {
									recentFiles.add(ds);
								}
							}
						} else if(url.contains("<IS>")) {
							interfaceSize = Integer.parseInt(url.replace("<IS>", ""));
						} else if(url.contains("<OO>")) {
							overlayOpacity = Float.parseFloat(url.replace("<OO>", ""));
						} else if(url.contains("<PT>")) {
							Boolean.parseBoolean(url.replace("<PT>", ""));
						}
					}
				}
				
				Collections.sort(recentFiles);
				
				for(int i = 6; i < recentFiles.size(); i++) {
					recentFiles.remove(i);
				}

				br.close();

				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			
		}

		return false;
	}

	/**
	 * Saves the user's preferences to the OS's app data folder.
	 * @return true if the prefs could be saved; else, false.
	 */
	public Boolean writePreferences() {
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			try {
				String content = "<SL>" + fileSaveLocation + "\n";

				content = content + "<IS>" + interfaceSize  + "\n";
				content = content + "<OO>" + overlayOpacity + "\n";
				content = content + "<PT>" + premeireTime   + "\n";

				for(DateString s : recentFiles) {
					content = content + s.string + "<d>" + s.timeStamp + "\n";
				}

				File file = new File(System.getenv("APPDATA") + "\\GSE_PREFS.txt");

				if(!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content);
				bw.close();

				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}
}
