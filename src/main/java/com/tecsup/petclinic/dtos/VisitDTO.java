package com.tecsup.petclinic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VisitDTO {

	private Long id;
	private Integer petId;
	private Integer vetId;
	// fecha en formato yyyy-MM-dd, igual que birthDate en PetDTO
	private String visitDate;
	private String description;
	private BigDecimal cost;
}
