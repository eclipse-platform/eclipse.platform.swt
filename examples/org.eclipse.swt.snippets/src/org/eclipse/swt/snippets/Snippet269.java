/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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
 * Combo example snippet: set the caret position within a Combo's text
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class Snippet269 {

public static void main(String[] args) {
	final Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	Combo combo = new Combo (shell, SWT.NONE);
	combo.add ("item");
	combo.select (0);
	shell.pack ();
	shell.open ();
	int stringLength = combo.getText ().length ();
	combo.setSelection (new Point (stringLength, stringLength));
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
