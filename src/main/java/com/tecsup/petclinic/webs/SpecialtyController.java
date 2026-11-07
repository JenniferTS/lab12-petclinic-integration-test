package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class SpecialtyController {

	private final SpecialtyService specialtyService;

	public SpecialtyController(SpecialtyService specialtyService) {
		this.specialtyService = specialtyService;
	}

	/**
	 * Lista todos los registros o filtra por name si se envia como parametro
	 */
	@GetMapping(value = "/specialties")
	public ResponseEntity<List<SpecialtyDTO>> findAll(@RequestParam(required = false) String name) {
		if (name != null)
			return ResponseEntity.ok(specialtyService.findByName(name));
		return ResponseEntity.ok(specialtyService.findAll());
	}

	@GetMapping(value = "/specialties/{id}")
	public ResponseEntity<SpecialtyDTO> findById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(specialtyService.findById(id));
		} catch (SpecialtyNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping(value = "/specialties")
	public ResponseEntity<SpecialtyDTO> create(@RequestBody SpecialtyDTO specialtyDTO) {
		// el campo name es obligatorio
		if (specialtyDTO.getName() == null || specialtyDTO.getName().isBlank())
			return ResponseEntity.badRequest().build();
		SpecialtyDTO created = specialtyService.create(specialtyDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping(value = "/specialties/{id}")
	public ResponseEntity<SpecialtyDTO> update(@RequestBody SpecialtyDTO specialtyDTO, @PathVariable Integer id) {
		try {
			specialtyDTO.setId(id);
			return ResponseEntity.ok(specialtyService.update(specialtyDTO));
		} catch (SpecialtyNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping(value = "/specialties/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		try {
			specialtyService.delete(id);
			return ResponseEntity.ok(" Delete ID :" + id);
		} catch (SpecialtyNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
