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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * The class {@link ClassExtensions} provides extension methods for the class {@link Class}.
 */
@UtilityClass
public final class ClassExtensions
{

	/** The Constant CGLIB_TAG contains the tag of a cglib class name. */
	private static final String CGLIB_TAG = "$$";

	/**
	 * Get the jar file from where the given class is running
	 *
	 * @param clazz
	 *            the class
	 * @return The directory or null if the directory does not exists.
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri.
	 */
	public static File getRunningJarFile(final @NonNull Class<?> clazz) throws URISyntaxException
	{
		return new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
	}

	/**
	 * Look up the class in the "current" ClassLoader.
	 *
	 * @param className
	 *            The class name to load
	 * @return the class
	 * @throws ClassNotFoundException
	 *             is thrown if the Class was not found or could not be located.
	 */
	public static Class<?> forName(final @NonNull String className) throws ClassNotFoundException
	{
		Class<?> clazz;
		try
		{
			clazz = Class.forName(className);
		}
		catch (final Throwable throwable)
		{
			try
			{
				clazz = Class.forName(className, true, getClassLoader());
			}
			catch (final Throwable throwable2)
			{
				clazz = Class.forName(className, false, getClassLoader());
			}
		}
		return clazz;
	}

	/**
	 * Gets the parent base class from the given child class.
	 *
	 * @param childClass
	 *            the child class
	 * @return the parent base class from the given child class.
	 */
	public static Class<?> getBaseClass(final Class<?> childClass)
	{
		if (childClass == null || childClass.equals(Object.class))
		{
			return childClass;
		}
		Class<?> superClass = childClass.getSuperclass();
		if (superClass != null && superClass.equals(Object.class))
		{
			return childClass;
		}
		while (superClass != null && !(superClass.getSuperclass() != null
			&& superClass.getSuperclass().equals(Object.class)))
		{
			superClass = superClass.getSuperclass();
		}
		return superClass;
	}

	/**
	 * Gets the calling method name.
	 *
	 * @param elements
	 *            the elements
	 * @return the calling method name
	 */
	public static String getCallingMethodName(final @NonNull StackTraceElement[] elements)
	{
		String callingMethodName = null;
		if (2 < elements.length)
		{
			final StackTraceElement element = elements[2];
			callingMethodName = element.getMethodName();
		}
		return callingMethodName;
	}

	/**
	 * Gets the real class if the given class is decorated with cglib proxy classes.
	 *
	 * @param clazz
	 *            the class
	 * @return the real class if the given class is decorated with cglib proxy classes and if not
	 *         the given class will be returned.
	 */
	public static Class<?> getCglibProxy(final @NonNull Class<?> clazz)
	{
		Class<?> found = clazz;
		while (isCglib(found))
		{
			found = found.getSuperclass();
		}
		return found;
	}

	/**
	 * Gets the {@link Class} of the given object.
	 *
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object to resolve the class
	 * @return the {@link Class} of the given object or null if the object is null.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(final T object)
	{
		if (object != null)
		{
			return (Class<T>)object.getClass();
		}
		return null;
	}

	/**
	 * Gets the component {@link Class} type of the given array object
	 *
	 * @param <T>
	 *            the generic type
	 * @param arrayObject
	 *            the object to resolve the class
	 * @return the component {@link Class} of the given object
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getComponentClassType(final @NonNull T[] arrayObject)
	{
		if (0 < arrayObject.length)
		{
			return (Class<T>)arrayObject[0].getClass();
		}
		return (Class<T>)arrayObject.getClass().getComponentType();
	}

	/**
	 * Gets the current class loader.
	 *
	 * @return 's the current class loader
	 */
	public static ClassLoader getClassLoader()
	{
		return ClassExtensions.getClassLoader(null);
	}

	/**
	 * Gets the ClassLoader from the given object.
	 *
	 * @param obj
	 *            The object.
	 * @return the ClassLoader from the given object.
	 */
	public static ClassLoader getClassLoader(final Object obj)
	{
		ClassLoader classLoader;
		if (null != obj)
		{
			if (isDerivate(Thread.currentThread().getContextClassLoader(),
				obj.getClass().getClassLoader()))
			{
				classLoader = obj.getClass().getClassLoader();
			}
			else
			{
				classLoader = Thread.currentThread().getContextClassLoader();
			}
			if (isDerivate(classLoader, ClassLoader.getSystemClassLoader()))
			{
				classLoader = ClassLoader.getSystemClassLoader();
			}
		}
		else
		{
			if (isDerivate(Thread.currentThread().getContextClassLoader(),
				ClassLoader.getSystemClassLoader()))
			{
				classLoader = ClassLoader.getSystemClassLoader();
			}
			else
			{
				classLoader = Thread.currentThread().getContextClassLoader();
			}
		}
		return classLoader;
	}

