package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.entities.PetType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetTypeMapper {

	PetType mapToEntity(PetTypeDTO dto);

	PetTypeDTO mapToDto(PetType entity);

	List<PetTypeDTO> mapToDtoList(List<PetType> list);
}
