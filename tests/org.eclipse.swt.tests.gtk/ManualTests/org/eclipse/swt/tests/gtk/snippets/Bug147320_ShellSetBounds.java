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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Bug147320_ShellSetBounds {

	
		static final Rectangle[] DIMENSIONS = {
			new Rectangle (520, 367, 240, 290),
			new Rectangle (520, 320, 240, 320),
			new Rectangle (320, 272, 640, 480),
			new Rectangle (240, 212, 800, 600)};
		static int counter = 0;
		public static void main(String[] args) {
		    Display display = new Display();
		    final Shell shell = new Shell(display, SWT.NONE);
		    Button button = new Button(shell, SWT.PUSH);
		    button.setBounds(10,10,50,30);
		    button.setText("Push");
		    button.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event event) {
					Rectangle bounds = DIMENSIONS[counter++];
					if (counter == 4) counter = 0;
					System.out.println("setting to: " + bounds);
					shell.setBounds(bounds);
				}
			});
		    shell.setBounds(DIMENSIONS[counter++]);
		    shell.open();
		    while (!shell.isDisposed()) {
		        if (!display.readAndDispatch()) display.sleep();
		    }
		    display.dispose();
		}
}
