/*******************************************************************************
 * Copyright (c) 2013, 2016 Google Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Google Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.watchdog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class WatchdogPlugin extends AbstractUIPlugin implements IStartup {
	private static final int EVENT_DURATION_THRESHOLD = 1001;

	private static WatchdogPlugin plugin;
	private final Display display;
	private final TimedEventWatchdog watchdog;

	public WatchdogPlugin() {
		super();
		plugin = this;

		IWorkbench workbench = getWorkbench();
		display = (workbench != null) ? workbench.getDisplay() : null;

		Thread displayThread = (display != null) ? display.getThread() : null;
		this.watchdog = displayThread != null
				? new TimedEventWatchdog(displayThread, EVENT_DURATION_THRESHOLD)
				: null;
	}

	@Override
	public void earlyStartup() {
		if (display != null) {
			display.asyncExec(() -> {
				display.addListener(SWT.PreEvent, watchdog);
				display.addListener(SWT.PostEvent, watchdog);
			});
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		try {
			if (display != null && !display.isDisposed()) {
				display.asyncExec(() -> {
					if (!display.isDisposed()) {
						display.removeListener(SWT.PreEvent, watchdog);
						display.removeListener(SWT.PostEvent, watchdog);
					}
				});
			}
		} finally {
			super.stop(context);
		}
	}

	public static WatchdogPlugin getDefault() {
		return plugin;
	}
}