	/**
	 * Gets the classname from the given class.
	 *
	 * @param clazz
	 *            The class.
	 * @return The classname.
	 */
	public static String getClassname(final @NonNull Class<?> clazz)
	{
		return clazz.getName();
	}

	/**
	 * Gets the classname from the given class.
	 *
	 * @param clazz
	 *            The class.
	 * @return The classname.
	 */
	public static String getClassCanonicalName(final @NonNull Class<?> clazz)
	{
		return clazz.getCanonicalName();
	}

	/**
	 * Gets the classname and concats the suffix ".class" from the class.
	 *
	 * @param clazz
	 *            The class.
	 * @return The classname and concats the suffix ".class".
	 */
	public static String getClassnameWithSuffix(final @NonNull Class<?> clazz)
	{
		String className = clazz.getName();
		className = className.substring(className.lastIndexOf('.') + 1) + ".class";
		return className;
	}

	/**
	 * Gets the classname and concats the suffix ".class" from the object.
	 *
	 * @param obj
	 *            The object.
	 * @return The classname and concats the suffix ".class".
	 */
	public static String getClassnameWithSuffix(final @NonNull Object obj)
	{
		return getClassnameWithSuffix(obj.getClass());
	}

	/**
	 * Gets the {@link ClassType} from the given class.
	 *
	 * @param clazz
	 *            The class.
	 * @return the {@link ClassType} from the given class.
	 */
	public static ClassType getClassType(final @NonNull Class<?> clazz)
	{
		if (clazz.isArray())
		{
			return ClassType.ARRAY;
		}
		if (isCollection(clazz))
		{
			return ClassType.COLLECTION;
		}
		if (isMap(clazz))
		{
			return ClassType.MAP;
		}
		if (clazz.isLocalClass())
		{
			return ClassType.LOCAL;
		}
		if (clazz.isMemberClass())
		{
			return ClassType.MEMBER;
		}
		if (clazz.isPrimitive())
		{
			return ClassType.PRIMITIVE;
		}
		if (clazz.isAnnotation())
		{
			return ClassType.ANNOTATION;
		}
		if (clazz.isEnum())
		{
			return ClassType.ENUM;
		}
		if (clazz.isInterface())
		{
			return ClassType.INTERFACE;
		}
		if (clazz.isSynthetic())
		{
			return ClassType.SYNTHETIC;
		}
		if (clazz.isAnonymousClass())
		{
			return ClassType.ANONYMOUS;
		}
		return ClassType.DEFAULT;
	}

	/**
	 * Gets the current method name.
	 *
	 * @param elements
	 *            the elements
	 * @return the current method name
	 */
	public static String getCurrentMethodName(final @NonNull StackTraceElement[] elements)
	{
		String currentMethodName = null;
		boolean isNext = false;
		for (final StackTraceElement element : elements)
		{
			if (isNext)
			{
				currentMethodName = element.getMethodName();
				break;
			}
			isNext = element.getMethodName().equals("getStackTrace");
		}
		return currentMethodName;
	}

	/**
	 * Gets the directories from the given path.
	 *
	 * @param path
	 *            the path
	 * @param isPackage
	 *            If the Flag is true than the given path is a package.
	 * @return the directories from resources
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static List<File> getDirectoriesFromResources(@NonNull String path,
		final boolean isPackage) throws IOException
	{
		if (isPackage)
		{
			path = path.replace('.', '/');
		}
		final List<URL> resources = ClassExtensions.getResources(path);
		final List<File> dirs = new ArrayList<>();
		for (final URL resource : resources)
		{
			dirs.add(new File(URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8)));
		}
		return dirs;
	}

	/**
	 * Gets the jdk proxy interfaces.
	 *
	 * @param clazz
	 *            the class
	 * @return the jdk proxy interfaces
	 */
	public static Class<?>[] getJdkProxyInterfaces(final @NonNull Class<?> clazz)
	{
		if (isJdkProxy(clazz))
		{
			return clazz.getInterfaces();
		}
		return new Class<?>[] { clazz };
	}

