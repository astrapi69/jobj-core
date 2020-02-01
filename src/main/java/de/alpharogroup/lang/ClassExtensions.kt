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

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Proxy
import java.net.URISyntaxException
import java.net.URL
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*

/**
 * The class [ClassExtensions] provides extension methods for the class [Class].
 */
object ClassExtensions {
    /** The Constant CGLIB_TAG contains the tag of a cglib class name.  */
    internal const val CGLIB_TAG = "$$"

    /**
     * Look up the class in the "current" ClassLoader.
     *
     * @param className
     * The class name to load
     * @return the class
     * @throws ClassNotFoundException
     * is thrown if the Class was not found or could not be located.
     */
    @JvmStatic
	@Throws(ClassNotFoundException::class)
    fun forName(className: String): Class<*>? {
        var clazz: Class<*>?
        try {
            clazz = Class.forName(className)
        } catch (throwable: Throwable) {
            clazz = Class.forName(className, true, classLoader)
            if (clazz == null) {
                clazz = Class.forName(className, false, classLoader)
                if (clazz == null) {
                    throw throwable
                }
            }
        }
        return clazz
    }

    /**
     * Gets the parent base class from the given child class.
     *
     * @param childClass
     * the child class
     * @return the parent base class from the given child class.
     */
	@JvmStatic
	fun getBaseClass(childClass: Class<*>?): Class<*>? {
        if (childClass == null || childClass == Any::class.java) {
            return childClass
        }
        var superClass = childClass.superclass
        if (superClass != null && superClass == Any::class.java) {
            return childClass
        }
        while (superClass != null && !(superClass.superclass != null
                        && superClass.superclass == Any::class.java)) {
            superClass = superClass.superclass
        }
        return superClass
    }

    /**
     * Gets the calling method name.
     *
     * @param elements
     * the elements
     * @return the calling method name
     */
	@JvmStatic
	fun getCallingMethodName(elements: Array<StackTraceElement>): String? {
        var callingMethodName: String? = null
        if (2 < elements.size) {
            val element = elements[2]
            callingMethodName = element.methodName
        }
        return callingMethodName
    }

    /**
     * Gets the real class if the given class is decorated with cglib proxy classes.
     *
     * @param clazz
     * the class
     * @return the real class if the given class is decorated with cglib proxy classes and if not
     * the given class will be returned.
     */
	@JvmStatic
	fun getCglibProxy(clazz: Class<*>): Class<*> {
        var found = clazz
        while (isCglib(found)) {
            found = found.superclass
        }
        return found
    }

    /**
     * Gets the [Class] of the given object.
     *
     * @param <T>
     * the generic type
     * @param instance
     * the object to resolve the class
     * @return the [Class] of the given object or null if the object is null.
    </T> */
	@JvmStatic
	fun <T : Any> getClass(instance: T): Class<T>? {
        return instance.javaClass
    }

    /**
     * Gets the component [Class] type of the given array object
     *
     * @param <T>
     * the generic type
     * @param arrayObject
     * the object to resolve the class
     * @return the component [Class] of the given object
    </T> */
	@JvmStatic
    @Suppress("UNCHECKED_CAST")
	fun <T> getComponentClassType(arrayObject: Array<T>): Class<T> {
        return arrayObject.javaClass.componentType as Class<T>
    }

    /**
     * Gets the current class loader.
     *
     * @return 's the current class loader
     */
	@JvmStatic
	val classLoader: ClassLoader
        get() = getClassLoader(null)

