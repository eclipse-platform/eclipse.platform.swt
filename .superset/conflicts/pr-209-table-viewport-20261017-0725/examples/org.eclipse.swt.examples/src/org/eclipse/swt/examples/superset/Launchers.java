/*******************************************************************************
 * Copyright (c) 2026 Hitesh Soliwal and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TaskItem;
import org.eclipse.swt.widgets.Tracker;
import org.eclipse.swt.widgets.TrayItem;

/**
 * Opens, on a user click only, the widgets the matrix runner never drives:
 * modal dialogs (DIALOG), the Tracker loop (MODAL) and desktop side effects
 * (SIDE_EFFECT: tray item, taskbar progress, Nebula Notifier).
 */
final class Launchers {

	private Launchers() {
	}

	static boolean supports(DemoSpec spec) {
		return switch (spec.kind()) {
			case "DIALOG", "MODAL", "SIDE_EFFECT" -> true;
			default -> false;
		};
	}

	static void open(DemoSpec spec, Shell shell) {
		switch (spec.simpleName()) {
			case "Tracker" -> tracker(shell);
			case "Tray" -> tray(shell);
			case "TaskBar" -> taskBar(shell);
			case "Notifier" -> Reflect.callStatic(spec.widgetClass(), "notify", "Superset showcase", "Nebula Notifier toast");
			case "PreferenceWindow" -> preferences(spec, shell);
			case "LoginDialog" -> Reflect.call(Reflect.make(spec.widgetClass()), "open");
			case "TipOfTheDay" -> tips(spec, shell);
			default -> swtDialog(spec, shell);
		}
	}

	private static void swtDialog(DemoSpec spec, Shell shell) {
		Object dialog = Reflect.make(spec.widgetClass(), shell, "MessageBox".equals(spec.simpleName()) ? SWT.ICON_INFORMATION | SWT.OK : SWT.NONE);
		if ("MessageBox".equals(spec.simpleName())) Reflect.call(dialog, "setMessage", "SWT MessageBox from the superset showcase");
		Reflect.call(dialog, "open");
	}

	private static void tracker(Shell shell) {
		Tracker tracker = new Tracker(shell.getDisplay(), SWT.RESIZE);
		Rectangle b = shell.getBounds();
		tracker.setRectangles(new Rectangle[] {new Rectangle(b.x + 100, b.y + 100, 200, 120)});
		tracker.open();
		tracker.dispose();
	}

	private static void tray(Shell shell) {
		if (shell.getDisplay().getSystemTray() == null) return;
		TrayItem item = new TrayItem(shell.getDisplay().getSystemTray(), SWT.NONE);
		item.setToolTipText("SWT superset showcase TrayItem (disposed in 10 s)");
		item.setImage(shell.getDisplay().getSystemImage(SWT.ICON_INFORMATION));
		shell.getDisplay().timerExec(10_000, item::dispose);
	}

	private static void taskBar(Shell shell) {
		if (shell.getDisplay().getSystemTaskBar() == null) return;
		TaskItem item = shell.getDisplay().getSystemTaskBar().getItem(shell);
		if (item == null) return;
		item.setProgressState(SWT.NORMAL);
		item.setProgress(42);
		shell.getDisplay().timerExec(10_000, () -> item.setProgressState(SWT.DEFAULT));
	}

	private static void preferences(DemoSpec spec, Shell shell) {
		Object window = Reflect.callStatic(spec.widgetClass(), "create", shell, new java.util.HashMap<String, Object>());
		Reflect.call(window, "open");
	}

	private static void tips(DemoSpec spec, Shell shell) {
		Object tips = Reflect.make(spec.widgetClass());
		Reflect.call(tips, "addTip", "Every concept of the superset catalog has a tab in this showcase.");
		Reflect.call(tips, "open", shell);
	}
}
