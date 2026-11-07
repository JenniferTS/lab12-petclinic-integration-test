package com.tecsup.petclinic.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Entity(name = "visits")
@Data
public class Visit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "visit_date")
	private LocalDate visitDate;

	@Column(name = "description")
	private String description;

	@Column(name = "cost")
	private BigDecimal cost;

	@Column(name = "vet_id")
	private Integer vetId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pet_id")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Pet pet;

}
