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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.lang.model.MethodTypes;
import io.github.astrapi69.test.base.BaseTestCase;

/**
 * The unit test class for the class {@link MethodTypeArgumentsExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class MethodTypeArgumentsExtensionsTest extends BaseTestCase
{

	/**
	 * Test for {@link MethodTypeArgumentsExtensions#getParameterTypes(Method)} to ensure the
	 * correct parameter types are returned
	 */
	@Test
	@DisplayName("Test getParameterTypes for generic parameter types")
	public void testGetParameterTypes() throws NoSuchMethodException
	{
		Method method = TestClass.class.getMethod("methodWithGenericParameters", List.class);
		Type[] parameterTypes = MethodTypeArgumentsExtensions.getParameterTypes(method);

		// Verify that the parameter types are correctly returned
		assertArrayEquals(new Type[] { String.class }, parameterTypes);
	}

	/**
	 * Test for {@link MethodTypeArgumentsExtensions#getParameterTypes(Method)} with a method that
	 * has no parameters
	 */
	@Test
	@DisplayName("Test getParameterTypes for non-parameterized method")
	public void testGetParameterTypesNonParameterized() throws NoSuchMethodException
	{
		Method method = TestClass.class.getMethod("methodWithoutParameters");
		Type[] parameterTypes = MethodTypeArgumentsExtensions.getParameterTypes(method);
		// Verify that the method has no parameter types (null)
		assertNull(parameterTypes);
	}

	/**
	 * Test for {@link MethodTypeArgumentsExtensions#getMethodTypes(Method)} to ensure the correct
	 * generic parameter types and return type are returned
	 */
	@Test
	@DisplayName("Test getMethodTypes for generic parameter and return types")
	public void testGetMethodTypes() throws NoSuchMethodException
	{
		Method method = TestClass.class.getMethod("methodWithGenericParameters", List.class);
		MethodTypes methodTypes = MethodTypeArgumentsExtensions.getMethodTypes(method);
		assertEquals(1, methodTypes.getGenericParameterTypes().length);
	}

	/**
	 * Test for {@link MethodTypeArgumentsExtensions#getParameterType(Method)} with a parameterized
	 * method
	 */
	@Test
	@DisplayName("Test getParameterType for parameterized method")
	public void testGetParameterType() throws NoSuchMethodException
	{
		Method method = TestClass.class.getMethod("methodWithGenericParameters", List.class);
		Class<?> parameterType = MethodTypeArgumentsExtensions.getParameterType(method);
		assertEquals(String.class, parameterType);
	}

	/**
	 * Test for {@link MethodTypeArgumentsExtensions#getParameterType(Method)} with a method that
	 * has no parameters
	 */
	@Test
	@DisplayName("Test getParameterType for non-parameterized method")
	public void testGetParameterTypeNonParameterized() throws NoSuchMethodException
	{
		Method method = TestClass.class.getMethod("methodWithoutParameters");
		Class<?> parameterType = MethodTypeArgumentsExtensions.getParameterType(method);
		assertNull(parameterType);
	}

	/**
	 * Test for verifying the return type of
	 * {@link MethodTypeArgumentsExtensions#getReturnType(Method)}
	 */
	@Test
	@DisplayName("Test getReturnType for method with generic return type")
	public void testGetReturnType() throws NoSuchMethodException
	{
		Method method = TestClass.class.getMethod("methodWithGenericReturnType", List.class);
		Class<?> returnType = MethodTypeArgumentsExtensions.getReturnType(method);

		// Verify the return type is a List
		assertEquals(List.class, returnType);
	}

	/**
	 * Test for {@link MethodTypeArgumentsExtensions#getReturnType(Method)} for method with a void
	 * return type
	 */
	@Test
	@DisplayName("Test getReturnType for method with void return type")
	public void testGetReturnTypeVoidMethod() throws NoSuchMethodException
	{
		Method method = TestClass.class.getMethod("methodWithoutParameters");
		Class<?> returnType = MethodTypeArgumentsExtensions.getReturnType(method);

		// Verify the return type is void
		assertEquals(void.class, returnType);
	}

	/**
	 * A sample test class for reflection testing
	 */
	static class TestClass
	{
		public List<String> methodWithGenericParameters(List<String> input)
		{
			return input;
		}

		public List<Integer> methodWithGenericReturnType(List<String> input)
		{
			return List.of(1, 2, 3);
		}

		public void methodWithoutParameters()
		{
		}
	}

	/**
	 * Test method for {@link MethodTypeArgumentsExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(MethodTypeArgumentsExtensions.class);
	}

}
