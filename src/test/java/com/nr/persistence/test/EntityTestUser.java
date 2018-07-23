package com.nr.persistence.test;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EntityTestUser {

	@Id
	private Integer id;
	private String name;
	private Date since;
	private Boolean enabled;
	private Integer numberOfChildren;
	private Double score;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "departmentId")
	private EntityTestDepartment department;
}
