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
 * Text snippet: type in one text, output to another
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet243 {

public static void main(String [] args) {
	final Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout ());
	final Text text0 = new Text (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	final Text text1 = new Text (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	text0.addVerifyListener (event -> {
		text1.setTopIndex (text0.getTopIndex ());
		text1.setSelection (event.start, event.end);
		text1.insert (event.text);
	});
	shell.setBounds(10, 10, 200, 200);
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}
