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
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class TransformWin32Tests {

	@Test
	public void testShouldHaveDifferentHandlesAtDifferentZoomLevels() {
		Display display = Display.getDefault();
		int zoom = 100;
		Transform transform = new Transform(display);
		long scaledHandle = transform.getHandle(zoom * 2);
		assertNotEquals("There should be different handles for different zoom levels", scaledHandle, transform.getHandle(zoom));
		long scaledHandle2 = transform.getHandle(zoom * 3);
		assertNotEquals("There should be different handles for different zoom levels", scaledHandle, scaledHandle2);
	}

	@Test
	public void testScaledTrasformMustHaveScaledValues() {
		Display display = Display.getDefault();
		Transform transform = new Transform(display, 0, 0, 0, 0, 4, 2);
		float[] elements = new float[6];
		transform.getElements(elements);

		long handle200 = transform.getHandle(200);
		float[] scaledElements200 = new float[6];
		Gdip.Matrix_GetElements(handle200, scaledElements200);
		assertEquals(elements[4] * 2, scaledElements200[4], 0);
		assertEquals(elements[5] * 2, scaledElements200[5], 0);

		long handle300 = transform.getHandle(300);
		float[] scaledElements300 = new float[6];
		Gdip.Matrix_GetElements(handle300, scaledElements300);
		assertEquals(elements[4] * 3, scaledElements300[4], 0);
		assertEquals(elements[5] * 3, scaledElements300[5], 0);
	}

}
