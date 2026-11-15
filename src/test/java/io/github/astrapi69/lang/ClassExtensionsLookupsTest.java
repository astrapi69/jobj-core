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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.test.object.generic.PersonDao;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

/**
 * The lookups of {@link ClassExtensions} that the tests did not reach (the survivors of PIT after
 * #16): the class loader of an object under another context class loader, the name of an anonymous
 * class, primitive arrays, the calling method, and resources that only a fallback finds
 */
class ClassExtensionsLookupsTest
{

	/**
	 * Runs the given action with the given context class loader, and sets the one before again
	 * afterwards
	 *
	 * @param classLoader
	 *            the context class loader for the action
	 * @param action
	 *            the action
	 */
	private static void withContextClassLoader(final ClassLoader classLoader, final Runnable action)
	{
		final Thread thread = Thread.currentThread();
		final ClassLoader previous = thread.getContextClassLoader();
		try
		{
			thread.setContextClassLoader(classLoader);
			action.run();
		}
		finally
		{
			thread.setContextClassLoader(previous);
		}
	}

	/**
	 * Makes a class at runtime, a subclass of {@link PersonDao}, in a class loader of its own whose
	 * parent is the given one
	 *
	 * @param parent
	 *            the parent of the new class loader
	 * @return the new class
	 */
	private static Class<? extends PersonDao> classInALoaderOfItsOwn(final ClassLoader parent)
	{
		return new ByteBuddy().subclass(PersonDao.class).make()
			.load(parent, ClassLoadingStrategy.Default.WRAPPER).getLoaded();
	}

	/**
	 * Test method for {@link ClassExtensions#getClassLoader(Object)} with an object whose class
	 * comes from a child of the context class loader: its own class loader is the answer
	 *
	 * @throws ReflectiveOperationException
	 *             if the class made at runtime cannot be instantiated
	 */
	@Test
	void theClassLoaderOfAnObjectFromAChildLoader() throws ReflectiveOperationException
	{
		final Class<? extends PersonDao> clazz = classInALoaderOfItsOwn(
			ClassLoader.getSystemClassLoader());
		final Object object = clazz.getDeclaredConstructor().newInstance();
		withContextClassLoader(ClassLoader.getSystemClassLoader(),
			() -> assertSame(clazz.getClassLoader(), ClassExtensions.getClassLoader(object)));
	}

	/**
	 * Test method for {@link ClassExtensions#getClassLoader(Object)} under a context class loader
	 * that is no parent of the object's: the context class loader is the answer
	 *
	 * @throws IOException
	 *             if the class loader cannot be closed
	 */
	@Test
	void theClassLoaderOfAnObjectUnderAnUnrelatedContextLoader() throws IOException
	{
		try (URLClassLoader unrelated = new URLClassLoader(new URL[0], null))
		{
			withContextClassLoader(unrelated,
				() -> assertSame(unrelated, ClassExtensions.getClassLoader(this)));
		}
	}

	/**
	 * Test method for {@link ClassExtensions#getClassLoader()} and
	 * {@link ClassExtensions#getClassLoader(Object)} under the platform class loader, a parent of
	 * the system class loader: the system class loader is the answer
	 */
	@Test
	void theClassLoaderUnderAParentOfTheSystemLoader()
	{
		final ClassLoader system = ClassLoader.getSystemClassLoader();
		withContextClassLoader(ClassLoader.getPlatformClassLoader(), () -> {
			assertSame(system, ClassExtensions.getClassLoader());
			assertSame(system, ClassExtensions.getClassLoader("text"));
		});
	}

	/**
	 * Test method for {@link ClassExtensions#getName(Class, boolean)} with an anonymous class: the
	 * name of the class it extends
	 */
	@Test
	void theNameOfAnAnonymousClass()
	{
		final Class<?> anonymous = new Object()
		{
		}.getClass();
		final Class<?> anonymousThread = new Thread()
		{
		}.getClass();
		assertEquals("Object", ClassExtensions.getName(anonymous, true));
		assertEquals("java.lang.Object", ClassExtensions.getName(anonymous, false));
		assertEquals("Thread", ClassExtensions.getSimpleName(anonymousThread));
	}

