package com.insupro.customers.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerModulesRep extends CrudRepository<CustomerModules, Integer> {
	CustomerModules findOneByCustomerIdAndModuleId(Integer customerId, Integer moduleId);
	
	@Query("SELECT SUM(CM.priceTariff) FROM CustomerModules CM WHERE CM.customer.id=?1")
	Float getCustomerPriceTariff(Integer customerId);
}
