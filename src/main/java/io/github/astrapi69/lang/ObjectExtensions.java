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

import java.util.Map;


/**
 * The class {@link ObjectExtensions} provides extension methods to check if the object is the
 * default value.
 */
public final class ObjectExtensions
{

	/**
	 * The default value of each primitive type, its zero; every other type has null as its default
	 * value. Before 10.0 this was the public class DefaultValue, which no caller in the family used
	 * (#16)
	 */
	private static final Map<Class<?>, Object> DEFAULT_VALUES = Map.of(boolean.class, false,
		char.class, '\0', byte.class, (byte)0, short.class, (short)0, int.class, 0, long.class, 0L,
		float.class, 0f, double.class, 0d);

	/**
	 * Private constructor for prevent instantiation
	 */
	private ObjectExtensions()
	{
	}

	/**
	 * Checks if the given object has the default value.
	 *
	 * @param <T>
	 *            the generic type
	 * @param fieldClass
	 *            the field class
	 * @param object
	 *            the object
	 * @return true, if is default value
	 */
	public static final <T> boolean isDefaultValue(final Class<?> fieldClass, final T object)
	{
		if (object == null)
		{
			return true;
		}
		// Map.get throws a NullPointerException for a null class, as DefaultValue.get did
		final Object defaultValue = DEFAULT_VALUES.get(fieldClass);
		return defaultValue != null && defaultValue.equals(object);
	}

	/**
	 * Checks if the given object has not the default value.
	 *
	 * @param <T>
	 *            the generic type
	 * @param fieldClass
	 *            the field class
	 * @param object
	 *            the object
	 * @return true, if the given object has not the default value
	 */
	public static final <T> boolean isNotDefaultValue(final Class<?> fieldClass, final T object)
	{
		return !isDefaultValue(fieldClass, object);
	}

	/**
	 * Gets the {@link ClassType} from the given class.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            The class.
	 * @return the {@link ClassType} from the given class.
	 */
	public static <T> ClassType getClassType(final Class<T> clazz)
	{
		if (clazz == null)
		{
			return null;
		}
		return ClassExtensions.getClassType(clazz);
	}

}
