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
package io.github.astrapi69.check;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

/**
 * The parameterized test class for the class {@link Argument} using CSV data.
 */
public class ArgumentParameterizedTest
{

	/**
	 * Parameterized test method for
	 * {@link Argument#isInRange(Comparable, Comparable, Comparable, String)} using a CSV file as
	 * input
	 *
	 * @param min
	 *            the minimum value of the range
	 * @param max
	 *            the maximum value of the range
	 * @param value
	 *            the value to check
	 * @param name
	 *            the name of the argument
	 * @param shouldPass
	 *            boolean flag indicating if the test should pass
	 */
	@ParameterizedTest
	@CsvFileSource(resources = "/testIsInRange.csv", numLinesToSkip = 1)
	public void testIsInRangeWithCsv(double min, double max, double value, String name,
		boolean shouldPass)
	{
		if (shouldPass)
		{
			Double actual = Argument.isInRange(min, max, value, name);
			assertEquals(value, actual);
		}
		else
		{
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				Argument.isInRange(min, max, value, name);
			});
		}
	}
}
