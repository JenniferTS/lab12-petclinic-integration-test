package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyExistsException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;

import java.util.List;

public interface VetSpecialtyService {

	VetSpecialtyDTO assign(VetSpecialtyDTO dto) throws VetSpecialtyAlreadyExistsException;

	VetSpecialtyDTO update(VetSpecialtyDTO dto) throws VetSpecialtyNotFoundException;

	void remove(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException;

	VetSpecialtyDTO findById(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException;

	List<VetSpecialtyDTO> findByVetId(Integer vetId);

	List<VetSpecialtyDTO> findBySpecialtyId(Integer specialtyId);

	List<VetSpecialtyDTO> findAll();
}
