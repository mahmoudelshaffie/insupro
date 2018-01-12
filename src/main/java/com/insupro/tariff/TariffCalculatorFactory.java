package com.insupro.tariff;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Bean Factory to provide implementation of ITariffCalculator
 * using name qualifiers, to be used later in injection of bean.
 * instead of type. Since It is an interface not a class. 
 * @author Mahmoud
 *
 */
@Component
public class TariffCalculatorFactory {

	@Bean(name = "tariffCalculator")
	public ITariffCalculator getCalculator(){
		return new RiskBasedTariffCalculator();
	}
}
