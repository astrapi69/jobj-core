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

import java.util.Objects;

/**
 * The class {@link ThreadDataBean} holds data from a Thread.
 *
 * Written out from what Lombok's {@code @Data}, {@code @NoArgsConstructor},
 * {@code @AllArgsConstructor} and {@code @Builder(toBuilder = true)} generated, so that every
 * generated member carries its Javadoc; the members, their signatures and the text of
 * {@link #toString()} are the ones Lombok generated
 */
public class ThreadDataBean
{
	private boolean alive;
	private boolean daemon;
	private boolean interrupted;
	private long id;
	private String name;
	private Integer priority;
	private String threadGroup;

	/**
	 * Creates a new {@link ThreadDataBean} with every flag false, the id 0 and no name, priority or
	 * thread group
	 */
	public ThreadDataBean()
	{
	}

	/**
	 * Creates a new {@link ThreadDataBean} with the given values
	 *
	 * @param alive
	 *            whether the thread is alive
	 * @param daemon
	 *            whether the thread is a daemon thread
	 * @param interrupted
	 *            whether the thread is interrupted
	 * @param id
	 *            the id of the thread
	 * @param name
	 *            the name of the thread
	 * @param priority
	 *            the priority of the thread
	 * @param threadGroup
	 *            the name of the thread group of the thread
	 */
	public ThreadDataBean(final boolean alive, final boolean daemon, final boolean interrupted,
		final long id, final String name, final Integer priority, final String threadGroup)
	{
		this.alive = alive;
		this.daemon = daemon;
		this.interrupted = interrupted;
		this.id = id;
		this.name = name;
		this.priority = priority;
		this.threadGroup = threadGroup;
	}

	/**
	 * Factory method for create a new {@link ThreadDataBean} object from the given {@link Thread}
	 * object. A thread that has terminated has no thread group any more
	 * ({@link Thread#getThreadGroup()} returns null), so its {@link ThreadDataBean} holds no thread
	 * group
	 *
	 * @param thread
	 *            the thread
	 * @return the new {@link ThreadDataBean} object
	 * @throws NullPointerException
	 *             if the given thread is null
	 */
	public static ThreadDataBean of(final Thread thread)
	{
		Objects.requireNonNull(thread, "thread is marked non-null but is null");
		// read once: the thread may terminate between a check and a second read
		final ThreadGroup threadGroup = thread.getThreadGroup();
		return ThreadDataBean.builder().priority(thread.getPriority()).alive(thread.isAlive())
			.daemon(thread.isDaemon()).interrupted(thread.isInterrupted()).id(thread.threadId())
			.threadGroup(threadGroup != null ? threadGroup.getName() : null).name(thread.getName())
			.build();
	}

	/**
	 * Creates a new builder with every flag false, the id 0 and no name, priority or thread group
	 *
	 * @return the new builder
	 */
	public static ThreadDataBeanBuilder builder()
	{
		return new ThreadDataBeanBuilder();
	}

	/**
	 * Creates a new builder that holds the values of this {@link ThreadDataBean}
	 *
	 * @return the new builder
	 */
	public ThreadDataBeanBuilder toBuilder()
	{
		return new ThreadDataBeanBuilder().alive(this.alive).daemon(this.daemon)
			.interrupted(this.interrupted).id(this.id).name(this.name).priority(this.priority)
			.threadGroup(this.threadGroup);
	}

	/**
	 * Returns whether the thread is alive
	 *
	 * @return true if the thread is alive
	 */
	public boolean isAlive()
	{
		return this.alive;
	}

	/**
	 * Sets whether the thread is alive
	 *
	 * @param alive
	 *            whether the thread is alive
	 */
	public void setAlive(final boolean alive)
	{
		this.alive = alive;
	}

	/**
	 * Returns whether the thread is a daemon thread
	 *
	 * @return true if the thread is a daemon thread
	 */
	public boolean isDaemon()
	{
		return this.daemon;
	}

	/**
	 * Sets whether the thread is a daemon thread
	 *
	 * @param daemon
	 *            whether the thread is a daemon thread
	 */
	public void setDaemon(final boolean daemon)
	{
		this.daemon = daemon;
	}

	/**
	 * Returns whether the thread is interrupted
	 *
	 * @return true if the thread is interrupted
	 */
	public boolean isInterrupted()
	{
		return this.interrupted;
	}

	/**
	 * Sets whether the thread is interrupted
	 *
	 * @param interrupted
	 *            whether the thread is interrupted
	 */
	public void setInterrupted(final boolean interrupted)
	{
		this.interrupted = interrupted;
	}

	/**
	 * Returns the id of the thread
	 *
	 * @return the id of the thread
	 */
	public long getId()
	{
		return this.id;
	}

	/**
	 * Sets the id of the thread
	 *
	 * @param id
	 *            the id of the thread
	 */
	public void setId(final long id)
	{
		this.id = id;
	}

