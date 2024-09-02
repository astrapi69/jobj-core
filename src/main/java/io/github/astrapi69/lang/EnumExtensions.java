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

import java.util.Arrays;
import java.util.function.Function;


/**
 * The class {@link EnumExtensions} provides generic utility methods for java enums
 */
public final class EnumExtensions
{

	/**
	 * Private constructor for prevent instantiation
	 */
	private EnumExtensions()
	{
	}

	/**
	 * This method gets the enum value from the given enum {@link Class} object and the given
	 * {@link String} object that represents an enum value as string
	 *
	 * @param <E>
	 *            the generic type of the enum
	 * @param enumerationClass
	 *            the class of the enumeration
	 * @param name
	 *            the name of the enum value as {@link String} object
	 * @param defaultReturnEnumValue
	 *            the default enum value to return if no enum value has been found
	 * @return the corresponding enum value, or the given default enum value
	 */
	public static <E extends Enum<E>> E enumValueFromString(final Class<E> enumerationClass,
		final String name, final E defaultReturnEnumValue)
	{
		if (enumerationClass != null && name != null)
		{
			return Arrays.stream(enumerationClass.getEnumConstants())
				.filter(enumValue -> enumValue.name().equalsIgnoreCase(name)).findFirst()
				.orElse(defaultReturnEnumValue);
		}
		return defaultReturnEnumValue;
	}

	/**
	 * This method gets the enum value from the given enum {@link Class} object and the given
	 * {@link String} object that represents an enum field as string
	 *
	 * @param <E>
	 *            the generic type of the enum
	 * @param enumerationClass
	 *            the class of the enumeration
	 * @param enumField
	 *            the enum field of the enum value as {@link String} object.<br/>
	 *            Here is not necessarily be the name, it can be an instance field of the enum
	 * @param function
	 *            the function that returns the value of the enum field
	 * @param defaultReturnEnumValue
	 *            the default enum value to return if no enum value has been found
	 * @return the corresponding enum value, or the given default enum value
	 */
	public static <E extends Enum<E>> E enumFieldValueFromString(final Class<E> enumerationClass,
		final String enumField, final Function<E, String> function, final E defaultReturnEnumValue)
	{
		if (enumerationClass != null && enumField != null)
		{
			return Arrays.stream(enumerationClass.getEnumConstants())
				.filter(enumValue -> function.apply(enumValue).equalsIgnoreCase(enumField))
				.findFirst().orElse(defaultReturnEnumValue);
		}
		return defaultReturnEnumValue;
	}

	/**
	 * This method gets the enum value from the given enum {@link Class} object and the given
	 * generic type object that represents an enum field
	 *
	 * @param <E>
	 *            the generic type of the enum
	 * @param <R>
	 *            the generic type of the enum from the enum field
	 * @param enumerationClass
	 *            the class of the enumeration
	 * @param enumField
	 *            the enum field of the enum value.<br/>
	 *            This field is not the name of the enum
	 * @param function
	 *            the function that returns the value of the enum field
	 * @param defaultReturnEnumValue
	 *            the default enum value to return if no enum value has been found
	 * @return the corresponding enum value, or the given default enum value
	 */
	public static <E extends Enum<E>, R> E enumFieldValue(final Class<E> enumerationClass,
		final R enumField, final Function<E, R> function, final E defaultReturnEnumValue)
	{
		if (enumerationClass != null && enumField != null)
		{
			return Arrays.stream(enumerationClass.getEnumConstants())
				.filter(enumValue -> function.apply(enumValue).equals(enumField)).findFirst()
				.orElse(defaultReturnEnumValue);
		}
		return defaultReturnEnumValue;
	}

}
