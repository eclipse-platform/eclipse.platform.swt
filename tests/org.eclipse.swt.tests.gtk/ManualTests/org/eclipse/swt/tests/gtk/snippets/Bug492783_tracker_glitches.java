/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tracker;

public class Bug492783_tracker_glitches {

	public static void main (String [] args) {
		Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.open ();
		shell.addListener (SWT.MouseDown, e -> {
			Tracker tracker = new Tracker (display, SWT.NONE);
			tracker.setRectangles (new Rectangle [] {
				new Rectangle (e.x, e.y, 100, 100),
			});
			tracker.open ();
		});
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