	/**
	 * Classes and whether each is an array of a primitive type
	 *
	 * @return the class and the answer
	 */
	static Stream<Arguments> primitiveArrays()
	{
		return Stream.of(Arguments.of(int[].class, true), Arguments.of(boolean[].class, true),
			Arguments.of(int[][].class, false), Arguments.of(Integer[].class, false),
			Arguments.of(int.class, false), Arguments.of(String.class, false));
	}

	/**
	 * Test method for {@link ClassExtensions#isPrimitiveArray(Class)}
	 *
	 * @param clazz
	 *            the class
	 * @param primitiveArray
	 *            whether it is an array of a primitive type
	 */
	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("primitiveArrays")
	void isPrimitiveArray(final Class<?> clazz, final boolean primitiveArray)
	{
		assertEquals(primitiveArray, ClassExtensions.isPrimitiveArray(clazz));
	}

	/**
	 * Test method for {@link ClassExtensions#getCallingMethodName(StackTraceElement[])}: the method
	 * of the third element, or null if the stack has fewer
	 */
	@Test
	void theCallingMethodName()
	{
		final StackTraceElement current = new StackTraceElement("C", "current", "C.java", 1);
		final StackTraceElement caller = new StackTraceElement("C", "caller", "C.java", 2);
		final StackTraceElement callersCaller = new StackTraceElement("C", "callersCaller",
			"C.java", 3);
		assertNull(ClassExtensions.getCallingMethodName(new StackTraceElement[0]));
		assertNull(ClassExtensions.getCallingMethodName(new StackTraceElement[] { current }));
		assertNull(
			ClassExtensions.getCallingMethodName(new StackTraceElement[] { current, caller }));
		assertEquals("callersCaller", ClassExtensions
			.getCallingMethodName(new StackTraceElement[] { current, caller, callersCaller }));
	}

	/**
	 * Test method for {@link ClassExtensions#getResource(Class, String)} and
	 * {@link ClassExtensions#getResource(String, Object)} with a name relative to the root of the
	 * class path and a class of another package: {@link Class#getResource(String)} finds nothing,
	 * the class loader does
	 */
	@Test
	void aResourceThatOnlyTheClassLoaderFinds()
	{
		final String name = "io/github/astrapi69/lang/resources.properties";
		assertNull(String.class.getResource(name));
		assertNotNull(ClassExtensions.getResource(String.class, name));
		assertNotNull(ClassExtensions.getResource(name, "text"));
	}

	/**
	 * Test method for {@link ClassExtensions#getPathFromObject(Object)} with an object of a class
	 * made at runtime, which has no class file
	 *
	 * @throws ReflectiveOperationException
	 *             if the class made at runtime cannot be instantiated
	 */
	@Test
	void thePathOfAnObjectWithoutAClassFile() throws ReflectiveOperationException
	{
		final Object object = classInALoaderOfItsOwn(ClassLoader.getSystemClassLoader())
			.getDeclaredConstructor().newInstance();
		assertNull(ClassExtensions.getPathFromObject(object));
		assertTrue(
			ClassExtensions.getPathFromObject(this).endsWith("ClassExtensionsLookupsTest.class"));
	}

