package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.services.VetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class VetController {

	private final VetService vetService;

	public VetController(VetService vetService) {
		this.vetService = vetService;
	}

	/**
	 * Lista todos los registros o filtra por lastName si se envia como parametro
	 */
	@GetMapping(value = "/vets")
	public ResponseEntity<List<VetDTO>> findAll(@RequestParam(required = false) String lastName) {
		if (lastName != null)
			return ResponseEntity.ok(vetService.findByLastName(lastName));
		return ResponseEntity.ok(vetService.findAll());
	}

	@GetMapping(value = "/vets/{id}")
	public ResponseEntity<VetDTO> findById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(vetService.findById(id));
		} catch (VetNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping(value = "/vets")
	public ResponseEntity<VetDTO> create(@RequestBody VetDTO vetDTO) {
		// el campo lastName es obligatorio
		if (vetDTO.getLastName() == null || vetDTO.getLastName().isBlank())
			return ResponseEntity.badRequest().build();
		VetDTO created = vetService.create(vetDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping(value = "/vets/{id}")
	public ResponseEntity<VetDTO> update(@RequestBody VetDTO vetDTO, @PathVariable Integer id) {
		try {
			vetDTO.setId(id);
			return ResponseEntity.ok(vetService.update(vetDTO));
		} catch (VetNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping(value = "/vets/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		try {
			vetService.delete(id);
			return ResponseEntity.ok(" Delete ID :" + id);
		} catch (VetNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
