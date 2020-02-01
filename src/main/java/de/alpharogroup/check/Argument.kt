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

/**
 * The class [Argument] that can assert conditions on arguments.
 */
object Argument {
    /**
     * Checks if the given `value` argument is in the given a range.
     *
     * @param <T>
     * the generic type of the arguments
     * @param min
     * The minimum from the range to check.
     * @param max
     * The maximum from the range to check.
     * @param value
     * The value to check if it is in the given range.
     * @param name
     * the name of the given argument
     * @return if the check is successful the `value` object. This case is if the given
     * `value` is in the given range.
     * @throws IllegalArgumentException
     * when the given `value` is not in the given range.
    </T> */
	@JvmStatic
	fun <T : Comparable<T>?> isInRange(min: T, max: T,
									   value: T, name: String?): T {
        notNull(min, "min")
        notNull(max, "max")
        require(!(value!!.compareTo(min) < 0 || value.compareTo(max) > 0)) {
            String.format(
                    "Given argument '%s' should have a value between %s - %s, but given argument is currently:%s",
                    name, min, max, value)
        }
        return value
    }

    /**
     * Checks if the given `collection` is not null or empty.
     *
     * @param <T>
     * the generic type of the elements in the given collection
     * @param <C>
     * the generic type of the given collection
     * @param collection
     * the collection to check
     * @param name
     * the name of the given collection
     * @return if the check is successful the `collection` object. This case is if the given
     * collection is not null or empty.
     * @throws IllegalArgumentException
     * when the given `collection` is null or empty.
    </C></T> */
    @JvmStatic
    fun <T, C : Collection<T>?> notEmpty(collection: C, name: String): C {
        notNull(collection, name)
        require(!collection!!.isEmpty()) { "Given collection '$name' may not be empty." }
        return collection
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
     * @return if the check is successful the `map` object. This case is if the given
     * `map` is not null or empty.
     * @throws IllegalArgumentException
     * when the given `map` is null or empty.
    </M></V></K> */
    @JvmStatic
    fun <K, V, M : Map<K, V>?> notEmpty(map: M, name: String): M {
        notNull(map, name)
        require(!map!!.isEmpty()) { "Given map '$name' may not be empty." }
        return map
    }

    /**
     * Checks if the given `argument` is not empty.
     *
     * @param <T>
     * the generic type of the given argument
     * @param argument
     * the argument
     * @param name
     * the name of the given argument
     * @return if the check is successful the `argument` object. This case is if the given
     * `argument` is not null or empty.
     * @throws IllegalArgumentException
     * when the given `argument` is null or empty.
    </T> */
	@JvmStatic
	fun <T : CharSequence?> notEmpty(argument: T, name: String): T {
        notNull(argument, name)
        require(!(argument!!.length == 0 || argument.toString().trim { it <= ' ' }.length == 0)) { "Given argument '$name' may not be empty." }
        return argument
    }

    /**
     * Checks if the given `argument` is not null.
     *
     * @param <T>
     * the generic type of the given argument
     * @param argument
     * the argument
     * @param name
     * the name of the given argument
     * @return if the check is successful the `argument` object. This case is if the given
     * `argument` is not null.
     * @throws IllegalArgumentException
     * when the given `argument` is null.
    </T> */
	@JvmStatic
	fun <T> notNull(argument: T?, name: String): T {
        if(argument == null)
            throw IllegalArgumentException("Given argument '$name' may not be null.")
        return argument
    }
}