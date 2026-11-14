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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.meanbean.test.BeanTester;

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
				fail("Task was interrupted unexpectedly.");
			}
		};

		try
		{
			ThreadExtensions.runWithTimeout(task, 3, TimeUnit.SECONDS);
			// If no exception is thrown, the test passes
		}
		catch (TimeoutException e)
		{
			fail("Task exceeded the timeout, but it should have completed in time.");
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
			fail("Expected a TimeoutException to be thrown.");
		}
		catch (TimeoutException e)
		{
			// Test passes if TimeoutException is thrown
			assertTrue(e.getMessage().contains("Task exceeded the timeout"));
		}
	}

	/**
	 * Test method for {@link ThreadExtensions#newThreadData()}
	 */
	@Test
	public final void testNewThreadData()
	{
		// disabled before: it threw a NullPointerException on GitHub's runners, where a thread
		// could
		// end between resolveRunningThreads() and ThreadDataBean.of, which threw for a terminated
		// thread until #25
		List<ThreadDataBean> threadData = ThreadExtensions.newThreadData();
		actual = 0 < threadData.size();
		expected = true;
		assertEquals(expected, actual);
		final Thread current = Thread.currentThread();
		assertTrue(
			threadData.stream()
				.anyMatch(data -> data.getId() == current.threadId()
					&& current.getName().equals(data.getName())),
			"the data of the current thread is in the snapshot");
	}

	/**
	 * Test method for {@link ThreadExtensions#setCurrentThreadPriority(int)}: a priority from 1 to
	 * 10 is taken, any other gives the minimum priority 1. Run in a thread of its own, so the
	 * priority of the test thread stays as it is
	 *
	 * @param threadPriority
	 *            the priority to set
	 * @param expectedPriority
	 *            the priority the thread has afterwards
	 * @throws InterruptedException
	 *             if the test is interrupted while it waits for the thread
	 */
	@ParameterizedTest(name = "{0} gives {1}")
	@CsvSource({ "1, 1", "5, 5", "10, 10", "0, 1", "11, 1", "-1, 1", "2147483647, 1" })
	public void setCurrentThreadPriority(final int threadPriority, final int expectedPriority)
		throws InterruptedException
	{
		final int[] priority = new int[1];
		final Thread thread = new Thread(() -> {
			Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
			ThreadExtensions.setCurrentThreadPriority(threadPriority);
			priority[0] = Thread.currentThread().getPriority();
		});
		thread.start();
		thread.join(5000);
		assertEquals(expectedPriority, priority[0]);
	}

	/**
	 * Test method for {@link ThreadExtensions#shutdownExecutorService(ExecutorService, long)} with
	 * tasks that end in time: the executor shuts down and terminates
	 *
	 * @throws InterruptedException
	 *             if the test is interrupted while it waits
	 */
	@Test
	public void shutdownExecutorServiceWithTasksThatEndInTime() throws InterruptedException
	{
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		final Future<?> future = executorService.submit(() -> {
		});
		ThreadExtensions.shutdownExecutorService(executorService, 5);
		assertTrue(executorService.isTerminated());
		assertTrue(future.isDone());
		assertFalse(future.isCancelled());
	}

	/**
	 * Test method for {@link ThreadExtensions#shutdownExecutorService(ExecutorService, long, long)}
	 * with a task that does not end in time: the executor is shut down now, which interrupts the
	 * task
	 *
	 * @throws InterruptedException
	 *             if the test is interrupted while it waits
	 */
	@Test
	public void shutdownExecutorServiceWithATaskThatDoesNotEndInTime() throws InterruptedException
	{
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		final CountDownLatch started = new CountDownLatch(1);
		final CountDownLatch interrupted = new CountDownLatch(1);
		executorService.submit(() -> {
			started.countDown();
			try
			{
				Thread.sleep(60_000);
			}
			catch (InterruptedException e)
			{
				interrupted.countDown();
			}
		});
		assertTrue(started.await(5, TimeUnit.SECONDS));
		ThreadExtensions.shutdownExecutorService(executorService, 0, 1);
		assertTrue(interrupted.await(5, TimeUnit.SECONDS), "shutdownNow interrupted the task");
		assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));
	}

	/**
	 * Test method for {@link ThreadExtensions#shutdownExecutorService(ExecutorService, long, long)}
	 * when the calling thread is interrupted while it waits: the executor is shut down now and the
	 * interrupt status of the caller is kept
	 *
	 * @throws InterruptedException
	 *             if the test is interrupted while it waits
	 */
	@Test
	public void shutdownExecutorServiceWhenTheCallerIsInterrupted() throws InterruptedException
	{
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		final CountDownLatch started = new CountDownLatch(1);
		executorService.submit(() -> {
			started.countDown();
			try
			{
				Thread.sleep(60_000);
			}
			catch (InterruptedException e)
			{
				// ends the task
			}
		});
		assertTrue(started.await(5, TimeUnit.SECONDS));
		Thread.currentThread().interrupt();
		ThreadExtensions.shutdownExecutorService(executorService, 60);
		assertTrue(Thread.interrupted(), "the interrupt status of the caller is kept");
		assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));
	}

	/**
	 * Test method for {@link ThreadExtensions#runWithTimeout(Runnable, long, TimeUnit)} when the
	 * calling thread is interrupted while it waits for the task
	 *
	 * @throws Exception
	 *             if the test fails to wait
	 */
	@Test
	public void runWithTimeoutWhenTheCallerIsInterrupted() throws Exception
	{
		final AtomicBoolean started = new AtomicBoolean();
		final CountDownLatch interrupted = new CountDownLatch(1);
		Thread.currentThread().interrupt();
		ThreadExtensions.runWithTimeout(() -> {
			started.set(true);
			try
			{
				Thread.sleep(60_000);
			}
			catch (InterruptedException e)
			{
				interrupted.countDown();
			}
		}, 60, TimeUnit.SECONDS);
		final boolean callerStillInterrupted = Thread.interrupted();
		assertTrue(callerStillInterrupted, "the interrupt status of the caller is kept");
		// the task is stopped: cancelled before it started, or interrupted in its sleep
		assertTrue(!started.get() || interrupted.await(5, TimeUnit.SECONDS), "the task is stopped");
	}

	/**
	 * Test method for {@link ThreadExtensions#runWithTimeout(Runnable, long, TimeUnit)} with a task
	 * that throws: the exception is logged, not thrown to the caller
	 *
	 * @throws TimeoutException
	 *             if the task exceeds the timeout
	 */
	@Test
	public void runWithTimeoutWithATaskThatThrows() throws TimeoutException
	{
		ThreadExtensions.runWithTimeout(() -> {
			throw new IllegalStateException("thrown by the task");
		}, 5, TimeUnit.SECONDS);
	}

	/**
	 * Test method for {@link ThreadExtensions#runCallableWithCpuCores(Callable, int)} and
	 * {@link ThreadExtensions#runAsyncSupplierWithCpuCores(Supplier, int)}: the pool is shut down
	 * once the result is there, so the thread that ran the task ends
	 *
	 * @throws Exception
	 *             if the task fails or the test fails to wait
	 */
	@Test
	public void thePoolThreadOfTheCpuCoresMethodsAfterTheResult() throws Exception
	{
		final Thread[] callableWorker = new Thread[1];
		assertEquals("callable", ThreadExtensions.runCallableWithCpuCores(() -> {
			callableWorker[0] = Thread.currentThread();
			return "callable";
		}, 1));
		final Thread[] supplierWorker = new Thread[1];
		assertEquals("supplier", ThreadExtensions.runAsyncSupplierWithCpuCores(() -> {
			supplierWorker[0] = Thread.currentThread();
			return "supplier";
		}, 1));
		callableWorker[0].join(2000);
		supplierWorker[0].join(2000);
		assertFalse(callableWorker[0].isAlive(), "the worker of the callable has ended");
		assertFalse(supplierWorker[0].isAlive(), "the worker of the supplier has ended");
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
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ThreadExtensions.class);
	}

}
