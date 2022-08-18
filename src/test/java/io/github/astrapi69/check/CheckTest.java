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


import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.test.object.Person;

/**
 * The unit test class for the class {@link Check}
 */
public class CheckTest
{

	String name;

	/**
	 * Test method for {@link Check#get()}
	 */
	@Test
	public void testGet()
	{
		final Check actual = Check.get();
		assertNotNull(actual);
	}

	/**
	 * Test method for {@link Check#isInRange(Double, Double, Double, String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testIsInRangeDouble()
	{
		Double min;
		Double max;
		Double value;

		min = 0.0d;
		max = 5.0d;
		value = 6.0d;
		name = "parameter";
		Check.get().isInRange(min, max, value, name);
	}

	/**
	 * Test method for {@link Check#isInRange(Double, Double, Double, String)}
	 */
	@Test
	public void testIsInRangeDoubleNormalCase()
	{
		Double min;
		Double max;
		Double value;

		min = 0.0d;
		max = 5.0d;
		value = 4.0d;
		name = "parameter";

		Check check = Check.get().isInRange(min, max, value, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#isInRange(Float, Float, Float, String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testIsInRangeFloat()
	{
		Float min;
		Float max;
		Float value;

		min = 0.0f;
		max = 5.0f;
		value = 6.0f;
		name = "parameter";
		Check.get().isInRange(min, max, value, name);
	}

	/**
	 * Test method for {@link Check#isInRange(Float, Float, Float, String)}
	 */
	@Test
	public void testIsInRangeFloatNormalCase()
	{
		Float min;
		Float max;
		Float value;

		min = 0.0f;
		max = 5.0f;
		value = 4.0f;
		name = "parameter";

		Check check = Check.get().isInRange(min, max, value, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#isInRange(Integer, Integer, Integer, String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testIsInRangeInteger()
	{
		Integer min;
		Integer max;
		Integer value;

		min = 0;
		max = 5;
		value = 6;
		name = "parameter";
		Check.get().isInRange(min, max, value, name);
	}

	/**
	 * Test method for {@link Check#isInRange(Integer, Integer, Integer, String)}
	 */
	@Test
	public void testIsInRangeIntegerNormalCase()
	{
		Integer min;
		Integer max;
		Integer value;

		min = 0;
		max = 5;
		value = 2;
		name = "parameter";

		Check check = Check.get().isInRange(min, max, value, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#isInRange(Long, Long, Long, String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testIsInRangeLong()
	{
		Long min;
		Long max;
		Long value;

		min = 0l;
		max = 5l;
		value = 6l;
		name = "parameter";

		Check.get().isInRange(min, max, value, name);
	}

	/**
	 * Test method for {@link Check#isInRange(Long, Long, Long, String)}
	 */
	@Test
	public void testIsInRangeLongNormalCase()
	{
		Long min;
		Long max;
		Long value;

		min = 0l;
		max = 5l;
		value = 3l;
		name = "parameter";

		Check check = Check.get().isInRange(min, max, value, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#notEmpty(java.util.Collection, String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNotEmptyCollection()
	{
		List<String> list;

		name = "list";
		list = new ArrayList<>();

		Check.get().notEmpty(list, name);
	}

	/**
	 * Test method for {@link Check#notEmpty(java.util.Collection, String)}
	 */
	@Test
	public void testNotEmptyCollectionNormalCase()
	{
		List<String> list;

		name = "list";
		list = new ArrayList<>();
		list.add("foo");

		Check check = Check.get().notEmpty(list, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#notEmpty(java.util.Collection, String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNotEmptyMap()
	{
		Map<String, String> emptyMap;

		name = "map";
		emptyMap = new HashMap<>();

		Check.get().notEmpty(emptyMap, name);
	}

	/**
	 * Test method for {@link Check#notEmpty(java.util.Collection, String)}
	 */
	@Test
	public void testNotEmptyMapNormalCase()
	{
		Map<String, String> map;

		name = "map";
		map = new HashMap<>();
		map.put("foo", "bar");
		Check check = Check.get().notEmpty(map, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#notEmpty(CharSequence, String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNotEmptyString()
	{
		String argument;

		name = "parameter";
		argument = "";

		Check.get().notEmpty(argument, name);
	}

	/**
	 * Test method for {@link Check#notEmpty(CharSequence, String)}
	 */
	@Test
	public void testNotEmptyStringNormalCase()
	{
		String argument;

		name = "parameter";
		argument = "foo";

		Check check = Check.get().notEmpty(argument, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#notNull(Object, String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNotNull()
	{
		name = "parameter";

		Check.get().notNull(null, name);
	}

	/**
	 * Test method for {@link Check#notNull(Object, String)}
	 */
	@Test
	public void testNotNullNormalCase()
	{
		name = "parameter";

		Check check = Check.get().notNull(Person.builder().build(), name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#notEmpty(boolean[], String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNotEmptyBooleanArrayEmptyCase()
	{
		boolean[] actual;

		actual = new boolean[0];

		Check.get().notEmpty(actual, name);
	}

	/**
	 * Test method for {@link Check#notEmpty(byte[], String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNotEmptyByteArrayEmptyCase()
	{
		byte[] actual;

		actual = new byte[0];

		Check.get().notEmpty(actual, name);
	}

	/**
	 * Test method for {@link Check#notEmpty(char[], String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNotEmptyCharArrayEmptyCase()
	{
		char[] actual;

		actual = new char[0];

		Check.get().notEmpty(actual, name);
	}

	/**
	 * Test method for {@link Check#notEmpty(short[], String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNotEmptyShortArrayEmptyCase()
	{
		short[] actual;

		actual = new short[0];

		Check.get().notEmpty(actual, name);
	}

	/**
	 * Test method for {@link Check#notEmpty(int[], String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNotEmptyIntArrayEmptyCase()
	{
		int[] actual;

		actual = new int[0];

		Check.get().notEmpty(actual, name);
	}

	/**
	 * Test method for {@link Check#notEmpty(long[], String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNotEmptyLongArrayEmptyCase()
	{
		long[] actual;

		actual = new long[0];

		Check.get().notEmpty(actual, name);
	}

	/**
	 * Test method for {@link Check#notEmpty(float[], String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNotEmptyFloatArrayEmptyCase()
	{
		float[] actual;

		actual = new float[0];

		Check.get().notEmpty(actual, name);
	}

	/**
	 * Test method for {@link Check#notEmpty(double[], String)}
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNotEmptyDoubleArrayEmptyCase()
	{
		double[] actual;

		actual = new double[0];

		Check.get().notEmpty(actual, name);
	}

	/**
	 * Test method for {@link Check#notEmpty(boolean[], String)}
	 */
	@Test
	public void testNotEmptyBooleanArray()
	{
		boolean[] actual;
		boolean[] expected;

		actual = ArrayFactory.newBooleanArray(true);

		Check check = Check.get().notEmpty(actual, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#notEmpty(byte[], String)}
	 */
	@Test
	public void testNotEmptyByteArray()
	{
		byte[] actual;
		byte[] expected;

		actual = ArrayFactory.newByteArray((byte)1);

		Check check = Check.get().notEmpty(actual, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#notEmpty(char[], String)}
	 */
	@Test
	public void testNotEmptyCharArray()
	{
		char[] actual;
		char[] expected;

		actual = ArrayFactory.newCharArray('c');

		Check check = Check.get().notEmpty(actual, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#notEmpty(short[], String)}
	 */
	@Test
	public void testNotEmptyShortArray()
	{
		short[] actual;
		short[] expected;

		actual = ArrayFactory.newShortArray((short)1, (short)2, (short)3);

		Check check = Check.get().notEmpty(actual, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#notEmpty(int[], String)}
	 */
	@Test
	public void testNotEmptyIntArray()
	{
		int[] actual;
		int[] expected;

		actual = ArrayFactory.newIntArray(1, 2);

		Check check = Check.get().notEmpty(actual, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#notEmpty(long[], String)}
	 */
	@Test
	public void testNotEmptyLongArray()
	{
		long[] actual;
		long[] expected;

		actual = ArrayFactory.newLongArray(1L, 2L, 3L);

		Check check = Check.get().notEmpty(actual, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#notEmpty(float[], String)}
	 */
	@Test
	public void testNotEmptyFloatArray()
	{
		float[] actual;
		float[] expected;

		actual = ArrayFactory.newFloatArray(1.0F, 2.0F, 3.0F);

		Check check = Check.get().notEmpty(actual, name);
		assertNotNull(check);
	}

	/**
	 * Test method for {@link Check#notEmpty(double[], String)}
	 */
	@Test
	public void testNotEmptyDoubleArray()
	{
		double[] actual;
		double[] expected;

		actual = ArrayFactory.newDoubleArray(1.1D, 2.1D, 3.1D);

		Check check = Check.get().notEmpty(actual, name);
		assertNotNull(check);
	}


	/**
	 * Test method for {@link Check} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		BeanTester beanTester = new BeanTester();
		beanTester.testBean(Check.class);
	}

}
