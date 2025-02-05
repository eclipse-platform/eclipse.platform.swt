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

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
class ImageSmoothScalingWin32Tests {

	private static final String SWT_AUTOSCALE_METHOD = "swt.autoScale.method";
	private static String originalAutoScaleMethod;

	@BeforeAll
	static void setUpAutoScaleMethodSmooth() {
		originalAutoScaleMethod = System.getProperty(SWT_AUTOSCALE_METHOD);
		System.setProperty(SWT_AUTOSCALE_METHOD, "smooth");
	}

	@AfterAll
	static void restoreAutoScaleMethod() {
		if (originalAutoScaleMethod != null) {
			System.setProperty(SWT_AUTOSCALE_METHOD, originalAutoScaleMethod);
		} else {
			System.clearProperty(SWT_AUTOSCALE_METHOD);
		}
	}

	@Test
	public void testImageIconTypeShouldNotChangeAfterCallingGetHandleForDifferentZoom() {
		Image icon = Display.getDefault().getSystemImage(SWT.ICON_ERROR);
		try {
			Image.win32_getHandle(icon, 200);
			assertEquals("Image type should stay to SWT.ICON", SWT.ICON, icon.type);
		} finally {
			icon.dispose();
		}
	}
}
