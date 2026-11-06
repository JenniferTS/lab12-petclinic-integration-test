package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.VetSpecialty;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VetSpecialtyMapper {

	VetSpecialty mapToEntity(VetSpecialtyDTO dto);

	VetSpecialtyDTO mapToDto(VetSpecialty entity);

	List<VetSpecialtyDTO> mapToDtoList(List<VetSpecialty> list);
}
