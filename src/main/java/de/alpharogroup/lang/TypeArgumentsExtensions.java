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
package de.alpharogroup.lang;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * The class {@link TypeArgumentsExtensions} is a utility class for getting the type arguments from
 * a child class that extended a generic base class. <br>
 * <br>
 * It is inspired from the article Reflecting generics by Ian Robertson at
 * <a href="http://www.artima.com/weblogs/viewpost.jsp?thread=208860"
 * >http://www.artima.com/weblogs/viewpost.jsp?thread=208860</a>. <br>
 * <br>
 * In the comments someone asked if we are allowed to use the source code from the article. The
 * answer of Ian Robertson is: Absolutely, you may use this code. "Consider it open sourced".
 */
@UtilityClass
public final class TypeArgumentsExtensions
{

	/**
	 * Get the generic return type
	 *
	 * @param classThatContainsMethod
	 *            the class that contains the method
	 * @param methodName
	 * 				the method name
	 * @param parameterTypes the list of parameters
	 * @throws NoSuchMethodException is thrown when a particular method cannot be found
	 * @return the generic return type
	 */
	public static Type getGenericReturnType(Class<?> classThatContainsMethod,
		String methodName, Class<?>... parameterTypes) throws NoSuchMethodException
	{
		Objects.requireNonNull(classThatContainsMethod);
		Objects.requireNonNull(methodName);
		return classThatContainsMethod.getMethod(methodName, parameterTypes).getGenericReturnType();
	}

	/**
	 * Get the underlying class for the generic return type
	 *
	 * @param classThatContainsMethod
	 *            the class that contains the method
	 * @param methodName
	 * 				the method name
	 * @param parameterTypes the list of parameters
	 * @throws NoSuchMethodException is thrown when a particular method cannot be found
	 * @return the underlying class of the return type
	 */
	public static Class<?> getGenericReturnClassType(Class<?> classThatContainsMethod,
		String methodName, Class<?>... parameterTypes) throws NoSuchMethodException
	{
		Objects.requireNonNull(classThatContainsMethod);
		Objects.requireNonNull(methodName);
		return TypeArgumentsExtensions.getClass(getGenericReturnType(classThatContainsMethod,methodName, parameterTypes));
	}

