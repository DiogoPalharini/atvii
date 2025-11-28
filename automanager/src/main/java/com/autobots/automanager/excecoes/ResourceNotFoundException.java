package com.autobots.automanager.excecoes;

public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
	
	public ResourceNotFoundException(String resourceName, Long id) {
		super(String.format("%s com id %d não encontrado", resourceName, id));
	}
}

