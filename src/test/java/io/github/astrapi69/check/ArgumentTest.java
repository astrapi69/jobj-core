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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.array.ArrayFactory;


/**
 * The unit test class for the class {@link Argument}.
 */
public class ArgumentTest
{

	String name;

	/**
	 * Test method for {@link Argument#isInRange(Comparable, Comparable, Comparable, String)}
	 */
	@Test
	public void testIsInRange()
	{
		Double min;
		Double max;
		Double value;

		min = 0.0d;
		max = 5.0d;
		value = 6.0d;
		name = "parameter";
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.isInRange(min, max, value, name);
		});
	}

	/**
	 * Test method for {@link Argument#isInRange(Comparable, Comparable, Comparable, String)}
	 */
	@Test
	public void testIsInRangeNormalCase()
	{
		Double expected;
		Double actual;
		Double min;
		Double max;
		Double value;

		min = 0.0d;
		max = 5.0d;
		value = 4.0d;
		name = "parameter";

		expected = value;
		actual = Argument.isInRange(min, max, value, name);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link Argument#notEmpty(java.util.Collection, String)}
	 */
	@Test
	public void testNotEmptyCollection()
	{
		List<String> list;

		name = "list";
		list = new ArrayList<>();

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.notEmpty(list, name);
		});
	}

	/**
	 * Test method for {@link Argument#notEmpty(boolean[], String)}
	 */
	@Test
	public void testNotEmptyBooleanArrayEmptyCase()
	{
		boolean[] actual;

		actual = new boolean[0];

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.notEmpty(actual, name);
		});
	}

	/**
	 * Test method for {@link Argument#notEmpty(byte[], String)}
	 */
	@Test
	public void testNotEmptyByteArrayEmptyCase()
	{
		byte[] actual;

		actual = new byte[0];

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.notEmpty(actual, name);
		});
	}

	/**
	 * Test method for {@link Argument#notEmpty(char[], String)}
	 */
	@Test
	public void testNotEmptyCharArrayEmptyCase()
	{
		char[] actual;

		actual = new char[0];

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.notEmpty(actual, name);
		});
	}

	/**
	 * Test method for {@link Argument#notEmpty(short[], String)}
	 */
	@Test
	public void testNotEmptyShortArrayEmptyCase()
	{
		short[] actual;

		actual = new short[0];

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.notEmpty(actual, name);
		});
	}

	/**
	 * Test method for {@link Argument#notEmpty(int[], String)}
	 */
	@Test
	public void testNotEmptyIntArrayEmptyCase()
	{
		int[] actual;

		actual = new int[0];

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.notEmpty(actual, name);
		});
	}

	/**
	 * Test method for {@link Argument#notEmpty(long[], String)}
	 */
	@Test
	public void testNotEmptyLongArrayEmptyCase()
	{
		long[] actual;

		actual = new long[0];

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.notEmpty(actual, name);
		});
	}

	/**
	 * Test method for {@link Argument#notEmpty(float[], String)}
	 */
	@Test
	public void testNotEmptyFloatArrayEmptyCase()
	{
		float[] actual;

		actual = new float[0];

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.notEmpty(actual, name);
		});
	}

	/**
	 * Test method for {@link Argument#notEmpty(double[], String)}
	 */
	@Test
	public void testNotEmptyDoubleArrayEmptyCase()
	{
		double[] actual;

		actual = new double[0];

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.notEmpty(actual, name);
		});
	}

	/**
	 * Test method for {@link Argument#notEmpty(boolean[], String)}
	 */
	@Test
	public void testNotEmptyBooleanArray()
	{
		boolean[] actual;
		boolean[] expected;

		actual = ArrayFactory.newBooleanArray(true);

		expected = Argument.notEmpty(actual, name);
		assertNotNull(expected);
	}

	/**
	 * Test method for {@link Argument#notEmpty(byte[], String)}
	 */
	@Test
	public void testNotEmptyByteArray()
	{
		byte[] actual;
		byte[] expected;

		actual = ArrayFactory.newByteArray((byte)1);

		expected = Argument.notEmpty(actual, name);
		assertNotNull(expected);
	}

	/**
	 * Test method for {@link Argument#notEmpty(char[], String)}
	 */
	@Test
	public void testNotEmptyCharArray()
	{
		char[] actual;
		char[] expected;

		actual = ArrayFactory.newCharArray('c');

		expected = Argument.notEmpty(actual, name);
		assertNotNull(expected);
	}

	/**
	 * Test method for {@link Argument#notEmpty(short[], String)}
	 */
	@Test
	public void testNotEmptyShortArray()
	{
		short[] actual;
		short[] expected;

		actual = ArrayFactory.newShortArray((short)1, (short)2, (short)3);

		expected = Argument.notEmpty(actual, name);
		assertNotNull(expected);
	}

	/**
	 * Test method for {@link Argument#notEmpty(int[], String)}
	 */
	@Test
	public void testNotEmptyIntArray()
	{
		int[] actual;
		int[] expected;

		actual = ArrayFactory.newIntArray(1, 2);

		expected = Argument.notEmpty(actual, name);
		assertNotNull(expected);
	}

	/**
	 * Test method for {@link Argument#notEmpty(long[], String)}
	 */
	@Test
	public void testNotEmptyLongArray()
	{
		long[] actual;
		long[] expected;

		actual = ArrayFactory.newLongArray(1L, 2L, 3L);

		expected = Argument.notEmpty(actual, name);
		assertNotNull(expected);
	}

	/**
	 * Test method for {@link Argument#notEmpty(float[], String)}
	 */
	@Test
	public void testNotEmptyFloatArray()
	{
		float[] actual;
		float[] expected;

		actual = ArrayFactory.newFloatArray(1.0F, 2.0F, 3.0F);

		expected = Argument.notEmpty(actual, name);
		assertNotNull(expected);
	}

	/**
	 * Test method for {@link Argument#notEmpty(double[], String)}
	 */
	@Test
	public void testNotEmptyDoubleArray()
	{
		double[] actual;
		double[] expected;

		actual = ArrayFactory.newDoubleArray(1.1D, 2.1D, 3.1D);

		expected = Argument.notEmpty(actual, name);
		assertNotNull(expected);
	}

	/**
	 * Test method for {@link Argument#notEmpty(java.util.Collection, String)}
	 */
	@Test
	public void testNotEmptyCollectionNormalCase()
	{
		List<String> expected;
		List<String> actual;

		name = "list";
		expected = new ArrayList<>();
		expected.add("foo");

		actual = Argument.notEmpty(expected, name);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link Argument#notEmpty(Map, String)}
	 */
	@Test
	public void testNotEmptyMap()
	{
		Map<String, String> emptyMap;

		name = "map";
		emptyMap = new HashMap<>();
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.notEmpty(emptyMap, name);
		});
	}

	/**
	 * Test method for {@link Argument#notEmpty(Map, String)}
	 */
	@Test
	public void testNotEmptyMapNormalCase()
	{
		Map<String, String> expected;
		Map<String, String> actual;

		name = "map";
		expected = new HashMap<>();
		expected.put("foo", "bar");

		actual = Argument.notEmpty(expected, name);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link Argument#notEmpty(CharSequence, String)}
	 */
	@Test
	public void testNotEmptyString()
	{
		String argument;

		name = "parameter";
		argument = "";
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.notEmpty(argument, name);
		});
	}

	/**
	 * Test method for {@link Argument#notEmpty(CharSequence, String)}
	 */
	@Test
	public void testNotEmptyStringNormalCase()
	{
		String expected;
		String actual;

		name = "parameter";
		expected = "foo";
		actual = Argument.notEmpty(expected, name);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link Argument#notNull(Object, String)}
	 */
	@Test
	public void testNotNull()
	{
		name = "parameter";

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Argument.notNull(null, name);
		});
	}

	/**
	 * Test method for {@link Argument} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		BeanTester beanTester = new BeanTester();
		beanTester.testBean(Argument.class);
	}

}