	/**
	 * Returns the name of the given class or null if the given class is null.
	 *
	 * @param clazz
	 *            The class.
	 *
	 * @return The name of the given class.
	 */
	public static String getName(final @NonNull Class<?> clazz)
	{
		return getName(clazz, false);
	}

	/**
	 * Returns the name of the given class or null if the given class is null. If the given flag
	 * 'simple' is true the simple name (without the package) will be returned.
	 *
	 * @param clazz
	 *            The class
	 * @param simple
	 *            The flag if the simple name should be returned.
	 *
	 * @return The name of the given class or if the given flag 'simple' is true the simple name
	 *         (without the package) will be returned.
	 */
	public static String getName(Class<?> clazz, final boolean simple)
	{
		String name = null;
		if (clazz != null)
		{
			while (clazz.isAnonymousClass())
			{
				clazz = clazz.getSuperclass();
			}
			if (simple)
			{
				name = clazz.getSimpleName();
			}
			else
			{
				name = clazz.getName();
			}
		}
		return name;
	}


	/**
	 * Gets the path from the given class. For instance /java/lang/Object.class if the given class
	 * is from {@code Object}
	 *
	 * @param clazz
	 *            The class.
	 * @return the path from the given class.
	 */
	public static String getPath(final @NonNull Class<?> clazz)
	{
		final String packagePath = PackageExtensions.getPackagePath(clazz);
		final String className = ClassExtensions.getSimpleName(clazz);
		return new StringBuilder().append("/").append(packagePath).append(className)
			.append(".class").toString();
	}

	/**
	 * Finds the absolute path from the object.
	 *
	 * @param obj
	 *            The object.
	 * @return The absolute path from the object.
	 */
	public static String getPathFromObject(final Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		URL resource = obj.getClass().getResource(ClassExtensions.getClassnameWithSuffix(obj));
		if (resource == null)
		{
			return null;
		}
		return resource.getPath();
	}

	/**
	 * Gives the url from the path back.
	 *
	 * @param clazz
	 *            The class-object.
	 * @return 's the url from the path.
	 */
	public static URL getResource(final @NonNull Class<?> clazz)
	{
		final String path = ClassExtensions.getPath(clazz);
		URL url = clazz.getResource(path);
		if (url == null)
		{
			url = ClassExtensions.getClassLoader().getResource(path);
		}
		return url;
	}

	/**
	 * Gives the url from the path back.
	 *
	 * @param clazz
	 *            The class-object.
	 * @param path
	 *            The path.
	 * @return 's the url from the path.
	 */
	public static URL getResource(final @NonNull Class<?> clazz, final @NonNull String path)
	{
		URL url = clazz.getResource(path);
		if (url == null)
		{
			url = ClassExtensions.getClassLoader().getResource(path);
		}
		return url;
	}

	/**
	 * Gives the URL from the resource. Wrapes the Class.getResource(String)-method.
	 *
	 * @param name
	 *            The name from the resource.
	 * @return The resource or null if the resource does not exists.
	 */
	public static URL getResource(final @NonNull String name)
	{
		String path = name;
		if (name.startsWith("/"))
		{
			path = name.substring(1);
		}
		return ClassExtensions.getClassLoader().getResource(path);
	}

	/**
	 * Gives the URL from the resource. Wrapes the Class.getResource(String)-method.
	 *
	 * @param <T>
	 *            the generic type
	 * @param name
	 *            The name from the resource.
	 * @param obj
	 *            The Object.
	 * @return The resource or null if the resource does not exists.
	 */
	public static <T> URL getResource(final @NonNull String name, final @NonNull T obj)
	{
		final Class<?> clazz = obj.getClass();
		URL url = clazz.getResource(name);
		if (url == null)
		{
			url = getResource(clazz, name);
		}
		return url;
	}

