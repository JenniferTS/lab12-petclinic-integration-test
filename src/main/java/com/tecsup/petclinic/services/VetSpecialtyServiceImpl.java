package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyExistsException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.mapper.VetSpecialtyMapper;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

	private final VetSpecialtyRepository vetSpecialtyRepository;
	private final VetSpecialtyMapper vetSpecialtyMapper;

	public VetSpecialtyServiceImpl(VetSpecialtyRepository vetSpecialtyRepository, VetSpecialtyMapper vetSpecialtyMapper) {
		this.vetSpecialtyRepository = vetSpecialtyRepository;
		this.vetSpecialtyMapper = vetSpecialtyMapper;
	}

	@Override
	public VetSpecialtyDTO assign(VetSpecialtyDTO dto) throws VetSpecialtyAlreadyExistsException {
		// no se puede asignar dos veces la misma especialidad
		if (vetSpecialtyRepository.existsById(new VetSpecialtyId(dto.getVetId(), dto.getSpecialtyId())))
			throw new VetSpecialtyAlreadyExistsException("The vet already has this specialty...!");
		VetSpecialty saved = vetSpecialtyRepository.save(vetSpecialtyMapper.mapToEntity(dto));
		return vetSpecialtyMapper.mapToDto(saved);
	}

	@Override
	public VetSpecialtyDTO update(VetSpecialtyDTO dto) throws VetSpecialtyNotFoundException {
		findById(dto.getVetId(), dto.getSpecialtyId());
		VetSpecialty saved = vetSpecialtyRepository.save(vetSpecialtyMapper.mapToEntity(dto));
		return vetSpecialtyMapper.mapToDto(saved);
	}

	@Override
	public void remove(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException {
		findById(vetId, specialtyId);
		vetSpecialtyRepository.deleteById(new VetSpecialtyId(vetId, specialtyId));
	}

	@Override
	public VetSpecialtyDTO findById(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException {
		VetSpecialty vetSpecialty = vetSpecialtyRepository.findById(new VetSpecialtyId(vetId, specialtyId))
				.orElseThrow(() -> new VetSpecialtyNotFoundException("Record not found...!"));
		return vetSpecialtyMapper.mapToDto(vetSpecialty);
	}

	@Override
	public List<VetSpecialtyDTO> findByVetId(Integer vetId) {
		return vetSpecialtyMapper.mapToDtoList(vetSpecialtyRepository.findByVetId(vetId));
	}

	@Override
	public List<VetSpecialtyDTO> findBySpecialtyId(Integer specialtyId) {
		return vetSpecialtyMapper.mapToDtoList(vetSpecialtyRepository.findBySpecialtyId(specialtyId));
	}

	@Override
	public List<VetSpecialtyDTO> findAll() {
		return vetSpecialtyMapper.mapToDtoList(vetSpecialtyRepository.findAll());
	}
}
