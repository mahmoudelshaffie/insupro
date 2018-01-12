package com.insupro.customers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insupro.customers.data.Customer;
import com.insupro.customers.data.CustomerModules;
import com.insupro.customers.services.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomersRESTCtrl {

	@Autowired
	private CustomerService customerService;
	
	@GetMapping
	public Iterable<Customer> getAllCustomers() {
		return this.customerService.getAllCustomers();
	}
	
	@GetMapping("/{customerId}/tariff")
	public float getCustomerPriceTariff(@PathVariable Integer customerId) {
		return this.customerService.getCustomerPriceTariff(customerId);
	}
	
	@PutMapping("/{customerId}/select")
	public CustomerModules selectModule(@PathVariable Integer customerId, @RequestParam Integer moduleId, @RequestParam float coverage) throws Exception {
		return this.customerService.selectModule(moduleId, customerId, coverage);
	}
	
	@DeleteMapping("/{customerId}/clear")
	public void clearModuleSelection(@PathVariable Integer customerId, @RequestParam Integer moduleId) throws Exception {
		 this.customerService.clearModuleSelection(moduleId, customerId);
	}
	
}
