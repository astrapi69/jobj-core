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

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.astrapi69.classes.inner.OuterClass;
import io.github.astrapi69.collections.array.ArrayFactory;
import io.github.astrapi69.runtime.compiler.JavaSourceCompiler;
import io.github.astrapi69.test.objects.Member;
import io.github.astrapi69.test.objects.Person;
import io.github.astrapi69.test.objects.PremiumMember;
import io.github.astrapi69.test.objects.annotations.TestAnnotation;
import io.github.astrapi69.test.objects.annotations.interfaces.AnnotatedInterface;
import io.github.astrapi69.test.objects.enums.Brands;
import io.github.astrapi69.test.objects.generics.PersonDao;

/**
 * The unit test class for the class {@link ClassExtensions}.
 */
public class ClassExtensionsTest
{

	/** The result. */
	private boolean result;

	/**
	 * Sets up method will be invoked before every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@BeforeMethod
	public void setUp() throws Exception
	{
	}

	/**
	 * Tear down method will be invoked after every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@AfterMethod
	public void tearDown() throws Exception
	{
	}

	/**
	 * Test method for {@link ClassExtensions#forName(String)}
	 *
	 * @throws ClassNotFoundException
	 *             is thrown if the class was not found or could not be located
	 */
	@Test(enabled = true)
	public void testForName() throws ClassNotFoundException
	{
		final Class<?> expected = this.getClass();
		final String classname = "io.github.astrapi69.lang.ClassExtensionsTest";
		final Class<?> actual = ClassExtensions.forName(classname);

		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#forName(String)} in case of ClassNotFoundException
	 *
	 * @throws ClassNotFoundException
	 *             is thrown if the class was not found or could not be located
	 */
	@Test(enabled = true, expectedExceptions = ClassNotFoundException.class)
	public void testForNameClassNotFoundException() throws ClassNotFoundException
	{
		ClassExtensions.forName("ClassExtensionsTe");
	}

	/**
	 * Test method for {@link ClassExtensions#getComponentClassType(Object[])}
	 */
	@Test(enabled = true)
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
		expected = ClassExtensionsTest.class;
		assertEquals(expected, actual);

	}

	/**
	 * Test method for {@link ClassExtensions#getBaseClass(Class)}
	 */
	@Test(enabled = true)
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

		expected = Person.class;
		actual = ClassExtensions.getBaseClass(Member.class);
		assertEquals(expected, actual);

