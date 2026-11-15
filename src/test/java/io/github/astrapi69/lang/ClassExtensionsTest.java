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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import com.google.common.reflect.ClassPath;

import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.set.SetFactory;
import io.github.astrapi69.runtime.compiler.JavaSourceCompiler;
import io.github.astrapi69.test.object.Member;
import io.github.astrapi69.test.object.Person;
import io.github.astrapi69.test.object.PremiumMember;
import io.github.astrapi69.test.object.annotation.TestAnnotation;
import io.github.astrapi69.test.object.annotation.interfacetype.AnnotatedInterface;
import io.github.astrapi69.test.object.classes.inner.OuterClass;
import io.github.astrapi69.test.object.enumeration.Brand;
import io.github.astrapi69.test.object.generic.PersonDao;
import io.github.astrapi69.throwable.RuntimeExceptionDecorator;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * The unit test class for the class {@link ClassExtensions}
 */
public class ClassExtensionsTest
{

	/** The result. */
	private boolean result;

	/**
	 * Sets up method will be invoked before every unit test method
	 */
	@BeforeEach
	public void setUp()
	{
	}

	/**
	 * Tear down method will be invoked after every unit test method
	 */
	@AfterEach
	public void tearDown()
	{
	}

	/**
	 * Test method for {@link ClassExtensions#getDirectoriesFromResources(String, boolean)}
	 */
	@Test
	public void testGetDirectoriesFromResources() throws IOException
	{
		List<File> actual;
		String resourcesDirPath;

		resourcesDirPath = "io/github/astrapi69/lang";

		actual = ClassExtensions.getDirectoriesFromResources(resourcesDirPath, true);
		assertNotNull(actual);
	}

	/**
	 * Classes and what {@link ClassExtensions#isInstantiable(Class)} answers for them today
	 *
	 * @return the classes and the answers
	 */
	static Stream<Arguments> instantiableOrNot()
	{
		return Stream.of(Arguments.of(Brand.class, false, "an enum has no no-argument constructor"),
			Arguments.of(Integer.class, false, "no no-argument constructor"),
			Arguments.of(Runnable.class, false, "an interface has no constructor"),
			Arguments.of(Person.class, true, "a public no-argument constructor"),
			Arguments.of(String[].class, true, "an array type"),
			Arguments.of(ArrayList.class, true, "a collection type"),
			Arguments.of(List.class, true, "a collection interface"),
			Arguments.of(HashMap.class, true, "a map type"),
			Arguments.of(Map.class, true, "a map interface"),
			Arguments.of(InputStream.class, false, "abstract (#29)"),
			Arguments.of(AbstractWithConstructor.class, false,
				"abstract, though with a public no-argument constructor (#29)"),
			Arguments.of(ClassExtensions.class, false,
				"only a private no-argument constructor (#29)"),
			Arguments.of(OnlyPrivateConstructor.class, false, "only a private constructor (#29)"),
			Arguments.of(OnlyPrivateConstructors.class, false,
				"only private constructors, a no-argument one among them (#29)"),
			Arguments.of(PrivateNoArgumentConstructor.class, false,
				"the no-argument constructor is private (#29)"),
			Arguments.of(ProtectedNoArgumentConstructor.class, true,
				"a protected no-argument constructor"),
			Arguments.of(ObjectInputStream.class, true,
				"a protected no-argument constructor in a package java.base does not open"),
			Arguments.of(PackagePrivateNoArgumentConstructor.class, true,
				"a package-private no-argument constructor"),
			Arguments.of(Inner.class, false,
				"an inner class: its constructor takes the outer instance"));
	}

	/** Abstract, with a public no-argument constructor */
	abstract static class AbstractWithConstructor
	{
		/** The public no-argument constructor */
		public AbstractWithConstructor()
		{
		}
	}

	/** Has only a private constructor */
	static final class OnlyPrivateConstructor
	{
		private OnlyPrivateConstructor()
		{
		}
	}

	/** Has only private constructors, a no-argument one and one with an argument */
	static final class OnlyPrivateConstructors
	{
		private OnlyPrivateConstructors()
		{
		}

		/**
		 * The private constructor with an argument
		 *
		 * @param name
		 *            a name
		 */
		private OnlyPrivateConstructors(final String name)
		{
		}
	}

	/** A private no-argument constructor and a public one with an argument */
	static final class PrivateNoArgumentConstructor
	{
		private PrivateNoArgumentConstructor()
		{
		}

		/**
		 * The public constructor with an argument
		 *
		 * @param name
		 *            a name
		 */
		public PrivateNoArgumentConstructor(final String name)
		{
		}
	}

	/** A protected no-argument constructor */
	static class ProtectedNoArgumentConstructor
	{
		/** The protected no-argument constructor */
		protected ProtectedNoArgumentConstructor()
		{
		}
	}

