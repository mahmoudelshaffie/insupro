package com.insupro.customers.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.insupro.InsuProApplication;
import com.insupro.customers.data.CustomerModulesRep;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { InsuProApplication.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class CustomersRESTCtrlIntegrationTest {

	@Autowired
	private CustomerModulesRep rep;

	private static final String BASE_URL = "http://localhost:8080/customers";

	@After
	public void after() {
		rep.deleteAll(); // Reseting DB Should Be Through Scripts, Its work
							// around
	}

	@Test
	public void testGetAllCustomers_ShouldCallAPIAndReturnOk() {
		Response response = RestAssured.get(BASE_URL);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	public void testSaveSelectionForFirstTimeAndUpdateItSuccessfully() {
		Integer customerId = 5;
		// No modules has not been selected by customer yet, so customer tariff
		// should be 0.0
		testGetCustomerTariff(customerId, "0.0");

		// Customer select specific module
		testSelectModuleSuccssfully(customerId, 4, 2000f, 600f);

		// Customer's tariff should be increased to 600
		testGetCustomerTariff(customerId, "600.0");

		// UPdate selection
		testSelectModuleSuccssfully(customerId, 4, 1000f, 300f);

		// Customer's tariff should be decreased to 300
		testGetCustomerTariff(customerId, "300.0");
	}
	
	@Test
	public void testClearSavedSelectionSuccessfully() {
		Integer customerId = 5;
		// No modules has not been selected by customer yet, so customer tariff
		// should be 0.0
		testGetCustomerTariff(customerId, "0.0");

		// Customer select specific module
		testSelectModuleSuccssfully(customerId, 4, 2000f, 600f);

		// Customer's tariff should be increased to 600
		testGetCustomerTariff(customerId, "600.0");

		// UPdate selection
		testClearModuleSelectionSuccssfully(customerId, 4);

		// Customer's tariff should be decreased to 300
		testGetCustomerTariff(customerId, "0.0");
	}

	@Test
	public void testSelectModuleForFirstTimeSuccessfullyAndGetCustomerPriceTariff() {
		Integer customerId = 5;
		// No modules has not been selected by customer yet, so customer tariff
		// should be 0.0
		testGetCustomerTariff(customerId, "0.0");

		// Customer select specific module
		testSelectModuleSuccssfully(customerId, 4, 1000f, 300f);

		// Customer's tariff should be increased to 300
		testGetCustomerTariff(customerId, "300.0");

		// Customer select another module
		testSelectModuleSuccssfully(customerId, 1, 2000f, 600f);

		// Customer's tariff should be increased to 900
		testGetCustomerTariff(customerId, "900.0");

		// Customer change coverage of selected module
		testSelectModuleSuccssfully(customerId, 1, 1000f, 300f);

		// Customer's tariff should be decreased to 600
		testGetCustomerTariff(customerId, "600.0");

		// Clear Module Selection
		testClearModuleSelectionSuccssfully(customerId, 1);

		// Customer's tariff should be decreased to 300
		testGetCustomerTariff(customerId, "300.0");
	}

	private void testSelectModuleSuccssfully(Integer customerId, Integer moduleId, Float coverage,
			Float expectedTariff) {
		String url = BASE_URL + "/" + customerId + "/select?moduleId=" + moduleId + "&coverage=" + coverage;
		Response selectModuleRes = RestAssured.put(url);
		assertThat(selectModuleRes.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		float actualModuleTariff = selectModuleRes.getBody().jsonPath().getFloat("priceTariff");
		assertThat(actualModuleTariff).isEqualTo(expectedTariff);
	}

	private void testClearModuleSelectionSuccssfully(Integer customerId, Integer moduleId) {
		String url = BASE_URL + "/" + customerId + "/clear?moduleId=" + moduleId;
		Response selectModuleRes = RestAssured.delete(url);
		assertThat(selectModuleRes.getStatusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void testGetCustomerTariff(Integer customerId, String expectedTariff) {
		Response custTariffRes = RestAssured.get(BASE_URL + "/" + customerId + "/tariff");
		assertThat(custTariffRes.getStatusCode()).isEqualTo(HttpStatus.OK.value());
		String actualCustTariff = custTariffRes.getBody().asString();
		assertThat(actualCustTariff).isEqualTo(expectedTariff);
	}
}
