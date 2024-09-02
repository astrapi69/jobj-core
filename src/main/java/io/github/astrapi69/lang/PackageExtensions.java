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

import lombok.NonNull;

/**
 * The class {@link PackageExtensions} provides extension methods for the package of a {@link Class}
 * object
 */
public final class PackageExtensions
{

	/**
	 * Private constructor for prevent instantiation
	 */
	private PackageExtensions()
	{
	}

	/**
	 * Determines the package name from the given class object.
	 *
	 * @param clazz
	 *            The class object.
	 *
	 * @return The package name from the given class object.
	 */
	public static String getPackageName(final @NonNull Class<?> clazz)
	{
		final String packageName = clazz.getPackage().getName();
		return packageName;
	}

	/**
	 * Determines the package name from the given String(this must be the fully qualified class name
	 * without the file extension).
	 *
	 * @param qualifiedClassName
	 *            The fully qualified class name without the file extension. For instance:
	 *            xy.ab.Test =&gt; xy.ab
	 *
	 * @return The package name from the given String.
	 */
	public static String getPackageName(final @NonNull String qualifiedClassName)
	{
		final String packageName = qualifiedClassName.substring(0,
			qualifiedClassName.lastIndexOf("."));
		return packageName;
	}

	/**
	 * Determines the package name from the given class object and adds a dot at the end.
	 *
	 * @param clazz
	 *            The class object.
	 *
	 * @return The package name from the given class object.
	 */
	public static String getPackageNameWithDot(final @NonNull Class<?> clazz)
	{
		final String packageName = clazz.getPackage().getName() + ".";
		return packageName;
	}

	/**
	 * Determines the package path from the given class object.
	 *
	 * @param clazz
	 *            The class object.
	 *
	 * @return The package path from the given class object.
	 */
	public static String getPackagePath(final @NonNull Class<?> clazz)
	{
		final String packagePath = getPackageName(clazz).replace('.', '/') + "/";
		return packagePath;
	}

	/**
	 * Determines the package path from the given object.
	 *
	 * @param object
	 *            The object.
	 *
	 * @return The package path from the given object.
	 */
	public static String getPackagePath(final @NonNull Object object)
	{
		return getPackagePath(object.getClass());
	}

	/**
	 * Determines the package path from the given String object that is in the dot-format. For
	 * instance: given package string=='org.foo.bar' will result to 'org/foo/bar'
	 *
	 * @param packagePathWithDots
	 *            the package path with dots
	 * @return The package path from the given String object
	 */
	public static String getPackagePath(final @NonNull String packagePathWithDots)
	{
		return getPackagePath(packagePathWithDots, false);
	}


	/**
	 * Determines the package path from the given String object that is in the dot-format.
	 *
	 * For instance: given package string=='org.foo.bar' will result to 'org/foo/bar/' if flag is
	 * true otherwise 'org/foo/bar'
	 *
	 * @param packagePathWithDots
	 *            the package path with dots
	 * @param withEndSlash
	 *            flag that indicates if a slash will be appended at the end
	 * @return The package path from the given String object
	 */
	public static String getPackagePath(final @NonNull String packagePathWithDots,
		final boolean withEndSlash)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(packagePathWithDots.replace('.', '/'));
		if (withEndSlash)
		{
			sb.append("/");
		}
		final String packagePath = sb.toString();
		return packagePath;
	}

	/**
	 * Determines the package path from the given object and adds a slash at the front.
	 *
	 * @param clazz
	 *            the clazz
	 * @return The package path from the given object with the added slash at the front.
	 */
	public static String getPackagePathWithSlash(final @NonNull Class<?> clazz)
	{
		final String packagePath = "/" + getPackagePath(clazz);
		return packagePath;
	}

	/**
	 * Determines the package path from the given object and adds a slash at the front.
	 *
	 * @param object
	 *            The object.
	 *
	 * @return The package path from the given object with the added slash at the front.
	 */
	public static String getPackagePathWithSlash(final @NonNull Object object)
	{
		return getPackagePathWithSlash(object.getClass());
	}

}
