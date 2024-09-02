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
package io.github.astrapi69.check;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * The class {@link Check} can validate arguments in a chainable manner like a builder.
 */
public final class Check implements Serializable
{

	/** The Constant instance. */
	private static final Check instance = new Check();

	/** The serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Private constructor for prevent instantiation
	 */
	private Check()
	{
	}

	/**
	 * Gets the single Check instance.
	 *
	 * @return the single Check instance.
	 */
	public static synchronized Check get()
	{
		return instance;
	}

	/**
	 * Checks if the given argument is in the given a range.
	 *
	 * @param min
	 *            The minimum from the range to check.
	 * @param max
	 *            The maximum from the range to check.
	 * @param value
	 *            The value to check if it is in the given range.
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         {@code argument} is in the given range.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is not in the given range.
	 */
	public Check isInRange(final Double min, final Double max, final Double value,
		final String name)
	{
		Argument.notNull(min, "min");
		Argument.notNull(max, "max");
		Argument.notNull(value, name);
		if (!((min < value) && (value < max)))
		{
			throw new IllegalArgumentException(String.format(
				"Given argument '%s' should have a value between %s - %s, but given argument is currently:%s",
				name, min, max, value));
		}
		return this;
	}

	/**
	 * Checks if the given argument is in the given a range.
	 *
	 * @param min
	 *            The minimum from the range to check.
	 * @param max
	 *            The maximum from the range to check.
	 * @param value
	 *            The value to check if it is in the given range.
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         {@code argument} is in the given range.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is not in the given range.
	 */
	public Check isInRange(final Float min, final Float max, final Float value, final String name)
	{
		Argument.notNull(min, "min");
		Argument.notNull(max, "max");
		Argument.notNull(value, name);
		if (!((min < value) && (value < max)))
		{
			throw new IllegalArgumentException(String.format(
				"Given argument '%s' should have a value between %s - %s, but given argument is currently:%s",
				name, min, max, value));
		}
		return this;
	}

	/**
	 * Checks if the given argument is in the given a range.
	 *
	 * @param min
	 *            The minimum from the range to check.
	 * @param max
	 *            The maximum from the range to check.
	 * @param value
	 *            The value to check if it is in the given range.
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         {@code argument} is in the given range.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is not in the given range.
	 */
	public Check isInRange(final Integer min, final Integer max, final Integer value,
		final String name)
	{
		Argument.notNull(min, "min");
		Argument.notNull(max, "max");
		Argument.notNull(value, name);
		if (!((min < value) && (value < max)))
		{
			throw new IllegalArgumentException(String.format(
				"Given argument '%s' should have a value between %s - %s, but given argument is currently:%s",
				name, min, max, value));
		}
		return this;
	}

	/**
	 * Checks if the given argument is in the given a range.
	 *
	 * @param min
	 *            The minimum from the range to check.
	 * @param max
	 *            The maximum from the range to check.
	 * @param value
	 *            The value to check if it is in the given range.
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         {@code argument} is in the given range.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is not in the given range.
	 */
	public Check isInRange(final Long min, final Long max, final Long value, final String name)
	{
		Argument.notNull(min, "min");
		Argument.notNull(max, "max");
		Argument.notNull(value, name);
		if (!((min < value) && (value < max)))
		{
			throw new IllegalArgumentException(String.format(
				"Given argument '%s' should have a value between %s - %s, but given argument is currently:%s",
				name, min, max, value));
		}
		return this;
	}

	/**
	 * Checks if the given collection is not null or empty.
	 *
	 * @param <T>
	 *            the generic type of the elements in the given collection
	 * @param <C>
	 *            the generic type of the given collection
	 * @param collection
	 *            the collection to check
	 * @param name
	 *            the name of the given collection
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         collection is not null or empty.
	 * @throws IllegalArgumentException
	 *             when the given {@code collection} is null or empty.
	 */
	public <T, C extends Collection<T>> Check notEmpty(final C collection, final String name)
	{
		Argument.notEmpty(collection, name);
		return this;
	}