    /**
     * Gets the ClassLoader from the given object.
     *
     * @param obj
     * The object.
     * @return the ClassLoader from the given object.
     */
    @JvmStatic
    fun getClassLoader(obj: Any?): ClassLoader {
        var classLoader: ClassLoader
        if (null != obj) {
            classLoader = if (isDerivate(Thread.currentThread().contextClassLoader,
                            obj.javaClass.classLoader)) {
                obj.javaClass.classLoader
            } else {
                Thread.currentThread().contextClassLoader
            }
            if (isDerivate(classLoader, ClassLoader.getSystemClassLoader())) {
                classLoader = ClassLoader.getSystemClassLoader()
            }
        } else {
            classLoader = if (isDerivate(Thread.currentThread().contextClassLoader,
                            ClassLoader.getSystemClassLoader())) {
                ClassLoader.getSystemClassLoader()
            } else {
                Thread.currentThread().contextClassLoader
            }
        }
        return classLoader
    }

    /**
     * Gets the classname from the given class.
     *
     * @param clazz
     * The class.
     * @return The classname.
     */
	@JvmStatic
	fun getClassname(clazz: Class<*>): String {
        return clazz.name
    }

    /**
     * Gets the classname and concats the suffix ".class" from the class.
     *
     * @param clazz
     * The class.
     * @return The classname and concats the suffix ".class".
     */
    fun getClassnameWithSuffix(clazz: Class<*>): String {
        var className = clazz.name
        className = className.substring(className.lastIndexOf('.') + 1) + ".class"
        return className
    }

    /**
     * Gets the classname and concats the suffix ".class" from the object.
     *
     * @param obj
     * The object.
     * @return The classname and concats the suffix ".class".
     */
	@JvmStatic
	fun getClassnameWithSuffix(obj: Any): String {
        return getClassnameWithSuffix(obj.javaClass)
    }

    /**
     * Gets the [ClassType] from the given class.
     *
     * @param clazz
     * The class.
     * @return the [ClassType] from the given class.
     */
	@JvmStatic
	fun getClassType(clazz: Class<*>): ClassType {
        if (clazz.isArray) {
            return ClassType.ARRAY
        }
        if (isCollection(clazz)) {
            return ClassType.COLLECTION
        }
        if (isMap(clazz)) {
            return ClassType.MAP
        }
        if (clazz.isLocalClass) {
            return ClassType.LOCAL
        }
        if (clazz.isMemberClass) {
            return ClassType.MEMBER
        }
        if (clazz.isPrimitive) {
            return ClassType.PRIMITIVE
        }
        if (clazz.isAnnotation) {
            return ClassType.ANNOTATION
        }
        if (clazz.isEnum) {
            return ClassType.ENUM
        }
        if (clazz.isInterface) {
            return ClassType.INTERFACE
        }
        if (clazz.isSynthetic) {
            return ClassType.SYNTHETIC
        }
        return if (clazz.isAnonymousClass) {
            ClassType.ANONYMOUS
        } else ClassType.DEFAULT
    }

    /**
     * Gets the current method name.
     *
     * @param elements
     * the elements
     * @return the current method name
     */
	@JvmStatic
	fun getCurrentMethodName(elements: Array<StackTraceElement>): String? {
        var currentMethodName: String? = null
        var isNext = false
        for (element in elements) {
            if (isNext) {
                currentMethodName = element.methodName
                break
            }
            isNext = element.methodName == "getStackTrace"
        }
        return currentMethodName
    }

    /**
     * Gets the directories from the given path.
     *
     * @param path
     * the path
     * @param isPackage
     * If the Flag is true than the given path is a package.
     * @return the directories from resources
     * @throws IOException
     * Signals that an I/O exception has occurred.
     */
    @Throws(IOException::class)
    fun getDirectoriesFromResources(path: String,
                                    isPackage: Boolean): List<File> {
        var pathVar = path
        if (isPackage) {
            pathVar = pathVar.replace('.', '/')
        }
        val resources = getResources(pathVar)
        val dirs: MutableList<File> = ArrayList()
        for (resource in resources) {
            dirs.add(
                    File(URLDecoder.decode(resource.file, StandardCharsets.UTF_8.name())))
        }
        return dirs
    }

