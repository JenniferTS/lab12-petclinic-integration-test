package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.OwnerDTO;
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
 * Ejercicio 2 : Pruebas de integracion del controlador de duenos (/owners)
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class OwnerControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFindAllOwners() throws Exception {

		this.mockMvc.perform(get("/owners"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0].id", is(1)));
	}

	@Test
	public void testFindOwnerOK() throws Exception {

		this.mockMvc.perform(get("/owners/1"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.firstName", is("George")))
				.andExpect(jsonPath("$.lastName", is("Franklin")))
				.andExpect(jsonPath("$.city", is("Madison")));
	}

	@Test
	public void testFindOwnerKO() throws Exception {

		mockMvc.perform(get("/owners/666"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testFindOwnersByLastName() throws Exception {

		// en data.sql hay dos duenos con apellido Davis
		mockMvc.perform(get("/owners").param("lastName", "Davis"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void testCreateOwner() throws Exception {

		OwnerDTO newOwnerDTO = OwnerDTO.builder()
				.firstName("Carmen")
				.lastName("Valdivia")
				.address("Av. Ejercito 123")
				.city("Arequipa")
				.telephone("054223344")
				.build();

		this.mockMvc.perform(post("/owners")
						.content(om.writeValueAsString(newOwnerDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName", is("Carmen")))
				.andExpect(jsonPath("$.lastName", is("Valdivia")))
				.andExpect(jsonPath("$.city", is("Arequipa")));
	}

	@Test
	public void testCreateOwnerBadRequest() throws Exception {

		// falta el apellido
		OwnerDTO newOwnerDTO = OwnerDTO.builder().firstName("Juan").city("Arequipa").build();

		this.mockMvc.perform(post("/owners")
						.content(om.writeValueAsString(newOwnerDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdateOwner() throws Exception {

		OwnerDTO newOwnerDTO = OwnerDTO.builder()
				.firstName("Luis")
				.lastName("Chavez")
				.address("Calle Mercaderes 200")
				.city("Arequipa")
				.telephone("054111222")
				.build();

		// CREATE
		ResultActions mvcActions = mockMvc.perform(post("/owners")
						.content(om.writeValueAsString(newOwnerDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		// UPDATE: se muda a Cusco
		OwnerDTO upOwnerDTO = OwnerDTO.builder()
				.firstName("Luis")
				.lastName("Chavez")
				.address("Av. La Cultura 450")
				.city("Cusco")
				.telephone("084999888")
				.build();

		mockMvc.perform(put("/owners/" + id)
						.content(om.writeValueAsString(upOwnerDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// FIND BY ID
		mockMvc.perform(get("/owners/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.address", is("Av. La Cultura 450")))
				.andExpect(jsonPath("$.city", is("Cusco")))
				.andExpect(jsonPath("$.telephone", is("084999888")));

		// DELETE
		mockMvc.perform(delete("/owners/" + id))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdateOwnerKO() throws Exception {

		OwnerDTO upOwnerDTO = OwnerDTO.builder().firstName("Nadie").lastName("Existe").build();

		mockMvc.perform(put("/owners/666")
						.content(om.writeValueAsString(upOwnerDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteOwner() throws Exception {

		OwnerDTO newOwnerDTO = OwnerDTO.builder()
				.firstName("Ana")
				.lastName("Flores")
				.city("Arequipa")
				.build();

		ResultActions mvcActions = mockMvc.perform(post("/owners")
						.content(om.writeValueAsString(newOwnerDTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		mockMvc.perform(delete("/owners/" + id))
				.andExpect(status().isOk());

		mockMvc.perform(get("/owners/" + id))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteOwnerKO() throws Exception {

		mockMvc.perform(delete("/owners/" + "1000"))
				.andExpect(status().isNotFound());
	}
}
