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
package de.alpharogroup.check

import java.io.Serializable

/**
 * The class [Check] can validate arguments in a chainable manner like a builder.
 */
class Check
/**
 * Instantiates a new check.
 */
private constructor() : Serializable {
    /**
     * Checks if the given argument is in the given a range.
     *
     * @param min
     * The minimum from the range to check.
     * @param max
     * The maximum from the range to check.
     * @param value
     * The value to check if it is in the given range.
     * @param name
     * the name of the given argument
     * @return if the check is successful a reference to this object. This case is if the given
     * `argument` is in the given range.
     * @throws IllegalArgumentException
     * when the given `argument` is not in the given range.
     */
    fun isInRange(min: Double, max: Double, value: Double,
                  name: String): Check {
        nullCheck(min, "min")
        nullCheck(max, "max")
        nullCheck(value, name)
        require(min < value && value < max) {
            String.format(
                    "Given argument '%s' should have a value between %s - %s, but given argument is currently:%s",
                    name, min, max, value)
        }
        return this
    }

    /**
     * Checks if the given argument is in the given a range.
     *
     * @param min
     * The minimum from the range to check.
     * @param max
     * The maximum from the range to check.
     * @param value
     * The value to check if it is in the given range.
     * @param name
     * the name of the given argument
     * @return if the check is successful a reference to this object. This case is if the given
     * `argument` is in the given range.
     * @throws IllegalArgumentException
     * when the given `argument` is not in the given range.
     */
    fun isInRange(min: Float, max: Float, value: Float, name: String): Check {
        nullCheck(min, "min")
        nullCheck(max, "max")
        nullCheck(value, name)
        require(min < value && value < max) {
            String.format(
                    "Given argument '%s' should have a value between %s - %s, but given argument is currently:%s",
                    name, min, max, value)
        }
        return this
    }

    /**
     * Checks if the given argument is in the given a range.
     *
     * @param min
     * The minimum from the range to check.
     * @param max
     * The maximum from the range to check.
     * @param value
     * The value to check if it is in the given range.
     * @param name
     * the name of the given argument
     * @return if the check is successful a reference to this object. This case is if the given
     * `argument` is in the given range.
     * @throws IllegalArgumentException
     * when the given `argument` is not in the given range.
     */
    fun isInRange(min: Int, max: Int, value: Int,
                  name: String): Check {
        nullCheck(min, "min")
        nullCheck(max, "max")
        nullCheck(value, name)
        require(min < value && value < max) {
            String.format(
                    "Given argument '%s' should have a value between %s - %s, but given argument is currently:%s",
                    name, min, max, value)
        }
        return this
    }

    /**
     * Checks if the given argument is in the given a range.
     *
     * @param min
     * The minimum from the range to check.
     * @param max
     * The maximum from the range to check.
     * @param value
     * The value to check if it is in the given range.
     * @param name
     * the name of the given argument
     * @return if the check is successful a reference to this object. This case is if the given
     * `argument` is in the given range.
     * @throws IllegalArgumentException
     * when the given `argument` is not in the given range.
     */
    fun isInRange(min: Long, max: Long, value: Long, name: String): Check {
        nullCheck(min, "min")
        nullCheck(max, "max")
        nullCheck(value, name)
        require(min < value && value < max) {
            String.format(
                    "Given argument '%s' should have a value between %s - %s, but given argument is currently:%s",
                    name, min, max, value)
        }
        return this
    }

    /**
     * Checks if the given collection is not null or empty.
     *
     * @param <T>
     * the generic type of the elements in the given collection
     * @param <C>
     * the generic type of the given collection
     * @param collection
     * the collection to check
     * @param name
     * the name of the given collection
     * @return if the check is successful a reference to this object. This case is if the given
     * collection is not null or empty.
     * @throws IllegalArgumentException
     * when the given `collection` is null or empty.
    </C></T> */
    fun <T, C : Collection<T>?> notEmpty(collection: C, name: String): Check {
        nullCheck(collection, name)
        require(!collection!!.isEmpty()) { "Given collection '$name' may not be empty." }
        return this
    }

    /**
     * Checks if the given map is not null or empty.
     *
     * @param <K>
     * the generic type of the key from the given Map
     * @param <V>
     * the generic type of the value from the given Map
     * @param <M>
     * the generic type of the given Map
     * @param map
     * the map to check
     * @param name
     * the name of the given collection
     * @return if the check is successful a reference to this object. This case is if the given map
     * is not null or empty.
     * @throws IllegalArgumentException
     * when the given `map` is null or empty.
    </M></V></K> */
    fun <K, V, M : Map<K, V>?> notEmpty(map: M, name: String): Check {
        nullCheck(map, name)
        require(!map!!.isEmpty()) { "Given map '$name' may not be empty." }
        return this
    }

    /**
     * Checks if the given argument is not empty.
     *
     * @param <T>
     * the generic type of the given argument
     * @param argument
     * the argument
     * @param name
     * the name of the given argument
     * @return if the check is successful a reference to this object. This case is if the given
     * argument is not null or empty.
     * @throws IllegalArgumentException
     * when the given `argument` is null or empty.
    </T> */
    fun <T : CharSequence?> notEmpty(argument: T, name: String): Check {
        nullCheck(argument, name)
        require(!(argument!!.length == 0 || argument.toString().trim { it <= ' ' }.length == 0)) { "Given argument '$name' may not be empty." }
        return this
    }

    /**
     * Checks if the given argument is not null.
     *
     * @param <T>
     * the generic type of the given argument
     * @param argument
     * the argument
     * @param name
     * the name of the given argument
     * @return if the check is successful a reference to this object. This case is if the given
     * argument is not null.
     * @throws IllegalArgumentException
     * when the given `argument` is null.
    </T> */
    fun <T> notNull(argument: T, name: String): Check {
        nullCheck(argument, name)
        return this
    }

    /**
     * Checks if the given argument is null.
     *
     * @param <T>
     * the generic type of the given argument
     * @param argument
     * the argument to check.
     * @param name
     * the name of the given argument
     * @throws IllegalArgumentException
     * when the given `argument` is null.
    </T> */
    private fun <T> nullCheck(argument: T?, name: String) {
        Argument.notNull(argument, name)
    }

    companion object {
        /** The Constant instance.  */
        private val instance = Check()
        /** The serialVersionUID.  */
        private const val serialVersionUID = 1L

        /**
         * Gets the single Check instance.
         *
         * @return the single Check instance.
         */
        @JvmStatic
		@Synchronized
        fun get(): Check {
            return instance
        }
    }
}