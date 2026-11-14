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

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The unit test class for {@link InterruptableThread} to ensure proper behavior
 */
class InterruptableThreadTest
{
	private InterruptableThread interruptableThread;

	/**
	 * Sets up the test environment before each test
	 */
	@BeforeEach
	void setUp()
	{
		interruptableThread = new InterruptableThread()
		{
			@Override
			protected void process()
			{
				// Simulating processing by sleeping for a short duration
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					Thread.currentThread().interrupt();
				}
			}
		};
	}

	/**
	 * Tests the {@link InterruptableThread#interrupt()} method and verifies the thread stops
	 * processing when interrupted
	 *
	 * @throws InterruptedException
	 *             if the test is interrupted while it waits for the thread
	 */
	@Test
	void testInterrupt() throws InterruptedException
	{
		interruptableThread.setDaemon(true);
		interruptableThread.start();
		assertFalse(interruptableThread.isInterrupted());

		interruptableThread.interrupt();
		interruptableThread.join(5000);
		assertFalse(interruptableThread.isAlive(), "the loop ends after interrupt()");
		// read after the thread ended: in between, Thread.sleep clears the status when it throws,
		// and the catch block of process() sets it again
		assertTrue(interruptableThread.isInterrupted());
	}

	/**
	 * A process that catches the {@link InterruptedException} and clears the interrupt status: the
	 * loop of {@link InterruptableThread} ends all the same, as it keeps a flag of its own
	 *
	 * @throws InterruptedException
	 *             if the test is interrupted while it waits for the thread
	 */
	@Test
	void aProcessThatSwallowsTheInterruptEndsAllTheSame() throws InterruptedException
	{
		final AtomicInteger calls = new AtomicInteger();
		final InterruptableThread thread = new InterruptableThread()
		{
			@Override
			protected void process()
			{
				calls.incrementAndGet();
				try
				{
					Thread.sleep(10);
				}
				catch (InterruptedException e)
				{
					// swallowed on purpose: the interrupt status is cleared
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		Thread.sleep(50);
		thread.interrupt();
		thread.join(5000);
		assertFalse(thread.isAlive(), "the loop ends after interrupt()");
		assertTrue(0 < calls.get(), "process() ran before the interrupt");
	}

	/**
	 * A process that does no more than count: once the JIT compiler has compiled the loop, it reads
	 * the flag of {@link InterruptableThread} only if the write of
	 * {@link InterruptableThread#interrupt()} is guaranteed to become visible to it (JLS 17.4.5)
	 *
	 * @throws InterruptedException
	 *             if the test is interrupted while it waits for the thread
	 */
	@Test
	void aTightProcessEndsAfterInterruptToo() throws InterruptedException
	{
		final long[] calls = new long[1];
		final InterruptableThread thread = new InterruptableThread()
		{
			@Override
			protected void process()
			{
				calls[0]++;
			}
		};
		thread.setDaemon(true);
		thread.start();
		// long enough for the JIT compiler to compile the loop
		Thread.sleep(1000);
		thread.interrupt();
		thread.join(5000);
		assertFalse(thread.isAlive(), "the loop ends after interrupt()");
	}
}
