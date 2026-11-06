package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
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
 * Ejercicio 3 : Pruebas de integracion del controlador de especialidades (/specialties)
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class SpecialtyControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFindAllSpecialties() throws Exception {

		this.mockMvc.perform(get("/specialties"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("radiology")));
	}

	@Test
	public void testFindSpecialtyOK() throws Exception {

		this.mockMvc.perform(get("/specialties/2"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.name", is("surgery")))
				.andExpect(jsonPath("$.office", is("Maryland")))
				.andExpect(jsonPath("$.openHour", is(8)))
				.andExpect(jsonPath("$.closeHour", is(12)));
	}

	@Test
	public void testFindSpecialtyKO() throws Exception {

		mockMvc.perform(get("/specialties/666"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testFindSpecialtiesByName() throws Exception {

		mockMvc.perform(get("/specialties").param("name", "dentistry"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].office", is("Terranova")));
	}

	@Test
	public void testCreateSpecialty() throws Exception {

		SpecialtyDTO newSpecialtyDTO = SpecialtyDTO.builder()
				.name("cardiology")
				.office("Lima")
				.openHour(8)
				.closeHour(16)
				.build();

		this.mockMvc.perform(post("/specialties")
						.content(om.writeValueAsString(newSpecialtyDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is("cardiology")))
				.andExpect(jsonPath("$.office", is("Lima")))
				.andExpect(jsonPath("$.openHour", is(8)))
				.andExpect(jsonPath("$.closeHour", is(16)));
	}

	@Test
	public void testCreateSpecialtyBadRequest() throws Exception {

		// sin nombre
		SpecialtyDTO newSpecialtyDTO = SpecialtyDTO.builder().office("Lima").build();

		this.mockMvc.perform(post("/specialties")
						.content(om.writeValueAsString(newSpecialtyDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdateSpecialty() throws Exception {

		SpecialtyDTO newSpecialtyDTO = SpecialtyDTO.builder()
				.name("dermatology")
				.office("Arequipa")
				.openHour(9)
				.closeHour(13)
				.build();

		// CREATE
		ResultActions mvcActions = mockMvc.perform(post("/specialties")
						.content(om.writeValueAsString(newSpecialtyDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		// UPDATE: cambia de consultorio y de horario
		SpecialtyDTO upSpecialtyDTO = SpecialtyDTO.builder()
				.name("dermatology")
				.office("Yanahuara")
				.openHour(14)
				.closeHour(20)
				.build();

		mockMvc.perform(put("/specialties/" + id)
						.content(om.writeValueAsString(upSpecialtyDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// FIND BY ID
		mockMvc.perform(get("/specialties/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.office", is("Yanahuara")))
				.andExpect(jsonPath("$.openHour", is(14)))
				.andExpect(jsonPath("$.closeHour", is(20)));

		// DELETE
		mockMvc.perform(delete("/specialties/" + id))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteSpecialty() throws Exception {

		SpecialtyDTO newSpecialtyDTO = SpecialtyDTO.builder()
				.name("ophthalmology")
				.office("Cayma")
				.openHour(10)
				.closeHour(18)
				.build();

		ResultActions mvcActions = mockMvc.perform(post("/specialties")
						.content(om.writeValueAsString(newSpecialtyDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		mockMvc.perform(delete("/specialties/" + id))
				.andExpect(status().isOk());

		mockMvc.perform(get("/specialties/" + id))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteSpecialtyKO() throws Exception {

		mockMvc.perform(delete("/specialties/" + "1000"))
				.andExpect(status().isNotFound());
	}
}
