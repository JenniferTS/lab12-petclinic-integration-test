package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

	Owner mapToEntity(OwnerDTO dto);

	OwnerDTO mapToDto(Owner entity);

	List<OwnerDTO> mapToDtoList(List<Owner> list);
}
