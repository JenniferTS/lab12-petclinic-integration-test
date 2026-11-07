package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitMapper {

	// la mascota se asigna en el servicio a partir de petId
	@Mapping(target = "pet", ignore = true)
	Visit mapToEntity(VisitDTO dto);

	@Mapping(source = "pet.id", target = "petId")
	VisitDTO mapToDto(Visit entity);

	List<VisitDTO> mapToDtoList(List<Visit> list);
}
