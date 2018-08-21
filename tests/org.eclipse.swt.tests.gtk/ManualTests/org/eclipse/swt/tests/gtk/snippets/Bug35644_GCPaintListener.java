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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;


public class Bug35644_GCPaintListener {
	
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		
		shell.addListener(SWT.Paint, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (e.x == 0) return;
				GC gc1 = new GC(shell);
				gc1.drawLine(0, 0, 2000, 2000);
				gc1.dispose();
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}

}
