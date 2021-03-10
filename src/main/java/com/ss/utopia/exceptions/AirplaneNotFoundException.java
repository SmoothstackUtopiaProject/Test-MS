package com.ss.utopia.exceptions;

public class AirplaneNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public AirplaneNotFoundException() {};

	public AirplaneNotFoundException(String message) {
		super(message);
	}
}