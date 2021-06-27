package com.gcp.codecomp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class GameStatus {
	
	String requestId;
	
	@JsonProperty("data")
	GameParameters gameParameters;
	
	@JsonProperty("err")
	Error error;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public GameParameters getGameParameters() {
		return gameParameters;
	}

	public void setGameParameters(GameParameters gameParameters) {
		this.gameParameters = gameParameters;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "GameStatus [requestId=" + requestId + ", gameParameters=" + gameParameters + ", error=" + error + "]";
	}

}
