/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
 * Text example snippet: resize a text control (show about 10 characters)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet55 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Text text = new Text (shell, SWT.BORDER);
	text.setFont (new Font (display, "Courier", 13, SWT.NORMAL)); // Use a fixed size font
	Rectangle clientArea = shell.getClientArea ();
	text.setLocation (clientArea.x, clientArea.y);
	int columns = 10;
	GC gc = new GC (text);
	FontMetrics fm = gc.getFontMetrics ();
	int width = (int) (columns * fm.getAverageCharacterWidth ());
	int height = fm.getHeight ();
	gc.dispose ();
	text.setSize (text.computeSize (width, height));
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
