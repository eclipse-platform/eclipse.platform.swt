/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gdip.*;
import org.junit.jupiter.api.*;

class TransformWin32Tests extends Win32AutoscaleTestBase {

	@Test
	public void testShouldHaveDifferentHandlesAtDifferentZoomLevels() {
		int zoom = DPIUtil.getDeviceZoom();
		Transform transform = new Transform(display);
		long scaledHandle = transform.getHandle(zoom * 2);
		assertNotEquals("There should be different handles for different zoom levels", scaledHandle, transform.getHandle(zoom));
		long scaledHandle2 = transform.getHandle(zoom * 3);
		assertNotEquals("There should be different handles for different zoom levels", scaledHandle, scaledHandle2);
	}

	@Test
	public void testScaledTrasformMustHaveScaledValues() {
		int zoom = DPIUtil.getDeviceZoom();
		Transform transform = new Transform(display, 0, 0, 0, 0, 4, 2);
		float[] elements = new float[6];
		transform.getElements(elements);
		long scaledHandle = transform.getHandle(zoom * 2);
		float[] scaledElements = new float[6];
		Gdip.Matrix_GetElements(scaledHandle, scaledElements);
		assertEquals(elements[4] * 2, scaledElements[4], 0);
		assertEquals(elements[5] * 2, scaledElements[5], 0);
	}

}
