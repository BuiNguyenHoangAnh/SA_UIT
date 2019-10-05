package vn.uit.edu.sa.dto;

import java.io.Serializable;

public class Comment implements Serializable {
	public String message;
	
	public Comment() {

	}
	public Comment(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
