package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.mapper.VisitMapper;
import com.tecsup.petclinic.repositories.PetRepository;
import com.tecsup.petclinic.repositories.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Transactional
public class VisitServiceImpl implements VisitService {

	private final VisitRepository visitRepository;
	private final PetRepository petRepository;
	private final VisitMapper visitMapper;

	public VisitServiceImpl(VisitRepository visitRepository, PetRepository petRepository, VisitMapper visitMapper) {
		this.visitRepository = visitRepository;
		this.petRepository = petRepository;
		this.visitMapper = visitMapper;
	}

	@Override
	public VisitDTO create(VisitDTO visitDTO) {
		Visit visit = visitMapper.mapToEntity(visitDTO);
		visit.setPet(petRepository.getReferenceById(visitDTO.getPetId()));
		return visitMapper.mapToDto(visitRepository.save(visit));
	}

	@Override
	public VisitDTO update(VisitDTO visitDTO) throws VisitNotFoundException {
		Visit visit = visitRepository.findById(visitDTO.getId())
				.orElseThrow(() -> new VisitNotFoundException("Record not found...!"));
		visit.setVisitDate(LocalDate.parse(visitDTO.getVisitDate()));
		visit.setDescription(visitDTO.getDescription());
		visit.setCost(visitDTO.getCost());
		visit.setVetId(visitDTO.getVetId());
		if (visitDTO.getPetId() != null)
			visit.setPet(petRepository.getReferenceById(visitDTO.getPetId()));
		return visitMapper.mapToDto(visitRepository.save(visit));
	}

	@Override
	public void delete(Long id) throws VisitNotFoundException {
		findById(id);
		visitRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public VisitDTO findById(Long id) throws VisitNotFoundException {
		Visit visit = visitRepository.findById(id)
				.orElseThrow(() -> new VisitNotFoundException("Record not found...!"));
		return visitMapper.mapToDto(visit);
	}

	@Override
	@Transactional(readOnly = true)
	public List<VisitDTO> findByPetId(Integer petId) {
		return visitMapper.mapToDtoList(visitRepository.findByPet_Id(petId));
	}

	@Override
	@Transactional(readOnly = true)
	public List<VisitDTO> findAll() {
		return visitMapper.mapToDtoList(visitRepository.findAll());
	}
}
