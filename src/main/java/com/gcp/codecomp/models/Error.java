package com.gcp.codecomp.models;

public class Error {
	
	String message;
	
	String description;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Error [message=" + message + ", description=" + description + "]";
	}
	
	

}
