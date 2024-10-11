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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

/**
 * The parameterized test class for {@link InterruptableThread} that loads test data from a CSV file
 */
class InterruptableThreadParameterizedTest
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
	 * Tests the interrupt method with various inputs from a CSV file
	 *
	 * @param sleepDuration
	 *            The duration in milliseconds the thread will sleep before being interrupted
	 */
	@ParameterizedTest
	@CsvFileSource(resources = "/interruptable-thread.csv", numLinesToSkip = 1)
	void testInterruptWithCSV(long sleepDuration) throws InterruptedException
	{
		interruptableThread.start();
		Thread.sleep(sleepDuration);
		assertFalse(interruptableThread.isInterrupted());

		interruptableThread.interrupt();
		assertTrue(interruptableThread.isInterrupted());
	}
}
