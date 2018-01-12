package com.insupro.customers.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.insupro.FakeRepository;
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

public class CustomerServiceUnitTests {

	private CustomerService targetService;
	private CustomersRep mockedCustomerRep;
	private List<Customer> allCustomers;
	private CustomerModulesRep mockedCustomerModulesRep;
	private ModulesRep mockedModuleRep;
	private ITariffCalculator mockedCalc;

	@Before
	public void before() {
		targetService = new CustomerService();
		mockedCustomerRep = mock(CustomersRep.class);

		allCustomers = new ArrayList<Customer>(10);
		for (int i = 1; i <= 10; ++i) {
			allCustomers.add(new Customer("Customer" + i));
		}

		mockedCustomerModulesRep = mock(CustomerModulesRep.class);
		mockedModuleRep = mock(ModulesRep.class);
		mockedCalc = mock(ITariffCalculator.class);

		targetService.setTariffCalculator(mockedCalc);
		targetService.setModulesRep(mockedModuleRep);
		targetService.setCustomersRep(mockedCustomerRep);
		targetService.setCustomerModulesRep(mockedCustomerModulesRep);
	}

	@Test
	public void testGetAllCustomers_ShouldReturnAllCustomersSuccessfully() {
		when(mockedCustomerRep.findAll()).thenReturn(allCustomers);
		Iterable<Customer> customers = targetService.getAllCustomers();
		int count = 0;
		Iterator<Customer> it = customers.iterator();
		while (it.hasNext()) {
			it.next();
			++count;
		}
		assertThat(count).isEqualTo(allCustomers.size());
	}

	@Test
	public void testGetCustomerPriceTariff_ShouldReturnCalculatedTariffSuccessfully() {
		Float expectedTariff = 500f;
		Integer customerId = 1;
		when(mockedCustomerModulesRep.getCustomerPriceTariff(customerId)).thenReturn(expectedTariff);
		Float actualTariff = targetService.getCustomerPriceTariff(customerId);
		assertThat(actualTariff).isEqualTo(expectedTariff);
	}

	@Test(expected = InvalidSelectedModuleError.class)
	public void testSelectModule_WithInvalidModuleId_ShouldThrowInvalidSelectedModuleError() throws Exception {
		float selectedCoverage = 12;
		Integer moduleId = 1;
		Integer custId = 100;
		targetService.selectModule(moduleId, custId, selectedCoverage);
	}

	@Test(expected = InvalidSelectedCustomerError.class)
	public void testSelectModule_WithInvalidCustomerId_ShouldThrowInvalidSelectedCustomerError() throws Exception {
		float selectedCoverage = 12;
		Integer moduleId = 1;
		Integer custId = 100;
		when(mockedModuleRep.findOne(moduleId)).thenReturn(new Module());
		targetService.selectModule(moduleId, custId, selectedCoverage);
	}

	@Test(expected = InvalidSelectedCoverageError.class)
	public void testSelectModule_WithInvalidCoverageGreaterThanUpperLimit_ShouldThrowInvalidSelectedCoverageError()
			throws Exception {
		float selectedCoverage = 12000;
		Integer moduleId = 1;
		Integer custId = 100;
		Module module = new Module("", 100, 1000, 0);
		Customer customer = new Customer();
		when(mockedModuleRep.findOne(moduleId)).thenReturn(module);
		when(mockedCustomerRep.findOne(custId)).thenReturn(customer);
		targetService.selectModule(moduleId, custId, selectedCoverage);
	}

	@Test(expected = InvalidSelectedCoverageError.class)
	public void testSelectModule_WithInvalidCoverageLessThanLowerLimit_ShouldThrowInvalidSelectedCoverageError()
			throws Exception {
		float selectedCoverage = 12;
		Integer moduleId = 1;
		Integer custId = 100;
		Module module = new Module("", 100, 1000, 0);
		Customer customer = new Customer();
		when(mockedModuleRep.findOne(moduleId)).thenReturn(module);
		when(mockedCustomerRep.findOne(custId)).thenReturn(customer);
		targetService.selectModule(moduleId, custId, selectedCoverage);
	}

