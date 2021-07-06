package com.gcp.codecomp.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Participant {

	String teamId;
	Long currentScore;
	Long totalScore;
	List<String> killedBy;
	Boolean isAlive;
	Boolean isRobot;
	
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public Long getCurrentScore() {
		return currentScore;
	}
	public void setCurrentScore(Long currentScore) {
		this.currentScore = currentScore;
	}
	public Long getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}
	public List<String> getKilledBy() {
		return killedBy;
	}
	public void setKilledBy(List<String> killedBy) {
		this.killedBy = killedBy;
	}
	public Boolean getIsAlive() {
		return isAlive;
	}
	public void setIsAlive(Boolean isAlive) {
		this.isAlive = isAlive;
	}
	public Boolean getIsRobot() {
		return isRobot;
	}
	public void setIsRobot(Boolean isRobot) {
		this.isRobot = isRobot;
	}
	@Override
	public String toString() {
		return "Participant [teamId=" + teamId + ", currentScore=" + currentScore + ", totalScore=" + totalScore
				+ ", killedBy=" + killedBy + ", isAlive=" + isAlive + ", isRobot=" + isRobot + "]";
	}
	
	
	
	
}
