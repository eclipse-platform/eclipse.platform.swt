/*******************************************************************************
 * Copyright (c) 2019 Red Hat and others.
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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug507020_WaylandIconsDoubleScaled {

	public static void main (String [] args) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setText("Bug507020: Wayland Double Scaling");
		shell.setLayout (new GridLayout (1, false));

		new Label (shell, SWT.NONE).setText ("1. Canvas\n(PaintListener)");
		final Point size = new Point (100, 40);
		final Canvas canvas = new Canvas (shell, SWT.NONE);
		canvas.addPaintListener (e -> {
			Point size1 = canvas.getSize ();
			paintGradient (e.gc, size1);
		});
		GridData gridData = new GridData (size.x, size.y);
		gridData.horizontalSpan = 2;
		canvas.setLayoutData (gridData);

		new Label (shell, SWT.NONE).setText ("5. 50x50 box\n(Display#getDPI(): " + display.getDPI().x + ")");
		Label box= new Label (shell, SWT.NONE);
		box.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		box.setLayoutData (new GridData (50, 50));

		shell.pack ();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

	private static void paintGradient (GC gc, Point size) {
		// Minimal gradient example
		gc.setBackground (gc.getDevice ().getSystemColor (SWT.COLOR_LIST_SELECTION));
		gc.fillGradientRectangle(0, 0, size.x, size.y, true);
	}
}
