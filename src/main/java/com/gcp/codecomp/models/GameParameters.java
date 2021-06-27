package com.gcp.codecomp.models;

import java.util.List;

public class GameParameters {
	
	String gameId;
	
	String roundId;
	
	Integer roundNumber;
	
	String status;
	
	Integer secretLength;
	
	List<Participant> participants;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getRoundId() {
		return roundId;
	}

	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSecretLength() {
		return secretLength;
	}

	public void setSecretLength(Integer secretLength) {
		this.secretLength = secretLength;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	@Override
	public String toString() {
		return "GameParameters [gameId=" + gameId + ", roundId=" + roundId + ", roundNumber=" + roundNumber
				+ ", status=" + status + ", secretLength=" + secretLength + ", participants=" + participants + "]";
	}
	
	

}
