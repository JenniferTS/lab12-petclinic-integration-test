package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class OwnerController {

	private final OwnerService ownerService;

	public OwnerController(OwnerService ownerService) {
		this.ownerService = ownerService;
	}

	/**
	 * Lista todos los registros o filtra por lastName si se envia como parametro
	 */
	@GetMapping(value = "/owners")
	public ResponseEntity<List<OwnerDTO>> findAll(@RequestParam(required = false) String lastName) {
		if (lastName != null)
			return ResponseEntity.ok(ownerService.findByLastName(lastName));
		return ResponseEntity.ok(ownerService.findAll());
	}

	@GetMapping(value = "/owners/{id}")
	public ResponseEntity<OwnerDTO> findById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(ownerService.findById(id));
		} catch (OwnerNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping(value = "/owners")
	public ResponseEntity<OwnerDTO> create(@RequestBody OwnerDTO ownerDTO) {
		// el campo lastName es obligatorio
		if (ownerDTO.getLastName() == null || ownerDTO.getLastName().isBlank())
			return ResponseEntity.badRequest().build();
		OwnerDTO created = ownerService.create(ownerDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping(value = "/owners/{id}")
	public ResponseEntity<OwnerDTO> update(@RequestBody OwnerDTO ownerDTO, @PathVariable Long id) {
		try {
			ownerDTO.setId(id);
			return ResponseEntity.ok(ownerService.update(ownerDTO));
		} catch (OwnerNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping(value = "/owners/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		try {
			ownerService.delete(id);
			return ResponseEntity.ok(" Delete ID :" + id);
		} catch (OwnerNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
