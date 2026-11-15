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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.lang.thread.ThreadDataBean;
import io.github.astrapi69.test.object.factory.TestMessagesFactory;

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
		assertTrue(expected.equals(actual),
			TestMessagesFactory.newFailMessage("PackagePath", expected, actual));
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
		assertTrue(expected.equals(actual),
			TestMessagesFactory.newFailMessage("PackagePath", expected, actual));
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
		assertTrue(expected.equals(actual),
			TestMessagesFactory.newFailMessage("PackagePath", expected, actual));
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
		assertTrue(expected.equals(actual),
			TestMessagesFactory.newFailMessage("PackagePath", expected, actual));
	}

	/**
	 * Test method for {@link PackageExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(PackageExtensions.class);
	}

	/**
	 * Classes with a package of their own and the name of that package
	 *
	 * @return the classes and the names of their packages
	 */
	static Stream<Arguments> classesAndTheirPackages()
	{
		return Stream.of(Arguments.of(String.class, "java.lang"),
			Arguments.of(Map.Entry.class, "java.util"),
			Arguments.of(PackageExtensions.class, "io.github.astrapi69.lang"),
			Arguments.of(ThreadDataBean.class, "io.github.astrapi69.lang.thread"));
	}

	/**
	 * Test method for {@link PackageExtensions#getPackageName(Class)},
	 * {@link PackageExtensions#getPackageNameWithDot(Class)},
	 * {@link PackageExtensions#getPackagePath(Class)} and
	 * {@link PackageExtensions#getPackagePathWithSlash(Class)}: all four name the same package
	 *
	 * @param clazz
	 *            the class
	 * @param packageName
	 *            the name of its package
	 */
	@ParameterizedTest(name = "{0} is in {1}")
	@MethodSource("classesAndTheirPackages")
	public void theFourFormsOfThePackageOfAClass(final Class<?> clazz, final String packageName)
	{
		final String packagePath = packageName.replace('.', '/') + "/";
		assertEquals(packageName, PackageExtensions.getPackageName(clazz));
		assertEquals(packageName + ".", PackageExtensions.getPackageNameWithDot(clazz));
		assertEquals(packagePath, PackageExtensions.getPackagePath(clazz));
		assertEquals("/" + packagePath, PackageExtensions.getPackagePathWithSlash(clazz));
	}

	/**
	 * Classes for which {@link Class#getPackage()} returns null, the primitive types, void and the
	 * array types, and the package {@link Class#getPackageName()} gives them
	 *
	 * @return the classes and the names of their packages
	 */
	static Stream<Arguments> classesWithoutAPackageObject()
	{
		return Stream.of(Arguments.of(int.class, "java.lang"),
			Arguments.of(void.class, "java.lang"), Arguments.of(String[].class, "java.lang"),
			Arguments.of(int[][].class, "java.lang"),
			Arguments.of(ThreadDataBean[].class, "io.github.astrapi69.lang.thread"));
	}

	/**
	 * Test method for {@link PackageExtensions#getPackageName(Class)} and the other forms with a
	 * class for which {@link Class#getPackage()} returns null: they answer as
	 * {@link Class#getPackageName()} does
	 *
	 * @param clazz
	 *            the class
	 * @param packageName
	 *            the name of the package {@link Class#getPackageName()} gives it
	 */
	@ParameterizedTest(name = "{0} is in {1}")
	@MethodSource("classesWithoutAPackageObject")
	public void aClassWithoutAPackageObject(final Class<?> clazz, final String packageName)
	{
		theFourFormsOfThePackageOfAClass(clazz, packageName);
	}

	/**
	 * Test method for {@link PackageExtensions#getPackageName(String)} with a qualified name
	 *
	 * @param qualifiedClassName
	 *            the qualified class name
	 * @param packageName
	 *            the name of its package
	 */
	@ParameterizedTest(name = "{0} is in {1}")
	@CsvSource({ "java.lang.String, java.lang", "a.B, a",
			"io.github.astrapi69.lang.PackageExtensions, io.github.astrapi69.lang",
			"java.util.Map$Entry, java.util" })
	public void thePackageOfAQualifiedClassName(final String qualifiedClassName,
		final String packageName)
	{
		assertEquals(packageName, PackageExtensions.getPackageName(qualifiedClassName));
	}

	/**
	 * Test method for {@link PackageExtensions#getPackageName(String)} with a name that holds no
	 * dot: it is in the unnamed package, whose name is the empty string
	 *
	 * @param qualifiedClassName
	 *            the name without a dot
	 */
	@ParameterizedTest(name = "\"{0}\"")
	@ValueSource(strings = { "Unqualified", "" })
	public void aClassNameWithoutAPackage(final String qualifiedClassName)
	{
		assertEquals("", PackageExtensions.getPackageName(qualifiedClassName));
	}

	/**
	 * Test method for {@link PackageExtensions#getPackagePath(Object)} and
	 * {@link PackageExtensions#getPackagePathWithSlash(Object)}: they take the class of the object
	 */
	@Test
	public void thePackagePathOfAnObject()
	{
		assertEquals("java/lang/", PackageExtensions.getPackagePath((Object)"text"));
		assertEquals("/java/util/", PackageExtensions.getPackagePathWithSlash(new ArrayList<>()));
	}

	/**
	 * Every method refuses null
	 *
	 * @return the calls with null
	 */
	static Stream<Arguments> callsWithNull()
	{
		return Stream.of(
			Arguments.of("getPackageName(Class)",
				(Executable)() -> PackageExtensions.getPackageName((Class<?>)null)),
			Arguments.of("getPackageName(String)",
				(Executable)() -> PackageExtensions.getPackageName((String)null)),
			Arguments.of("getPackageNameWithDot(Class)",
				(Executable)() -> PackageExtensions.getPackageNameWithDot(null)),
			Arguments.of("getPackagePath(Class)",
				(Executable)() -> PackageExtensions.getPackagePath((Class<?>)null)),
			Arguments.of("getPackagePath(Object)",
				(Executable)() -> PackageExtensions.getPackagePath((Object)null)),
			Arguments.of("getPackagePath(String)",
				(Executable)() -> PackageExtensions.getPackagePath((String)null)),
			Arguments.of("getPackagePath(String, boolean)",
				(Executable)() -> PackageExtensions.getPackagePath(null, true)),
			Arguments.of("getPackagePathWithSlash(Class)",
				(Executable)() -> PackageExtensions.getPackagePathWithSlash((Class<?>)null)),
			Arguments.of("getPackagePathWithSlash(Object)",
				(Executable)() -> PackageExtensions.getPackagePathWithSlash((Object)null)));
	}

	/**
	 * Test method for the null checks of every method of {@link PackageExtensions}
	 *
	 * @param call
	 *            the name of the call
	 * @param executable
	 *            the call with null
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("callsWithNull")
	public void everyMethodRefusesNull(final String call, final Executable executable)
	{
		assertThrows(NullPointerException.class, executable);
	}

}
