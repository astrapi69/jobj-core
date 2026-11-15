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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * The bounds of {@link Argument#isInRange(Comparable, Comparable, Comparable, String)} and of the
 * four {@code isInRange} methods of {@link Check}: both take the range with both bounds, as Apache
 * Commons Lang {@code Validate.inclusiveBetween} does (#34; before 10.0 {@link Check} refused the
 * bounds)
 */
class IsInRangeBoundariesTest
{

	/**
	 * {@link Argument#isInRange(Comparable, Comparable, Comparable, String)} with the range 0 to 5
	 *
	 * @param value
	 *            the value
	 * @param inRange
	 *            whether the value passes
	 */
	@ParameterizedTest(name = "{0} in 0..5: {1}")
	@CsvSource({ "-1, false", "0, true", "1, true", "4, true", "5, true", "6, false",
			"-2147483648, false", "2147483647, false" })
	void argumentTakesTheRangeWithBothBounds(final int value, final boolean inRange)
	{
		if (inRange)
		{
			assertEquals(value, Argument.isInRange(0, 5, value, "value"));
		}
		else
		{
			final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> Argument.isInRange(0, 5, value, "value"));
			assertEquals("Given argument 'value' should have a value between 0 - 5, "
				+ "but given argument is currently:" + value, exception.getMessage());
		}
	}

	/**
	 * The four {@code isInRange} methods of {@link Check} with the range 0 to 5, each in the type
	 * it takes
	 *
	 * @param value
	 *            the value
	 * @param inRange
	 *            whether the value passes
	 */
	@ParameterizedTest(name = "{0} in 0..5: {1}")
	@CsvSource({ "-1, false", "0, true", "1, true", "4, true", "5, true", "6, false" })
	void checkTakesTheRangeWithBothBounds(final int value, final boolean inRange)
	{
		final Check check = Check.get();
		if (inRange)
		{
			assertSame(check, check.isInRange(0, 5, value, "value"));
			assertSame(check, check.isInRange(0L, 5L, (long)value, "value"));
			assertSame(check, check.isInRange(0f, 5f, (float)value, "value"));
			assertSame(check, check.isInRange(0d, 5d, (double)value, "value"));
		}
		else
		{
			assertThrows(IllegalArgumentException.class,
				() -> check.isInRange(0, 5, value, "value"));
			assertThrows(IllegalArgumentException.class,
				() -> check.isInRange(0L, 5L, (long)value, "value"));
			assertThrows(IllegalArgumentException.class,
				() -> check.isInRange(0f, 5f, (float)value, "value"));
			assertThrows(IllegalArgumentException.class,
				() -> check.isInRange(0d, 5d, (double)value, "value"));
		}
	}

	/**
	 * NaN is in no range: the {@code Float} and {@code Double} methods of {@link Check} and
	 * {@link Argument#isInRange(Comparable, Comparable, Comparable, String)}, whose
	 * {@link Double#compareTo(Double)} puts NaN above every other value, refuse it
	 */
	@Test
	void notANumberIsInNoRange()
	{
		final Check check = Check.get();
		assertThrows(IllegalArgumentException.class,
			() -> check.isInRange(0f, 5f, Float.NaN, "value"));
		assertThrows(IllegalArgumentException.class,
			() -> check.isInRange(0d, 5d, Double.NaN, "value"));
		assertThrows(IllegalArgumentException.class,
			() -> Argument.isInRange(0d, 5d, Double.NaN, "value"));
	}

	/**
	 * A null bound or a null value is refused with an {@link IllegalArgumentException} that names
	 * it, in {@link Check} and in {@link Argument} (before 10.0 {@link Argument} threw a
	 * {@link NullPointerException} for a null value)
	 */
	@Test
	void nullBoundsAndValues()
	{
		assertEquals("Given argument 'min' may not be null.",
			assertThrows(IllegalArgumentException.class,
				() -> Argument.isInRange(null, 5, 1, "value")).getMessage());
		assertEquals("Given argument 'max' may not be null.",
			assertThrows(IllegalArgumentException.class,
				() -> Argument.isInRange(0, null, 1, "value")).getMessage());
		assertEquals("Given argument 'value' may not be null.",
			assertThrows(IllegalArgumentException.class,
				() -> Argument.isInRange(0, 5, null, "value")).getMessage());
		assertEquals("Given argument 'value' may not be null.",
			assertThrows(IllegalArgumentException.class,
				() -> Check.get().isInRange(0, 5, (Integer)null, "value")).getMessage());
	}

	/**
	 * {@link Argument#notEmpty(CharSequence, String)} refuses a text of blanks only, as an empty
	 * one
	 *
	 * @param text
	 *            the text
	 */
	@ParameterizedTest(name = "\"{0}\"")
	@ValueSource(strings = { "", " ", "   ", "\t", "\n" })
	void aTextOfBlanksIsEmpty(final String text)
	{
		assertEquals("Given argument 'text' may not be empty.",
			assertThrows(IllegalArgumentException.class, () -> Argument.notEmpty(text, "text"))
				.getMessage());
		assertThrows(IllegalArgumentException.class, () -> Check.get().notEmpty(text, "text"));
	}
}
