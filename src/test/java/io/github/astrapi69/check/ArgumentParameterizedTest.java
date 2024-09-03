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
