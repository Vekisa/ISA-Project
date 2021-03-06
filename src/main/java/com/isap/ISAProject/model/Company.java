package com.isap.ISAProject.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Company extends RatableEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false)
	private String description;
	
	public Company(Long id, String name, String address, String description) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.description = description;
	}

	public Company() {}
	
	public String getName() { return name; }
	
	public void setName(String name) { this.name = name; }
	
	public String getAddress() { return address; }
	
	public void setAddress(String address) { this.address = address; }
	
	public String getDescription() { return description; }
	
	public void setDescription(String description) { this.description = description; }
	
	public Long getId() { return this.id; }

	public void setId(Long id) {
		this.id = id;
	}
}
