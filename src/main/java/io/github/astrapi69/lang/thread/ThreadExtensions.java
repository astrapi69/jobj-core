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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.logging.Level;

import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

/**
 * The class {@link ThreadExtensions} provides utility methods for working with threads, such as
 * executing tasks with a timeout, running tasks with a specified number of CPU cores, and
 * retrieving information about running threads
 */
@UtilityClass
@Log
public final class ThreadExtensions
{

	/**
	 * Executes the given {@link Runnable} task and attempts to stop it if it exceeds the specified
	 * timeout
	 *
	 * @param task
	 *            the {@link Runnable} task to be executed
	 * @param timeout
	 *            the maximum time to wait for the task to complete
	 * @param timeUnit
	 *            the time unit of the timeout parameter
	 * @throws TimeoutException
	 *             if the task execution exceeds the specified timeout
	 */
	public static void runWithTimeout(Runnable task, long timeout, TimeUnit timeUnit)
		throws TimeoutException
	{
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<?> future = executor.submit(task);
		try
		{
			// Wait for the task to complete or timeout
			future.get(timeout, timeUnit);
		}
		catch (TimeoutException e)
		{
			// Cancel the task if it exceeds the timeout
			future.cancel(true);
			throw new TimeoutException("Task exceeded the timeout of " + timeout + " "
				+ timeUnit.toString().toLowerCase());
		}
		catch (InterruptedException | ExecutionException e)
		{
			log.log(Level.WARNING, "Runnable Interrupted ", e);
		}
		finally
		{
			executor.shutdownNow();
		}
	}

	/**
	 * Creates a custom thread pool that executes tasks in parallel processes with the given number
	 * of CPU cores
	 *
	 * @param task
	 *            the {@link Callable} task to execute
	 * @param cpuCores
	 *            the number of CPU cores to run with
	 * @param <T>
	 *            the generic type of the result
	 * @return the result of the given task
	 * @throws ExecutionException
	 *             if the computation threw an exception
	 * @throws InterruptedException
	 *             if the current thread is not a member of a {@link ForkJoinPool} and was
	 *             interrupted while waiting
	 */
	public static <T> T runCallableWithCpuCores(Callable<T> task, int cpuCores)
		throws ExecutionException, InterruptedException
	{
		ForkJoinPool forkJoinPool = new ForkJoinPool(cpuCores);
		return forkJoinPool.submit(task).get();
	}

	/**
	 * Creates a custom thread pool that executes tasks in parallel processes with the given number
	 * of CPU cores
	 *
	 * @param supplier
	 *            the {@link Supplier} task to execute
	 * @param cpuCores
	 *            the number of CPU cores to run with
	 * @param <T>
	 *            the generic type of the result
	 * @return the result of the given task
	 * @throws ExecutionException
	 *             if the computation threw an exception
	 * @throws InterruptedException
	 *             if the current thread is not a member of a {@link ForkJoinPool} and was
	 *             interrupted while waiting
	 */
	public static <T> T runAsyncSupplierWithCpuCores(Supplier<T> supplier, int cpuCores)
		throws ExecutionException, InterruptedException
	{
		ForkJoinPool forkJoinPool = new ForkJoinPool(cpuCores);
		CompletableFuture<T> future = CompletableFuture.supplyAsync(supplier, forkJoinPool);
		return future.get();
	}

	/**
	 * Finds all threads that are currently running
	 *
	 * @return an array with all threads that are currently running
	 */
	public static Thread[] resolveRunningThreads()
	{
		final Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		return threadSet.toArray(new Thread[threadSet.size()]);
	}

	/**
	 * Finds all threads that are currently running, converts them to {@link ThreadDataBean}, and
	 * puts them into a {@link List}
	 *
	 * @return the new {@link List} containing {@link ThreadDataBean} instances
	 */
	public static List<ThreadDataBean> newThreadData()
	{
		final Thread[] threads = resolveRunningThreads();
		final List<ThreadDataBean> snapshotOfThreadDataBeans = new ArrayList<>(threads.length);
		for (Thread t : threads)
		{
			snapshotOfThreadDataBeans.add(ThreadDataBean.of(t));
		}
		return snapshotOfThreadDataBeans;
	}

	/**
	 * Sets the given priority of the current thread
	 * <p>
	 * Note: the thread priority is between 1 and 10; if the provided priority is smaller or
	 * greater, the minimum priority (1) will be used
	 *
	 * @param threadPriority
	 *            the priority for the current thread to set
	 */
	public static void setCurrentThreadPriority(int threadPriority)
	{
		if (threadPriority >= 1 && threadPriority <= 10)
		{
			Thread.currentThread().setPriority(threadPriority);
		}
		else
		{
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		}
	}

	/**
	 * Returns the number of available processors (cores) on the current machine.
	 *
	 * <p>
	 * This method is a wrapper around {@link Runtime#availableProcessors()} and provides the total
	 * number of processors that the Java Virtual Machine (JVM) can utilize. This can be used to
	 * optimize concurrent tasks by determining how many threads can be effectively run in parallel.
	 * </p>
	 *
	 * @return the number of available processors (cores)
	 * @see Runtime#availableProcessors()
	 */
	public static int getAvailableProcessors()
	{
		return Runtime.getRuntime().availableProcessors();
	}

}
