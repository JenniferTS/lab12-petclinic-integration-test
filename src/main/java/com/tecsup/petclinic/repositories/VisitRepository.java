package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

	// visitas de una mascota (pet.id)
	List<Visit> findByPet_Id(Integer petId);

	@Override
	List<Visit> findAll();
}
