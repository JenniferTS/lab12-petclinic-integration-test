package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.VetDTO;
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
 * Ejercicio 1 : Pruebas de integracion del controlador de veterinarios (/vets)
 * La peticion pasa por el controlador, el servicio, el repositorio y llega a la base de datos H2.
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFindAllVets() throws Exception {

		final int ID_FIRST_RECORD = 1;

		this.mockMvc.perform(get("/vets"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
	}

	@Test
	public void testFindVetOK() throws Exception {

		this.mockMvc.perform(get("/vets/1"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.firstName", is("James")))
				.andExpect(jsonPath("$.lastName", is("Carter")))
				.andExpect(jsonPath("$.email", is("james.carter@petclinic.com")))
				.andExpect(jsonPath("$.active", is(true)));
	}

	@Test
	public void testFindVetKO() throws Exception {

		mockMvc.perform(get("/vets/666"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testFindVetsByLastName() throws Exception {

		mockMvc.perform(get("/vets").param("lastName", "Douglas"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].firstName", is("Linda")));
	}

	@Test
	public void testCreateVet() throws Exception {

		String FIRST_NAME = "Lucia";
		String LAST_NAME = "Mendoza";
		String EMAIL = "lucia.mendoza@petclinic.com";

		VetDTO newVetDTO = VetDTO.builder()
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.email(EMAIL)
				.phone("959111222")
				.active(true)
				.build();

		this.mockMvc.perform(post("/vets")
						.content(om.writeValueAsString(newVetDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
				.andExpect(jsonPath("$.lastName", is(LAST_NAME)))
				.andExpect(jsonPath("$.email", is(EMAIL)));
	}

	@Test
	public void testCreateVetBadRequest() throws Exception {

		// sin apellido no se debe registrar
		VetDTO newVetDTO = VetDTO.builder().firstName("Sin Apellido").build();

		this.mockMvc.perform(post("/vets")
						.content(om.writeValueAsString(newVetDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdateVet() throws Exception {

		VetDTO newVetDTO = VetDTO.builder()
				.firstName("Mario")
				.lastName("Quispe")
				.email("mario.quispe@petclinic.com")
				.phone("959333444")
				.active(true)
				.build();

		String UP_LAST_NAME = "Quispe Huaman";
		String UP_PHONE = "959000111";

		// CREATE
		ResultActions mvcActions = mockMvc.perform(post("/vets")
						.content(om.writeValueAsString(newVetDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		// UPDATE
		VetDTO upVetDTO = VetDTO.builder()
				.firstName("Mario")
				.lastName(UP_LAST_NAME)
				.email("mario.quispe@petclinic.com")
				.phone(UP_PHONE)
				.active(false)
				.build();

		mockMvc.perform(put("/vets/" + id)
						.content(om.writeValueAsString(upVetDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// FIND BY ID
		mockMvc.perform(get("/vets/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(id)))
				.andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)))
				.andExpect(jsonPath("$.phone", is(UP_PHONE)))
				.andExpect(jsonPath("$.active", is(false)));

		// DELETE
		mockMvc.perform(delete("/vets/" + id))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteVet() throws Exception {

		VetDTO newVetDTO = VetDTO.builder()
				.firstName("Rosa")
				.lastName("Paredes")
				.active(true)
				.build();

		ResultActions mvcActions = mockMvc.perform(post("/vets")
						.content(om.writeValueAsString(newVetDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		mockMvc.perform(delete("/vets/" + id))
				.andExpect(status().isOk());

		// ya no debe existir
		mockMvc.perform(get("/vets/" + id))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteVetKO() throws Exception {

		mockMvc.perform(delete("/vets/" + "1000"))
				.andExpect(status().isNotFound());
	}
}
