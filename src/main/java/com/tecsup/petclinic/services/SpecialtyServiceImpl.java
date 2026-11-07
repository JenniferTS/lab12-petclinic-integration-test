package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.mapper.SpecialtyMapper;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SpecialtyServiceImpl implements SpecialtyService {

	private final SpecialtyRepository specialtyRepository;
	private final SpecialtyMapper specialtyMapper;

	public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository, SpecialtyMapper specialtyMapper) {
		this.specialtyRepository = specialtyRepository;
		this.specialtyMapper = specialtyMapper;
	}

	@Override
	public SpecialtyDTO create(SpecialtyDTO specialtyDTO) {
		Specialty saved = specialtyRepository.save(specialtyMapper.mapToEntity(specialtyDTO));
		return specialtyMapper.mapToDto(saved);
	}

	@Override
	public SpecialtyDTO update(SpecialtyDTO specialtyDTO) throws SpecialtyNotFoundException {
		// se busca el registro guardado y solo se cambian sus datos
		Specialty specialty = specialtyRepository.findById(specialtyDTO.getId())
				.orElseThrow(() -> new SpecialtyNotFoundException("Record not found...!"));
		specialty.setName(specialtyDTO.getName());
		specialty.setOffice(specialtyDTO.getOffice());
		specialty.setOpenHour(specialtyDTO.getOpenHour());
		specialty.setCloseHour(specialtyDTO.getCloseHour());
		return specialtyMapper.mapToDto(specialtyRepository.save(specialty));
	}

	@Override
	public void delete(Integer id) throws SpecialtyNotFoundException {
		findById(id);
		specialtyRepository.deleteById(id);
	}

	@Override
	public SpecialtyDTO findById(Integer id) throws SpecialtyNotFoundException {
		Specialty specialty = specialtyRepository.findById(id)
				.orElseThrow(() -> new SpecialtyNotFoundException("Record not found...!"));
		return specialtyMapper.mapToDto(specialty);
	}

	@Override
	public List<SpecialtyDTO> findByName(String name) {
		return specialtyMapper.mapToDtoList(specialtyRepository.findByName(name));
	}

	@Override
	public List<SpecialtyDTO> findAll() {
		return specialtyMapper.mapToDtoList(specialtyRepository.findAll());
	}
}
