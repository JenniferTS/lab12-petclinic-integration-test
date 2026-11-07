package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mapper.OwnerMapper;
import com.tecsup.petclinic.repositories.OwnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OwnerServiceImpl implements OwnerService {

	private final OwnerRepository ownerRepository;
	private final OwnerMapper ownerMapper;

	public OwnerServiceImpl(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
		this.ownerRepository = ownerRepository;
		this.ownerMapper = ownerMapper;
	}

	@Override
	public OwnerDTO create(OwnerDTO ownerDTO) {
		Owner saved = ownerRepository.save(ownerMapper.mapToEntity(ownerDTO));
		return ownerMapper.mapToDto(saved);
	}

	@Override
	public OwnerDTO update(OwnerDTO ownerDTO) throws OwnerNotFoundException {
		// se busca el registro guardado y solo se cambian sus datos
		Owner owner = ownerRepository.findById(ownerDTO.getId())
				.orElseThrow(() -> new OwnerNotFoundException("Record not found...!"));
		owner.setFirstName(ownerDTO.getFirstName());
		owner.setLastName(ownerDTO.getLastName());
		owner.setAddress(ownerDTO.getAddress());
		owner.setCity(ownerDTO.getCity());
		owner.setTelephone(ownerDTO.getTelephone());
		return ownerMapper.mapToDto(ownerRepository.save(owner));
	}

	@Override
	public void delete(Long id) throws OwnerNotFoundException {
		findById(id);
		ownerRepository.deleteById(id);
	}

	@Override
	public OwnerDTO findById(Long id) throws OwnerNotFoundException {
		Owner owner = ownerRepository.findById(id)
				.orElseThrow(() -> new OwnerNotFoundException("Record not found...!"));
		return ownerMapper.mapToDto(owner);
	}

	@Override
	public List<OwnerDTO> findByLastName(String lastName) {
		return ownerMapper.mapToDtoList(ownerRepository.findByLastName(lastName));
	}

	@Override
	public List<OwnerDTO> findAll() {
		return ownerMapper.mapToDtoList(ownerRepository.findAll());
	}
}
