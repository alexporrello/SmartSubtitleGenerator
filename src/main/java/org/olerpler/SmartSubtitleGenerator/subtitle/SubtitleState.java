package org.olerpler.SmartSubtitleGenerator.subtitle;

/**
 * Manages the current state of a sin, whether the sin should be added, 
 * subtracted, or whether it should go unchanged.
 * @author Alexander Porrello
 */
public enum SubtitleState {
	
	ADD(0),
	SUBTRACT(1),
	DO_NOTHING(2);
	
	public int state;
	
	SubtitleState(int sinState) {
		if(sinState >= 0 && sinState <= 2) {
			this.state = sinState;
		} else {
			throw new IllegalArgumentException("The given SinState (" + sinState + ") is not valid.\n"
					+ " Valid SinStates are 0, 1, 2. Please see SinState documentation for more information.");
		}
	}

	
	/**
	 * @return the current SinState
	 */
	public int state() {
		return state;
	}
	
	/**
	 * Updates the sinState.
	 * @param sinState must be one of ADD, SUBTRACT, or DO_NOTHING.
	 */
	public void updateSinState(int sinState) {
		if(sinState >= 0 && sinState <= 2) {
			this.state = sinState;
		} else {
			throw new IllegalArgumentException("The given SinState (" + sinState + ") is not valid.\n"
					+ " Valid SinStates are 0, 1, 2. Please see SinState documentation for more information.");
		}
	}
	
	/**
	 * Given an integer, returns the proper SinState enum.
	 * @param sinState is the integer to be matched to a SinState.
	 * @return the proper SinState.
	 */
	public static SubtitleState returnProperEnum(int sinState) {
		if(sinState == ADD.state) {
			return ADD;
		} else if(sinState == SUBTRACT.state) {
			return SUBTRACT;
		} else if(sinState == DO_NOTHING.state) {
			return DO_NOTHING;
		} else {
			throw new IllegalArgumentException("The given SinState (" + sinState + ") is not valid.\n"
					+ " Valid SinStates are 0, 1, 2. Please see SinState documentation for more information.");
		}
	}
}