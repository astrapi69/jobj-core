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

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import io.github.astrapi69.test.base.BaseTestCase;
import io.github.astrapi69.test.object.Person;
import io.github.astrapi69.test.object.factory.TestMessagesFactory;
import io.github.astrapi69.test.object.generic.GenericDao;
import io.github.astrapi69.test.object.generic.PersonDao;

/**
 * The unit test class for the class {@link TypeArgumentsExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class TypeArgumentsExtensionsTest extends BaseTestCase
{
	List<String>[] array;

	/**
	 * Test method for
	 * {@link TypeArgumentsExtensions#getGenericReturnClassType(Class, String, Class[])}
	 *
	 * @throws SecurityException
	 *             is thrown if a security manager says no.
	 * @throws NoSuchMethodException
	 *             is thrown when a particular method cannot be found
	 */
	@Test
	public void testGetGenericReturnClassType() throws SecurityException, NoSuchMethodException
	{
		Type actual;
		// Type expected;

		actual = TypeArgumentsExtensions.getGenericReturnType(List.class, "toArray",
			Object[].class);
		assertNotNull(actual);
	}

	/**
	 * Test method for
	 * {@link TypeArgumentsExtensions#getGenericReturnClassType(Class, String, Class[])}
	 *
	 * @throws SecurityException
	 *             is thrown if a security manager says no.
	 * @throws NoSuchMethodException
	 *             is thrown when a particular method cannot be found
	 */
	@Test
	public void testGetGenericReturnClassTypeClass() throws SecurityException, NoSuchMethodException
	{
		Class<?> actual;
		Class<?> expected;

		actual = TypeArgumentsExtensions.getGenericReturnClassType(List.class, "toArray",
			Object[].class);
		Class<Object> objectClass = Object.class;
		expected = Array.newInstance(objectClass, 0).getClass();
		assertEquals(expected, actual);
		Class<String> stringClass = String.class;
		actual = TypeArgumentsExtensions.getGenericReturnClassType(TestMessagesFactory.class,
			"newFailMessage", stringClass, stringClass, stringClass);
		expected = stringClass;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions#getClass(Type)}.
	 *
	 * @throws SecurityException
	 *             is thrown if a security manager says no.
	 * @throws NoSuchMethodException
	 *             is thrown when a particular method cannot be found
	 */
	@Test
	public void testGetClassType1() throws SecurityException, NoSuchMethodException
	{
		Class<?> actual;
		Class<?> expected;
		Type type;
		type = List.class.getMethod("toArray", Object[].class).getGenericReturnType();
		actual = TypeArgumentsExtensions.getClass(type);
		Class<Object> objectClass = Object.class;
		expected = Array.newInstance(objectClass, 0).getClass();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions#getFirstTypeArgument(Class, Class)}.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testGetFirstTypeArgument()
	{
		Class<Person> actualClass;
		Class<Person> expectedClass;
		Class baseClass;

		expectedClass = Person.class;
		actualClass = (Class<Person>)TypeArgumentsExtensions.getFirstTypeArgument(GenericDao.class,
			PersonDao.class);
		assertEquals(expectedClass, actualClass);

		baseClass = ClassExtensions.getBaseClass(PersonDao.class);
		actualClass = TypeArgumentsExtensions.getFirstTypeArgument(baseClass, PersonDao.class);
		assertEquals(expectedClass, actualClass);
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions#getFirstTypeArgument(Class)}
	 */
	@Test
	public void testGetFirstTypeArgumentClassOfQextendsT()
	{
		Class<?> expected;
		Class<?> actual;
		actual = TypeArgumentsExtensions.getFirstTypeArgument(Bar.class);
		expected = String.class;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions#getTypeArgument(Class, Class, int)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetTypeArgumentClassClassInt()
	{
		final Class<Integer> expectedClass = Integer.class;
		final Class<Integer> integerClass = (Class<Integer>)TypeArgumentsExtensions
			.getTypeArgument(GenericDao.class, PersonDao.class, 1);
		assertEquals(expectedClass, integerClass);
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions#getTypeArgument(Class, int)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetTypeArgumentClassInt()
	{
		final Class<Person> expectedPersonClass = Person.class;
		Class<Person> personClass = (Class<Person>)TypeArgumentsExtensions
			.getTypeArgument(PersonDao.class, 0);
		assertEquals(expectedPersonClass, personClass);
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions#getTypeArguments(Class, Class)}.
	 */
	@Test(enabled = true)
	public void testGetTypeArguments()
	{
		List<Class<?>> typeArguments;

		typeArguments = TypeArgumentsExtensions.getTypeArguments(GenericDao.class, PersonDao.class);
		assertNotNull(typeArguments);
		assertEquals(2, typeArguments.size());
		assertEquals(Person.class, typeArguments.get(0));
		assertEquals(Integer.class, typeArguments.get(1));
	}

	// ====================================================================== //

	/**
	 * Test method for
	 * {@link TypeArgumentsExtensions#getTypeArgumentsAndParameters(ParameterizedType)}.
	 *
	 * @throws SecurityException
	 *             is thrown if a security manager says no.
	 */
	@Test
	public void testGetTypeArgumentsAndParameters() throws SecurityException
	{
		Optional<ParameterizedType> parameterizedTypeOptional;
		Map<Type, Type> typeArgumentsAndParameters;
		ParameterizedType parameterizedType;

		parameterizedTypeOptional = TypeArgumentsExtensions.getParameterizedType(PersonDao.class);
		if (parameterizedTypeOptional.isPresent())
		{
			parameterizedType = parameterizedTypeOptional.get();
			typeArgumentsAndParameters = TypeArgumentsExtensions
				.getTypeArgumentsAndParameters(parameterizedType);
			assertEquals(2, typeArgumentsAndParameters.size());
			assertTrue(typeArgumentsAndParameters.containsValue(Integer.class));
			assertTrue(typeArgumentsAndParameters.containsValue(Person.class));

		}
		else
		{
			org.testng.Assert.fail("parameterizedTypeOptional should not be empty");
		}
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions#getTypeArguments(Class)}.
	 */
	@Test
	public void testGetTypeArgumentsClassOfQextendsT()
	{
		List<Class<?>> typeArguments = TypeArgumentsExtensions.getTypeArguments(PersonDao.class);
		assertNotNull(typeArguments);
		assertEquals(2, typeArguments.size());
		assertEquals(Person.class, typeArguments.get(0));
		assertEquals(Integer.class, typeArguments.get(1));
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(TypeArgumentsExtensions.class);
	}

	static class Bar extends Foo<String, Integer>
	{

	}

	static class Foo<T, E>
	{

	}

}
