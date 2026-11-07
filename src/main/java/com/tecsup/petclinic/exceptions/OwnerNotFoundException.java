package com.tecsup.petclinic.exceptions;

/**
 * Se lanza cuando no se encuentra el dueño buscado
 */
public class OwnerNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public OwnerNotFoundException(String message) {
		super(message);
	}
}
