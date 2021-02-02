package com.ewolff.monolith.dto;

import lombok.*;
import org.springframework.hateoas.ResourceSupport;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO extends ResourceSupport {

	// Note: Lombok annotations implement boilerplate constructors,
	// setters, getters and equals / hashcode

	private Long customerId;

	private String name;

	private String firstname;

	private String email;

	private String street;

	private String city;

}
