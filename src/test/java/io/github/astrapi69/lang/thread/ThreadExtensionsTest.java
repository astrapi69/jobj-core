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
package io.github.astrapi69.lang.thread;

import static org.testng.AssertJUnit.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.astrapi69.test.base.BaseTestCase;

/**
 * The unit test class for the class {@link ThreadExtensions}.
 */
public class ThreadExtensionsTest extends BaseTestCase
{
	/**
	 * Test method for {@link ThreadExtensions#getAvailableProcessors()}
	 */
	@Test
	public void testGetAvailableProcessors()
	{
		// Arrange
		int expectedProcessors = Runtime.getRuntime().availableProcessors();

		// Act
		int actualProcessors = ThreadExtensions.getAvailableProcessors();

		// Assert
		assertEquals(expectedProcessors, actualProcessors);
	}

	/**
	 * Test method for {@link ThreadExtensions#getHalfOfAvailableProcessors()}
	 */
	@Test
	public void testGetHalfOfAvailableProcessors()
	{
		// Arrange
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		int expectedHalf = Math.max(1, availableProcessors / 2);

		// Act
		int actualHalf = ThreadExtensions.getHalfOfAvailableProcessors();

		// Assert
		assertEquals(expectedHalf, actualHalf);
	}

	/**
	 * Test to verify that the runWithTimeout method successfully completes a task within the given
	 * timeout.
	 */
	@Test
	public void testRunWithTimeout_Success()
	{
		Runnable task = () -> {
			try
			{
				// Simulate a task that completes within the timeout
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				// Task was interrupted
				Assert.fail("Task was interrupted unexpectedly.");
			}
		};

		try
		{
			ThreadExtensions.runWithTimeout(task, 3, TimeUnit.SECONDS);
			// If no exception is thrown, the test passes
		}
		catch (TimeoutException e)
		{
			Assert.fail("Task exceeded the timeout, but it should have completed in time.");
		}
	}

	/**
	 * Test to verify that the runWithTimeout method throws a TimeoutException when the task exceeds
	 * the given timeout.
	 */
	@Test
	public void testRunWithTimeout_TimeoutExceeded()
	{
		Runnable task = () -> {
			try
			{
				// Simulate a long-running task
				Thread.sleep(5000);
			}
			catch (InterruptedException e)
			{
				// Task was interrupted as expected
				System.out.println("Task was interrupted as expected.");
			}
		};

		try
		{
			ThreadExtensions.runWithTimeout(task, 2, TimeUnit.SECONDS);
			Assert.fail("Expected a TimeoutException to be thrown.");
		}
		catch (TimeoutException e)
		{
			// Test passes if TimeoutException is thrown
			Assert.assertTrue(e.getMessage().contains("Task exceeded the timeout"));
		}
	}

	/**
	 * Test method for {@link ThreadExtensions#newThreadData()}
	 */
	@Test
	public final void testNewThreadData()
	{
		List<ThreadDataBean> threadData = ThreadExtensions.newThreadData();
		actual = 0 < threadData.size();
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ThreadExtensions#resolveRunningThreads()}
	 */
	@Test
	public final void testResolveRunningThreads()
	{
		Thread[] runningThreads = ThreadExtensions.resolveRunningThreads();
		actual = 0 < runningThreads.length;
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ThreadExtensions#runAsyncSupplierWithCpuCores(Supplier, int)}
	 */
	@Test
	public void testRunAsyncSupplierWithCpuCores() throws ExecutionException, InterruptedException
	{

		String actual;
		String expected;

		int cores = Runtime.getRuntime().availableProcessors();
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "linode.com");
		map.put(2, "heroku.com");
		map.put(3, "heroku.uk");
		actual = ThreadExtensions.runAsyncSupplierWithCpuCores(
			() -> map.entrySet().parallelStream().filter(x -> x.getValue().endsWith("com"))
				.map(x -> x.getValue() + "\n").collect(Collectors.joining()),
			cores);

		expected = "linode.com\nheroku.com\n";
		assertEquals(actual, expected);
	}


	/**
	 * Test method for {@link ThreadExtensions#runCallableWithCpuCores(Callable, int)}
	 */
	@Test
	public void testRunCallableWithCpuCores() throws ExecutionException, InterruptedException
	{

		String actual;
		String expected;

		int cores = Runtime.getRuntime().availableProcessors();
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "linode.com");
		map.put(2, "heroku.com");
		map.put(3, "heroku.uk");
		actual = ThreadExtensions.runCallableWithCpuCores(() ->
		// parallel task here, for example
		map.entrySet().stream().parallel().filter(x -> x.getValue().endsWith("com"))
			.map(Map.Entry::getValue).collect(Collectors.joining()), cores);

		expected = "linode.comheroku.com";
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link ThreadExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ThreadExtensions.class);
	}

}
