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

/*
 * GridLayout snippet: show a wrappable Label that fills available width
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet335 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new GridLayout());

	Label label = new Label(shell, SWT.WRAP | SWT.BORDER);
	GridData data = new GridData();
	data.widthHint = 10;					// <-- default width
	data.horizontalAlignment = SWT.FILL;	// <-- grow to fill available width
	label.setLayoutData(data);
	label.setText("Snippets are minimal stand-alone programs that demonstrate specific techniques or functionality.");

	new Button(shell, SWT.PUSH).setText("This button determines the Shell's width");
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}
