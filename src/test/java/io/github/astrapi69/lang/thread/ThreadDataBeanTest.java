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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * The unit test class for the class {@link ThreadDataBean}.
 */
public class ThreadDataBeanTest
{

	/**
	 * Test method for {@link ThreadDataBean#equals(Object)} , {@link ThreadDataBean#hashCode()}
	 */
	@Test
	public void equalsContract()
	{
		EqualsVerifier.simple().forClass(ThreadDataBean.class).verify();
	}

	/**
	 * Test method for {@link ThreadDataBean}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ThreadDataBean.class);
	}

	/**
	 * Test method for {@link ThreadDataBean#builder()}, {@link ThreadDataBean#toBuilder()} and the
	 * constructor that takes every field
	 */
	@Test
	public void theBuilderAndTheConstructorCarryEveryField()
	{
		final ThreadDataBean built = ThreadDataBean.builder().alive(true).daemon(true)
			.interrupted(true).id(7L).name("worker").priority(5).threadGroup("main").build();
		assertTrue(built.isAlive());
		assertTrue(built.isDaemon());
		assertTrue(built.isInterrupted());
		assertEquals(7L, built.getId());
		assertEquals("worker", built.getName());
		assertEquals(Integer.valueOf(5), built.getPriority());
		assertEquals("main", built.getThreadGroup());
		assertEquals(new ThreadDataBean(true, true, true, 7L, "worker", 5, "main"), built);
		assertEquals(built, built.toBuilder().build());
		assertNotSame(built, built.toBuilder().build());
		assertEquals(new ThreadDataBean(false, false, false, 0L, null, null, null),
			ThreadDataBean.builder().build());
		assertEquals(new ThreadDataBean(false, false, false, 0L, null, null, null),
			new ThreadDataBean());
	}

	/**
	 * Test method for {@link ThreadDataBean#toString()} and the builder's toString
	 */
	@Test
	public void toStringNamesEveryField()
	{
		final ThreadDataBean.ThreadDataBeanBuilder builder = ThreadDataBean.builder().alive(true)
			.id(7L).name("worker").priority(5).threadGroup("main");
		assertEquals(
			"ThreadDataBean(alive=true, daemon=false, interrupted=false, id=7, name=worker, priority=5, threadGroup=main)",
			builder.build().toString());
		assertEquals(
			"ThreadDataBean.ThreadDataBeanBuilder(alive=true, daemon=false, interrupted=false, id=7, name=worker, priority=5, threadGroup=main)",
			builder.toString());
	}

	/**
	 * Test method for {@link ThreadDataBean#of(Thread)}
	 */
	@Test
	public void ofTakesTheStateOfTheThread()
	{
		final ThreadGroup group = new ThreadGroup("of-group");
		final Thread thread = new Thread(group, () -> {
		}, "of-thread");
		thread.setDaemon(true);
		thread.setPriority(Thread.MIN_PRIORITY);
		final ThreadDataBean data = ThreadDataBean.of(thread);
		assertFalse(data.isAlive());
		assertTrue(data.isDaemon());
		assertFalse(data.isInterrupted());
		assertEquals(thread.threadId(), data.getId());
		assertEquals("of-thread", data.getName());
		assertEquals(Integer.valueOf(Thread.MIN_PRIORITY), data.getPriority());
		assertEquals("of-group", data.getThreadGroup());
		assertThrows(NullPointerException.class, () -> ThreadDataBean.of(null));
	}

}
