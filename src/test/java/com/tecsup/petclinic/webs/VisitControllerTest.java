package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.VisitDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Ejercicio 4 : Pruebas de integracion del controlador de visitas (/visits)
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VisitControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFindAllVisits() throws Exception {

		this.mockMvc.perform(get("/visits"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0].id", is(1)));
	}

	@Test
	public void testFindVisitOK() throws Exception {

		this.mockMvc.perform(get("/visits/3"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(3)))
				.andExpect(jsonPath("$.petId", is(8)))
				.andExpect(jsonPath("$.vetId", is(3)))
				.andExpect(jsonPath("$.visitDate", is("2009-06-04")))
				.andExpect(jsonPath("$.description", is("neutered")))
				.andExpect(jsonPath("$.cost", is(250.0)));
	}

	@Test
	public void testFindVisitKO() throws Exception {

		mockMvc.perform(get("/visits/666"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testFindVisitsByPet() throws Exception {

		// la mascota 7 (Samantha) tiene 2 visitas en data.sql
		mockMvc.perform(get("/visits").param("petId", "7"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void testCreateVisit() throws Exception {

		VisitDTO newVisitDTO = VisitDTO.builder()
				.petId(1)
				.vetId(5)
				.visitDate("2026-10-30")
				.description("vaccination")
				.cost(new BigDecimal("80.50"))
				.build();

		this.mockMvc.perform(post("/visits")
						.content(om.writeValueAsString(newVisitDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.petId", is(1)))
				.andExpect(jsonPath("$.vetId", is(5)))
				.andExpect(jsonPath("$.visitDate", is("2026-10-30")))
				.andExpect(jsonPath("$.description", is("vaccination")))
				.andExpect(jsonPath("$.cost", is(80.5)));
	}

	@Test
	public void testCreateVisitBadRequest() throws Exception {

		// sin mascota no se puede registrar la visita
		VisitDTO newVisitDTO = VisitDTO.builder()
				.visitDate("2026-10-30")
				.description("sin mascota")
				.build();

		this.mockMvc.perform(post("/visits")
						.content(om.writeValueAsString(newVisitDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdateVisit() throws Exception {

		VisitDTO newVisitDTO = VisitDTO.builder()
				.petId(1)
				.vetId(5)
				.visitDate("2026-10-15")
				.description("general checkup")
				.cost(new BigDecimal("60.00"))
				.build();

		// CREATE
		ResultActions mvcActions = mockMvc.perform(post("/visits")
						.content(om.writeValueAsString(newVisitDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		// UPDATE: se agrego desparasitacion, cambia descripcion y costo
		VisitDTO upVisitDTO = VisitDTO.builder()
				.petId(1)
				.vetId(5)
				.visitDate("2026-10-15")
				.description("checkup and deworming")
				.cost(new BigDecimal("95.00"))
				.build();

		mockMvc.perform(put("/visits/" + id)
						.content(om.writeValueAsString(upVisitDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// FIND BY ID
		mockMvc.perform(get("/visits/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.description", is("checkup and deworming")))
				.andExpect(jsonPath("$.cost", is(95.0)));

		// DELETE
		mockMvc.perform(delete("/visits/" + id))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteVisit() throws Exception {

		VisitDTO newVisitDTO = VisitDTO.builder()
				.petId(1)
				.vetId(5)
				.visitDate("2026-09-01")
				.description("nail trimming")
				.cost(new BigDecimal("25.00"))
				.build();

		ResultActions mvcActions = mockMvc.perform(post("/visits")
						.content(om.writeValueAsString(newVisitDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		mockMvc.perform(delete("/visits/" + id))
				.andExpect(status().isOk());

		mockMvc.perform(get("/visits/" + id))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteVisitKO() throws Exception {

		mockMvc.perform(delete("/visits/" + "1000"))
				.andExpect(status().isNotFound());
	}
}
