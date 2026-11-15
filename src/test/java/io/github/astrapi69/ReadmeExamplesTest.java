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
package io.github.astrapi69;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.check.Argument;
import io.github.astrapi69.check.Check;
import io.github.astrapi69.lang.ClassExtensions;
import io.github.astrapi69.lang.ClassType;
import io.github.astrapi69.lang.ObjectExtensions;
import io.github.astrapi69.lang.PackageExtensions;
import io.github.astrapi69.lang.TypeArgumentsExtensions;
import io.github.astrapi69.lang.thread.ThreadDataBean;
import io.github.astrapi69.lang.thread.ThreadExtensions;

/**
 * The examples of the README's Quick start, each block as a test with the values its comments name
 * as assertions, so that the README says what the code does
 */
class ReadmeExamplesTest
{

	/** A list of strings, for the type argument example */
	@SuppressWarnings("serial")
	static class StringList extends ArrayList<String>
	{
	}

	/**
	 * The block "Checking arguments"
	 */
	@Test
	void checkingArguments()
	{
		String name = Argument.notEmpty("jobj", "name"); // "jobj"
		int percent = Argument.isInRange(0, 100, 100, "percent"); // 100: both bounds are in the
																	// range
		IllegalArgumentException empty = assertThrows(IllegalArgumentException.class,
			() -> Argument.notEmpty("  ", "name")); // a text of blanks is empty
		Check check = Check.get().notNull(name, "name").isInRange(1, 10, 5, "count"); // chained

		assertEquals("jobj", name);
		assertEquals(100, percent);
		assertEquals("Given argument 'name' may not be empty.", empty.getMessage());
		assertSame(Check.get(), check);
	}

	/**
	 * The block "Classes"
	 */
	@Test
	void classes()
	{
		ClassType arrayType = ClassExtensions.getClassType(String[].class); // ClassType.ARRAY
		boolean list = ClassExtensions.isInstantiable(ArrayList.class); // true
		boolean stream = ClassExtensions.isInstantiable(InputStream.class); // false: abstract
		String simpleName = ClassExtensions.getSimpleName(Map.Entry.class); // "Entry"
		URL url = ClassExtensions.getResource("io/github/astrapi69/lang/resources.properties");

		assertEquals(ClassType.ARRAY, arrayType);
		assertTrue(list);
		assertFalse(stream);
		assertEquals("Entry", simpleName);
		assertNotNull(url);
	}

	/**
	 * The block "Packages"
	 */
	@Test
	void packages()
	{
		String name = PackageExtensions.getPackageName(String.class); // "java.lang"
		String path = PackageExtensions.getPackagePath(String.class); // "java/lang/"
		String primitive = PackageExtensions.getPackageName(int.class); // "java.lang"
		String unnamed = PackageExtensions.getPackageName("Unqualified"); // "", the unnamed package

		assertEquals("java.lang", name);
		assertEquals("java/lang/", path);
		assertEquals("java.lang", primitive);
		assertEquals("", unnamed);
	}

	/**
	 * The block "Type arguments"
	 */
	@Test
	void typeArguments()
	{
		Class<?> element = TypeArgumentsExtensions.getFirstTypeArgument(StringList.class); // String
		Class<?> missing = TypeArgumentsExtensions.getTypeArgument(StringList.class, 1); // null

		assertEquals(String.class, element);
		assertEquals(null, missing);
	}

	/**
	 * The block "Default values"
	 */
	@Test
	void defaultValues()
	{
		boolean zero = ObjectExtensions.isDefaultValue(int.class, 0); // true
		boolean one = ObjectExtensions.isDefaultValue(int.class, 1); // false
		boolean none = ObjectExtensions.isDefaultValue(String.class, null); // true

		assertTrue(zero);
		assertFalse(one);
		assertTrue(none);
	}

	/**
	 * The block "Threads"
	 *
	 * @throws Exception
	 *             if the example task fails
	 */
	@Test
	void threads() throws Exception
	{
		ThreadExtensions.runWithTimeout(() -> {
		}, 1, TimeUnit.SECONDS); // returns once the task is done
		TimeoutException timeout = assertThrows(TimeoutException.class,
			() -> ThreadExtensions.runWithTimeout(() -> {
				try
				{
					Thread.sleep(10_000);
				}
				catch (InterruptedException e)
				{
					// stopped by the timeout
				}
			}, 100, TimeUnit.MILLISECONDS)); // the task is stopped
		int result = ThreadExtensions.runCallableWithCpuCores(() -> 6 * 7, 2); // 42, on 2 threads
		List<ThreadDataBean> snapshot = ThreadExtensions.newThreadData(); // the running threads

		assertEquals("Task exceeded the timeout of 100 milliseconds", timeout.getMessage());
		assertEquals(42, result);
		assertFalse(snapshot.isEmpty());
	}
}
