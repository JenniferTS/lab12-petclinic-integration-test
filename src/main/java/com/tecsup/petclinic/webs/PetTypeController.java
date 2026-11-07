package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.services.PetTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PetTypeController {

	private final PetTypeService petTypeService;

	public PetTypeController(PetTypeService petTypeService) {
		this.petTypeService = petTypeService;
	}

	/**
	 * Lista todos los registros o filtra por name si se envia como parametro
	 */
	@GetMapping(value = "/types")
	public ResponseEntity<List<PetTypeDTO>> findAll(@RequestParam(required = false) String name) {
		if (name != null)
			return ResponseEntity.ok(petTypeService.findByName(name));
		return ResponseEntity.ok(petTypeService.findAll());
	}

	@GetMapping(value = "/types/{id}")
	public ResponseEntity<PetTypeDTO> findById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(petTypeService.findById(id));
		} catch (PetTypeNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping(value = "/types")
	public ResponseEntity<PetTypeDTO> create(@RequestBody PetTypeDTO petTypeDTO) {
		// el campo name es obligatorio
		if (petTypeDTO.getName() == null || petTypeDTO.getName().isBlank())
			return ResponseEntity.badRequest().build();
		PetTypeDTO created = petTypeService.create(petTypeDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping(value = "/types/{id}")
	public ResponseEntity<PetTypeDTO> update(@RequestBody PetTypeDTO petTypeDTO, @PathVariable Integer id) {
		try {
			petTypeDTO.setId(id);
			return ResponseEntity.ok(petTypeService.update(petTypeDTO));
		} catch (PetTypeNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping(value = "/types/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		try {
			petTypeService.delete(id);
			return ResponseEntity.ok(" Delete ID :" + id);
		} catch (PetTypeNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
