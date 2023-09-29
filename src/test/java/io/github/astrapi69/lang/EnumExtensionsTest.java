/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.lang;

import static org.testng.AssertJUnit.assertEquals;

import java.util.function.Function;

import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import io.github.astrapi69.crypt.api.algorithm.key.KeyPairGeneratorAlgorithm;
import io.github.astrapi69.crypt.api.key.KeyType;
import io.github.astrapi69.test.object.enumtype.Brand;

/**
 * The unit test class for the class {@link EnumExtensions}
 */
public class EnumExtensionsTest
{

	/**
	 * Test method for {@link EnumExtensions#enumFieldValue(Class, Object, Function, Enum)}
	 */
	@Test
	public void testEnumFieldValue()
	{
		TestCar actual;
		TestCar expected;
		Class<TestCar> testCarClass;

		testCarClass = TestCar.class;

		actual = EnumExtensions.enumFieldValue(testCarClass, Brand.FERRARI, TestCar::getBrand,
			TestCar.UNKNOWN);
		expected = TestCar.GARAGE_PARIS;

		assertEquals(expected, actual);
		actual = EnumExtensions.enumFieldValue(testCarClass, Brand.PORSCHE, TestCar::getBrand,
			TestCar.UNKNOWN);
		expected = TestCar.GARAGE_BERLIN;
		assertEquals(expected, actual);

		assertEquals(expected, actual);
		actual = EnumExtensions.enumFieldValue(testCarClass, Brand.MASERATI, TestCar::getBrand,
			TestCar.UNKNOWN);
		expected = TestCar.UNKNOWN;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link EnumExtensions#enumFieldValueFromString(Class, String, Function, Enum)}
	 */
	@Test
	public void testEnumFieldValueFromString()
	{

		KeyType actual;
		KeyType expected;
		String enumFieldName;
		Class<KeyType> keyTypeClass;

		keyTypeClass = KeyType.class;

		enumFieldName = "foo bar";

		actual = EnumExtensions.enumFieldValueFromString(keyTypeClass, enumFieldName,
			KeyType::getDisplayValue, KeyType.UNKNOWN);
		expected = KeyType.UNKNOWN;
		assertEquals(expected, actual);

		enumFieldName = "Public key";

		actual = EnumExtensions.enumFieldValueFromString(keyTypeClass, enumFieldName,
			KeyType::getDisplayValue, KeyType.UNKNOWN);
		expected = KeyType.PUBLIC_KEY;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link EnumExtensions#enumValueFromString(Class, String, Enum)}
	 */
	@Test
	public void testEnumValueFromString()
	{
		KeyPairGeneratorAlgorithm actual;
		KeyPairGeneratorAlgorithm expected;
		String enumName;

		enumName = "DSA";
		actual = EnumExtensions.enumValueFromString(KeyPairGeneratorAlgorithm.class, enumName,
			KeyPairGeneratorAlgorithm.UNKNOWN);
		expected = KeyPairGeneratorAlgorithm.DSA;
		assertEquals(expected, actual);

		enumName = "foo";
		actual = EnumExtensions.enumValueFromString(KeyPairGeneratorAlgorithm.class, enumName,
			KeyPairGeneratorAlgorithm.UNKNOWN);
		expected = KeyPairGeneratorAlgorithm.UNKNOWN;
		assertEquals(expected, actual);

		enumName = "foo";
		actual = EnumExtensions.enumValueFromString(KeyPairGeneratorAlgorithm.class, enumName,
			null);
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link EnumExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(EnumExtensions.class);
	}

}