	/**
	 * Returns the name of the thread
	 *
	 * @return the name of the thread
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Sets the name of the thread
	 *
	 * @param name
	 *            the name of the thread
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Returns the priority of the thread
	 *
	 * @return the priority of the thread
	 */
	public Integer getPriority()
	{
		return this.priority;
	}

	/**
	 * Sets the priority of the thread
	 *
	 * @param priority
	 *            the priority of the thread
	 */
	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}

	/**
	 * Returns the name of the thread group of the thread
	 *
	 * @return the name of the thread group of the thread
	 */
	public String getThreadGroup()
	{
		return this.threadGroup;
	}

	/**
	 * Sets the name of the thread group of the thread
	 *
	 * @param threadGroup
	 *            the name of the thread group of the thread
	 */
	public void setThreadGroup(final String threadGroup)
	{
		this.threadGroup = threadGroup;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ThreadDataBean))
		{
			return false;
		}
		final ThreadDataBean other = (ThreadDataBean)o;
		return other.canEqual(this) && this.alive == other.alive && this.daemon == other.daemon
			&& this.interrupted == other.interrupted && this.id == other.id
			&& Objects.equals(this.priority, other.priority)
			&& Objects.equals(this.name, other.name)
			&& Objects.equals(this.threadGroup, other.threadGroup);
	}

	/**
	 * Tells whether the given object may be equal to this one, so that a subclass that adds state
	 * can refuse equality with a {@link ThreadDataBean}
	 *
	 * @param other
	 *            the other object
	 * @return true if the other object is a {@link ThreadDataBean}
	 */
	protected boolean canEqual(final Object other)
	{
		return other instanceof ThreadDataBean;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return Objects.hash(this.alive, this.daemon, this.interrupted, this.id, this.priority,
			this.name, this.threadGroup);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ThreadDataBean(alive=" + this.alive + ", daemon=" + this.daemon + ", interrupted="
			+ this.interrupted + ", id=" + this.id + ", name=" + this.name + ", priority="
			+ this.priority + ", threadGroup=" + this.threadGroup + ")";
	}

	/**
	 * The builder of {@link ThreadDataBean}
	 */
	public static class ThreadDataBeanBuilder
	{
		private boolean alive;
		private boolean daemon;
		private boolean interrupted;
		private long id;
		private String name;
		private Integer priority;
		private String threadGroup;

		ThreadDataBeanBuilder()
		{
		}

		/**
		 * Sets whether the thread is alive
		 *
		 * @param alive
		 *            whether the thread is alive
		 * @return this builder
		 */
		public ThreadDataBeanBuilder alive(final boolean alive)
		{
			this.alive = alive;
			return this;
		}

		/**
		 * Sets whether the thread is a daemon thread
		 *
		 * @param daemon
		 *            whether the thread is a daemon thread
		 * @return this builder
		 */
		public ThreadDataBeanBuilder daemon(final boolean daemon)
		{
			this.daemon = daemon;
			return this;
		}

		/**
		 * Sets whether the thread is interrupted
		 *
		 * @param interrupted
		 *            whether the thread is interrupted
		 * @return this builder
		 */
		public ThreadDataBeanBuilder interrupted(final boolean interrupted)
		{
			this.interrupted = interrupted;
			return this;
		}

		/**
		 * Sets the id of the thread
		 *
		 * @param id
		 *            the id of the thread
		 * @return this builder
		 */
		public ThreadDataBeanBuilder id(final long id)
		{
			this.id = id;
			return this;
		}

		/**
		 * Sets the name of the thread
		 *
		 * @param name
		 *            the name of the thread
		 * @return this builder
		 */
		public ThreadDataBeanBuilder name(final String name)
		{
			this.name = name;
			return this;
		}

		/**
		 * Sets the priority of the thread
		 *
		 * @param priority
		 *            the priority of the thread
		 * @return this builder
		 */
		public ThreadDataBeanBuilder priority(final Integer priority)
		{
			this.priority = priority;
			return this;
		}

		/**
		 * Sets the name of the thread group of the thread
		 *
		 * @param threadGroup
		 *            the name of the thread group of the thread
		 * @return this builder
		 */
		public ThreadDataBeanBuilder threadGroup(final String threadGroup)
		{
			this.threadGroup = threadGroup;
			return this;
		}

		/**
		 * Creates a new {@link ThreadDataBean} with the values of this builder
		 *
		 * @return the new {@link ThreadDataBean}
		 */
		public ThreadDataBean build()
		{
			return new ThreadDataBean(this.alive, this.daemon, this.interrupted, this.id, this.name,
				this.priority, this.threadGroup);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			return "ThreadDataBean.ThreadDataBeanBuilder(alive=" + this.alive + ", daemon="
				+ this.daemon + ", interrupted=" + this.interrupted + ", id=" + this.id + ", name="
				+ this.name + ", priority=" + this.priority + ", threadGroup=" + this.threadGroup
				+ ")";
		}
	}
}
