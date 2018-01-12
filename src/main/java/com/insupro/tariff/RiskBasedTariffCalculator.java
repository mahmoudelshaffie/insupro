package com.insupro.tariff;

import com.insupro.modules.data.Module;

public class RiskBasedTariffCalculator implements ITariffCalculator {

	@Override
	public float calcualte(Module module, float selectedCvrg) {
		float tariff = module.getRisk() / 100;
		tariff = tariff * selectedCvrg;
		return tariff;
	}

}
