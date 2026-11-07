package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.PetTypeDTO;
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
 * Ejercicio 5 : Pruebas de integracion del controlador de tipos de mascota (/types)
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class TypesControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFindAllTypes() throws Exception {

		this.mockMvc.perform(get("/types"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("cat")));
	}

	@Test
	public void testFindTypeOK() throws Exception {

		this.mockMvc.perform(get("/types/2"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.name", is("dog")))
				.andExpect(jsonPath("$.description", is("Domestic canine")))
				.andExpect(jsonPath("$.averageLifespan", is(12)));
	}

	@Test
	public void testFindTypeKO() throws Exception {

		mockMvc.perform(get("/types/666"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testFindTypesByName() throws Exception {

		mockMvc.perform(get("/types").param("name", "snake"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].active", is(false)));
	}

	@Test
	public void testCreateType() throws Exception {

		PetTypeDTO newTypeDTO = PetTypeDTO.builder()
				.name("guinea pig")
				.description("Small rodent from the Andes")
				.active(true)
				.sizeCategory("small")
				.averageLifespan(6)
				.careLevel("low")
				.build();

		this.mockMvc.perform(post("/types")
						.content(om.writeValueAsString(newTypeDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is("guinea pig")))
				.andExpect(jsonPath("$.averageLifespan", is(6)))
				.andExpect(jsonPath("$.careLevel", is("low")));
	}

	@Test
	public void testCreateTypeBadRequest() throws Exception {

		// el nombre es NOT NULL en la tabla types
		PetTypeDTO newTypeDTO = PetTypeDTO.builder().description("Sin nombre").build();

		this.mockMvc.perform(post("/types")
						.content(om.writeValueAsString(newTypeDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdateType() throws Exception {

		PetTypeDTO newTypeDTO = PetTypeDTO.builder()
				.name("ferret")
				.description("Small carnivore")
				.active(true)
				.sizeCategory("small")
				.averageLifespan(7)
				.careLevel("medium")
				.build();

		// CREATE
		ResultActions mvcActions = mockMvc.perform(post("/types")
						.content(om.writeValueAsString(newTypeDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		// UPDATE
		PetTypeDTO upTypeDTO = PetTypeDTO.builder()
				.name("ferret")
				.description("Domestic ferret")
				.active(false)
				.sizeCategory("small")
				.averageLifespan(8)
				.careLevel("high")
				.build();

		mockMvc.perform(put("/types/" + id)
						.content(om.writeValueAsString(upTypeDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// FIND BY ID
		mockMvc.perform(get("/types/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.description", is("Domestic ferret")))
				.andExpect(jsonPath("$.careLevel", is("high")))
				.andExpect(jsonPath("$.active", is(false)));

		// DELETE
		mockMvc.perform(delete("/types/" + id))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteType() throws Exception {

		PetTypeDTO newTypeDTO = PetTypeDTO.builder()
				.name("parrot")
				.description("Talking bird")
				.active(true)
				.build();

		ResultActions mvcActions = mockMvc.perform(post("/types")
						.content(om.writeValueAsString(newTypeDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		mockMvc.perform(delete("/types/" + id))
				.andExpect(status().isOk());

		mockMvc.perform(get("/types/" + id))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteTypeKO() throws Exception {

		mockMvc.perform(delete("/types/" + "1000"))
				.andExpect(status().isNotFound());
	}
}
