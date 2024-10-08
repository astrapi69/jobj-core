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
package io.github.astrapi69.lang.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.Configuration;
import org.meanbean.test.ConfigurationBuilder;

import io.github.astrapi69.collection.array.ArrayFactory;
import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * The unit test class for the class {@link PropertiesKeyAndParameters}.
 */
public class PropertiesKeyAndParametersTest
{

	/**
	 * Test method for {@link PropertiesKeyAndParameters} constructors
	 */
	@Test
	public final void testConstructors()
	{
		PropertiesKeyAndParameters model = new PropertiesKeyAndParameters();
		assertNotNull(model);
		/** The key. */
		String key = "";
		/** The parameters. */
		Object[] parameters = ArrayFactory.newArray("foo", "bar");

		model = new PropertiesKeyAndParameters(key, parameters);
		assertNotNull(model);
		model = PropertiesKeyAndParameters.builder().build();
		assertNotNull(model);
	}

	/**
	 * Test method for {@link PropertiesKeyAndParameters#equals(Object)} ,
	 * {@link PropertiesKeyAndParameters#hashCode()}
	 */
	@Test
	public void equalsContract()
	{
		EqualsVerifier.simple().forClass(PropertiesKeyAndParameters.class).verify();
	}

	/**
	 * Test method for {@link PropertiesKeyAndParameters}
	 */
	@Test
	public void testWithBeanTester()
	{
		final Configuration configuration = new ConfigurationBuilder()
			.overrideFactory("parameters", new Factory<Object[]>()
			{
				@Override
				public Object[] create()
				{
					return new Object[] { "foo", "bar", "john", "doe" };
				}
			}).build();
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(PropertiesKeyAndParameters.class, configuration);
	}
}
