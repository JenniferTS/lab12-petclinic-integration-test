package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class VisitController {

	private final VisitService visitService;

	public VisitController(VisitService visitService) {
		this.visitService = visitService;
	}

	/**
	 * Lista todas las visitas o solo las de una mascota si se envia petId
	 */
	@GetMapping(value = "/visits")
	public ResponseEntity<List<VisitDTO>> findAll(@RequestParam(required = false) Integer petId) {
		if (petId != null)
			return ResponseEntity.ok(visitService.findByPetId(petId));
		return ResponseEntity.ok(visitService.findAll());
	}

	@GetMapping(value = "/visits/{id}")
	public ResponseEntity<VisitDTO> findById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(visitService.findById(id));
		} catch (VisitNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping(value = "/visits")
	public ResponseEntity<VisitDTO> create(@RequestBody VisitDTO visitDTO) {
		// la mascota y la fecha son obligatorias en la tabla visits
		if (visitDTO.getPetId() == null || visitDTO.getVisitDate() == null)
			return ResponseEntity.badRequest().build();
		VisitDTO created = visitService.create(visitDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping(value = "/visits/{id}")
	public ResponseEntity<VisitDTO> update(@RequestBody VisitDTO visitDTO, @PathVariable Long id) {
		try {
			visitDTO.setId(id);
			return ResponseEntity.ok(visitService.update(visitDTO));
		} catch (VisitNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping(value = "/visits/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		try {
			visitService.delete(id);
			return ResponseEntity.ok(" Delete ID :" + id);
		} catch (VisitNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
