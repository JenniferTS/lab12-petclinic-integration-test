package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VetSpecialtyRepository extends JpaRepository<VetSpecialty, VetSpecialtyId> {

	// especialidades de un veterinario
	List<VetSpecialty> findByVetId(Integer vetId);

	// veterinarios que tienen una especialidad
	List<VetSpecialty> findBySpecialtyId(Integer specialtyId);
}
