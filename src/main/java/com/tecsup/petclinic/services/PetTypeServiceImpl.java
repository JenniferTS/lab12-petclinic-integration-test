package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.mapper.PetTypeMapper;
import com.tecsup.petclinic.repositories.PetTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PetTypeServiceImpl implements PetTypeService {

	private final PetTypeRepository petTypeRepository;
	private final PetTypeMapper petTypeMapper;

	public PetTypeServiceImpl(PetTypeRepository petTypeRepository, PetTypeMapper petTypeMapper) {
		this.petTypeRepository = petTypeRepository;
		this.petTypeMapper = petTypeMapper;
	}

	@Override
	public PetTypeDTO create(PetTypeDTO petTypeDTO) {
		PetType saved = petTypeRepository.save(petTypeMapper.mapToEntity(petTypeDTO));
		return petTypeMapper.mapToDto(saved);
	}

	@Override
	public PetTypeDTO update(PetTypeDTO petTypeDTO) throws PetTypeNotFoundException {
		// se busca el registro guardado y solo se cambian sus datos
		PetType petType = petTypeRepository.findById(petTypeDTO.getId())
				.orElseThrow(() -> new PetTypeNotFoundException("Record not found...!"));
		petType.setName(petTypeDTO.getName());
		petType.setDescription(petTypeDTO.getDescription());
		petType.setActive(petTypeDTO.getActive());
		petType.setSizeCategory(petTypeDTO.getSizeCategory());
		petType.setAverageLifespan(petTypeDTO.getAverageLifespan());
		petType.setCareLevel(petTypeDTO.getCareLevel());
		return petTypeMapper.mapToDto(petTypeRepository.save(petType));
	}

	@Override
	public void delete(Integer id) throws PetTypeNotFoundException {
		findById(id);
		petTypeRepository.deleteById(id);
	}

	@Override
	public PetTypeDTO findById(Integer id) throws PetTypeNotFoundException {
		PetType petType = petTypeRepository.findById(id)
				.orElseThrow(() -> new PetTypeNotFoundException("Record not found...!"));
		return petTypeMapper.mapToDto(petType);
	}

	@Override
	public List<PetTypeDTO> findByName(String name) {
		return petTypeMapper.mapToDtoList(petTypeRepository.findByName(name));
	}

	@Override
	public List<PetTypeDTO> findAll() {
		return petTypeMapper.mapToDtoList(petTypeRepository.findAll());
	}
}
