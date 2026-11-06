package com.tecsup.petclinic.exceptions;

/**
 * Se lanza cuando no se encuentra el tipo de mascota buscado
 */
public class PetTypeNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public PetTypeNotFoundException(String message) {
		super(message);
	}
}
