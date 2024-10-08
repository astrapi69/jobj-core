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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;

/**
 * The class DefaultValue provide the default values of the primitive types, as defined by the JLS.
 */
public final class DefaultValue
{

	/**
	 * Private constructor for prevent instantiation
	 */
	private DefaultValue()
	{
	}

	/** The constant map with the default values. */
	private static final Map<Class<?>, Object> DEFAULT_VALUE = Collections
		.unmodifiableMap(new HashMap<Class<?>, Object>()
		{
			{
				put(boolean.class, false);
				put(char.class, '\0');
				put(byte.class, (byte)0);
				put(short.class, (short)0);
				put(int.class, 0);
				put(long.class, 0L);
				put(float.class, 0f);
				put(double.class, 0d);
				put(Object.class, null);
				put(null, null);
				put(void.class, null);
			}
		});

	/**
	 * Gets the default value from the given {@link Class}.
	 *
	 * @param classType
	 *            the class type
	 * @return the default value
	 */
	public static Object get(final @NonNull Class<?> classType)
	{
		final Object defaultValue = DEFAULT_VALUE.get(classType);
		return defaultValue;
	}
}