    /**
     * Gets the jdk proxy interfaces.
     *
     * @param clazz
     * the class
     * @return the jdk proxy interfaces
     */
	@JvmStatic
	fun getJdkProxyInterfaces(clazz: Class<*>): Array<Class<*>> {
        return if (isJdkProxy(clazz)) {
            clazz.interfaces
        } else arrayOf(clazz)
    }

    /**
     * Returns the name of the given class or null if the given class is null.
     *
     * @param clazz
     * The class.
     *
     * @return The name of the given class.
     */
    @JvmStatic
    fun getName(clazz: Class<*>): String? {
        return getName(clazz, false)
    }

    /**
     * Returns the name of the given class or null if the given class is null. If the given flag
     * 'simple' is true the simple name (without the package) will be returned.
     *
     * @param clazz
     * The class
     * @param simple
     * The flag if the simple name should be returned.
     *
     * @return The name of the given class or if the given flag 'simple' is true the simple name
     * (without the package) will be returned.
     */
	@JvmStatic
	fun getName(clazz: Class<*>?, simple: Boolean): String? {
        var clazzVar = clazz
        var name: String? = null
        if (clazzVar != null) {
            while (clazzVar!!.isAnonymousClass) {
                clazzVar = clazzVar.superclass
            }
            name = if (simple) {
                clazzVar.simpleName
            } else {
                clazzVar.name
            }
        }
        return name
    }

    /**
     * Gets the path from the given class. For instance /java/lang/Object.class if the given class
     * is from `Object`
     *
     * @param clazz
     * The class.
     * @return the path from the given class.
     */
	@JvmStatic
	fun getPath(clazz: Class<*>): String {
        val packagePath = PackageExtensions.getPackagePath(clazz)
        val className = getSimpleName(clazz)
        return StringBuilder().append("/").append(packagePath).append(className)
                .append(".class").toString()
    }

    /**
     * Finds the absolute path from the object.
     *
     * @param obj
     * The object.
     * @return The absolute path from the object.
     */
	@JvmStatic
	fun getPathFromObject(obj: Any?): String? {
        return obj?.javaClass?.getResource(getClassnameWithSuffix(obj))?.path
    }

    /**
     * Gives the url from the path back.
     *
     * @param clazz
     * The class-object.
     * @return 's the url from the path.
     */
    @JvmStatic
    fun getResource(clazz: Class<*>): URL? {
        val path = getPath(clazz)
        var url = clazz.getResource(path)
        if (url == null) {
            url = classLoader.getResource(path)
        }
        return url
    }

    /**
     * Gives the url from the path back.
     *
     * @param clazz
     * The class-object.
     * @param path
     * The path.
     * @return 's the url from the path.
     */
    @JvmStatic
    fun getResource(clazz: Class<*>, path: String): URL? {
        var url = clazz.getResource(path)
        if (url == null) {
            url = classLoader.getResource(path)
        }
        return url
    }

    /**
     * Gives the URL from the resource. Wrapes the Class.getResource(String)-method.
     *
     * @param name
     * The name from the resource.
     * @return The resource or null if the resource does not exists.
     */
    @JvmStatic
    fun getResource(name: String): URL {
        var path = name
        if (name.startsWith("/")) {
            path = name.substring(1, name.length)
        }
        return classLoader.getResource(path)
    }

    /**
     * Gives the URL from the resource. Wrapes the Class.getResource(String)-method.
     *
     * @param <T>
     * the generic type
     * @param name
     * The name from the resource.
     * @param obj
     * The Object.
     * @return The resource or null if the resource does not exists.
    </T> */
	@JvmStatic
	fun <T : Any> getResource(name: String, obj: T): URL? {
        val clazz: Class<*> = obj.javaClass
        var url = clazz.getResource(name)
        if (url == null) {
            url = getResource(clazz, name)
        }
        return url
    }