	/**
	 * Test method for {@link ClassExtensions#getDirectoriesFromResources(String, boolean)}: the
	 * directories of a package, given with dots or as a path
	 *
	 * @throws IOException
	 *             if the class path cannot be read
	 */
	@Test
	void theDirectoriesOfAPackage() throws IOException
	{
		final List<File> withDots = ClassExtensions
			.getDirectoriesFromResources("io.github.astrapi69.lang", true);
		assertFalse(withDots.isEmpty());
		withDots.forEach(directory -> assertTrue(directory.isDirectory(), directory + ""));
		assertEquals(withDots,
			ClassExtensions.getDirectoriesFromResources("io/github/astrapi69/lang", false));
		assertTrue(ClassExtensions.getDirectoriesFromResources("io.github.astrapi69.lang", false)
			.isEmpty(), "a name with dots that is no package path finds nothing");
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsFile(String, Object)} with a resource in
	 * a jar: it is copied into the temporary directory, under the simple name of the object's class
	 *
	 * @param temporaryDirectory
	 *            the temporary directory for the copy
	 * @throws Exception
	 *             if the resource cannot be read or copied
	 */
	@Test
	void aResourceInAJarAsFile(final @TempDir Path temporaryDirectory) throws Exception
	{
		final String previous = System.getProperty("java.io.tmpdir");
		try
		{
			System.setProperty("java.io.tmpdir", temporaryDirectory.toString());
			final String name = "/org/junit/jupiter/api/Test.class";
			final File copy = ClassExtensions.getResourceAsFile(name, this);
			assertEquals(temporaryDirectory.resolve("ClassExtensionsLookupsTest").toFile(), copy);
			try (InputStream original = Test.class.getResourceAsStream("Test.class"))
			{
				assertArrayEquals(original.readAllBytes(), Files.readAllBytes(copy.toPath()));
			}
		}
		finally
		{
			System.setProperty("java.io.tmpdir", previous);
		}
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsFile(String, Object)} with a resource
	 * that is a file of the class path, found relative to the package of the object: the file
	 * itself, and no copy in the temporary directory
	 *
	 * @param temporaryDirectory
	 *            the temporary directory, which has to stay empty
	 * @throws Exception
	 *             if the resource cannot be found or the directory not read
	 */
	@Test
	void aFileResourceAsFile(final @TempDir Path temporaryDirectory) throws Exception
	{
		final String previous = System.getProperty("java.io.tmpdir");
		try
		{
			System.setProperty("java.io.tmpdir", temporaryDirectory.toString());
			final File file = ClassExtensions.getResourceAsFile("resources.properties", this);
			assertEquals(new File(getClass().getResource("resources.properties").toURI()), file);
			assertTrue(file.isFile());
			try (Stream<Path> copies = Files.list(temporaryDirectory))
			{
				assertEquals(0, copies.count(), "nothing is copied for a file");
			}
		}
		finally
		{
			System.setProperty("java.io.tmpdir", previous);
		}
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsFile(String, Object)} with a resource
	 * that only the class loader of the object has: the lookups through the class and the context
	 * class loader find nothing, the fallback to the object's class loader finds it. And a resource
	 * that does not exist gives null
	 *
	 * @param temporaryDirectory
	 *            the directory that holds the resource
	 * @throws Exception
	 *             if the resource cannot be written or the class made at runtime instantiated
	 */
	@Test
	void aResourceThatOnlyTheObjectsLoaderHas(final @TempDir Path temporaryDirectory)
		throws Exception
	{
		final Path resource = temporaryDirectory.resolve("fixture").resolve("only-here.txt");
		Files.createDirectories(resource.getParent());
		Files.writeString(resource, "only here", StandardCharsets.UTF_8);
		try (URLClassLoader loader = new URLClassLoader(
			new URL[] { temporaryDirectory.toUri().toURL() }, ClassLoader.getSystemClassLoader()))
		{
			final Object object = classInALoaderOfItsOwn(loader).getDeclaredConstructor()
				.newInstance();
			final File[] found = new File[2];
			withContextClassLoader(ClassLoader.getSystemClassLoader(), () -> {
				try
				{
					found[0] = ClassExtensions.getResourceAsFile("fixture/only-here.txt", object);
					found[1] = ClassExtensions.getResourceAsFile("fixture/missing.txt", object);
				}
				catch (URISyntaxException | IOException exception)
				{
					throw new IllegalStateException(exception);
				}
			});
			assertEquals(resource.toFile(), found[0]);
			assertNull(found[1]);
		}
	}
}
