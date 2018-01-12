package com.insupro.customers.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.insupro.modules.data.Module;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "customerId", "moduleId" }))
public class CustomerModules {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "customerId")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "moduleId")
	private Module module;

	private Float coverage;

	private Float priceTariff;

	public CustomerModules() {
	}

	public CustomerModules(Customer customer, Module module) {
		this.customer = customer;
		this.module = module;
	}
	
	public CustomerModules(Integer id, Float coverage, Float priceTariff) {
		this.id = id;
		this.priceTariff = priceTariff;
		this.coverage = coverage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Float getCoverage() {
		return coverage;
	}

	public void setCoverage(Float coverage) {
		this.coverage = coverage;
	}

	public Float getPriceTariff() {
		return priceTariff;
	}

	public void setPriceTariff(Float priceTariff) {
		this.priceTariff = priceTariff;
	}

}
