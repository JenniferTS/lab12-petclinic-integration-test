package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyExistsException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.services.VetSpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class VetSpecialtyController {

	private final VetSpecialtyService vetSpecialtyService;

	public VetSpecialtyController(VetSpecialtyService vetSpecialtyService) {
		this.vetSpecialtyService = vetSpecialtyService;
	}

	/**
	 * Lista todas las asignaciones, o filtra por veterinario (vetId) o por especialidad (specialtyId)
	 */
	@GetMapping(value = "/vet-specialties")
	public ResponseEntity<List<VetSpecialtyDTO>> findAll(@RequestParam(required = false) Integer vetId,
														 @RequestParam(required = false) Integer specialtyId) {
		if (vetId != null)
			return ResponseEntity.ok(vetSpecialtyService.findByVetId(vetId));
		if (specialtyId != null)
			return ResponseEntity.ok(vetSpecialtyService.findBySpecialtyId(specialtyId));
		return ResponseEntity.ok(vetSpecialtyService.findAll());
	}

	@GetMapping(value = "/vet-specialties/{vetId}/{specialtyId}")
	public ResponseEntity<VetSpecialtyDTO> findById(@PathVariable Integer vetId, @PathVariable Integer specialtyId) {
		try {
			return ResponseEntity.ok(vetSpecialtyService.findById(vetId, specialtyId));
		} catch (VetSpecialtyNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping(value = "/vet-specialties")
	public ResponseEntity<VetSpecialtyDTO> assign(@RequestBody VetSpecialtyDTO dto) {
		if (dto.getVetId() == null || dto.getSpecialtyId() == null)
			return ResponseEntity.badRequest().build();
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(vetSpecialtyService.assign(dto));
		} catch (VetSpecialtyAlreadyExistsException e) {
			// la asignacion ya existe
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@PutMapping(value = "/vet-specialties/{vetId}/{specialtyId}")
	public ResponseEntity<VetSpecialtyDTO> update(@RequestBody VetSpecialtyDTO dto,
												  @PathVariable Integer vetId, @PathVariable Integer specialtyId) {
		try {
			dto.setVetId(vetId);
			dto.setSpecialtyId(specialtyId);
			return ResponseEntity.ok(vetSpecialtyService.update(dto));
		} catch (VetSpecialtyNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping(value = "/vet-specialties/{vetId}/{specialtyId}")
	public ResponseEntity<String> remove(@PathVariable Integer vetId, @PathVariable Integer specialtyId) {
		try {
			vetSpecialtyService.remove(vetId, specialtyId);
			return ResponseEntity.ok(" Delete vet " + vetId + " - specialty " + specialtyId);
		} catch (VetSpecialtyNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
