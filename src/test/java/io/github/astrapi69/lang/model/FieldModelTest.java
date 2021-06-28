/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.lang.model;

import static org.testng.AssertJUnit.assertNotNull;

import java.util.List;

import javax.lang.model.element.Modifier;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import io.github.astrapi69.collections.list.ListFactory;

/**
 * The unit test class for the class {@link FieldModel}.
 */
public class FieldModelTest
{

	/**
	 * Test method for {@link FieldModel} constructors
	 */
	@Test
	public final void testConstructors()
	{
		FieldModel model = new FieldModel();
		assertNotNull(model);
		List<Modifier> modifiers = ListFactory.newArrayList();
		model = new FieldModel(modifiers);
		assertNotNull(model);
		model = FieldModel.builder().build();
		assertNotNull(model);
	}

	/**
	 * Test method for {@link FieldModel}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(FieldModel.class);
	}

	/**
	 * Test method for {@link FieldModel#equals(Object)} , {@link FieldModel#hashCode()}
	 */
	@Test
	public void equalsContract()
	{
		EqualsVerifier.simple().forClass(FieldModel.class).verify();
	}

}
