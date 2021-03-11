package com.ss.utopia.exceptions;

public class RouteAlreadyExistsException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public RouteAlreadyExistsException() {}
	public RouteAlreadyExistsException(String message) {
		super(message);
	}
}