    /**
     * Gives the resource as a file Object.
     *
     * @param name
     * The name from the file.
     * @return The file or null if the file does not exists.
     *
     * @throws URISyntaxException
     * occurs by creation of the file with an uri.
     */
    @JvmStatic
    @Throws(URISyntaxException::class)
    fun getResourceAsFile(name: String): File? {
        var file: File? = null
        var url: URL? = getResource(name)
        if (null == url) {
            url = classLoader.getResource(name)
            if (null != url) {
                file = File(url.toURI())
            }
        } else {
            if (url.protocol == "jar") {
                throw URISyntaxException(url.toString(),
                        "Resource is in a jar file. Use instead the method ClassExtensions#getResourceAsStream(String). Given resource is")
            }
            if (url.protocol == "file") {
                file = File(url.toURI())
            }
        }
        return file
    }

    /**
     * Gives the resource as a file Object.
     *
     * @param name
     * The name from the file.
     * @param obj
     * The Object.
     * @return The file or null if the file does not exists.
     * @throws URISyntaxException
     * occurs by creation of the file with an uri.
     * @throws IOException
     * Signals that an I/O exception has occurred.
     */
    @JvmStatic
	@Throws(URISyntaxException::class, IOException::class)
    fun getResourceAsFile(name: String, obj: Any): File? {
        var file: File? = null
        var url = getResource(name, obj)
        if (null == url) {
            url = getClassLoader(obj).getResource(name)
            if (null != url) {
                file = File(url.toURI())
            }
        } else {
            if (url.protocol == "jar") {
                val resourceAsStream = getResourceAsStream(name, obj)
                file = File(System.getProperty("java.io.tmpdir"),
                        obj.javaClass.simpleName)
                Files.copy(resourceAsStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
            if (url.protocol == "file") {
                file = File(url.toURI())
            }
        }
        return file
    }

    /**
     * This method call the getResourceAsStream from the ClassLoader. You can use this method to
     * read files from jar-files.
     *
     * @param clazz
     * the clazz
     * @param uri
     * The uri as String.
     * @return The InputStream from the uri.
     */
    @JvmStatic
    fun getResourceAsStream(clazz: Class<*>,
                            uri: String): InputStream? {
        var `is` = clazz.getResourceAsStream(uri)
        if (null == `is`) {
            `is` = classLoader.getResourceAsStream(uri)
        }
        return `is`
    }

    /**
     * Gives the Inputstream from the resource. Wrapes the Class.getResourceAsStream(String)-method.
     *
     * @param name
     * The name from the resource.
     * @return The resource or null if the resource does not exists.
     */
    @JvmStatic
    fun getResourceAsStream(name: String): InputStream {
        return classLoader.getResourceAsStream(name)
    }

    /**
     * Gives the Inputstream from the resource. Wrapes the Class.getResourceAsStream(String)-method.
     *
     * @param name
     * The name from the resource.
     * @param obj
     * The Object.
     * @return The resource or null if the resource does not exists.
     */
	@JvmStatic
	fun getResourceAsStream(name: String,
							obj: Any): InputStream? {
        var inputStream = obj.javaClass.getResourceAsStream(name)
        if (null == inputStream) {
            val loader = getClassLoader(obj)
            inputStream = loader.getResourceAsStream(name)
        }
        return inputStream
    }

    /**
     * Gets a list with urls from the given path for all resources.
     *
     * @param path
     * The base path.
     * @return The resources.
     * @throws IOException
     * Signals that an I/O exception has occurred.
     */
    @Throws(IOException::class)
    fun getResources(path: String): List<URL> {
        return Collections.list(classLoader.getResources(path))
    }

    /**
     * Returns the simple name of the given class or null if the given class is null.
     *
     * @param clazz
     * The class.
     *
     * @return The simple name of the given class.
     */
    fun getSimpleName(clazz: Class<*>): String? {
        return getName(clazz, true)
    }

    /**
     * Gets the unwrapped proxy class.
     *
     * @param clazz
     * the class
     * @return the unwrapped proxy class or null if the given [Class] is null.
     */
	@JvmStatic
	fun getUnwrappedProxy(clazz: Class<*>?): Class<*>? {
        val found = unwrapProxy(clazz)
        return if (found != null && 0 < found.size) {
            found[0]
        } else null
    }

    /**
     * Returns the URL from the given class.
     *
     * @param clazz
     * The class.
     * @return the URL from the given class.
     */
	@JvmStatic
	fun getURL(clazz: Class<*>): URL {
        return getResource(getPath(clazz))
    }

    /**
     * Checks if the given [Class] is cglib proxy class.
     *
     * @param <T>
     * the generic type
     * @param clazz
     * the class to check
     * @return true, if the given [Class] is cglib proxy class otherwise false.
    </T> */
	@JvmStatic
	fun <T> isCglib(clazz: Class<T>?): Boolean {
        return clazz != null && clazz.name.contains(CGLIB_TAG)
    }

    /**
     * Checks if the given class is assignable from [Collection].
     *
     * @param clazz
     * The class.
     * @return true, if the given class is assignable from [Collection] otherwise false.
     */
    fun isCollection(clazz: Class<*>): Boolean {
        return MutableCollection::class.java.isAssignableFrom(clazz)
    }

    /**
     * Compares the two given ClassLoader objects and returns true if compare is a derivate of
     * source.
     *
     * @param source
     * the source
     * @param compare
     * the compare
     * @return true, if compare is a derivate of source.
     */
	@JvmStatic
	fun isDerivate(source: ClassLoader?, compare: ClassLoader?): Boolean {
        var compareVar = compare
        if (source === compareVar) {
            return true
        }
        if (compareVar == null) {
            return false
        }
        if (source == null) {
            return true
        }
        while (null != compareVar) {
            compareVar = compareVar.parent
            if (source === compareVar) {
                return true
            }
        }
        return false
    }

    /**
     * Checks if the given [Class] is a JDK proxy class.
     *
     * @param <T>
     * the generic type
     * @param clazz
     * the class to check
     * @return true, if the given [Class] is a JDK proxy class otherwise false.
    </T> */
	@JvmStatic
	fun <T> isJdkProxy(clazz: Class<T>?): Boolean {
        return clazz != null && Proxy.isProxyClass(clazz)
    }

    /**
     * Checks if the given class is assignable from [Map].
     *
     * @param clazz
     * The class.
     * @return true, if the given class is assignable from [Map] otherwise false.
     */
    fun isMap(clazz: Class<*>): Boolean {
        return MutableMap::class.java.isAssignableFrom(clazz)
    }

    /**
     * Checks if the given class is an array of primitive type
     *
     * @param clazz
     * The class
     * @return true, if the given class is an array of primitive type otherwise false
     */
	@JvmStatic
	fun isPrimitiveArray(clazz: Class<*>): Boolean {
        return clazz.isArray && clazz.componentType.isPrimitive
    }

    /**
     * Checks if the given [Class] is a proxy class.
     *
     * @param <T>
     * the generic type
     * @param clazz
     * the class to check
     * @return true, if the given [Class] is a proxy class otherwise false.
    </T> */
	@JvmStatic
	fun <T> isProxy(clazz: Class<T>?): Boolean {
        return isJdkProxy(clazz) || isCglib(clazz)
    }

    /**
     * Unwrap the given [Class] if it is wrapped from cglib or jdk proxies.
     *
     * @param clazz
     * the class
     * @return the unwrapped classes as an array
     */
    fun unwrapProxy(clazz: Class<*>?): Array<Class<*>>? {
        if (clazz == null) {
            return null
        }
        var found: Class<*> = clazz
        if (isCglib(found)) {
            found = getCglibProxy(found)
        }
        return if (isJdkProxy(found)) {
            getJdkProxyInterfaces(found)
        } else arrayOf(found)
    }
}