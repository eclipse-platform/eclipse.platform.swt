/*******************************************************************************
 * Copyright (c) 2025 Yatta and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Transform
 *
 * @see org.eclipse.swt.graphics.Transform
 */
public class Test_org_eclipse_swt_graphics_Transform {
	private Display display;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
	}

	@Test
	public void createTransform() {
		Transform transform = new Transform(display);
		if (transform.isDisposed()) {
			fail("Constructor for Transform didn't initialize");
		}
		transform.dispose();
	}


	@Test
	public void multiplyWithDisposedTransform() {
		Transform transform = new Transform(display);
		transform.translate(10, 10);
		Transform transformDisposed = new Transform(display);
		transformDisposed.translate(20, 20);

		transform.multiply(transformDisposed);
		transformDisposed.dispose();
		float [] elements = new float[6];
		transform.getElements(elements);
		assertEquals(30, elements[4], 0.01);
		transform.dispose();
	}

	@Test
	public void disposeTransform() {
		Transform transform = new Transform(display);
		transform.setElements(2, 3, 4, 5, 6, 7);
		if (transform.isDisposed()) {
			fail("Transform should not be in the disposed state");
		}

		// dispose twice as this is allowed
		for (int i = 0; i < 2; i++) {
			transform.dispose();
			if (!transform.isDisposed()) {
				fail("Transform should be in the disposed state");
			}
		}
	}

	@Test
	public void testToString() {
		Transform transform = new Transform(display);
		String s = transform.toString();

		if (s == null || s.length() == 0) {
			fail("toString returns null or empty string");
		}

		transform.setElements(2, 3, 4, 5, 6, 7);
		s = transform.toString();

		if (s == null || s.length() == 0) {
			fail("toString returns null or empty string");
		}

		transform.dispose();
		s = transform.toString();

		if (s == null || s.length() == 0) {
			fail("toString returns null or empty string");
		}
	}
}
