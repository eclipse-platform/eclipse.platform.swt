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
package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Taskbar icon of a shell whose UI thread does not respond, see
 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/2037.
 * <p>
 * The Windows taskbar asks the UI thread of a window for its icon. If the thread
 * does not answer in time, for example because the application starts up or
 * rescales its UI, the taskbar falls back to the icon of the window class and does
 * not ask again. Each scenario is run on its own, by passing its name as program
 * argument, and gets a taskbar button of its own. Its shell shows the red icon of
 * this snippet, or blue for a second shell, and the taskbar button must show the
 * same icon instead of the generic application icon, also after the shell has
 * become responsive:
 * <ul>
 * <li><code>idleBeforeOpen</code>: the UI thread does not read messages for 6 s
 * before the shell is opened, as in a long application startup.</li>
 * <li><code>idleBeforeSetVisible</code>: the same, with the shell shown by
 * <code>setVisible(true)</code>.</li>
 * <li><code>busyAfterOpen</code>: the UI thread does not read messages for 6 s
 * right after the shell is opened.</li>
 * <li><code>busyAfterSetVisible</code>: the same, with the shell shown by
 * <code>setVisible(true)</code>.</li>
 * <li><code>maximizedWithSlowLayout</code>: a maximized shell whose layout takes
 * 3 s, as a large UI, which runs while the shell is shown.</li>
 * <li><code>twoShellsIdleBeforeOpen</code>: two top-level shells with different
 * icons, opened after 6 s without reading messages. Hovering the taskbar button
 * shows each shell with its own icon.</li>
 * <li><code>noImage</code>: one shell without an icon and one whose icon is
 * removed after 2 s. Both show the generic application icon, as before.</li>
 * <li><code>zoomChange</code>: the UI thread is blocked for 3 s on every zoom
 * change, as a large application rescaling its UI. Change the scaling of the
 * monitor with the taskbar in the Windows settings, or move the shell to a monitor
 * with another zoom, and run it with and without
 * <code>-Dswt.autoScale.updateOnRuntime=true</code>.</li>
 * </ul>
 */
public class SWTIssue2037_TaskbarIcon {

	private static final int[] ICON_SIZES = { 16, 24, 32, 48, 64, 128 };

	public static void main(String[] args) {
		String scenario = args.length > 0 ? args[0] : "";
		Display.setAppName("SWTIssue2037 " + scenario);
		Display display = new Display();
		Image[] red = createImages(display, SWT.COLOR_RED);
		Image[] blue = createImages(display, SWT.COLOR_BLUE);
		Shell shell = createShell(display, scenario, red);
		switch (scenario) {
		case "idleBeforeOpen" -> {
			sleep(6000);
			shell.open();
		}
		case "idleBeforeSetVisible" -> {
			sleep(6000);
			shell.setVisible(true);
		}
		case "busyAfterOpen" -> {
			shell.open();
			sleep(6000);
		}
		case "busyAfterSetVisible" -> {
			shell.setVisible(true);
			sleep(6000);
		}
		case "maximizedWithSlowLayout" -> {
			shell.setMaximized(true);
			shell.addListener(SWT.Resize, e -> sleep(3000));
			shell.open();
		}
		case "twoShellsIdleBeforeOpen" -> {
			Shell secondShell = createShell(display, scenario + " (second)", blue);
			secondShell.setLocation(secondShell.getLocation().x + 100, secondShell.getLocation().y + 100);
			sleep(6000);
			shell.open();
			secondShell.open();
		}
		case "noImage" -> {
			Shell shellWithoutImage = new Shell(display);
			shellWithoutImage.setText(scenario + " (never had an image)");
			shellWithoutImage.setSize(400, 150);
			shell.open();
			shellWithoutImage.open();
			display.timerExec(2000, () -> shell.setImage(null));
		}
		case "zoomChange" -> {
			shell.addListener(SWT.ZoomChanged, e -> sleep(3000));
			shell.open();
		}
		default -> {
			System.out.println("Pass one of the scenarios as argument: idleBeforeOpen, idleBeforeSetVisible, "
					+ "busyAfterOpen, busyAfterSetVisible, maximizedWithSlowLayout, twoShellsIdleBeforeOpen, noImage, zoomChange");
			shell.dispose();
		}
		}
		while (display.getShells().length > 0) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		for (Image image : red) {
			image.dispose();
		}
		for (Image image : blue) {
			image.dispose();
		}
		display.dispose();
	}

	private static Shell createShell(Display display, String title, Image[] images) {
		Shell shell = new Shell(display);
		shell.setText(title);
		shell.setSize(400, 150);
		shell.setImages(images);
		return shell;
	}

	private static Image[] createImages(Display display, int color) {
		Image[] images = new Image[ICON_SIZES.length];
		for (int i = 0; i < ICON_SIZES.length; i++) {
			int size = ICON_SIZES[i];
			images[i] = new Image(display, size, size);
			GC gc = new GC(images[i]);
			gc.setBackground(display.getSystemColor(color));
			gc.fillRectangle(0, 0, size, size);
			gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
			gc.fillOval(size / 5, size / 5, size * 3 / 5, size * 3 / 5);
			gc.dispose();
		}
		return images;
	}

	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