	/**
	 * Get the underlying class for a type, or null if the type is a variable type.
	 *
	 * @param type
	 *            the type
	 * @return the underlying class
	 */
	public static Class<?> getClass(final @NonNull Type type)
	{
		if (type instanceof Class)
		{
			return (Class<?>)type;
		}
		else if (type instanceof ParameterizedType)
		{
			return getClass(((ParameterizedType)type).getRawType());
		}
		else if (type instanceof GenericArrayType)
		{
			final Type componentType = ((GenericArrayType)type).getGenericComponentType();
			final Class<?> componentClass = getClass(componentType);
			if (componentClass != null)
			{
				return Array.newInstance(componentClass, 0).getClass();
			}
			else
			{
				return null;
			}
		}
		else if (type instanceof TypeVariable)
		{
			TypeVariable typeVariable = (TypeVariable)type;
			Type[] bounds = typeVariable.getBounds();
			if (bounds.length == 1)
			{
				Type firstBound = bounds[0];
				final Class<?> componentClass = getClass(firstBound);
				return componentClass;
			}
			return null;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets the first type argument from the childClass. The base class will be resolved.
	 *
	 * @param <T>
	 *            the generic type of the baseClass
	 * @param childClass
	 *            the child class
	 * @return the first type argument
	 */
	public static <T> Class<?> getFirstTypeArgument(final @NonNull Class<? extends T> childClass)
	{
		return getTypeArgument(childClass, 0);
	}

	/**
	 * Gets the first type argument from the childClass.
	 *
	 * @param <T>
	 *            the generic type of the baseClass
	 * @param baseClass
	 *            the base class
	 * @param childClass
	 *            the child class
	 * @return the first type argument
	 */
	public static <T> Class<?> getFirstTypeArgument(final @NonNull Class<T> baseClass,
		final @NonNull Class<? extends T> childClass)
	{
		return getTypeArgument(baseClass, childClass, 0);
	}

	/**
	 * Gets the type argument from the childClass at the given index or null if it does not exists.
	 * The base class will be resolved.
	 *
	 * @param <T>
	 *            the generic type of the baseClass
	 * @param childClass
	 *            the child class
	 * @param index
	 *            the index of the type argument
	 * @return the type argument from the childClass at the given index or null if it does not
	 *         exists.
	 */
	public static <T> Class<?> getTypeArgument(final @NonNull Class<? extends T> childClass,
		final int index)
	{
		@SuppressWarnings("unchecked")
		Class<T> baseClass = (Class<T>)ClassExtensions.getBaseClass(childClass);
		return getTypeArgument(baseClass, childClass, index);
	}

	/**
	 * Gets the type argument from the childClass at the given index or null if it does not exists.
	 *
	 * @param <T>
	 *            the generic type of the baseClass
	 * @param baseClass
	 *            the base class
	 * @param childClass
	 *            the child class
	 * @param index
	 *            the index of the type argument
	 * @return the type argument from the childClass at the given index or null if it does not
	 *         exists.
	 */
	public static <T> Class<?> getTypeArgument(final @NonNull Class<T> baseClass,
		final @NonNull Class<? extends T> childClass, final int index)
	{
		final List<Class<?>> typeArguments = getTypeArguments(baseClass, childClass);
		if (typeArguments != null && !typeArguments.isEmpty() && index < typeArguments.size())
		{
			return typeArguments.get(index);
		}
		return null;
	}

	/**
	 * Get the actual type arguments from the given child class.
	 *
	 * @param <T>
	 *            the generic type of the baseClass
	 * @param childClass
	 *            the child class
	 * @return a list of the raw classes for the actual type arguments.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<Class<?>> getTypeArguments(final @NonNull Class<? extends T> childClass)
	{
		Class<T> baseClass = (Class<T>)ClassExtensions.getBaseClass(childClass);
		return getTypeArguments(baseClass, childClass);
	}

	/**
	 * Get the actual type arguments a child class has used to extend a generic base class.
	 *
	 * @param <T>
	 *            the generic type of the baseClass
	 * @param baseClass
	 *            the base class
	 * @param childClass
	 *            the child class
	 * @return a list of the raw classes for the actual type arguments.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<Class<?>> getTypeArguments(final @NonNull Class<T> baseClass,
		final @NonNull Class<? extends T> childClass)
	{
		Class<T> realBaseClass = baseClass;
		// handle interface case
		if (realBaseClass.isInterface())
		{
			realBaseClass = (Class<T>)ClassExtensions.getBaseClass(childClass);
		}
		final Map<Type, Type> resolvedTypes = new HashMap<>();
		Type type = childClass;
		// start walking up the inheritance hierarchy until we hit baseClass
		while (!getClass(type).equals(realBaseClass))
		{
			if (type instanceof Class)
			{
				// there is no useful information for us in raw types, so just
				// keep going.
				type = ((Class<?>)type).getGenericSuperclass();
			}
			else
			{
				final ParameterizedType parameterizedType = (ParameterizedType)type;
				final Class<?> rawType = (Class<?>)parameterizedType.getRawType();

				resolvedTypes.putAll(getTypeArgumentsAndParameters(parameterizedType));

				if (!rawType.equals(realBaseClass))
				{
					type = rawType.getGenericSuperclass();
				}
			}
		}

		// finally, for each actual type argument provided to baseClass,
		// determine (if possible)
		// the raw class for that type argument.
		Type[] actualTypeArguments;
		if (type instanceof Class)
		{
			actualTypeArguments = ((Class<?>)type).getTypeParameters();
		}
		else
		{
			actualTypeArguments = ((ParameterizedType)type).getActualTypeArguments();
		}
		final List<Class<?>> typeArgumentsAsClasses = new ArrayList<>();
		// resolve types by chasing down type variables.
		for (Type baseType : actualTypeArguments)
		{
			while (resolvedTypes.containsKey(baseType))
			{
				baseType = resolvedTypes.get(baseType);
			}
			typeArgumentsAsClasses.add(getClass(baseType));
		}
		return typeArgumentsAsClasses;
	}

	/**
	 * Gets the type arguments and parameters.
	 *
	 * @param parameterizedType
	 *            the parameterized type
	 * @return the type arguments and parameters
	 */
	public static Map<Type, Type> getTypeArgumentsAndParameters(
		final @NonNull ParameterizedType parameterizedType)
	{
		final Class<?> rawType = (Class<?>)parameterizedType.getRawType();
		final Map<Type, Type> resolvedTypes = new HashMap<>();
		final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		final TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
		for (int i = 0; i < actualTypeArguments.length; i++)
		{
			resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
		}
		return resolvedTypes;
	}

	/**
	 * Gets an optional from type {@link ParameterizedType} from the given class. The optional is
	 * empty if not found
	 *
	 * @param clazz
	 *            the clazz class
	 * @return the optional from type {@link ParameterizedType}. The optional is empty if not found
	 */
	public static Optional<ParameterizedType> getParameterizedType(final @NonNull Class<?> clazz) {
		Type[] types = getGenericTypeArray(clazz);
		if (0 < types.length && types[0] instanceof ParameterizedType) {
			return Optional.of((ParameterizedType)types[0]);
		}
		return Optional.empty();
	}

	/**
	 * Gets an array of the generic types from the given class
	 *
	 * @param clazz
	 *            the class
	 * @return an array of the generic types
	 */
	public static Type[] getGenericTypeArray(final @NonNull Class<?> clazz) {
		Type[] genericInterfaceTypes = clazz.getGenericInterfaces();
		if (0 < genericInterfaceTypes.length) {
			return genericInterfaceTypes;
		}
		Type genericSuperclassType = clazz.getGenericSuperclass();
		if (genericSuperclassType != null) {
			if (genericSuperclassType instanceof ParameterizedType) {
				return new Type[] { genericSuperclassType };
			}
		}
		return new Type[0];
	}

}
