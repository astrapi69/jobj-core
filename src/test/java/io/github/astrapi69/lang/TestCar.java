package io.github.astrapi69.lang;

import io.github.astrapi69.test.object.enumtype.Brand;
import lombok.Getter;

public enum TestCar
{
	GARAGE_PARIS(Brand.FERRARI), GARAGE_BERLIN(Brand.PORSCHE), UNKNOWN(Brand.LAMBORGINI);

	@Getter
	private final Brand brand;

	TestCar(final Brand brand)
	{
		this.brand = brand;
	}
}