	/** A package-private no-argument constructor */
	static class PackagePrivateNoArgumentConstructor
	{
		PackagePrivateNoArgumentConstructor()
		{
		}
	}

	/** An inner class, whose constructor takes the instance of the outer class */
	class Inner
	{
	}

	/**
	 * Test method for {@link ClassExtensions#isInstantiable(Class)}
	 *
	 * @param clazz
	 *            the class
	 * @param instantiable
	 *            what the method answers for it
	 * @param why
	 *            the reason of the answer
	 */
	@ParameterizedTest(name = "{0}: {1}, {2}")
	@MethodSource("instantiableOrNot")
	public void testIsInstantiable(final Class<?> clazz, final boolean instantiable,
		final String why)
	{
		assertEquals(instantiable, ClassExtensions.isInstantiable(clazz));
	}

	/**
	 * Test method for {@link ClassExtensions#isInstantiable(Class)} with every class of the family
	 * on the class path: each loads, and the method answers for each without an exception. The
	 * numbers of classes it counted before depended on the jars the build resolves (as #13), which
	 * is why it was disabled; it no longer counts them
	 */
	@Test
	public void testIsInstantiableWithAllClasses()
	{
		final ClassLoader classLoader = ClassExtensions.getClassLoader(ClassExtensions.class);
		ClassPath classPath = RuntimeExceptionDecorator.decorate(() -> ClassPath.from(classLoader));
		Set<ClassPath.ClassInfo> classes = classPath.getAllClasses();
		Set<Class> notInstantiableClasses = SetFactory.newHashSet();
		Set<Class> instantiableClasses = SetFactory.newHashSet();
		Set<String> notLoadableClasses = SetFactory.newHashSet();

		classes.stream()
			.filter(classInfo -> classInfo.getPackageName().startsWith("io.github.astrapi69"))
			.forEach(classInfo -> {
				Class<?> load;
				try
				{
					load = classInfo.load();
					final boolean instantiable = ClassExtensions.isInstantiable(load);
					if (instantiable)
					{
						instantiableClasses.add(load);
					}
					else
					{
						notInstantiableClasses.add(load);
					}

				}
				catch (Throwable t)
				{
					notLoadableClasses.add(classInfo.getName());
				}
			});
		assertEquals(Set.of(), notLoadableClasses, "every class loads and gets an answer");
		assertFalse(instantiableClasses.isEmpty(), "the family's classes were found");
		assertFalse(notInstantiableClasses.isEmpty(), "and some of them are not instantiable");
	}

