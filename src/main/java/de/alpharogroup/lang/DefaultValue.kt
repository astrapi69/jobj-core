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

import java.util.*

/**
 * The class DefaultValue provide the default values of the primitive types, as defined by the JLS.
 */
object DefaultValue {
    /** The constant map with the default values.  */
    private val DEFAULT_VALUE = Collections
            .unmodifiableMap<Class<*>, Any>(object : HashMap<Class<*>?, Any?>() {
                init {
                    put(Boolean::class.javaPrimitiveType, false)
                    put(Char::class.javaPrimitiveType, '\u0000')
                    put(Byte::class.javaPrimitiveType, 0.toByte())
                    put(Short::class.javaPrimitiveType, 0.toShort())
                    put(Int::class.javaPrimitiveType, 0)
                    put(Long::class.javaPrimitiveType, 0L)
                    put(Float::class.javaPrimitiveType, 0f)
                    put(Double::class.javaPrimitiveType, 0.0)
                    put(Any::class.java, null)
                    put(null, null)
                    put(Void.TYPE, null)
                }
            })

    /**
     * Gets the default value from the given [Class].
     *
     * @param classType
     * the class type
     * @return the default value
     */
    @JvmStatic
	operator fun get(classType: Class<*>): Any? {
        return DEFAULT_VALUE[classType]
    }
}