/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * LineAttributes example snippet: draw 2 polylines with different line attributes.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet252 {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);

		shell.addListener(SWT.Paint, event -> {
			GC gc = event.gc;

			gc.setLineAttributes(new LineAttributes(10, SWT.CAP_FLAT, SWT.JOIN_MITER, SWT.LINE_SOLID, null, 0, 10));
			gc.drawPolyline(new int[]{50, 100, 50, 20, 60, 30, 50, 45});

			gc.setLineAttributes(new LineAttributes(1/2f, SWT.CAP_FLAT, SWT.JOIN_MITER, SWT.LINE_DOT, null, 0, 10));
			gc.drawPolyline(new int[]{100, 100, 100, 20, 110, 30, 100, 45});
		});

		shell.setSize(150, 150);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
