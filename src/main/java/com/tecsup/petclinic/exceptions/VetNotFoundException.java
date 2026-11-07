package com.tecsup.petclinic.exceptions;

/**
 * Se lanza cuando no se encuentra el veterinario buscado
 */
public class VetNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public VetNotFoundException(String message) {
		super(message);
	}
}
