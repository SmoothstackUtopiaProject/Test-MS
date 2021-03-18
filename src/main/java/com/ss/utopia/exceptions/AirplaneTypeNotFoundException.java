package com.ss.utopia.exceptions;

public class AirplaneTypeNotFoundException extends Exception{

	private static final long serialVersionUID = -8599630070070691448L;
	
	public AirplaneTypeNotFoundException() {}
	public AirplaneTypeNotFoundException(String message) {
		super(message);
	}
}