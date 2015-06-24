package com.wonders.framework.exception;

public class WondersException extends Exception {

	public WondersException() {
		super();
	}

	public WondersException(String message, Throwable cause) {
		super(message, cause);
	}

	public WondersException(String message) {
		super(message);
	}

	public WondersException(Throwable cause) {
		super(cause);
	}
	
}