	/**
	 * Gives the resource as a file Object.
	 *
	 * @param name
	 *            The name from the file.
	 * @return The file or null if the file does not exists.
	 *
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri.
	 */
	public static File getResourceAsFile(final @NonNull String name) throws URISyntaxException
	{
		File file = null;
		URL url = getResource(name);
		if (null == url)
		{
			url = ClassExtensions.getClassLoader().getResource(name);
			if (null != url)
			{
				file = new File(url.toURI());
			}
		}
		else
		{
			if (url.getProtocol().equals("jar"))
			{
				throw new URISyntaxException(url.toString(),
					"Resource is in a jar file. Use instead the method ClassExtensions#getResourceAsStream(String). Given resource is");
			}
			if (url.getProtocol().equals("file"))
			{
				file = new File(url.toURI());
			}
		}
		return file;
	}

	/**
	 * Gives the resource as a file Object.
	 *
	 * @param name
	 *            The name from the file.
	 * @param obj
	 *            The Object.
	 * @return The file or null if the file does not exists.
	 * @throws URISyntaxException
	 *             occurs by creation of the file with an uri.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File getResourceAsFile(final @NonNull String name, final @NonNull Object obj)
		throws URISyntaxException, IOException
	{
		File file = null;
		URL url = getResource(name, obj);
		if (null == url)
		{
			url = ClassExtensions.getClassLoader(obj).getResource(name);
			if (null != url)
			{
				file = new File(url.toURI());
			}
		}
		else
		{
			if (url.getProtocol().equals("jar"))
			{
				InputStream resourceAsStream = ClassExtensions.getResourceAsStream(name, obj);
				file = new File(System.getProperty("java.io.tmpdir"),
					obj.getClass().getSimpleName());
				Files.copy(resourceAsStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			if (url.getProtocol().equals("file"))
			{
				file = new File(url.toURI());
			}

		}
		return file;
	}

	/**
	 * This method call the getResourceAsStream from the ClassLoader. You can use this method to
	 * read files from jar-files.
	 *
	 * @param clazz
	 *            the clazz
	 * @param uri
	 *            The uri as String.
	 * @return The InputStream from the uri.
	 */
	public static InputStream getResourceAsStream(final @NonNull Class<?> clazz,
		final @NonNull String uri)
	{
		InputStream is = clazz.getResourceAsStream(uri);
		if (null == is)
		{
			is = ClassExtensions.getClassLoader().getResourceAsStream(uri);
		}
		return is;
	}

	/**
	 * Gives the Inputstream from the resource. Wrapes the Class.getResourceAsStream(String)-method.
	 *
	 * @param name
	 *            The name from the resource.
	 * @return The resource or null if the resource does not exists.
	 */
	public static InputStream getResourceAsStream(final @NonNull String name)
	{
		return ClassExtensions.getClassLoader().getResourceAsStream(name);
	}

	/**
	 * Gives the Inputstream from the resource. Wrapes the Class.getResourceAsStream(String)-method.
	 *
	 * @param name
	 *            The name from the resource.
	 * @param obj
	 *            The Object.
	 * @return The resource or null if the resource does not exists.
	 */
	public static InputStream getResourceAsStream(final @NonNull String name,
		final @NonNull Object obj)
	{
		InputStream inputStream = obj.getClass().getResourceAsStream(name);
		if (null == inputStream)
		{
			final ClassLoader loader = ClassExtensions.getClassLoader(obj);
			inputStream = loader.getResourceAsStream(name);
		}
		return inputStream;
	}

	/**
	 * Gets a list with urls from the given path for all resources.
	 *
	 * @param path
	 *            The base path.
	 * @param excludeUrlProtocols
	 *            flag for exclude jor files from the result
	 * @return The resources.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static List<URL> getResources(final @NonNull String path, String... excludeUrlProtocols)
		throws IOException
	{
		ArrayList<URL> urls = Collections.list(ClassExtensions.getClassLoader().getResources(path));
		return 0 < excludeUrlProtocols.length
			? urls.stream()
				.filter(url -> !Arrays.asList(excludeUrlProtocols).contains(url.getProtocol()))
				.collect(Collectors.toList())
			: urls;
	}

	/**
	 * Returns the simple name of the given class or null if the given class is null.
	 *
	 * @param clazz
	 *            The class.
	 *
	 * @return The simple name of the given class.
	 */
	public static String getSimpleName(final @NonNull Class<?> clazz)
	{
		return getName(clazz, true);
	}

