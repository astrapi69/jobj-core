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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * {@link TypeArgumentsExtensions} over a set of generic types of its own, in parameterized tables:
 * every kind of {@link Type}, the walk up a hierarchy of type arguments, and the index of a type
 * argument
 */
class TypeArgumentsExtensionsTablesTest
{

	/** A base with two type parameters */
	abstract static class Base<K, V>
	{
	}

	/** Binds the first type parameter of {@link Base} and passes the second on */
	abstract static class Mid<V> extends Base<String, V>
	{
	}

	/** Binds the second type parameter of {@link Base} */
	static class Leaf extends Mid<Integer>
	{
	}

	/** A list of strings */
	@SuppressWarnings("serial")
	static class StringList extends ArrayList<String>
	{
	}

	/** A raw list */
	@SuppressWarnings({ "serial", "rawtypes" })
	static class RawList extends ArrayList
	{
	}

	/** A generic interface */
	interface Repository<T>
	{
	}

	/** Binds the type parameter of {@link Repository} */
	static class NumberRepository implements Repository<Number>
	{
	}

	/** Implements an interface that is not generic */
	@SuppressWarnings("serial")
	static class Plain implements Serializable
	{
	}

	/** Neither a generic interface nor a generic superclass */
	static class Nothing
	{
	}

	/**
	 * The methods whose generic return types are the types of the table
	 *
	 * @param <N>
	 *            a type variable with one bound
	 * @param <O>
	 *            a type variable without a bound
	 * @param <C>
	 *            a type variable with two bounds
	 */
	@SuppressWarnings("unused")
	abstract static class Types<N extends Number, O, C extends Number & Comparable<C>>
	{
		abstract String plain();

		abstract List<String> parameterized();

		abstract List<String>[] genericArray();

		abstract N[] arrayOfABoundVariable();

		abstract C[] arrayOfAVariableWithTwoBounds();

		abstract N boundVariable();

		abstract O unboundVariable();

		abstract C variableWithTwoBounds();

		abstract List<? extends Number> wildcard();
	}

	private static Type returnTypeOf(final String method) throws NoSuchMethodException
	{
		return Types.class.getDeclaredMethod(method).getGenericReturnType();
	}

