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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import io.github.astrapi69.lang.model.MethodTypes;

/**
 * The class {@link MethodTypeArgumentsExtensions} is a utility class for getting the generic type
 * arguments from a given method. <br>
 * <br>
 */
public final class MethodTypeArgumentsExtensions
{

	private MethodTypeArgumentsExtensions()
	{
	}

	/**
	 * Gets the {@link MethodTypes} instance for the given method
	 *
	 * @param method
	 *            the method to extract the generic parameter and return types from
	 * @return a new {@link MethodTypes} instance
	 */
	public static MethodTypes getMethodTypes(Method method)
	{
		return MethodTypes.builder().genericParameterTypes(method.getGenericParameterTypes())
			.genericReturnType(method.getGenericReturnType()).build();
	}

	/**
	 * Gets the first parameter type of the given method if it is parameterized
	 *
	 * @param method
	 *            the method to inspect
	 * @return the first parameter class type if it is parameterized, otherwise {@code null}
	 */
	public static Class<?> getParameterType(Method method)
	{
		getParameterTypes(method);
		MethodTypes methodTypes = getMethodTypes(method);
		if (methodTypes.getGenericParameterTypes() != null
			&& methodTypes.getGenericParameterTypes().length > 0)
		{
			Type type = methodTypes.getGenericParameterTypes()[0];
			if (type instanceof ParameterizedType)
			{
				ParameterizedType pType = (ParameterizedType)type;
				final Type[] actualArgs = pType.getActualTypeArguments();
				if (actualArgs.length == 0)
					return null;
				Type t = actualArgs[0];
				if (t instanceof Class)
					return (Class<?>)t;
				if (t instanceof TypeVariable)
				{
					TypeVariable<?> tv = (TypeVariable<?>)actualArgs[0];
					return (Class<?>)tv.getAnnotatedBounds()[0].getType();
				}
			}
		}
		return null;
	}

	/**
	 * Gets the parameter types of the given method if they are parameterized
	 *
	 * @param method
	 *            the method to inspect
	 * @return an array of {@link Type} representing the parameter types, or {@code null} if not
	 *         parameterized
	 */
	public static Type[] getParameterTypes(Method method)
	{
		MethodTypes methodTypes = getMethodTypes(method);
		if (methodTypes.getGenericParameterTypes() != null
			&& methodTypes.getGenericParameterTypes().length > 0)
		{
			Type type = methodTypes.getGenericParameterTypes()[0];
			if (type instanceof ParameterizedType)
			{
				ParameterizedType pType = (ParameterizedType)type;
				return pType.getActualTypeArguments();
			}
		}
		return null;
	}

	/**
	 * Gets the return type of the given method
	 *
	 * @param method
	 *            the method to inspect
	 * @return the class of the return type
	 */
	public static Class<?> getReturnType(Method method)
	{
		Type returnType = method.getGenericReturnType();
		if (returnType instanceof Class)
		{
			return (Class<?>)returnType;
		}
		else if (returnType instanceof ParameterizedType)
		{
			ParameterizedType pType = (ParameterizedType)returnType;
			Type rawType = pType.getRawType();
			if (rawType instanceof Class)
			{
				return (Class<?>)rawType;
			}
		}
		return null;
	}

}
