/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/*
 * Wrappable button snippet: create a wrappable button for each button type.
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
public class Snippet345 {
	public static void main (String[] args) {
		final Display  display = new Display ();
		final Shell shell = new Shell ();
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		String string = "The quick brown fox jumped over the lazy brown dog";
		Button button;
		button = new Button(shell, SWT.PUSH | SWT.WRAP);
		button.setText(string);
		button = new Button(shell, SWT.RADIO | SWT.WRAP);
		button.setText(string);
		button = new Button(shell, SWT.TOGGLE | SWT.WRAP);
		button.setText(string);
		button = new Button(shell, SWT.CHECK | SWT.WRAP);
		button.setText(string);
		shell.pack();
		shell.open ();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep ();
			}
		}
		display.dispose ();
	}
}
