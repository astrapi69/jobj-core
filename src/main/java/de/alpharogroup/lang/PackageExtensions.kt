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
package de.alpharogroup.lang

/**
 * The class [PackageExtensions] provides extension methods for the package of a [Class]
 * object
 */
object PackageExtensions {
    /**
     * Determines the package name from the given class object.
     *
     * @param clazz
     * The class object.
     *
     * @return The package name from the given class object.
     */
    @JvmStatic
    fun getPackageName(clazz: Class<*>): String {
        return clazz.getPackage().name
    }

    /**
     * Determines the package name from the given String(this must be the fully qualified class name
     * without the file extension).
     *
     * @param qualifiedClassName
     * The fully qualified class name without the file extension. For instance:
     * xy.ab.Test =&gt; xy.ab
     *
     * @return The package name from the given String.
     */
	@JvmStatic
	fun getPackageName(qualifiedClassName: String): String {
        return qualifiedClassName.substring(0,
                qualifiedClassName.lastIndexOf("."))
    }

    /**
     * Determines the package name from the given class object and adds a dot at the end.
     *
     * @param clazz
     * The class object.
     *
     * @return The package name from the given class object.
     */
	@JvmStatic
	fun getPackageNameWithDot(clazz: Class<*>): String {
        return clazz.getPackage().name + "."
    }

    /**
     * Determines the package path from the given class object.
     *
     * @param clazz
     * The class object.
     *
     * @return The package path from the given class object.
     */
    fun getPackagePath(clazz: Class<*>): String {
        return getPackageName(clazz).replace('.', '/') + "/"
    }

    /**
     * Determines the package path from the given object.
     *
     * @param instance
     * The object.
     *
     * @return The package path from the given object.
     */
    @JvmStatic
    fun getPackagePath(instance: Any): String {
        return getPackagePath(instance.javaClass)
    }

    /**
     * Determines the package path from the given String object that is in the dot-format. For
     * instance: given package string=='org.foo.bar' will result to 'org/foo/bar'
     *
     * @param packagePathWithDots
     * the package path with dots
     * @return The package path from the given String object
     */
    @JvmStatic
    fun getPackagePath(packagePathWithDots: String): String {
        return getPackagePath(packagePathWithDots, false)
    }

    /**
     * Determines the package path from the given String object that is in the dot-format.
     *
     * For instance: given package string=='org.foo.bar' will result to 'org/foo/bar/' if flag is
     * true otherwise 'org/foo/bar'
     *
     * @param packagePathWithDots
     * the package path with dots
     * @param withEndSlash
     * flag that indicates if a slash will be appended at the end
     * @return The package path from the given String object
     */
	@JvmStatic
	fun getPackagePath(packagePathWithDots: String,
					   withEndSlash: Boolean): String {
        val sb = StringBuilder()
        sb.append(packagePathWithDots.replace('.', '/'))
        if (withEndSlash) {
            sb.append("/")
        }
        return sb.toString()
    }

    /**
     * Determines the package path from the given object and adds a slash at the front.
     *
     * @param clazz
     * the clazz
     * @return The package path from the given object with the added slash at the front.
     */
    fun getPackagePathWithSlash(clazz: Class<*>): String {
        return "/" + getPackagePath(clazz)
    }

    /**
     * Determines the package path from the given object and adds a slash at the front.
     *
     * @param object
     * The object.
     *
     * @return The package path from the given object with the added slash at the front.
     */
	@JvmStatic
	fun getPackagePathWithSlash(`object`: Any): String {
        return getPackagePathWithSlash(`object`.javaClass)
    }
}