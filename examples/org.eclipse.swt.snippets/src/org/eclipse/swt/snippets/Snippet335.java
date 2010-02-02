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
 * without expanding it
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
	GridData labelData = new GridData();
	labelData.widthHint = 10;					// <-- default width
	labelData.horizontalAlignment = SWT.FILL;	// <-- grow to fill available width
	label.setLayoutData(labelData);
	label.setText("Snippets are minimal stand-alone programs that demonstrate specific techniques or functionality.");
	new Button(shell, SWT.PUSH).setText("This button determines the Shell's width");

	/* do an initial pack() so that the Shell determines its required width */
	shell.pack();

	/* update the Label's width hint to match what the layout allocated for it */
	labelData.widthHint = label.getBounds().width;
	
	/*
	 * do a second pack() so that the Label will compute its required height
	 * based on its correct width instead of its previously-set default width
	 */
	shell.pack();

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}