	/**
	 * Checks if the given map is not null or empty.
	 *
	 * @param <K>
	 *            the generic type of the key from the given Map
	 * @param <V>
	 *            the generic type of the value from the given Map
	 * @param <M>
	 *            the generic type of the given Map
	 * @param map
	 *            the map to check
	 * @param name
	 *            the name of the given collection
	 * @return if the check is successful a reference to this object. This case is if the given map
	 *         is not null or empty.
	 * @throws IllegalArgumentException
	 *             when the given {@code map} is null or empty.
	 */
	public <K, V, M extends Map<K, V>> Check notEmpty(final M map, final String name)
	{
		Argument.notEmpty(map, name);
		return this;
	}

	/**
	 * Checks if the given argument is not empty.
	 *
	 * @param <T>
	 *            the generic type of the given argument
	 * @param argument
	 *            the argument
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         argument is not null or empty.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is null or empty.
	 */
	public <T extends CharSequence> Check notEmpty(final T argument, final String name)
	{
		Argument.notEmpty(argument, name);
		return this;
	}

	/**
	 * Checks if the given byte array argument is not empty
	 *
	 * @param argument
	 *            the argument
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         argument is not null or empty.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is null or empty.
	 */
	public Check notEmpty(final byte[] argument, final String name)
	{
		Argument.notEmpty(argument, name);
		return this;
	}

	/**
	 * Checks if the given int array argument is not empty
	 *
	 * @param argument
	 *            the argument
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         argument is not null or empty.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is null or empty.
	 */
	public Check notEmpty(final int[] argument, final String name)
	{
		Argument.notEmpty(argument, name);
		return this;
	}

	/**
	 * Checks if the given char array argument is not empty
	 *
	 * @param argument
	 *            the argument
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         argument is not null or empty.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is null or empty.
	 */
	public Check notEmpty(final char[] argument, final String name)
	{
		Argument.notEmpty(argument, name);
		return this;
	}

	/**
	 * Checks if the given boolean array argument is not empty
	 *
	 * @param argument
	 *            the argument
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         argument is not null or empty.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is null or empty.
	 */
	public Check notEmpty(final boolean[] argument, final String name)
	{
		Argument.notEmpty(argument, name);
		return this;
	}

	/**
	 * Checks if the given double array argument is not empty
	 *
	 * @param argument
	 *            the argument
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         argument is not null or empty.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is null or empty.
	 */
	public Check notEmpty(final double[] argument, final String name)
	{
		Argument.notEmpty(argument, name);
		return this;
	}

	/**
	 * Checks if the given float array argument is not empty
	 *
	 * @param argument
	 *            the argument
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         argument is not null or empty.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is null or empty.
	 */
	public Check notEmpty(final float[] argument, final String name)
	{
		Argument.notEmpty(argument, name);
		return this;
	}

	/**
	 * Checks if the given long array argument is not empty
	 *
	 * @param argument
	 *            the argument
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         argument is not null or empty.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is null or empty.
	 */
	public Check notEmpty(final long[] argument, final String name)
	{
		Argument.notEmpty(argument, name);
		return this;
	}

	/**
	 * Checks if the given short array argument is not empty
	 *
	 * @param argument
	 *            the argument
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         argument is not null or empty.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is null or empty.
	 */
	public Check notEmpty(final short[] argument, final String name)
	{
		Argument.notEmpty(argument, name);
		return this;
	}

	/**
	 * Checks if the given argument is not null.
	 *
	 * @param <T>
	 *            the generic type of the given argument
	 * @param argument
	 *            the argument
	 * @param name
	 *            the name of the given argument
	 * @return if the check is successful a reference to this object. This case is if the given
	 *         argument is not null.
	 * @throws IllegalArgumentException
	 *             when the given {@code argument} is null.
	 */
	public <T> Check notNull(final T argument, final String name)
	{
		Argument.notNull(argument, name);
		return this;
	}

}
