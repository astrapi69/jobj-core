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

/**
 * The abstract class {@link InterruptableThread} provides a template for threads that can be
 * interrupted gracefully during execution.
 */
public abstract class InterruptableThread extends Thread
{
	/** A flag that indicates whether the thread is interrupted */
	private boolean interrupted = false;

	/**
	 * Runs the thread and repeatedly calls the {@link #process()} method until the thread is
	 * interrupted
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void run()
	{
		while (!interrupted)
		{
			process();
		}
	}

	/**
	 * Interrupts the thread by setting the interrupted flag to true and calling the
	 * {@link Thread#interrupt()} method
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void interrupt()
	{
		super.interrupt();
		interrupted = true;
	}

	/**
	 * Abstract method to be implemented by subclasses that defines the processing logic of the
	 * thread
	 */
	protected abstract void process();
}
