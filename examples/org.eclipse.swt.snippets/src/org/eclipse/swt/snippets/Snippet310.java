/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Spinner example snippet: example on how to validates the current input in
 * a Spinner control. 
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.5
 */

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet310 {
	public static void main (String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		final Spinner spinner = new Spinner (shell, SWT.BORDER);
		spinner.setValues(0, -100, 100, 0, 1, 10);
		spinner.setLayoutData(new GridData(200, SWT.DEFAULT));
		final ToolTip toolTip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_WARNING);
		spinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String string = spinner.getText();
				String message = null;
				try {
					int value = Integer.parseInt(string);
					int maximum = spinner.getMaximum();
					int minimum = spinner.getMinimum();
					if (value > maximum) {
						message = "Current input is greater than the maximum limit ("+maximum+")";
					} else if (value < minimum) {
						message = "Current input is less than the minimum limit ("+minimum+")";
					}
				} catch (Exception ex) {
					message = "Current input is not numeric";
				}
				if (message != null) {
					spinner.setForeground(display.getSystemColor(SWT.COLOR_RED));
					Rectangle rect = spinner.getBounds();
					GC gc = new GC(spinner);
					Point pt = gc.textExtent(string);
					gc.dispose();
					toolTip.setLocation(display.map(shell, null, rect.x + pt.x, rect.y + rect.height));
					toolTip.setMessage(message);
					toolTip.setVisible(true);
				} else {
					toolTip.setVisible(false);
					spinner.setForeground(null);
				}
			}
		});
		shell.setSize(300, 100);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
