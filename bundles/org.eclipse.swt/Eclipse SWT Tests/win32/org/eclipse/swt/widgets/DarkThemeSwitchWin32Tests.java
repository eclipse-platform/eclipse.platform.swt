/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
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
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.PlatformSpecificExecutionExtension;
import org.eclipse.swt.internal.win32.OS;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(PlatformSpecificExecutionExtension.class)
class DarkThemeSwitchWin32Tests {

	private static final String DARK_KEY = "org.eclipse.swt.internal.win32.useDarkModeExplorerTheme";

	private Display display;
	private Shell shell;

	@BeforeEach
	void setUp() {
		display = Display.getDefault();
		assumeTrue(OS.IsDarkModeAvailable());
		shell = new Shell(display);
		awayFromCursor(shell);
	}

	@AfterEach
	void tearDown() {
		if (shell != null) shell.dispose();
		display.setData(DARK_KEY, false);
	}

	@Test
	void buttonsMatchFreshButtonsAfterSwitch() {
		Consumer<Boolean> theme = dark -> display.setData(DARK_KEY, dark);
		for (int style : new int[] { SWT.PUSH, SWT.CHECK, SWT.RADIO }) {
			assertSwitchedMatchesFresh(theme, parent -> {
				Button button = new Button(parent, style);
				button.setText("Button");
				return button;
			});
		}
	}

	/**
	 * Creates a control in one theme, switches to the other and compares it with a
	 * control created in that theme, in both directions.
	 */
	private void assertSwitchedMatchesFresh(Consumer<Boolean> theme, Function<Composite, Control> factory) {
		Canvas focusSink = new Canvas(shell, SWT.NONE);
		focusSink.setBounds(0, 0, 1, 1);
		for (boolean dark : new boolean[] { true, false }) {
			theme.accept(!dark);
			Control switched = factory.apply(shell);
			switched.pack();
			shell.setSize(400, 300);
			shell.open();
			focusSink.forceFocus();
			dispatchEvents();
			theme.accept(dark);
			Control fresh = factory.apply(shell);
			Point size = fresh.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			switched.setBounds(10, 10, size.x, size.y);
			fresh.setBounds(10, size.y + 20, size.x, size.y);
			focusSink.forceFocus();
			dispatchEvents();
			assertEquals(0, differentPixels(switched, fresh, size), "dark=" + dark + " " + switched);
			switched.dispose();
			fresh.dispose();
		}
	}

	/** Keeps controls out of their hot state, which differs between themes. */
	private void awayFromCursor(Shell target) {
		Rectangle area = display.getPrimaryMonitor().getClientArea();
		Point cursor = display.getCursorLocation();
		int x = cursor.x < area.x + area.width / 2 ? area.x + area.width / 2 : area.x;
		int y = cursor.y < area.y + area.height / 2 ? area.y + area.height / 2 : area.y;
		target.setLocation(x, y);
	}

	private void dispatchEvents() {
		while (display.readAndDispatch()) {
			// paint
		}
	}

	private static int differentPixels(Control a, Control b, Point size) {
		ImageData dataA = print(a, size), dataB = print(b, size);
		int count = 0;
		for (int y = 0; y < size.y; y++) {
			for (int x = 0; x < size.x; x++) {
				if (dataA.getPixel(x, y) != dataB.getPixel(x, y)) count++;
			}
		}
		return count;
	}

	private static ImageData print(Control control, Point size) {
		Image image = new Image(control.getDisplay(), size.x, size.y);
		try {
			GC gc = new GC(image);
			try {
				// PrintWindow, which Control.print uses, can return stale content of the shell
				OS.SendMessage(control.handle, OS.WM_PRINT, gc.handle, OS.PRF_NONCLIENT | OS.PRF_CLIENT | OS.PRF_ERASEBKGND);
			} finally {
				gc.dispose();
			}
			return image.getImageData();
		} finally {
			image.dispose();
		}
	}
}
