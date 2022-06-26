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

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import io.github.astrapi69.test.message.TestMessagesExtensions;

/**
 * The unit test class for the class {@link PackageExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class PackageExtensionsTest
{

	/**
	 * Test method for {@link PackageExtensions#getPackageName(Class)}
	 */
	@Test
	public void testGetPackageNameOfClass()
	{
		String expected;
		String actual;

		actual = PackageExtensions.getPackageName(TypeArgumentsExtensions.class);
		expected = "io.github.astrapi69.lang";
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PackageExtensions#getPackageName(String)}
	 */
	@Test
	public void testGetPackageNameOfQualifiedClassName()
	{
		String expected;
		String actual;

		actual = PackageExtensions
			.getPackageName("io.github.astrapi69.lang.TypeArgumentsExtensions");
		expected = "io.github.astrapi69.lang";
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PackageExtensions#getPackageNameWithDot(Class)}
	 */
	@Test
	public void testGetPackageNameWithDot()
	{
		String expected;
		String actual;

		actual = PackageExtensions.getPackageNameWithDot(TypeArgumentsExtensions.class);
		expected = "io.github.astrapi69.lang.";
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link PackageExtensions#getPackagePath(Object)}.
	 */
	@Test
	public void testGetPackagePath()
	{
		String expected;
		String actual;
		expected = "io/github/astrapi69/lang/";
		actual = PackageExtensions.getPackagePath(this);
		assertTrue(TestMessagesExtensions.newFailMessage("PackagePath", expected, actual),
			expected.equals(actual));
	}

	/**
	 * Test method for {@link PackageExtensions#getPackagePath(String)}.
	 */
	@Test
	public void testGetPackagePathString()
	{
		String expected;
		String actual;
		String input;
		expected = "io/github/astrapi69/lang";
		input = "io.github.astrapi69.lang";
		actual = PackageExtensions.getPackagePath(input);
		assertTrue(TestMessagesExtensions.newFailMessage("PackagePath", expected, actual),
			expected.equals(actual));
	}

	/**
	 * Test method for {@link PackageExtensions#getPackagePath(String, boolean)}.
	 */
	@Test
	public void testGetPackagePathStringBoolean()
	{
		String expected;
		String actual;
		String input;
		expected = "io/github/astrapi69/lang/";
		input = "io.github.astrapi69.lang";
		actual = PackageExtensions.getPackagePath(input, true);
		assertTrue(TestMessagesExtensions.newFailMessage("PackagePath", expected, actual),
			expected.equals(actual));
	}

	/**
	 * Test method for {@link PackageExtensions#getPackagePathWithSlash(Object)}.
	 */
	@Test
	public void testGetPackagePathWithSlash()
	{
		String expected;
		String actual;
		Object input;
		expected = "/io/github/astrapi69/lang/";
		input = this;
		actual = PackageExtensions.getPackagePathWithSlash(input);
		assertTrue(TestMessagesExtensions.newFailMessage("PackagePath", expected, actual),
			expected.equals(actual));
	}

	/**
	 * Test method for {@link PackageExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(PackageExtensions.class);
	}

}
