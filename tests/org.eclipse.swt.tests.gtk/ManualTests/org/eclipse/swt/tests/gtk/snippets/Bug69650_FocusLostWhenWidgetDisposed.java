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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug69650_FocusLostWhenWidgetDisposed {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		display.addFilter(SWT.KeyDown, new Listener() {
			int count = 0;
			@Override
			public void handleEvent(Event event) {
				System.out.println("Got a key down: " + count);
				count++;
			}
		});
		Text text = new Text(shell, SWT.BORDER);
		text.setBounds(20, 20, 200, 40);
		
		final Text text2 = new Text(shell, SWT.BORDER);
		text2.setBounds(80, 80, 200, 40);
		text2.addListener(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("ok, here goes");
				text2.dispose();
			}
		});
		
		shell.open();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
