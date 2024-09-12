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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;


/**
 * The class {@code OptionalExtensionsTest} contains test methods for the {@link OptionalExtensions}
 * class
 */
public class OptionalExtensionsParameterizedTest
{

	/**
	 * Parameterized test method for {@link OptionalExtensions#getOptionalValue(Optional, Object)}
	 * using CSV file source
	 *
	 * @param optionalValue
	 *            the optional value as string
	 * @param defaultValue
	 *            the default value
	 * @param expected
	 *            the expected result
	 */
	@ParameterizedTest
	@CsvFileSource(resources = "/optional_values.csv", numLinesToSkip = 1)
	public void testGetOptionalValueParameterized(String optionalValue, String defaultValue,
		String expected)
	{
		Optional<String> value = optionalValue.equals("null")
			? Optional.empty()
			: Optional.of(optionalValue);
		String result = OptionalExtensions.getOptionalValue(value, defaultValue);
		assertEquals(expected, result);
	}

}
