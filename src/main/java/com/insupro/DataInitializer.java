package com.insupro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.insupro.customers.data.Customer;
import com.insupro.customers.data.CustomersRep;
import com.insupro.modules.data.Module;
import com.insupro.modules.data.ModulesRep;

@Configuration
public class DataInitializer {

	@Autowired
	private CustomersRep customersRep;
	
	@Autowired
	private ModulesRep modulesRep;
	
	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		Module bikeModule = new Module("Bike", 0, 3000, 30);
		Module jewelryModule = new Module("Jewlery", 500, 10000, 5);
		Module electronicsModule = new Module("Electronics", 500, 6000, 35);
		Module sportEqModule = new Module("Sports Equipment", 0, 20000, 30);
		
		this.modulesRep.save(bikeModule);
		this.modulesRep.save(jewelryModule);
		this.modulesRep.save(electronicsModule);
		this.modulesRep.save(sportEqModule);
		
		for (int i = 1; i <= 10; ++i) {
			this.customersRep.save(new Customer("Customer" + i));
		}
	}
}
