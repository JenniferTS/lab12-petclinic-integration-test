package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;

import java.util.List;

public interface VisitService {

	VisitDTO create(VisitDTO visitDTO);

	VisitDTO update(VisitDTO visitDTO) throws VisitNotFoundException;

	void delete(Long id) throws VisitNotFoundException;

	VisitDTO findById(Long id) throws VisitNotFoundException;

	List<VisitDTO> findByPetId(Integer petId);

	List<VisitDTO> findAll();
}
