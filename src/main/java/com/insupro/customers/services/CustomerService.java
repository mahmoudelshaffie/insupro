package com.insupro.customers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.insupro.customers.data.Customer;
import com.insupro.customers.data.CustomerModules;
import com.insupro.customers.data.CustomerModulesRep;
import com.insupro.customers.data.CustomersRep;
import com.insupro.errors.InvalidSelectedCoverageError;
import com.insupro.errors.InvalidSelectedCustomerError;
import com.insupro.errors.InvalidSelectedModuleError;
import com.insupro.modules.data.Module;
import com.insupro.modules.data.ModulesRep;
import com.insupro.tariff.ITariffCalculator;

@Service
public class CustomerService {

	private ModulesRep modulesRep;

	private CustomersRep customersRep;

	private CustomerModulesRep customerModulesRep;

	/* Dependency on abstraction (interface) rather
	 * than implementation to achieve dependency inversion 
	 * principle specially tariff calculation is a policy
	 */
	private ITariffCalculator tariffCalculator;
	
	// Using setters for dependency injection to make unit testable
	
	@Autowired
	public void setModulesRep(ModulesRep modulesRep) {
		this.modulesRep = modulesRep;
	}

	@Autowired
	public void setCustomersRep(CustomersRep customersRep) {
		this.customersRep = customersRep;
	}

	@Autowired
	public void setCustomerModulesRep(CustomerModulesRep customerModulesRep) {
		this.customerModulesRep = customerModulesRep;
	}

	@Autowired
	@Qualifier("tariffCalculator")
	public void setTariffCalculator(ITariffCalculator tariffCalculator) {
		this.tariffCalculator = tariffCalculator;
	}

	public float getCustomerPriceTariff(Integer customerId) {
		Float priceTariff = customerModulesRep.getCustomerPriceTariff(customerId);
		if (priceTariff == null) priceTariff = new Float(0);
		return priceTariff;
	}
	
	public Iterable<Customer> getAllCustomers() {
		return customersRep.findAll();
	}

	public CustomerModules selectModule(Integer moduleId, Integer custId, float selectedCoverage) throws Exception {
		CustomerModules customerModule = customerModulesRep.findOneByCustomerIdAndModuleId(custId, moduleId);

		if (customerModule == null) {
			// New Selection To Be Added To The Customers, Other wise will be updated with new selection
			Module module = this.modulesRep.findOne(moduleId);
			Customer customer = this.customersRep.findOne(custId);

			if (module == null) {
				throw new InvalidSelectedModuleError();
			}
			
			if (customer == null) {
				throw new InvalidSelectedCustomerError();
			}
			
			 customerModule = new CustomerModules(customer, module);
		}

		this.validateThatCoverageWithinRange(customerModule.getModule(), selectedCoverage);
		customerModule.setCoverage(selectedCoverage);

		float moduleTariff = this.tariffCalculator.calcualte(customerModule.getModule(), selectedCoverage);
		customerModule.setPriceTariff(moduleTariff);
		customerModule = customerModulesRep.save(customerModule);
		
		return customerModule;
	}
	
	public void clearModuleSelection(Integer moduleId, Integer custId) {
		CustomerModules customerModule = customerModulesRep.findOneByCustomerIdAndModuleId(custId, moduleId);

		if (customerModule != null) {
			customerModulesRep.delete(customerModule);
		}
		
	}

	private void validateThatCoverageWithinRange(Module module, float selectedCoverage)
			throws InvalidSelectedCoverageError {
		float lowerLimit = module.getCoverageLowerLimit();
		float upperLimit = module.getCoverageUpperLimit();
		if (selectedCoverage < lowerLimit || selectedCoverage > upperLimit) {
			throw new InvalidSelectedCoverageError(lowerLimit, upperLimit);
		}
	}
}