	/**
	 * The types and the class {@link TypeArgumentsExtensions#getClass(Type)} gives for each
	 *
	 * @return the method whose return type is the type, and the class
	 */
	static Stream<Arguments> typesAndTheirClasses()
	{
		return Stream.of(Arguments.of("plain", String.class),
			Arguments.of("parameterized", List.class), Arguments.of("genericArray", List[].class),
			Arguments.of("arrayOfABoundVariable", Number[].class),
			Arguments.of("arrayOfAVariableWithTwoBounds", null),
			Arguments.of("boundVariable", Number.class),
			Arguments.of("unboundVariable", Object.class),
			Arguments.of("variableWithTwoBounds", null), Arguments.of("wildcard", List.class));
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions#getClass(Type)}: a class is itself, a
	 * parameterized type its raw type, a generic array the array of its component's class, a type
	 * variable the class of its single bound; a variable with two bounds and an array of it give
	 * null
	 *
	 * @param method
	 *            the method whose generic return type is the type
	 * @param expected
	 *            the class
	 * @throws NoSuchMethodException
	 *             if the fixture lacks the method
	 */
	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("typesAndTheirClasses")
	void theClassOfAType(final String method, final Class<?> expected) throws NoSuchMethodException
	{
		assertEquals(expected, TypeArgumentsExtensions.getClass(returnTypeOf(method)));
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions#getClass(Type)} with a wildcard, which is no
	 * class, no parameterized type, no generic array and no type variable
	 *
	 * @throws NoSuchMethodException
	 *             if the fixture lacks the method
	 */
	@Test
	void theClassOfAWildcard() throws NoSuchMethodException
	{
		final Type wildcard = ((ParameterizedType)returnTypeOf("wildcard"))
			.getActualTypeArguments()[0];
		assertNull(TypeArgumentsExtensions.getClass(wildcard));
	}

	/**
	 * Base classes, child classes and the type arguments the child gives the base
	 *
	 * @return the base, the child and the type arguments
	 */
	static Stream<Arguments> typeArgumentsOfHierarchies()
	{
		return Stream.of(
			Arguments.of(Base.class, Leaf.class, Arrays.asList(String.class, Integer.class)),
			Arguments.of(Base.class, Mid.class, Arrays.asList(String.class, Object.class)),
			Arguments.of(Mid.class, Leaf.class, Arrays.asList(Integer.class)),
			Arguments.of(ArrayList.class, StringList.class, Arrays.asList(String.class)),
			Arguments.of(ArrayList.class, RawList.class, Arrays.asList(Object.class)),
			Arguments.of(Leaf.class, Leaf.class, Arrays.asList()));
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions#getTypeArguments(Class, Class)}: the walk up
	 * the hierarchy resolves each type parameter of the base to the class a subclass binds it to;
	 * one that no class binds gives the class of its bound
	 *
	 * @param baseClass
	 *            the base class
	 * @param childClass
	 *            the child class
	 * @param expected
	 *            the type arguments
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ParameterizedTest(name = "{1} gives {0} {2}")
	@MethodSource("typeArgumentsOfHierarchies")
	void theTypeArgumentsAChildGivesItsBase(final Class baseClass, final Class childClass,
		final List<Class<?>> expected)
	{
		assertEquals(expected, TypeArgumentsExtensions.getTypeArguments(baseClass, childClass));
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions#getTypeArgument(Class, Class, int)}: the type
	 * argument at an index, and null for an index that does not exist
	 */
	@Test
	void theTypeArgumentAtAnIndex()
	{
		assertEquals(String.class,
			TypeArgumentsExtensions.getTypeArgument(Base.class, Leaf.class, 0));
		assertEquals(Integer.class,
			TypeArgumentsExtensions.getTypeArgument(Base.class, Leaf.class, 1));
		assertNull(TypeArgumentsExtensions.getTypeArgument(Base.class, Leaf.class, 2));
		assertNull(TypeArgumentsExtensions.getTypeArgument(Leaf.class, Leaf.class, 0));
		assertEquals(String.class, TypeArgumentsExtensions.getFirstTypeArgument(StringList.class));
		assertThrows(IndexOutOfBoundsException.class,
			() -> TypeArgumentsExtensions.getTypeArgument(Base.class, Leaf.class, -1));
		assertThrows(IndexOutOfBoundsException.class,
			() -> TypeArgumentsExtensions.getTypeArgument(StringList.class, -1));
	}

	/**
	 * Test method for {@link TypeArgumentsExtensions#getParameterizedType(Class)} and
	 * {@link TypeArgumentsExtensions#getGenericTypeArray(Class)}: the generic interfaces come
	 * first, then a parameterized superclass, else nothing
	 */
	@Test
	void theParameterizedTypeOfAClass()
	{
		final Optional<ParameterizedType> ofInterface = TypeArgumentsExtensions
			.getParameterizedType(NumberRepository.class);
		assertTrue(ofInterface.isPresent());
		assertSame(Repository.class, ofInterface.get().getRawType());
		final Optional<ParameterizedType> ofSuperclass = TypeArgumentsExtensions
			.getParameterizedType(Mid.class);
		assertTrue(ofSuperclass.isPresent());
		assertSame(Base.class, ofSuperclass.get().getRawType());
		assertFalse(TypeArgumentsExtensions.getParameterizedType(Plain.class).isPresent());
		assertFalse(TypeArgumentsExtensions.getParameterizedType(Nothing.class).isPresent());
		assertArrayEquals(new Type[0], TypeArgumentsExtensions.getGenericTypeArray(Nothing.class));
		assertArrayEquals(new Type[0],
			TypeArgumentsExtensions.getGenericTypeArray(Repository.class));
		assertArrayEquals(new Type[] { Serializable.class },
			TypeArgumentsExtensions.getGenericTypeArray(Plain.class));
	}
}
