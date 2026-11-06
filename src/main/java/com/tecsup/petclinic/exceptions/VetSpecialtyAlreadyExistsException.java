package com.tecsup.petclinic.exceptions;

/**
 * Se lanza cuando se quiere asignar dos veces la misma especialidad a un veterinario
 */
public class VetSpecialtyAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public VetSpecialtyAlreadyExistsException(String message) {
		super(message);
	}
}
