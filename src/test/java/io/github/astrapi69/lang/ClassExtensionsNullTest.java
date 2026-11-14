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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The tests of how {@link ClassExtensions} handles null: the methods whose parameter is marked
 * non-null refuse it with a {@link NullPointerException}, the others answer for it
 */
class ClassExtensionsNullTest
{

	/**
	 * Every call of a method of {@link ClassExtensions} whose parameter is marked non-null, with
	 * null for that parameter
	 *
	 * @return the name of each call and the call
	 */
	static Stream<Arguments> callsThatRefuseNull()
	{
		final Class<?> nullClass = null;
		final String nullString = null;
		final Object nullObject = null;
		final StackTraceElement[] nullElements = null;
		return Stream.of(
			Arguments.of("getRunningJarFile(null)",
				(Executable)() -> ClassExtensions.getRunningJarFile(nullClass)),
			Arguments.of("forName(null)", (Executable)() -> ClassExtensions.forName(nullString)),
			Arguments.of("getCallingMethodName(null)",
				(Executable)() -> ClassExtensions.getCallingMethodName(nullElements)),
			Arguments.of("getCglibProxy(null)",
				(Executable)() -> ClassExtensions.getCglibProxy(nullClass)),
			Arguments.of("getComponentClassType(null)",
				(Executable)() -> ClassExtensions.getComponentClassType((Object[])null)),
			Arguments.of("getClassname(null)",
				(Executable)() -> ClassExtensions.getClassname(nullClass)),
			Arguments.of("getClassCanonicalName(null)",
				(Executable)() -> ClassExtensions.getClassCanonicalName(nullClass)),
			Arguments.of("getClassnameWithSuffix((Class)null)",
				(Executable)() -> ClassExtensions.getClassnameWithSuffix(nullClass)),
			Arguments.of("getClassnameWithSuffix((Object)null)",
				(Executable)() -> ClassExtensions.getClassnameWithSuffix(nullObject)),
			Arguments.of("getClassType(null)",
				(Executable)() -> ClassExtensions.getClassType(nullClass)),
			Arguments.of("getCurrentMethodName(null)",
				(Executable)() -> ClassExtensions.getCurrentMethodName(nullElements)),
			Arguments.of("getDirectoriesFromResources(null, true)",
				(Executable)() -> ClassExtensions.getDirectoriesFromResources(nullString, true)),
			Arguments.of("getJdkProxyInterfaces(null)",
				(Executable)() -> ClassExtensions.getJdkProxyInterfaces(nullClass)),
			Arguments.of("getName(null)", (Executable)() -> ClassExtensions.getName(nullClass)),
			Arguments.of("getPath(null)", (Executable)() -> ClassExtensions.getPath(nullClass)),
			Arguments.of("getResource((Class)null)",
				(Executable)() -> ClassExtensions.getResource(nullClass)),
			Arguments.of("getResource(null, path)",
				(Executable)() -> ClassExtensions.getResource(nullClass, "path")),
			Arguments.of("getResource(class, null)",
				(Executable)() -> ClassExtensions.getResource(String.class, nullString)),
			Arguments.of("getResource((String)null)",
				(Executable)() -> ClassExtensions.getResource(nullString)),
			Arguments.of("getResource(null, object)",
				(Executable)() -> ClassExtensions.getResource(nullString, "object")),
			Arguments.of("getResource(name, null)",
				(Executable)() -> ClassExtensions.getResource("name", nullObject)),
			Arguments.of("getResourceAsFile(null)",
				(Executable)() -> ClassExtensions.getResourceAsFile(nullString)),
			Arguments.of("getResourceAsFile(null, object)",
				(Executable)() -> ClassExtensions.getResourceAsFile(nullString, "object")),
			Arguments.of("getResourceAsFile(name, null)",
				(Executable)() -> ClassExtensions.getResourceAsFile("name", nullObject)),
			Arguments.of("getResourceAsStream(null, uri)",
				(Executable)() -> ClassExtensions.getResourceAsStream(nullClass, "uri")),
			Arguments.of("getResourceAsStream(class, null)",
				(Executable)() -> ClassExtensions.getResourceAsStream(String.class, nullString)),
			Arguments.of("getResourceAsStream((String)null)",
				(Executable)() -> ClassExtensions.getResourceAsStream(nullString)),
			Arguments.of("getResourceAsStream(null, object)",
				(Executable)() -> ClassExtensions.getResourceAsStream(nullString, "object")),
			Arguments.of("getResourceAsStream(name, null)",
				(Executable)() -> ClassExtensions.getResourceAsStream("name", nullObject)),
			Arguments.of("getResources(null)",
				(Executable)() -> ClassExtensions.getResources(nullString)),
			Arguments.of("getSimpleName(null)",
				(Executable)() -> ClassExtensions.getSimpleName(nullClass)),
			Arguments.of("getURL(null)", (Executable)() -> ClassExtensions.getURL(nullClass)),
			Arguments.of("getProtocol(null)",
				(Executable)() -> ClassExtensions.getProtocol(nullClass)),
			Arguments.of("isCollection(null)",
				(Executable)() -> ClassExtensions.isCollection(nullClass)),
			Arguments.of("isMap(null)", (Executable)() -> ClassExtensions.isMap(nullClass)),
			Arguments.of("isPrimitiveArray(null)",
				(Executable)() -> ClassExtensions.isPrimitiveArray(nullClass)),
			Arguments.of("isFromJar(null)",
				(Executable)() -> ClassExtensions.isFromJar(nullClass)));
	}

	/**
	 * Each method whose parameter is marked non-null throws a {@link NullPointerException} for null
	 *
	 * @param call
	 *            the name of the call
	 * @param executable
	 *            the call with null
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("callsThatRefuseNull")
	void aParameterMarkedNonNullRefusesNull(final String call, final Executable executable)
	{
		assertThrows(NullPointerException.class, executable);
	}

	/**
	 * The methods whose parameter is not marked non-null answer for null: no class, no name, no
	 * path, not a proxy
	 */
	@Test
	void theMethodsThatTakeNullAnswerForIt()
	{
		assertNull(ClassExtensions.getBaseClass(null));
		assertNull(ClassExtensions.getClass(null));
		assertNull(ClassExtensions.getName(null, true));
		assertNull(ClassExtensions.getName(null, false));
		assertNull(ClassExtensions.getPathFromObject(null));
		assertNull(ClassExtensions.getUnwrappedProxy(null));
		assertArrayEquals(new Class<?>[] { }, ClassExtensions.unwrapProxy(null));
		assertFalse(ClassExtensions.isCglib(null));
		assertFalse(ClassExtensions.isJdkProxy(null));
		assertFalse(ClassExtensions.isProxy(null));
		assertEquals(ClassExtensions.getClassLoader(), ClassExtensions.getClassLoader(null));
	}
}
