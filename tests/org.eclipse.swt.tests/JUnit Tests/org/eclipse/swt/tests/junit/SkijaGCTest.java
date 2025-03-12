/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.eclipse.swt.graphics.NativeGC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.SkijaGC;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;

public class SkijaGCTest {

	@Test
	public void textExtent() {
		NativeGC nativeGC = new NativeGC(Display.getDefault());
		SkijaGC gc = SkijaGC.createDefaultInstance(nativeGC);
		Point extentA = gc.textExtent("A");
		assertEquals(gc.getFontMetrics().getHeight(), extentA.y);
		Point extentAwhitespace = gc.textExtent("A ");
		assertNotEquals(extentA.x, extentAwhitespace.x);
		assertEquals(gc.getFontMetrics().getHeight(), extentAwhitespace.y);
		Point extentAwhitespaceA = gc.textExtent("A A");
		assertNotEquals(extentAwhitespace.x, extentAwhitespaceA.x);
		assertEquals(gc.getFontMetrics().getHeight(), extentAwhitespaceA.y);
		gc.dispose();
		nativeGC.dispose();
	}

}
