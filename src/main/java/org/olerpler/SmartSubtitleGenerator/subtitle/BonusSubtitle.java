package org.olerpler.SmartSubtitleGenerator.subtitle;

public class BonusSubtitle extends Subtitle {
	private static final long serialVersionUID = -2228987137101974024L;
	
	/** The multiplier used for a BonusSin **/
	public String multiplier;
	
	public BonusSubtitle() {
		this.contributor = "GamingSins";
		this.key         = Subtitle.generateKey();
		this.state       = SubtitleState.ADD;
		this.text        = "Bonus Sin";
		this.time        = new SubtitleTime(99, 99, 99, 99);
	}
	
	public BonusSubtitle(String s) {
		this.parseSinString(s);
	}
	
	@Override
	public void parseSinString(String bonusSinString) {
		if(bonusSinString.startsWith("<bsin>") && bonusSinString.endsWith("</bsin>")) {			
			this.key   = Subtitle.generateKey();
			this.time  = new SubtitleTime(99, 99, 99, 99);
			this.state = SubtitleState.ADD;
			this.text  = "Bonus Sin.";
			this.contributor = "GamingSins";
			this.number = Integer.parseInt((String) bonusSinString.subSequence(bonusSinString.indexOf("<n>") + 3, bonusSinString.indexOf("</n>")));
		} else {
			throw new IllegalArgumentException("The given sinBonusString (" + bonusSinString + ") is not valid.\n"
					+ "Please see documentation for more information on parseable strings.");
		}
	}
	
	/**
	 * Method by which a string is made that can be parsed.
	 * @return a parseable string.
	 */
	@Override
	public String toString() {
		return "<bsin>" +
		"<k>"  + this.key            + "</k>" +
		"<n>"  + this.number         + "</n>" +
		"<h>"  + this.time.hour      + "</h>" +
		"<m>"  + this.time.minute    + "</m>" +
		"<s>"  + this.time.second    + "</s>" +
		"<st>" + this.text           + "</st>" +
		"<c>"  + this.contributor    + "</c>" +
		"<ss>" + this.state.state    + "</ss>" +
		"</bsin>";
	}
}