	/**
	 * Gets the unwrapped proxy class.
	 *
	 * @param clazz
	 *            the class
	 * @return the unwrapped proxy class or null if the given {@link Class} is null.
	 */
	public static Class<?> getUnwrappedProxy(final Class<?> clazz)
	{
		final Class<?>[] found = unwrapProxy(clazz);
		if (found != null && 0 < found.length)
		{
			return found[0];
		}
		return null;
	}

	/**
	 * Returns the URL from the given class.
	 *
	 * @param clazz
	 *            The class.
	 * @return the URL from the given class.
	 */
	public static URL getURL(final @NonNull Class<?> clazz)
	{
		return ClassExtensions.getResource(ClassExtensions.getPath(clazz));
	}

	/**
	 * Checks if the given {@link Class} is cglib proxy class.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the class to check
	 * @return true, if the given {@link Class} is cglib proxy class otherwise false.
	 */
	public static <T> boolean isCglib(final Class<T> clazz)
	{
		return clazz != null && clazz.getName().contains(CGLIB_TAG);
	}

	/**
	 * Checks if the given class is assignable from {@link Collection}.
	 *
	 * @param clazz
	 *            The class.
	 * @return true, if the given class is assignable from {@link Collection} otherwise false.
	 */
	public static boolean isCollection(final @NonNull Class<?> clazz)
	{
		return Collection.class.isAssignableFrom(clazz);
	}

	/**
	 * Compares the two given ClassLoader objects and returns true if compare is a derivate of
	 * source.
	 *
	 * @param source
	 *            the source
	 * @param compare
	 *            the compare
	 * @return true, if compare is a derivate of source.
	 */
	public static boolean isDerivate(final ClassLoader source, ClassLoader compare)
	{
		if (source == compare)
		{
			return true;
		}
		if (compare == null)
		{
			return false;
		}
		if (source == null)
		{
			return true;
		}
		while (null != compare)
		{
			compare = compare.getParent();
			if (source == compare)
			{
				return true;
			}
		}
		return false;
	}


	/**
	 * Checks if the given {@link Class} is instantiable
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the class to check
	 * @return true, if the given {@link Class} is instantiable otherwise false
	 */
	public static <T> boolean isInstantiable(final Class<T> clazz)
	{
		try
		{
			final Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
			return declaredConstructor != null;
		}
		catch (NoSuchMethodException throwable)
		{
			return false;
		}
	}

	/**
	 * Checks if the given {@link Class} is a JDK proxy class.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the class to check
	 * @return true, if the given {@link Class} is a JDK proxy class otherwise false.
	 */
	public static <T> boolean isJdkProxy(final Class<T> clazz)
	{
		return clazz != null && Proxy.isProxyClass(clazz);
	}

	/**
	 * Checks if the given class is assignable from {@link Map}.
	 *
	 * @param clazz
	 *            The class.
	 * @return true, if the given class is assignable from {@link Map} otherwise false.
	 */
	public static boolean isMap(final @NonNull Class<?> clazz)
	{
		return Map.class.isAssignableFrom(clazz);
	}

	/**
	 * Checks if the given class is an array of primitive type
	 *
	 * @param clazz
	 *            The class
	 * @return true, if the given class is an array of primitive type otherwise false
	 */
	public static boolean isPrimitiveArray(final @NonNull Class<?> clazz)
	{
		return clazz.isArray() && clazz.getComponentType().isPrimitive();
	}

	/**
	 * Checks if the given {@link Class} is a proxy class.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the class to check
	 * @return true, if the given {@link Class} is a proxy class otherwise false.
	 */
	public static <T> boolean isProxy(final Class<T> clazz)
	{
		return isJdkProxy(clazz) || isCglib(clazz);
	}

	/**
	 * Unwrap the given {@link Class} if it is wrapped from cglib or jdk proxies.
	 *
	 * @param clazz
	 *            the class
	 * @return the unwrapped classes as an array
	 */
	public static Class<?>[] unwrapProxy(final Class<?> clazz)
	{
		if (clazz == null)
		{
			return new Class<?>[] { };
		}
		Class<?> found = clazz;
		if (isCglib(found))
		{
			found = getCglibProxy(found);
		}
		if (isJdkProxy(found))
		{
			return getJdkProxyInterfaces(found);
		}
		return new Class<?>[] { found };
	}

}
