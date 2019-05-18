/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
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
 * BIDI example snippet: Set the text direction independent of the widget orientation.
 *
 * Note: This snippet currently only works on Windows.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

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

		new Label(shell, SWT.NONE).setText("with FLIP_TEXT_DIRECTION:");

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