		expected = Person.class;
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
		expected = "invoke0";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getCglibProxy(Class)}.
	 */
	@Test
	public void testGetCglibProxy()
	{
		PersonDao personDao = new PersonDao();
		MethodInterceptor handler = new MethodInterceptorHandler<>(personDao);
		PersonDao proxy = (PersonDao)Enhancer.create(PersonDao.class, handler);

		Class<?> actual = ClassExtensions.getCglibProxy(proxy.getClass());
		Class<?> expected = PersonDao.class;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getClass(Object)}.
	 */
	@Test(enabled = true)
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
		expected = "io.github.astrapi69.test.objects.Person";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions#getClassnameWithSuffix(Object)}.
	 */
	@Test(enabled = true)
	public void testGetClassnameWithSuffix()
	{
		final String expected = "ClassExtensionsTest.class";
		final String classname = ClassExtensions.getClassnameWithSuffix(this);
		this.result = expected.equals(classname);
		assertTrue("", this.result);
	}

	/**
	 * Test method for {@link ClassExtensions#getClassType(Class)}.
	 *
	 * @throws InstantiationException
	 *             if a new instance of the bean's class cannot be instantiated
	 * @throws IllegalAccessException
	 *             if the caller does not have access to the property accessor method
	 */
	@Test(enabled = true)
	public void testGetClassType() throws InstantiationException, IllegalAccessException
	{
		ClassType actual = ClassExtensions.getClassType(OuterClass.class);
		ClassType expected = ClassType.DEFAULT;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(OuterClass.InnerClass.class);
		expected = ClassType.MEMBER;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(StaticNestedClass.class);
		expected = ClassType.MEMBER;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(new Runnable()
		{
			@Override
			public void run()
			{
			};
		}.getClass());
		expected = ClassType.ANONYMOUS;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(Brands.class);
		expected = ClassType.ENUM;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(TestAnnotation.class);
		expected = ClassType.ANNOTATION;
		assertEquals(expected, actual);

		final String[] foo = { "foo" };
		actual = ClassExtensions.getClassType(foo.getClass());
		expected = ClassType.ARRAY;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(ArrayList.class);
		expected = ClassType.COLLECTION;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(HashMap.class);
		expected = ClassType.MAP;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(AnnotatedInterface.class);
		expected = ClassType.INTERFACE;
		assertEquals(expected, actual);

		actual = ClassExtensions.getClassType(int.class);
		expected = ClassType.PRIMITIVE;
		assertEquals(expected, actual);

		final JavaSourceCompiler<Runnable> runtimeCompiler = new JavaSourceCompiler<>();
		final String source = "package io.github.astrapi69.lang;public final class FooRunnable implements Runnable { public void run() { System.out.println(\"Foo bar\"); } } ";
		final Class<Runnable> clazz = runtimeCompiler.compile("io.github.astrapi69.lang",
			"FooRunnable", source);

		actual = ClassExtensions.getClassType(clazz);
		expected = ClassType.DEFAULT;
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

		PersonDao personDao = new PersonDao();
		MethodInterceptor handler = new MethodInterceptorHandler<>(personDao);
		PersonDao proxy = (PersonDao)Enhancer.create(PersonDao.class, handler);
		jdkProxyInterfaces = ClassExtensions.getJdkProxyInterfaces(proxy.getClass());

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
	 * Test method for {@link ClassExtensions#getPathFromObject(Object)}.
	 */
	@Test
	public void testGetPathFromObject()
	{
		String expected;
		String actual;
		actual = ClassExtensions.getPathFromObject(Person.builder().build());
		assertTrue(actual.endsWith("/io/github/astrapi69/test/objects/Person.class"));
		assertTrue(actual.startsWith("file:"));

		actual = ClassExtensions.getPathFromObject(new PersonDao());
		assertTrue(actual.endsWith("/io/github/astrapi69/test/objects/generics/PersonDao.class"));

		actual = ClassExtensions.getPathFromObject(null);
		expected = null;
		assertEquals(expected, actual);

	}

	/**
	 * Test method for {@link ClassExtensions#getResource(String)}
	 */
	@Test(enabled = true)
	public void testGetResource()
	{
		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";
		final URL url = ClassExtensions.getResource(propertiesFilename);
		this.result = url != null;
		assertTrue("", this.result);

	}

	/**
	 * Test method for {@link ClassExtensions#getResources(String, String...)}
	 */
	@Test(enabled = true)
	public void testGetResources() throws IOException
	{
		List<URL> urls;
		String resourcesDirPath;

		resourcesDirPath = "io/github/astrapi69/lang";
		urls = ClassExtensions.getResources(resourcesDirPath);
		assertTrue(urls.size() == 3);

		urls = ClassExtensions.getResources(resourcesDirPath, "jar");
		assertTrue(urls.size() == 3);

		urls = ClassExtensions.getResources(resourcesDirPath, "file");
		assertTrue(urls.size() == 0);
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsFile(String)}
	 */
	@Test(enabled = true)
	public void testGetResourceAsFileString() throws URISyntaxException
	{
		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final File file = ClassExtensions.getResourceAsFile(propertiesFilename);
		this.result = file != null;
		assertTrue("File should not be null", this.result);
		assertTrue("File should exist.", file.exists());
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsFile(String, Object)}
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = true)
	public void testGetResourceAsFileStringObject() throws URISyntaxException, IOException
	{
		final String filename = "/io/github/astrapi69/test/objects/Person.class";

		final File file = ClassExtensions.getResourceAsFile(filename, Person.builder().build());
		this.result = file != null;
		assertTrue("File should not be null", this.result);
		assertTrue("File should exist.", file.exists());
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsFile(String, Object)} that throws an
	 * URISyntaxException
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri.
	 */
	@Test(expectedExceptions = URISyntaxException.class)
	public void testGetResourceAsFileStringThrowsURISyntaxException() throws URISyntaxException
	{
		ClassExtensions.getResourceAsFile("io/github/astrapi69/test/objects/Person.class");
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsStream(Class, String)}.
	 */
	@Test
	public void testGetResourceAsStreamClassOfQString()
	{
		InputStream inputStream = ClassExtensions.getResourceAsStream(Person.class,
			"io/github/astrapi69/test/objects/Person.class");
		assertNotNull(inputStream);
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsStream(String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test(enabled = true)
	public void testGetResourceAsStreamString() throws IOException
	{
		final String propertiesFilename = "io/github/astrapi69/lang/resources.properties";

		final InputStream is = ClassExtensions.getResourceAsStream(propertiesFilename);
		this.result = is != null;
		assertTrue("", this.result);
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
		final String filename = "io/github/astrapi69/test/objects/Person.class";

		InputStream inputStream = ClassExtensions.getResourceAsStream(filename,
			Person.builder().build());
		assertNotNull(inputStream);
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
	@Test(enabled = true)
	public void testGetResourceStringObject()
	{
		final String propertiesFilename = "resources.properties";

		final ClassExtensionsTest obj = new ClassExtensionsTest();
		final URL url = ClassExtensions.getResource(propertiesFilename, obj);

		this.result = url != null;
		assertTrue("", this.result);
	}

	/**
	 * Test method for {@link ClassExtensions#getResource(Class, String)}
	 */
	@Test(enabled = true)
	public void testGetRessource()
	{
		final String propertiesFilename = "resources.properties";

		final URL url = ClassExtensions.getResource(ClassExtensionsTest.class, propertiesFilename);

		this.result = url != null;
		assertTrue("", this.result);
	}

	/**
	 * Test method for {@link ClassExtensions#getResourceAsStream(Class, String)}
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test(enabled = false)
	public void testGetRessourceAsStream() throws IOException
	{
		final String propertiesFilename = "resources.properties";
		final String pathFromObject = PackageExtensions.getPackagePathWithSlash(this);
		final String path = pathFromObject + propertiesFilename;

		final ClassExtensionsTest obj = new ClassExtensionsTest();
		final InputStream is = ClassExtensions.getResourceAsStream(obj.getClass(), path);
		this.result = is != null;
		assertTrue("InputStream should not be null", this.result);
		final Properties prop = new Properties();
		prop.load(is);
		this.result = prop.size() == 3;
		assertTrue("Size of prop should be 3.", this.result);
	}

	/**
	 * Test method for {@link ClassExtensions#getUnwrappedProxy(Class)}.
	 */
	@Test
	public void testGetUnwrappedProxy()
	{
		Class<?> actual;
		Class<?> expected;
		PersonDao personDao = new PersonDao();
		MethodInterceptor handler = new MethodInterceptorHandler<>(personDao);
		PersonDao proxy = (PersonDao)Enhancer.create(PersonDao.class, handler);

		actual = ClassExtensions.getUnwrappedProxy(proxy.getClass());
		expected = PersonDao.class;
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
		final URL actual = ClassExtensions.getURL(Object.class);
		assertTrue(actual.toString().startsWith("jrt:"));
		assertTrue(actual.toString().endsWith("/java/lang/Object.class"));
	}

	/**
	 * Test method for {@link ClassExtensions#isCglib(Class)}.
	 */
	@Test
	public void testIsCglib()
	{
		PersonDao personDao = new PersonDao();
		MethodInterceptor handler = new MethodInterceptorHandler<>(personDao);
		PersonDao proxy = (PersonDao)Enhancer.create(PersonDao.class, handler);

		boolean cglib = ClassExtensions.isCglib(proxy.getClass());
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
		expected = true;
		assertEquals(expected, actual);

		actual = ClassExtensions.isDerivate(null, ClassExtensions.getClassLoader());
		expected = true;
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
		PersonDao personDao = new PersonDao();
		MethodInterceptor methodInterceptor = new MethodInterceptorHandler<>(personDao);
		PersonDao proxy = (PersonDao)Enhancer.create(PersonDao.class, methodInterceptor);
		expected = false;
		actual = ClassExtensions.isJdkProxy(proxy.getClass());
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
		boolean expected;

		PersonDao personDao = new PersonDao();
		MethodInterceptor methodInterceptor = new MethodInterceptorHandler<>(personDao);
		PersonDao cglibProxy = (PersonDao)Enhancer.create(PersonDao.class, methodInterceptor);
		expected = true;
		actual = ClassExtensions.isProxy(cglibProxy.getClass());
		assertEquals(expected, actual);

		Bla bla = new Bla();
		InvocationHandler invocationHandler = new InvocationHandlerHandler<>(bla);
		Foo proxy = (Foo)Proxy.newProxyInstance(ClassExtensions.getClassLoader(),
			new Class[] { Foo.class }, invocationHandler);
		expected = true;
		actual = ClassExtensions.isProxy(proxy.getClass());
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ClassExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
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
			Object object = method.invoke(original, args);
			return object;
		}
	}

	/**
	 * The class {@link MethodInterceptorHandler} for unit tests purposes.
	 */
	static class MethodInterceptorHandler<T> implements MethodInterceptor
	{
		private final T origin;

		public MethodInterceptorHandler(T origin)
		{
			this.origin = origin;
		}

		@Override
		public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy)
			throws Throwable
		{
			Object object = method.invoke(origin, args);
			return object;
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
				};
			};
			System.out.println(runnable.getClass().getName());
			System.out.println("Is anonymous class:" + runnable.getClass().isAnonymousClass());
			System.out.println("Enclosing class:" + runnable.getClass().getEnclosingClass());
			System.out.println("Canonical Name:" + runnable.getClass().getCanonicalName());
			System.out.println("toString:" + runnable.getClass().toString());
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
