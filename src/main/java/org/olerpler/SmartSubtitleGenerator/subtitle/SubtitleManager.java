package org.olerpler.SmartSubtitleGenerator.subtitle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * A glorified ArrayList that doubles as a sin manger.
 * @author Alexander Porrello
 */
public class SubtitleManager extends ArrayList<Subtitle> implements Serializable {
	private static final long serialVersionUID = -1120726220040629802L;

	/** This is the url of the .gse file **/
	public String url;

	/** This is the path for sin exporting **/
	public String exportPath = "";

	/** Determines where to start counting for sin exporting **/
	public int startingNumber = 0;

	/**
	 * Manages sin files.
	 * @param url is the url from which files will be loaded.
	 */
	public SubtitleManager(String url) {
		this.url  = url;

		add(new Subtitle(0.0, 0, new SubtitleTime(88,88,88, 88), "", "", SubtitleState.ADD));

		readSinsFromFile();
	}

	/**
	 * When given a sin's key, the sin will be pulled out of the ArrayList.
	 * @param key is the key of the sin to be found.
	 * @return the sin that belongs to the key.
	 */
	public Subtitle get(Double key) {
		for(Subtitle s : this) {
			if(s.key.equals(key)) {
				return s;
			}
		}

		throw new NoSuchElementException();
	}

	public Subtitle getNextSin(Double key) {
		for(Subtitle s : this) {
			if(s.key.equals(key)) {
				if(indexOf(s.key)+1 <= size()) {
					return get(indexOf(s.key)+1);
				}
			}
		}

		throw new NoSuchElementException();
	}
	
	public int indexOf(Object o) {
		if(o instanceof Integer || o instanceof Subtitle) {
			return super.indexOf(o);
		} else if(o instanceof Double) {
			return super.indexOf(get((Double) o));
		}
		
		return 0;
	}

	@Override
	public boolean contains(Object o) {
		if(o instanceof Subtitle) {
			return containsKey(((Subtitle) o).key);
		} else if(o instanceof Double) {			
			return containsKey((Double) o);
		}

		return false;
	}

	/**
	 * Checks if this contains a sin's key.
	 * @param key the key to check.
	 * @return true if it is contained; else, false.
	 */
	private boolean containsKey(Double key) {
		for(Subtitle s : this) {
			if(s.key == key) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Adds a sin and then sorts and numbers it.
	 */
	@Override
	public boolean add(Subtitle sin) {
		super.add(sin);

		sort();
		reNumber();

		return true;
	}

	/**
	 * Removes a sin and then sorts and numbers it.
	 */
	@Override
	public boolean remove(Object o) {
		if(o instanceof Subtitle) {
			super.remove(o);

			reNumber();

			return true;
		} else if(o instanceof Double) {
			super.remove(this.get((Double) o));

			reNumber();

			return true;
		}

		return false;
	}

	/**
	 * Updates sins already in the array and re-numbers the array.
	 * @param sin is the updated version of the sin.
	 * @return true if the sin was updated.
	 */
	public Boolean updateSin(Subtitle sin) {
		for(Subtitle s : this) {
			if(s.key == sin.key) {
				this.remove(s);
				this.add(sin);

				reNumber();

				return true;
			}
		}

		return false;
	}

	/** Sorts the sins and gives each sin its proper number **/
	public void reNumber() {
		sort();

		int sinNumber  = startingNumber;
		//int multiplier = 0;

		for(Subtitle s : this) {
			if(s.visible) {
				s.number = sinNumber;

				if(s.state == SubtitleState.ADD) {
					sinNumber++;
				} else if(s.state == SubtitleState.SUBTRACT) {
					sinNumber--;
				}
			}
		}
	}

	/**
	 * Sorts all of the sins.
	 */
	public void sort() {
		Collections.sort(this);		
	}

	/**
	 * Reads the sins from {@link #url}.
	 */
	public Boolean readSinsFromFile() {		
		try {
			if(new File(url).exists()) {
				BufferedReader br = new BufferedReader(new FileReader(url));

				String thisLine;

				while ((thisLine = br.readLine()) != null) {
					if(thisLine.startsWith("<sin>")) {
						Subtitle toPut = new Subtitle(thisLine);

						if(toPut.key != Subtitle.PLACEHOLDER_KEY) {
							add(toPut);
						}
					} else if(thisLine.startsWith("<bsin>")) {
						BonusSubtitle toPut = new BonusSubtitle(thisLine);

						if(toPut.key != Subtitle.PLACEHOLDER_KEY) {
							add(toPut);
						}
					} else if(thisLine.startsWith("<ssn>")) {
						startingNumber = Integer.parseInt(
								(String) thisLine.subSequence(thisLine.indexOf("<ssn>") + 5, 
										thisLine.indexOf("</ssn>")));
					} else if(thisLine.startsWith("<ep>")) {
						exportPath = (String) thisLine.subSequence(thisLine.indexOf("<ep>") + 4, 
								thisLine.indexOf("</ep>"));
					}
				}

				br.close();
				return true;
			} else {
				writeSinsToFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Writes all the sins to {@link #url}.
	 */
	public Boolean writeSinsToFile() {
		try {
			if(!new File(url).exists()) {
				File makeFile = new File(url);
				makeFile.createNewFile();
			}

			String toWrite = "";

			for(Subtitle s : this) {
				toWrite = s.toString() + "\n" + toWrite;
			}

			if(size() > 0) {
				toWrite = "<ssn>" + startingNumber + "</ssn>" + "\n" + toWrite;
				toWrite = "<ep>"  + exportPath     + "</ep>"  + "\n" + toWrite;

				toWrite = toWrite.substring(0, toWrite.lastIndexOf("\n"));
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(url));
			writer.write(toWrite);
			writer.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public Boolean modified() {
		//TODO make this work.
		Boolean flag = false;

		SubtitleManager savedFile = new SubtitleManager(url);

		for(Subtitle s : this) {
			if(savedFile.contains(s)) {
				if(!savedFile.get(s.key).equals(s)) {
					System.out.println(true + " 3");
					return true;
				}
			} else {
				System.out.println(true + " 4");
				return true;
			}
		}

		return flag;
	}

	/**
	 * Given a search keyword, this goes through all the sins and finds 
	 * those whose text contains this keyword.
	 * @param s is the search keyword.
	 * @return is an ArrayList of all the sins that matched the query.
	 */
	public ArrayList<Subtitle> find(String s) {
		ArrayList<Subtitle> toReturn = new ArrayList<Subtitle>();

		for(Subtitle thisSin : this) {
			if(thisSin.text.contains(s)) {
				toReturn.add(thisSin);
			}
		}

		return toReturn;
	}
}