	/**
	 * Test method for {@link ClassExtensions#forName(String)}
	 *
	 * @throws ClassNotFoundException
	 *             is thrown if the class was not found or could not be located
	 */
	@Test
	public void testForName() throws ClassNotFoundException
	{
		Class<?> expected;
		Class<?> actual;
		String classname;

		classname = "io.github.astrapi69.lang.ClassExtensionsTest";
		actual = ClassExtensions.forName(classname);
		expected = this.getClass();
		assertEquals(expected, actual);

		classname = "io.github.astrapi69.test.object.Person";
		actual = ClassExtensions.forName(classname);
		expected = Person.class;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#forName(String)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri
	 */
	@Test
	public void testGetRunningJarFile() throws URISyntaxException
	{
		File runningJarFile = ClassExtensions.getRunningJarFile(Person.class);
		assertNotNull(runningJarFile);

		String absolutePath = runningJarFile.getAbsolutePath();
		assertTrue(absolutePath.endsWith(".jar"));
		assertTrue(absolutePath.contains("test-object"));
	}

	/**
	 * Test method for {@link ClassExtensions#forName(String)} in case of ClassNotFoundException
	 *
	 * @throws ClassNotFoundException
	 *             is thrown if the class was not found or could not be located
	 */
	@Test
	public void testForNameClassNotFoundException() throws ClassNotFoundException
	{
		Assertions.assertThrows(ClassNotFoundException.class, () -> {
			ClassExtensions.forName("ClassExtensionsTe");
		});
	}

	/**
	 * Test method for {@link ClassExtensions#getComponentClassType(Object[])}
	 */
	@Test
	public void testGetArrayClass()
	{
		Class<ClassExtensionsTest> expected;
		Class<ClassExtensionsTest> actual;
		ClassExtensionsTest[] objectArray;

		objectArray = new ClassExtensionsTest[0];
		actual = ClassExtensions.getComponentClassType(objectArray);
		expected = ClassExtensionsTest.class;
		assertEquals(expected, actual);

		objectArray = new ClassExtensionsTest[1];
		objectArray[0] = this;
		actual = ClassExtensions.getComponentClassType(objectArray);
		assertEquals(expected, actual);

	}

	/**
	 * Test method for {@link ClassExtensions#getComponentClassType(Object[])} with an array whose
	 * elements are of a subclass of its component type, or null: the answer is the component type
	 * the array was created with
	 */
	@Test
	public void theComponentTypeOfAnArrayOfSubclasses()
	{
		assertEquals(Number.class, ClassExtensions.getComponentClassType(new Number[] { 1, 2.5 }));
		assertEquals(Number.class, ClassExtensions.getComponentClassType(new Number[] { 2.5, 1 }));
		assertEquals(Object.class, ClassExtensions.getComponentClassType(new Object[] { "a" }));
		assertEquals(String.class, ClassExtensions.getComponentClassType(new String[] { null }));
	}

	/**
	 * Test method for {@link ClassExtensions#getBaseClass(Class)}
	 */
	@Test
	public void testGetBaseClass()
	{
		Class<?> expected;
		Class<?> actual;

		expected = null;
		actual = ClassExtensions.getBaseClass(null);
		assertEquals(expected, actual);

		expected = Object.class;
		actual = ClassExtensions.getBaseClass(Object.class);
		assertEquals(expected, actual);

		expected = Person.class;
		actual = ClassExtensions.getBaseClass(PremiumMember.class);
		assertEquals(expected, actual);

		actual = ClassExtensions.getBaseClass(Member.class);
		assertEquals(expected, actual);

		actual = ClassExtensions.getBaseClass(Person.class);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getCallingMethodName(StackTraceElement[])}.
	 */
	@Test
	public void testGetCallingMethodName()
	{
		String expected;
		String actual;
		actual = ClassExtensions.getCallingMethodName(Thread.currentThread().getStackTrace());
		expected = "invoke";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getClass(Object)}.
	 */
	@Test
	public void testGetClass()
	{
		Class<ClassExtensionsTest> expected;
		Class<ClassExtensionsTest> actual;

		actual = ClassExtensions.getClass(this);
		expected = ClassExtensionsTest.class;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClass(null);
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getClassLoader(Object)}.
	 */
	@Test
	public void testGetClassLoaderObject()
	{
		ClassLoader classLoader = ClassExtensions.getClassLoader(Person.builder().build());
		assertNotNull(classLoader);

		classLoader = ClassExtensions.getClassLoader(this);
		assertNotNull(classLoader);
	}

	/**
	 * Test method for {@link ClassExtensions#getClassname(Class)}.
	 */
	@Test
	public void testGetClassname()
	{
		String expected;
		String actual;
		actual = ClassExtensions.getClassname(Person.class);
		expected = "io.github.astrapi69.test.object.Person";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getClassnameWithSuffix(Object)}.
	 */
	@Test
	public void testGetClassnameWithSuffix()
	{
		final String expected = "ClassExtensionsTest.class";
		final String classname = ClassExtensions.getClassnameWithSuffix(this);
		this.result = expected.equals(classname);
		assertTrue(this.result);
	}

	/**
	 * Test method for {@link ClassExtensions#getClassnameWithSuffix(Class)}
	 */
	@Test
	public void testGetClassnameWithSuffixFromClass()
	{
		final String expected = "ClassExtensionsTest.class";
		final String classname = ClassExtensions.getClassnameWithSuffix(ClassExtensionsTest.class);
		this.result = expected.equals(classname);
		assertTrue(this.result);
	}

	/**
	 * Test method for {@link ClassExtensions#getClassCanonicalName(Class)}
	 */
	@Test
	public void testGetClassCanonicalName()
	{
		final String expected = "io.github.astrapi69.lang.ClassExtensionsTest";
		final String classname = ClassExtensions.getClassCanonicalName(ClassExtensionsTest.class);
		this.result = expected.equals(classname);
		assertTrue(this.result);
	}

	/**
	 * Test method for {@link ClassExtensions#getClassType(Class)}
	 */
	@Test
	public void testGetClassType()
	{
		ClassType actual;
		ClassType expected;

		actual = ClassExtensions.getClassType(TestAnnotation.class);
		expected = ClassType.ANNOTATION;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(new Runnable()
		{
			@Override
			public void run()
			{
			}
		}.getClass());
		expected = ClassType.ANONYMOUS;
		assertEquals(expected, actual);

		final String[] foo = { "foo" };
		actual = ClassExtensions.getClassType(foo.getClass());
		expected = ClassType.ARRAY;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(ArrayList.class);
		expected = ClassType.COLLECTION;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(OuterClass.class);
		expected = ClassType.DEFAULT;
		assertEquals(expected, actual);

		final JavaSourceCompiler<Runnable> runtimeCompiler = new JavaSourceCompiler<>();
		final String source = "package io.github.astrapi69.lang;public final class FooRunnable implements Runnable { public void run() { System.out.println(\"Foo bar\"); } } ";
		final Class<Runnable> clazz = runtimeCompiler.compile("io.github.astrapi69.lang",
			"FooRunnable", source);

		actual = ClassExtensions.getClassType(clazz);
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(Brand.class);
		expected = ClassType.ENUM;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(AnnotatedInterface.class);
		expected = ClassType.INTERFACE;
		assertEquals(expected, actual);
		// local class example
		class LocalClass
		{
			String foo;

			LocalClass(String bar)
			{
				foo = bar;
			}
		}

		actual = ClassExtensions.getClassType(LocalClass.class);
		expected = ClassType.LOCAL;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(HashMap.class);
		expected = ClassType.MAP;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(OuterClass.InnerClass.class);
		expected = ClassType.MEMBER;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(StaticNestedClass.class);
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(int.class);
		expected = ClassType.PRIMITIVE;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(((Runnable)() -> {
		}).getClass());
		expected = ClassType.SYNTHETIC;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getCurrentMethodName(StackTraceElement[])}.
	 */
	@Test
	public void testGetCurrentMethodName()
	{
		String expected;
		String actual;
		actual = ClassExtensions.getCurrentMethodName(Thread.currentThread().getStackTrace());
		expected = "testGetCurrentMethodName";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getJdkProxyInterfaces(Class)}.
	 */
	@Test
	public void testGetJdkProxyInterfaces()
	{
		Class<?> expected;
		Class<?> actual;
		Class<?>[] jdkProxyInterfaces;

		jdkProxyInterfaces = ClassExtensions
			.getJdkProxyInterfaces(subclassProxyOf(PersonDao.class));

		assertNotNull(jdkProxyInterfaces);
		assertTrue(jdkProxyInterfaces.length == 1);

		Bla bla = new Bla();
		InvocationHandler invocationHandler = new InvocationHandlerHandler<>(bla);
		Foo jdkProxy = (Foo)Proxy.newProxyInstance(ClassExtensions.getClassLoader(),
			new Class[] { Foo.class }, invocationHandler);
		jdkProxyInterfaces = ClassExtensions.getJdkProxyInterfaces(jdkProxy.getClass());

		assertNotNull(jdkProxyInterfaces);
		assertTrue(jdkProxyInterfaces.length == 1);
		expected = Foo.class;
		actual = jdkProxyInterfaces[0];
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getName(Class)}.
	 */
	@Test
	public void testGetNameClass()
	{
		String expected;
		String actual;
		actual = ClassExtensions.getName(getClass());
		expected = "io.github.astrapi69.lang.ClassExtensionsTest";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getName(Class, boolean)}.
	 */
	@Test
	public void testGetNameClassBoolean()
	{
		String expected;
		String actual;
		actual = ClassExtensions.getName(getClass(), false);
		expected = "io.github.astrapi69.lang.ClassExtensionsTest";
		assertEquals(expected, actual);

		actual = ClassExtensions.getName(getClass(), true);
		expected = "ClassExtensionsTest";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getPath(Class)}
	 */
	@Test
	public void testGetPath()
	{
		String expected = "/java/lang/Object.class";
		String actual = ClassExtensions.getPath(Object.class);
		assertEquals(expected, actual);

		expected = "/java/lang/Class.class";
		actual = ClassExtensions.getPath(Class.class);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#isFromJar(Class)} and
	 * {@link ClassExtensions#getProtocol(Class)}, which look the class file up by
	 * {@link ClassExtensions#getPath(Class)}: a class of a jar, of the build's class directory, of
	 * the JDK's runtime image, and a nested class, for which the path was wrong before 10.0
	 */
	@Test
	public void theProtocolOfTheClassFile()
	{
		assertTrue(ClassExtensions.isFromJar(Test.class));
		assertEquals("jar", ClassExtensions.getProtocol(Test.class));
		assertFalse(ClassExtensions.isFromJar(ClassExtensions.class));
		assertEquals("file", ClassExtensions.getProtocol(ClassExtensions.class));
		assertFalse(ClassExtensions.isFromJar(String.class));
		assertEquals("jrt", ClassExtensions.getProtocol(String.class));
		assertEquals("jrt", ClassExtensions.getProtocol(Map.Entry.class));
		assertEquals("file", ClassExtensions.getProtocol(StaticNestedClass.class));
	}

	/**
	 * Test method for {@link ClassExtensions#getPath(Class)} and
	 * {@link ClassExtensions#getResource(Class)} with a nested, an anonymous and a local class: the
	 * path is the one of the class file, from the binary name
	 */
	@Test
	public void thePathOfANestedClass()
	{
		final Object anonymous = new Object()
		{
		};
		class Local
		{
		}
		assertEquals("/java/util/Map$Entry.class", ClassExtensions.getPath(Map.Entry.class));
		assertEquals("/" + anonymous.getClass().getName().replace('.', '/') + ".class",
			ClassExtensions.getPath(anonymous.getClass()));
		assertEquals("/io/github/astrapi69/lang/ClassExtensionsTest$1Local.class",
			ClassExtensions.getPath(Local.class));
		assertNotNull(ClassExtensions.getResource(Map.Entry.class));
		assertNotNull(ClassExtensions.getResource(Local.class));
		assertNotNull(ClassExtensions.getResource(anonymous.getClass()));
		assertNull(
			ClassExtensions.getResource(ClassExtensionsTest.subclassProxyOf(PersonDao.class)),
			"a class made at runtime has no class file");
		assertNotNull(ClassExtensions.getResource(String.class));
	}

	/**
	 * Test method for {@link ClassExtensions#getPathFromObject(Object)}.
	 */
	@Test
	public void testGetPathFromObject()
	{
		String expected;
		String actual;
		actual = ClassExtensions.getPathFromObject(Person.builder().build());
		assertTrue(actual.endsWith("/io/github/astrapi69/test/object/Person.class"));
		assertTrue(actual.startsWith("file:"));

		actual = ClassExtensions.getPathFromObject(new PersonDao());
		assertTrue(actual.endsWith("/io/github/astrapi69/test/object/generic/PersonDao.class"));

		actual = ClassExtensions.getPathFromObject(null);
		expected = null;
		assertEquals(expected, actual);

	}

	/**
	 * Test method for {@link ClassExtensions#getResource(String)}
	 */
	@Test
	public void testGetResource()
	{
		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";
		final URL url = ClassExtensions.getResource(propertiesFilename);
		this.result = url != null;
		assertTrue(this.result);

	}

	/**
	 * The protocols to exclude and the protocols of the resources that
	 * {@link ClassExtensions#getResources(String, String...)} finds without them, on a class path
	 * of one directory and one jar
	 *
	 * @return the excluded protocols and the protocols that are left, in their order
	 */
	static Stream<Arguments> resourcesByExcludedProtocols()
	{
		return Stream.of(Arguments.of(List.of(), List.of("file", "jar")),
			Arguments.of(List.of("jar"), List.of("file")),
			Arguments.of(List.of("file"), List.of("jar")),
			Arguments.of(List.of("file", "jar"), List.of()),
			Arguments.of(List.of("http"), List.of("file", "jar")));
	}

	/**
	 * Test method for {@link ClassExtensions#getResources(String, String...)}
	 *
	 * The class path is built here, one directory and one jar that both hold the package, and given
	 * to the method through the context class loader, so that what it finds does not depend on the
	 * jars the build resolves (#13)
	 *
	 * @param excludedProtocols
	 *            the protocols to exclude
	 * @param expectedProtocols
	 *            the protocols of the resources that are found, in their order
	 * @param temporaryDirectory
	 *            the directory that holds the class path
	 * @throws IOException
	 *             if the class path cannot be written
	 */
	@ParameterizedTest(name = "excluding {0} finds {1}")
	@MethodSource("resourcesByExcludedProtocols")
	public void testGetResources(final List<String> excludedProtocols,
		final List<String> expectedProtocols, final @TempDir Path temporaryDirectory)
		throws IOException
	{
		final String packagePath = "io/github/astrapi69/lang/resources/fixture";
		final Path directory = temporaryDirectory.resolve("classes");
		Files.createDirectories(directory.resolve(packagePath));
		Files.writeString(directory.resolve(packagePath).resolve("in-directory.txt"), "directory");
		final Path jar = temporaryDirectory.resolve("fixture.jar");
		try (JarOutputStream output = new JarOutputStream(Files.newOutputStream(jar)))
		{
			output.putNextEntry(new JarEntry(packagePath + "/"));
			output.closeEntry();
			output.putNextEntry(new JarEntry(packagePath + "/in-jar.txt"));
			output.write("jar".getBytes(StandardCharsets.UTF_8));
			output.closeEntry();
		}
		final Thread thread = Thread.currentThread();
		final ClassLoader previous = thread.getContextClassLoader();
		// no parent: the class path of the test JVM stays out of the search
		try (URLClassLoader classLoader = new URLClassLoader(
			new URL[] { directory.toUri().toURL(), jar.toUri().toURL() }, null))
		{
			thread.setContextClassLoader(classLoader);
			final List<URL> urls = ClassExtensions.getResources(packagePath,
				excludedProtocols.toArray(new String[0]));
			assertEquals(expectedProtocols,
				urls.stream().map(URL::getProtocol).collect(Collectors.toList()));
			for (final URL url : urls)
			{
				assertTrue(url.toString().endsWith(packagePath),
					url + " is the package of the class path");
			}
		}
		finally
		{
			thread.setContextClassLoader(previous);
		}
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsFile(String)}
	 */
	@Test
	public void testGetResourceAsFileString() throws URISyntaxException
	{
		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final File file = ClassExtensions.getResourceAsFile(propertiesFilename);
		this.result = file != null;
		assertTrue(this.result, "File should not be null");
		assertTrue(file.exists(), "File should exist.");
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsFile(String, Object)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetResourceAsFileStringObject() throws URISyntaxException, IOException
	{
		final String filename = "/io/github/astrapi69/test/object/Person.class";

		final File file = ClassExtensions.getResourceAsFile(filename, Person.builder().build());
		this.result = file != null;
		assertTrue(this.result, "File should not be null");
		assertTrue(file.exists(), "File should exist.");
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsFile(String, Object)} that throws an
	 * URISyntaxException
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri.
	 */
	@Test
	public void testGetResourceAsFileStringThrowsURISyntaxException() throws URISyntaxException
	{

		Assertions.assertThrows(URISyntaxException.class, () -> {
			ClassExtensions.getResourceAsFile("io/github/astrapi69/test/object/Person.class");
		});
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsStream(Class, String)}.
	 */
	@Test
	public void testGetResourceAsStreamClassOfQString()
	{
		InputStream inputStream = ClassExtensions.getResourceAsStream(Person.class,
			"io/github/astrapi69/test/object/Person.class");
		assertNotNull(inputStream);
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsStream(String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testGetResourceAsStreamString() throws IOException
	{
		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final InputStream is = ClassExtensions.getResourceAsStream(propertiesFilename);
		this.result = is != null;
		assertTrue(this.result);
		final Properties prop = new Properties();
		prop.load(is);
		this.result = prop.size() == 3;
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsStream(String, Object)}.
	 */
	@Test
	public void testGetResourceAsStreamStringObject()
	{
		final String filename = "io/github/astrapi69/test/object/Person.class";

		InputStream inputStream = ClassExtensions.getResourceAsStream(filename,
			Person.builder().build());
		assertNotNull(inputStream);
	}

	/**
	 * Test method for {@link ClassExtensions#getResource(Class, String)} and
	 * {@link ClassExtensions#getResource(String, Object)} with an absolute name and a class of a
	 * named module of the JDK, whose {@link Class#getResource(String)} finds no resource of the
	 * class path: the class loader finds it
	 */
	@Test
	public void anAbsoluteResourceNameAndAClassOfTheJdk()
	{
		final String name = "/io/github/astrapi69/lang/resources.properties";
		assertNull(String.class.getResource(name));
		assertNotNull(ClassExtensions.getResource(String.class, name));
		assertNotNull(ClassExtensions.getResource(name, "text"));
		assertNotNull(ClassExtensions.getResource(ClassExtensionsTest.class, name));
	}

	/**
	 * Test method for {@link ClassExtensions#getResource(Class)}.
	 */
	@Test
	public void testGetResourceClass()
	{
		String expected;
		String actual;
		URL resource = ClassExtensions.getResource(this.getClass());

		actual = resource.getProtocol();
		expected = "file";
		assertEquals(expected, actual);

		actual = resource.getPath();
		assertTrue(actual.endsWith("/io/github/astrapi69/lang/ClassExtensionsTest.class"));
	}

	/**
	 * Test method for {@link ClassExtensions#getResource(String, Object)}
	 */
	@Test
	public void testGetResourceStringObject()
	{
		final String propertiesFilename = "resources.properties";

		final ClassExtensionsTest obj = new ClassExtensionsTest();
		final URL url = ClassExtensions.getResource(propertiesFilename, obj);

		this.result = url != null;
		assertTrue(this.result);
	}

	/**
	 * Test method for {@link ClassExtensions#getResource(Class, String)}
	 */
	@Test
	public void testGetRessource()
	{
		final String propertiesFilename = "resources.properties";

		final URL url = ClassExtensions.getResource(ClassExtensionsTest.class, propertiesFilename);

		this.result = url != null;
		assertTrue(this.result);
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsStream(Class, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetRessourceAsStream() throws IOException
	{
		final String propertiesFilename = "resources.properties";
		final String pathFromObject = PackageExtensions.getPackagePathWithSlash(this);
		final String path = pathFromObject + propertiesFilename;

		final ClassExtensionsTest obj = new ClassExtensionsTest();
		final InputStream is = ClassExtensions.getResourceAsStream(obj.getClass(), path);
		this.result = is != null;
		assertTrue(this.result, "InputStream should not be null");
		final Properties prop = new Properties();
		prop.load(is);
		this.result = prop.size() == 3;
		assertTrue(this.result, "Size of prop should be 3.");
	}

	/**
	 * Test method for {@link ClassExtensions#getUnwrappedProxy(Class)}.
	 */
	@Test
	public void testGetUnwrappedProxy()
	{
		Class<?> actual;
		Class<?> expected;
		Map proxyInstance = (Map)Proxy.newProxyInstance(ClassExtensions.getClassLoader(),
			new Class[] { Map.class }, (proxy, method, methodArgs) -> {
				if (method.getName().equals("get"))
				{
					return 42;
				}
				else
				{
					throw new UnsupportedOperationException(
						"Unsupported method: " + method.getName());
				}
			});

		actual = ClassExtensions.getUnwrappedProxy(proxyInstance.getClass());
		expected = Map.class;
		assertEquals(expected, actual);

		actual = ClassExtensions.getUnwrappedProxy(null);
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getURL(Class)}
	 */
	@Test
	public void testGetURL()
	{
		URL actualUrl;
		actualUrl = ClassExtensions.getURL(Object.class);
		String urlAsString = actualUrl.toString();
		assertTrue(urlAsString.startsWith("jrt:"));
		assertTrue(urlAsString.endsWith("/java/lang/Object.class"));
	}

	/**
	 * Test method for {@link ClassExtensions#getProtocol(Class)}
	 */
	@Test
	public void testGetProtocol()
	{
		String actual;
		String expected;

		actual = ClassExtensions.getProtocol(Object.class);
		expected = "jrt";
		assertEquals(expected, actual);

		actual = ClassExtensions.getProtocol(ClassExtensionsTest.class);
		expected = "file";
		assertEquals(expected, actual);
	}

	/**
	 * Makes a subclass proxy of the given class with ByteBuddy, named the way a cglib proxy is
	 * named, with the tag {@code $$} that {@link ClassExtensions#isCglib(Class)} looks for. Before
	 * 10.0 the tests made it with cglib 3.3.0, which needed {@code --add-opens java.base/java.lang}
	 * on Java 17 and later; ByteBuddy loads it through a class loader of its own and needs nothing
	 * of the kind
	 *
	 * @param <T>
	 *            the type of the class
	 * @param superclass
	 *            the class to subclass
	 * @return the proxy class
	 */
	static <T> Class<? extends T> subclassProxyOf(final Class<T> superclass)
	{
		return new ByteBuddy().subclass(superclass)
			.name(superclass.getName() + "$$EnhancerByByteBuddy$$test")
			.method(ElementMatchers.any()).intercept(SuperMethodCall.INSTANCE).make()
			.load(ClassExtensionsTest.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
			.getLoaded();
	}

	/**
	 * Test method for {@link ClassExtensions#getCglibProxy(Class)},
	 * {@link ClassExtensions#unwrapProxy(Class)}, {@link ClassExtensions#getUnwrappedProxy(Class)}
	 * and {@link ClassExtensions#isProxy(Class)} with a subclass proxy: each sees through it to the
	 * class it extends
	 */
	@Test
	public void aSubclassProxyIsUnwrappedToItsSuperclass()
	{
		final Class<? extends PersonDao> proxyClass = subclassProxyOf(PersonDao.class);
		assertTrue(ClassExtensions.isProxy(proxyClass));
		assertFalse(ClassExtensions.isJdkProxy(proxyClass));
		assertEquals(PersonDao.class, ClassExtensions.getCglibProxy(proxyClass));
		assertEquals(PersonDao.class, ClassExtensions.getCglibProxy(PersonDao.class));
		assertArrayEquals(new Class<?>[] { PersonDao.class },
			ClassExtensions.unwrapProxy(proxyClass));
		assertEquals(PersonDao.class, ClassExtensions.getUnwrappedProxy(proxyClass));
	}

	/**
	 * Test method for {@link ClassExtensions#isCglib(Class)}.
	 */
	@Test
	public void testIsCglib()
	{
		boolean cglib = ClassExtensions.isCglib(subclassProxyOf(PersonDao.class));
		assertTrue(cglib);
	}

	/**
	 * Test method for {@link ClassExtensions#isDerivate(ClassLoader, ClassLoader)}.
	 */
	@Test
	public void testIsDerivate()
	{
		boolean expected;
		boolean actual;

		actual = ClassExtensions.isDerivate(Thread.currentThread().getContextClassLoader(),
			ClassLoader.getSystemClassLoader());
		expected = true;
		assertEquals(expected, actual);

		actual = ClassExtensions.isDerivate(null, null);
		assertEquals(expected, actual);

		actual = ClassExtensions.isDerivate(null, ClassExtensions.getClassLoader());
		assertEquals(expected, actual);

		actual = ClassExtensions.isDerivate(ClassExtensions.getClassLoader(), null);
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#isJdkProxy(Class)}.
	 */
	@Test
	public void testIsJdkProxy()
	{
		boolean actual;
		boolean expected;
		expected = false;
		actual = ClassExtensions.isJdkProxy(subclassProxyOf(PersonDao.class));
		assertEquals(expected, actual);

		Bla bla = new Bla();
		InvocationHandler invocationHandler = new InvocationHandlerHandler<>(bla);
		Foo jdkProxy = (Foo)Proxy.newProxyInstance(ClassExtensions.getClassLoader(),
			new Class[] { Foo.class }, invocationHandler);
		expected = true;
		actual = ClassExtensions.isJdkProxy(jdkProxy.getClass());
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#isPrimitiveArray(Class)}
	 */
	@Test
	public void testIsPrimitiveArray()
	{
		boolean actual;
		boolean expected;
		boolean[] primitiveBooleanArray;
		Boolean[] booleanObjectArray;
		byte[] primitiveByteArray;
		Byte[] byteObjectArray;
		int[] primitiveIntArray;
		Integer[] integerArray;

		expected = true;
		primitiveByteArray = ArrayFactory.newByteArray((byte)-84, (byte)-19, (byte)0);
		actual = ClassExtensions.isPrimitiveArray(primitiveByteArray.getClass());
		assertEquals(expected, actual);

		expected = false;
		byteObjectArray = ArrayFactory.newArray((byte)-84, (byte)-19, (byte)0);
		actual = ClassExtensions.isPrimitiveArray(byteObjectArray.getClass());
		assertEquals(expected, actual);

		expected = true;
		primitiveBooleanArray = ArrayFactory.newBooleanArray(true, true, false);
		actual = ClassExtensions.isPrimitiveArray(primitiveBooleanArray.getClass());
		assertEquals(expected, actual);

		expected = false;
		booleanObjectArray = ArrayFactory.newArray(true, true, false);
		actual = ClassExtensions.isPrimitiveArray(booleanObjectArray.getClass());
		assertEquals(expected, actual);

		expected = true;
		primitiveIntArray = ArrayFactory.newIntArray(1, 2, 3);
		actual = ClassExtensions.isPrimitiveArray(primitiveIntArray.getClass());
		assertEquals(expected, actual);

		expected = false;
		integerArray = ArrayFactory.newArray(1, 2, 3);
		actual = ClassExtensions.isPrimitiveArray(integerArray.getClass());
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#isProxy(Class)}.
	 */
	@Test
	public void testIsProxy()
	{
		boolean actual;

		Map proxyInstance = (Map)Proxy.newProxyInstance(ClassExtensions.getClassLoader(),
			new Class[] { Map.class }, (proxy, method, methodArgs) -> {
				if (method.getName().equals("get"))
				{
					return 42;
				}
				else
				{
					throw new UnsupportedOperationException(
						"Unsupported method: " + method.getName());
				}
			});
		actual = ClassExtensions.isProxy(proxyInstance.getClass());
		assertTrue(actual);

	}

	/**
	 * Test method for {@link ClassExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ClassExtensions.class);
	}

	/**
	 * The interface {@link Foo} for unit tests purposes.
	 */
	interface Foo
	{
		String bar(String string);
	}

	/**
	 * The class {@link InvocationHandlerHandler} for unit tests purposes.
	 */
	static class InvocationHandlerHandler<T> implements InvocationHandler
	{
		private final T original;

		public InvocationHandlerHandler(T original)
		{
			this.original = original;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
		{
			return method.invoke(original, args);
		}
	}

	/**
	 * The class {@link StaticNestedClass} for unit test purposes.
	 */
	static class StaticNestedClass
	{

		/**
		 * Static nested class method for unit test purposes.
		 */
		public static void staticNestedClassMethod()
		{
			final Runnable runnable = new Runnable()
			{
				@Override
				public void run()
				{
				}
			};
			System.out.println(runnable.getClass().getName());
			System.out.println("Is anonymous class:" + runnable.getClass().isAnonymousClass());
			System.out.println("Enclosing class:" + runnable.getClass().getEnclosingClass());
			System.out.println("Canonical Name:" + runnable.getClass().getCanonicalName());
			System.out.println("toString:" + runnable.getClass());
			System.out.println(StaticNestedClass.class.getName());
			System.out.println("getEnclosingMethod():" + runnable.getClass().getEnclosingMethod());
			// Object[] objects = { runnable };
		}
	}

	/**
	 * The class {@link Bla} for unit tests purposes.
	 */
	class Bla implements Foo
	{

		@Override
		public String bar(String string)
		{
			return string + "!!!";
		}

	}

}
