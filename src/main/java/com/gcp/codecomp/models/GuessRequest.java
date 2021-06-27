package com.gcp.codecomp.models;

import java.util.List;

public class GuessRequest {
	
	List<Guess> guesses;

	public List<Guess> getGuesses() {
		return guesses;
	}

	public void setGuesses(List<Guess> guesses) {
		this.guesses = guesses;
	}

	@Override
	public String toString() {
		return "GuessRequest [guesses=" + guesses + "]";
	}
	
	

}
