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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.test.object.Company;
import io.github.astrapi69.test.object.annotation.Mandatory;
import io.github.astrapi69.test.object.annotation.interfacetype.AnnotatedInterface;
import io.github.astrapi69.test.object.enumeration.Gender;

/**
 * The unit test class for the class {@link ObjectExtensions}.
 */
public class ObjectExtensionsTest
{

	/**
	 * Test method for {@link ObjectExtensions#getClassType(Class)}
	 */
	@Test
	public void testGetClassType()
	{
		ClassType expected;
		ClassType actual;

		class Local
		{
		}
		expected = ClassType.LOCAL;
		actual = ObjectExtensions.getClassType(Local.class);
		assertEquals(expected, actual);

		expected = ClassType.DEFAULT;
		actual = ObjectExtensions.getClassType(Integer.class);
		assertEquals(expected, actual);

		expected = ClassType.DEFAULT;
		actual = ObjectExtensions.getClassType(Boolean.class);
		assertEquals(expected, actual);

		expected = ClassType.PRIMITIVE;
		actual = ObjectExtensions.getClassType(int.class);
		assertEquals(expected, actual);

		expected = ClassType.DEFAULT;
		actual = ObjectExtensions.getClassType(String.class);
		assertEquals(expected, actual);

		expected = ClassType.ANNOTATION;
		actual = ObjectExtensions.getClassType(Mandatory.class);
		assertEquals(expected, actual);

		expected = null;
		actual = ObjectExtensions.getClassType(null);
		assertEquals(expected, actual);

		expected = ClassType.ENUM;
		actual = ObjectExtensions.getClassType(Gender.class);
		assertEquals(expected, actual);

		expected = ClassType.ARRAY;
		final String[] stringArray = { "foo", "bar" };
		actual = ObjectExtensions.getClassType(stringArray.getClass());
		assertEquals(expected, actual);

		expected = ClassType.COLLECTION;
		final List<String> strings = new ArrayList<>();
		actual = ObjectExtensions.getClassType(strings.getClass());
		assertEquals(expected, actual);

		expected = ClassType.MAP;
		final Map<String, String> map = new HashMap<>();
		actual = ObjectExtensions.getClassType(map.getClass());
		assertEquals(expected, actual);

		expected = ClassType.INTERFACE;
		actual = ObjectExtensions.getClassType(AnnotatedInterface.class);
		assertEquals(expected, actual);

		expected = ClassType.ANONYMOUS;
		actual = ObjectExtensions.getClassType(new Runnable()
		{

			@Override
			public void run()
			{
			}
		}.getClass());
		assertEquals(expected, actual);


		expected = ClassType.MEMBER;
		actual = ObjectExtensions.getClassType(Member.class);
		assertEquals(expected, actual);

	}

	/**
	 * Test method for {@link ObjectExtensions#isDefaultValue(Class, Object)}
	 */
	@Test
	public void testIsDefaultValue()
	{
		boolean expected;
		boolean actual;

		expected = true;
		actual = ObjectExtensions.isDefaultValue(int.class, 0);
		assertEquals(expected, actual);

		final Company company = Company.builder().build();
		expected = true;
		actual = ObjectExtensions.isDefaultValue(String.class, company.getName());
		assertEquals(expected, actual);

		expected = false;
		actual = ObjectExtensions.isDefaultValue(int.class, 2);
		assertEquals(expected, actual);

		expected = false;
		actual = ObjectExtensions.isDefaultValue(Company.class, company);
		assertEquals(expected, actual);

		expected = true;
		actual = ObjectExtensions.isDefaultValue(Company.class, null);
		assertEquals(expected, actual);

	}

	/**
	 * Test method for {@link ObjectExtensions#isNotDefaultValue(Class, Object)}
	 */
	@Test
	public void testIsNotDefaultValue()
	{
		boolean expected;
		boolean actual;

		expected = false;
		actual = ObjectExtensions.isNotDefaultValue(int.class, 0);
		assertEquals(expected, actual);

		final Company company = Company.builder().build();
		expected = false;
		actual = ObjectExtensions.isNotDefaultValue(String.class, company.getName());
		assertEquals(expected, actual);

		expected = true;
		actual = ObjectExtensions.isNotDefaultValue(int.class, 2);
		assertEquals(expected, actual);

		expected = true;
		actual = ObjectExtensions.isNotDefaultValue(Company.class, company);
		assertEquals(expected, actual);

		expected = false;
		actual = ObjectExtensions.isNotDefaultValue(Company.class, null);
		assertEquals(expected, actual);

	}

	/**
	 * Test method for {@link ObjectExtensions} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ObjectExtensions.class);
	}

	/**
	 * Each type, its default value and a value that is not its default; the cases of
	 * DefaultValueTest, which ObjectExtensions reads its default values from
	 *
	 * @return the type, its default value and another value
	 */
	static Stream<Arguments> defaultValues()
	{
		return Stream.of(Arguments.of(boolean.class, false, true),
			Arguments.of(char.class, '\0', 'a'), Arguments.of(byte.class, (byte)0, (byte)1),
			Arguments.of(short.class, (short)0, (short)1), Arguments.of(int.class, 0, 1),
			Arguments.of(long.class, 0L, 1L), Arguments.of(float.class, 0f, 0.5f),
			Arguments.of(double.class, 0d, 0.5d), Arguments.of(Object.class, null, "text"),
			Arguments.of(Integer.class, null, 0), Arguments.of(void.class, null, "text"));
	}

	/**
	 * Test method for {@link ObjectExtensions#isDefaultValue(Class, Object)} and
	 * {@link ObjectExtensions#isNotDefaultValue(Class, Object)}: the default value of a primitive
	 * type is its zero, of every other type null
	 *
	 * @param type
	 *            the type
	 * @param defaultValue
	 *            its default value
	 * @param otherValue
	 *            a value that is not its default
	 */
	@ParameterizedTest(name = "{0}: {1} is the default, {2} is not")
	@MethodSource("defaultValues")
	public void theDefaultValueOfAType(final Class<?> type, final Object defaultValue,
		final Object otherValue)
	{
		assertTrue(ObjectExtensions.isDefaultValue(type, defaultValue));
		assertFalse(ObjectExtensions.isNotDefaultValue(type, defaultValue));
		assertFalse(ObjectExtensions.isDefaultValue(type, otherValue));
		assertTrue(ObjectExtensions.isNotDefaultValue(type, otherValue));
	}

	/**
	 * Test method for {@link ObjectExtensions#isDefaultValue(Class, Object)} without a class: null
	 * is the default value of every type, any other value needs the class, as with DefaultValue
	 * before 10.0
	 */
	@Test
	public void theDefaultValueWithoutAClass()
	{
		assertTrue(ObjectExtensions.isDefaultValue(null, null));
		assertThrows(NullPointerException.class, () -> ObjectExtensions.isDefaultValue(null, 1));
	}

	/**
	 * The interface {@link Member} that represents the member class
	 */
	interface Member
	{
	}

}
