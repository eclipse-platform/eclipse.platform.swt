/*******************************************************************************
 * Copyright (c) 2020 Paul Pazderski and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Paul Pazderski - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32.snippets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Manual test to check a bunch of uncommon combinations of shell open/hide and
 * setFullscreen.
 * Unfortunately not stable enough for an automated test and not working on Linux.
 */
public class Bug512629_test_fullscreen_before_open {

	Shell shell;
	Shell testShell;

	@Before
	public void setUp() {
		shell = new Shell();
	}

	@After
	public void tearDown() {
		shell.getDisplay().dispose();
	}

	private void createShell(int shellStyle) {
		if (testShell != null) {
			testShell.dispose();
		}
		testShell = new Shell(shell, shellStyle);
		testShell.setSize(100,300);
		testShell.setText("Shell");
		testShell.setLayout(new FillLayout());
	}

	@Test
	public void test_setFullscreen() {
		Function<Shell, Boolean> looksLikeNormalWindow = s -> {
			Rectangle monitorRect = s.getMonitor().getClientArea();
			Rectangle shellSize = s.getClientArea();
			return shellSize.width < monitorRect.width * 0.8 && shellSize.height < monitorRect.height * 0.8;
		};
		Function<Shell, Boolean> looksLikeMaximizedWindow = s -> {
			Rectangle monitorRect = s.getMonitor().getClientArea();
			Rectangle shellSize = s.getClientArea();
			return shellSize.width > monitorRect.width * 0.8 && shellSize.height > monitorRect.height * 0.8
					&& shellSize.width <= monitorRect.width && shellSize.height <= monitorRect.height;
		};
		Function<Shell, Boolean> looksLikeFullscreen = s -> {
			Rectangle monitorRect = s.getMonitor().getClientArea();
			Rectangle shellSize = s.getClientArea();
			return shellSize.width >= monitorRect.width && shellSize.height >= monitorRect.height;
		};

		Function<Shell, Boolean> hasTrimming = s -> {
			Rectangle clientArea = s.getClientArea();
			Point size = s.getSize();
			return s.isVisible() && size.y - clientArea.height > 10;
		};

		Runnable setFullscreenBeforeOpen = () -> {
			testShell.setFullScreen(true);
			testShell.open();
		};
		Runnable setFullscreenAfterOpen = () -> {
			testShell.open();
			testShell.setFullScreen(true);
		};
		Runnable openHideSetFullscreenOpen = () -> {
			testShell.open();
			testShell.setVisible(false);
			testShell.setFullScreen(true);
			testShell.setVisible(true);
		};
		Runnable oscillateFullscreenBeforeOpen = () -> {
			testShell.setFullScreen(true);
			testShell.setFullScreen(false);
			testShell.setFullScreen(true);
			testShell.open();
		};
		Runnable toggleFullscreenWhileHidden = () -> {
			testShell.open();
			testShell.setFullScreen(true);
			testShell.setVisible(false);
			testShell.setFullScreen(false);
			testShell.setVisible(true);
			testShell.setFullScreen(true);
		};

		for (Runnable setFullscreenAndOpen : new Runnable[] { setFullscreenBeforeOpen, setFullscreenAfterOpen,
				openHideSetFullscreenOpen, oscillateFullscreenBeforeOpen, toggleFullscreenWhileHidden }) {
			for (int shellStyle : new int[] { SWT.SHELL_TRIM, SWT.DIALOG_TRIM, SWT.NO_TRIM, SWT.BORDER }) {
				createShell(shellStyle);
				setFullscreenAndOpen.run();
				assertTrue("Not fullscreen.", looksLikeFullscreen.apply(testShell));
				assertFalse("Not fullscreen.", hasTrimming.apply(testShell));
				assertTrue(testShell.getFullScreen());
				assertFalse(testShell.getMaximized());
				testShell.setFullScreen(false);
				assertTrue("Window not restored.", looksLikeNormalWindow.apply(testShell));
				assertTrue(hasTrimming.apply(testShell) == ((shellStyle & SWT.TITLE) != 0));
				assertFalse(testShell.getFullScreen());
				assertFalse(testShell.getMaximized());

				createShell(shellStyle);
				testShell.setMaximized(true);
				assertTrue(testShell.getMaximized());
				setFullscreenAndOpen.run();
				assertTrue("Not fullscreen.", looksLikeFullscreen.apply(testShell));
				assertFalse("Not fullscreen.", hasTrimming.apply(testShell));
				assertTrue(testShell.getFullScreen());
				assertFalse(testShell.getMaximized());
				testShell.setFullScreen(false);
				assertTrue("Not maximized.", looksLikeMaximizedWindow.apply(testShell));
				assertTrue(hasTrimming.apply(testShell) == ((shellStyle & SWT.TITLE) != 0));
				assertFalse(testShell.getFullScreen());
				assertTrue(testShell.getMaximized());
			}
		}

		createShell(SWT.SHELL_TRIM);
		testShell.setFullScreen(false);
		testShell.open();
		assertTrue("Window not restored.", looksLikeNormalWindow.apply(testShell));
		assertTrue("Lost trimming.", hasTrimming.apply(testShell));
		assertFalse(testShell.getFullScreen());
		assertFalse(testShell.getMaximized());
	}
}

