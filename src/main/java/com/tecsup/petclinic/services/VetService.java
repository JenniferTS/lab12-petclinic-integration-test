package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;

import java.util.List;

public interface VetService {

	VetDTO create(VetDTO vetDTO);

	VetDTO update(VetDTO vetDTO) throws VetNotFoundException;

	void delete(Integer id) throws VetNotFoundException;

	VetDTO findById(Integer id) throws VetNotFoundException;

	List<VetDTO> findByLastName(String lastName);

	List<VetDTO> findAll();
}
