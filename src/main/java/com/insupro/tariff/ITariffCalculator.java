package com.insupro.tariff;

import com.insupro.modules.data.Module;

public interface ITariffCalculator {
	public float calcualte(Module module, float selectedCvrg);
}
