package com.gcp.codecomp.models;

public class Guess {
	
	String team;
	
	String guess;

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getGuess() {
		return guess;
	}

	public void setGuess(String guess) {
		this.guess = guess;
	}

	@Override
	public String toString() {
		return "Guess [team=" + team + ", guess=" + guess + "]";
	}
	
	

}
