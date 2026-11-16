/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

/**
 * Automated tests for class org.eclipse.swt.widgets.Shell for Windows specific
 * behavior
 *
 * @see org.eclipse.swt.widgets.Shell
 */
@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class ShellWin32Tests {

	private Display display;
	private Shell shell;
	private final java.util.List<RuntimeException> reportedExceptions = new ArrayList<>();

	@BeforeEach
	void setUp() {
		display = Display.getDefault();
		display.setRuntimeExceptionHandler(reportedExceptions::add);
		shell = new Shell(display, SWT.NO_TRIM);
	}

	/**
	 * Setting a region resizes the shell, which can move it to a monitor with a
	 * different zoom, so that the operating system processes the zoom change
	 * while the region is being set. Replacing a region by disposing the previous
	 * one before passing the new one must not fail in that case, as reported in
	 * <a href=
	 * "https://github.com/eclipse-platform/eclipse.platform.swt/issues/3857">issue
	 * #3857</a>.
	 */
	@Test
	void testReplaceRegionWhileResizeChangesZoom() {
		Region previousRegion = createRegion(50, 50);
		shell.setRegion(previousRegion);
		int newZoom = shell.nativeZoom * 2;
		boolean[] zoomChangeTriggered = { false };
		shell.addListener(SWT.Resize, event -> {
			if (!zoomChangeTriggered[0]) {
				zoomChangeTriggered[0] = true;
				DPITestUtil.changeDPIZoomOnMonitorChange(shell, newZoom);
			}
		});

		previousRegion.dispose();
		Region newRegion = createRegion(80, 60);
		try {
			shell.setRegion(newRegion);

			assertTrue(zoomChangeTriggered[0], "Setting the region did not resize the shell");
			assertEquals(newZoom, shell.getAutoscalingZoom(), "Shell was not adapted to the new zoom");
			assertEquals(0, reportedExceptions.size(),
					"Unexpected failures reported to the display: " + reportedExceptions);
			assertSame(newRegion, shell.getRegion());
			Rectangle expectedBoundsInPixels = Win32DPIUtils.pointToPixel(newRegion.getBounds(), newZoom);
			assertEquals(expectedBoundsInPixels, getWindowRegionBoundsInPixels(shell),
					"Window region does not match the new region at the new zoom");
		} finally {
			newRegion.dispose();
		}
	}

	@Test
	void testSetDisposedRegion() {
		Region region = createRegion(50, 50);
		region.dispose();

		assertThrows(IllegalArgumentException.class, () -> shell.setRegion(region));
	}

	private Region createRegion(int width, int height) {
		Region region = new Region(display);
		region.add(new Rectangle(0, 0, width, height));
		return region;
	}

	private static Rectangle getWindowRegionBoundsInPixels(Shell shell) {
		long windowRegion = OS.CreateRectRgn(0, 0, 0, 0);
		try {
			OS.GetWindowRgn(shell.handle, windowRegion);
			RECT rect = new RECT();
			OS.GetRgnBox(windowRegion, rect);
			return new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
		} finally {
			OS.DeleteObject(windowRegion);
		}
	}

}
