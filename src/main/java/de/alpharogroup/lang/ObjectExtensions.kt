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

import de.alpharogroup.lang.DefaultValue.get

/**
 * The class [ObjectExtensions] provides extension methods to check if the object is the
 * default value.
 */
object ObjectExtensions {
    /**
     * Checks if the given object has the default value.
     *
     * @param <T>
     * the generic type
     * @param fieldClass
     * the field class
     * @param instance
     * the object instance
     * @return true, if is default value
    </T> */
	@JvmStatic
	fun <T> isDefaultValue(fieldClass: Class<*>?, instance: T?): Boolean {
        if (instance == null) {
            return true
        }
        val defaultValue = get(fieldClass!!)
        return if (defaultValue != null) {
            get(fieldClass) == instance
        } else false
    }

    /**
     * Checks if the given object has not the default value.
     *
     * @param <T>
     * the generic type
     * @param fieldClass
     * the field class
     * @param instance
     * the object instance
     * @return true, if the given object has not the default value
    </T> */
	@JvmStatic
	fun <T> isNotDefaultValue(fieldClass: Class<*>?, instance: T): Boolean {
        return !isDefaultValue(fieldClass, instance)
    }

    /**
     * Gets the [ClassType] from the given class.
     *
     * @param <T>
     * the generic type
     * @param clazz
     * The class.
     * @return the [ClassType] from the given class.
    </T> */
	@JvmStatic
	fun <T> getClassType(clazz: Class<T>?): ClassType? {
        return if (clazz == null) {
            null
        } else ClassExtensions.getClassType(clazz)
    }
}