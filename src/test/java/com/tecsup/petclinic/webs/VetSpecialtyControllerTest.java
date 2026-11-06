package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Ejercicio 6 : Pruebas de integracion del controlador de la relacion
 * veterinario - especialidad (/vet-specialties). Su llave es compuesta: vetId + specialtyId.
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetSpecialtyControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFindAllVetSpecialties() throws Exception {

		this.mockMvc.perform(get("/vet-specialties"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0].vetId", is(2)))
				.andExpect(jsonPath("$[0].specialtyId", is(1)));
	}

	@Test
	public void testFindVetSpecialtyOK() throws Exception {

		// Linda Douglas (3) - surgery (2)
		this.mockMvc.perform(get("/vet-specialties/3/2"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.vetId", is(3)))
				.andExpect(jsonPath("$.specialtyId", is(2)))
				.andExpect(jsonPath("$.yearsExperience", is(11)))
				.andExpect(jsonPath("$.primary", is(true)))
				.andExpect(jsonPath("$.certificationDate", is("2012-03-20")));
	}

	@Test
	public void testFindVetSpecialtyKO() throws Exception {

		// James Carter (1) no tiene especialidades
		mockMvc.perform(get("/vet-specialties/1/2"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testFindSpecialtiesByVet() throws Exception {

		mockMvc.perform(get("/vet-specialties").param("vetId", "3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void testFindVetsBySpecialty() throws Exception {

		// surgery (2) la tienen Linda Douglas (3) y Rafael Ortega (4)
		mockMvc.perform(get("/vet-specialties").param("specialtyId", "2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].vetId", is(3)))
				.andExpect(jsonPath("$[1].vetId", is(4)));
	}

	@Test
	public void testAssignSpecialtyToVet() throws Exception {

		// Henry Stevens (5) - dentistry (3)
		VetSpecialtyDTO dto = VetSpecialtyDTO.builder()
				.vetId(5)
				.specialtyId(3)
				.certificationDate("2025-05-20")
				.yearsExperience(1)
				.primary(false)
				.notes("Basic dental course")
				.build();

		this.mockMvc.perform(post("/vet-specialties")
						.content(om.writeValueAsString(dto))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.vetId", is(5)))
				.andExpect(jsonPath("$.specialtyId", is(3)))
				.andExpect(jsonPath("$.notes", is("Basic dental course")));

		// se quita para dejar los datos como estaban
		mockMvc.perform(delete("/vet-specialties/5/3"))
				.andExpect(status().isOk());
	}

	@Test
	public void testAssignDuplicateSpecialty() throws Exception {

		// Helen Leary (2) ya tiene radiology (1)
		VetSpecialtyDTO dto = VetSpecialtyDTO.builder()
				.vetId(2)
				.specialtyId(1)
				.yearsExperience(3)
				.build();

		this.mockMvc.perform(post("/vet-specialties")
						.content(om.writeValueAsString(dto))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict());
	}

	@Test
	public void testUpdateVetSpecialty() throws Exception {

		// Sharon Jenkins (6) - radiology (1)
		VetSpecialtyDTO dto = VetSpecialtyDTO.builder()
				.vetId(6)
				.specialtyId(1)
				.certificationDate("2022-03-10")
				.yearsExperience(2)
				.primary(false)
				.build();

		// ASSIGN
		mockMvc.perform(post("/vet-specialties")
						.content(om.writeValueAsString(dto))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		// UPDATE: mas anios de experiencia y pasa a ser su especialidad principal
		VetSpecialtyDTO upDto = VetSpecialtyDTO.builder()
				.certificationDate("2022-03-10")
				.yearsExperience(4)
				.primary(true)
				.notes("Now her main specialty")
				.build();

		mockMvc.perform(put("/vet-specialties/6/1")
						.content(om.writeValueAsString(upDto))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// FIND
		mockMvc.perform(get("/vet-specialties/6/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.yearsExperience", is(4)))
				.andExpect(jsonPath("$.primary", is(true)));

		// REMOVE
		mockMvc.perform(delete("/vet-specialties/6/1"))
				.andExpect(status().isOk());
	}

	@Test
	public void testRemoveSpecialtyFromVet() throws Exception {

		// Rafael Ortega (4) - dentistry (3)
		VetSpecialtyDTO dto = VetSpecialtyDTO.builder()
				.vetId(4)
				.specialtyId(3)
				.yearsExperience(1)
				.primary(false)
				.build();

		mockMvc.perform(post("/vet-specialties")
						.content(om.writeValueAsString(dto))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		mockMvc.perform(delete("/vet-specialties/4/3"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/vet-specialties/4/3"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testRemoveSpecialtyKO() throws Exception {

		mockMvc.perform(delete("/vet-specialties/1/3"))
				.andExpect(status().isNotFound());
	}
}
