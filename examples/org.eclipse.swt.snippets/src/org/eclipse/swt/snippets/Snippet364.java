/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
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
 * BIDI example snippet: Set the text direction independent of the widget orientation.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Snippet364 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));

		Image i = new Image(display, Snippet364.class.getResourceAsStream("eclipse.png"));
		Button b = new Button(shell, SWT.PUSH | SWT.LEFT_TO_RIGHT);
		b.setText("Button LEFT_TO_RIGHT...");
		b.setImage(i);
		
		Button b2 = new Button(shell, SWT.PUSH | SWT.RIGHT_TO_LEFT);
		b2.setText("Button RIGHT_TO_LEFT...");
		b2.setImage(i);

		Button b3 = new Button(shell, SWT.PUSH | SWT.LEFT_TO_RIGHT | SWT.FLIP_TEXT_DIRECTION);
		b3.setText("Button LEFT_TO_RIGHT...");
		b3.setImage(i);
		
		Button b4 = new Button(shell, SWT.PUSH | SWT.RIGHT_TO_LEFT | SWT.FLIP_TEXT_DIRECTION);
		b4.setText("Button RIGHT_TO_LEFT...");
		b4.setImage(i);
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		i.dispose();
	}

}
