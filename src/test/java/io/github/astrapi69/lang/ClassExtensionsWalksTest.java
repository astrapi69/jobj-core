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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The walks of {@link ClassExtensions} up a hierarchy, the superclasses of
 * {@link ClassExtensions#getBaseClass(Class)} and the parents of
 * {@link ClassExtensions#isDerivate(ClassLoader, ClassLoader)}, and what
 * {@link ClassExtensions#getResourceAsFile(String)} answers for each kind of resource, in tables
 */
class ClassExtensionsWalksTest
{

	/**
	 * Classes and their base class, the class below {@link Object} in their hierarchy
	 *
	 * @return the class and its base class
	 */
	static Stream<Arguments> baseClasses()
	{
		return Stream.of(Arguments.of(Object.class, Object.class),
			Arguments.of(String.class, String.class),
			Arguments.of(ArrayList.class, AbstractCollection.class),
			Arguments.of(AbstractCollection.class, AbstractCollection.class),
			Arguments.of(Runnable.class, null), Arguments.of(int.class, null),
			Arguments.of(null, null));
	}

	/**
	 * Test method for {@link ClassExtensions#getBaseClass(Class)}: the class below {@link Object};
	 * a class whose superclass is {@link Object} is its own base, and an interface or a primitive
	 * type, which has no superclass, has none
	 *
	 * @param clazz
	 *            the class
	 * @param baseClass
	 *            its base class
	 */
	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("baseClasses")
	void theBaseClass(final Class<?> clazz, final Class<?> baseClass)
	{
		assertEquals(baseClass, ClassExtensions.getBaseClass(clazz));
	}

	/**
	 * Class loaders and whether the first is the second or one of its parents
	 *
	 * @return the source, the compared loader and the answer
	 */
	static Stream<Arguments> derivates()
	{
		final ClassLoader platform = ClassLoader.getPlatformClassLoader();
		final ClassLoader system = ClassLoader.getSystemClassLoader();
		return Stream.of(Arguments.of("system, system", system, system, true),
			Arguments.of("platform, system", platform, system, true),
			Arguments.of("system, platform", system, platform, false),
			Arguments.of("bootstrap, system", null, system, true),
			Arguments.of("system, bootstrap", system, null, false),
			Arguments.of("bootstrap, bootstrap", null, null, true));
	}

	/**
	 * Test method for {@link ClassExtensions#isDerivate(ClassLoader, ClassLoader)}: true if the
	 * source is the compared class loader or one of its parents; null is the bootstrap class
	 * loader, the parent of every chain
	 *
	 * @param name
	 *            the name of the case
	 * @param source
	 *            the source
	 * @param compare
	 *            the compared class loader
	 * @param derivate
	 *            the answer
	 */
	@ParameterizedTest(name = "{0}: {3}")
	@MethodSource("derivates")
	void isDerivate(final String name, final ClassLoader source, final ClassLoader compare,
		final boolean derivate)
	{
		assertEquals(derivate, ClassExtensions.isDerivate(source, compare));
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsFile(String)} for each kind of resource:
	 * a file of the class path gives its file, a resource that does not exist and one of the JDK's
	 * runtime image give null, and one in a jar is refused
	 *
	 * @throws URISyntaxException
	 *             if the file resource has no valid URI
	 */
	@Test
	void theResourceAsFileOfEachKindOfResource() throws URISyntaxException
	{
		final File file = ClassExtensions
			.getResourceAsFile("io/github/astrapi69/lang/resources.properties");
		assertTrue(file.isFile());
		assertEquals(file,
			ClassExtensions.getResourceAsFile("/io/github/astrapi69/lang/resources.properties"));
		assertNull(ClassExtensions.getResourceAsFile("io/github/astrapi69/no/such/resource"));
		assertNull(ClassExtensions.getResourceAsFile("java/lang/String.class"),
			"a jrt resource is no file");
		assertThrows(URISyntaxException.class,
			() -> ClassExtensions.getResourceAsFile("org/junit/jupiter/api/Test.class"));
	}
}
