package com.nr.persistence.test;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityTestDepartment {

	@Id
	private Integer id;

	private String name;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "locationId")
	private EntityTestLocation location;
}
