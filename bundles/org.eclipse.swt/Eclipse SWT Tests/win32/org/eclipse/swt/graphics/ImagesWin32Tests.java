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
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class ImagesWin32Tests {

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