	@Test
	public void testSelectModule_ForFirstTime_WithValidData_ShouldAddNewOneSuccessfuly() throws Exception {
		float selectedCoverage = 120;
		Integer moduleId = 1;
		Integer custId = 100;
		Module module = new Module("", 100, 1000, 0);
		Customer customer = new Customer();
		Float tariff = 36f;
		when(mockedModuleRep.findOne(moduleId)).thenReturn(module);
		when(mockedCustomerRep.findOne(custId)).thenReturn(customer);
		when(mockedCalc.calcualte(module, selectedCoverage)).thenReturn(tariff);
		// Stubbing Is Not Working Here
		targetService.setCustomerModulesRep(new FakeCustomerModulesRep());
		CustomerModules custModule = targetService.selectModule(moduleId, custId, selectedCoverage);
		assertThat(custModule).isNotNull();
		assertThat(custModule.getId()).isNotNull();
		assertThat(custModule.getCoverage()).isEqualTo(selectedCoverage);
		assertThat(custModule.getPriceTariff()).isEqualTo(tariff);
	}

	@Test
	public void testSelectModule_ForSecondTime_WithValidData_ShouldUpdateSuccessfuly() throws Exception {
		float selectedCoverage = 120;
		Integer moduleId = 1;
		Integer custId = 100;
		Module module = new Module("", 100, 1000, 0);
		Customer customer = new Customer();
		Float tariff = 36f;
		CustomerModules customerModules = new CustomerModules(10, 80f, 40f);
		customerModules.setModule(module);
		when(mockedCustomerModulesRep.findOneByCustomerIdAndModuleId(custId, moduleId)).thenReturn(customerModules);
		when(mockedCustomerModulesRep.save(any(CustomerModules.class))).thenReturn(customerModules);
		when(mockedModuleRep.findOne(moduleId)).thenReturn(module);
		when(mockedCustomerRep.findOne(custId)).thenReturn(customer);
		when(mockedCalc.calcualte(module, selectedCoverage)).thenReturn(tariff);
		CustomerModules custModule = targetService.selectModule(moduleId, custId, selectedCoverage);
		assertThat(custModule).isNotNull();
		// Assert it is no new entity created, it is only updated
		assertThat(custModule.getId()).isEqualTo(10);
		assertThat(custModule.getCoverage()).isEqualTo(selectedCoverage);
		assertThat(custModule.getPriceTariff()).isEqualTo(tariff); // Updates Are Asserted
	}
	
	@Test
	public void testClearModuleSelection_WithSeletedModuleId_ShouldReturnSuccessfully() {
		Integer moduleId = 1;
		Integer custId = 100;
		CustomerModules customerModules = new CustomerModules();
		when(mockedCustomerModulesRep.findOneByCustomerIdAndModuleId(custId, moduleId)).thenReturn(customerModules);
		targetService.clearModuleSelection(moduleId, custId);
		verify(mockedCustomerModulesRep, times(1)).delete(customerModules);
	}
	
	@Test
	public void testClearModuleSelection_WithUnSeletedModuleId_ShouldReturnSuccessfully() {
		Integer moduleId = 1;
		Integer custId = 100;
		when(mockedCustomerModulesRep.findOneByCustomerIdAndModuleId(custId, moduleId)).thenReturn(null);
		targetService.clearModuleSelection(moduleId, custId);
	}

	class FakeCustomerModulesRep extends FakeRepository<CustomerModules, Integer> implements CustomerModulesRep {

		@Override
		public CustomerModules findOneByCustomerIdAndModuleId(Integer customerId, Integer moduleId) {
			return null;
		}

		@Override
		public Float getCustomerPriceTariff(Integer customerId) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <S extends CustomerModules> S save(S entity) {
			entity.setId(50);
			return entity;
		}

	}
}
