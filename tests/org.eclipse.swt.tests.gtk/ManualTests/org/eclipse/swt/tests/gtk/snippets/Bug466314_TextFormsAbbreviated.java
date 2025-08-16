package org.eclipse.swt.tests.gtk.snippets;
/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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


/*
 * GridLayout snippet: grow/shrink a wrappable Text's height to show its
 * content as it changes
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug466314_TextFormsAbbreviated {

public static void main(String[] args) {
	final int TEXT_WIDTH = 100;

	Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setLayout(new GridLayout());
	final Text text = new Text(shell, SWT.MULTI | SWT.WRAP | SWT.BORDER);
	text.setLayoutData(new GridData(TEXT_WIDTH, 10));
	text.setText("Eclipse very good IDE to develop multiple RCP apps and other products");
	text.addListener(SWT.Modify, event -> {
		int currentHeight = text.getSize().y;
		int preferredHeight = text.computeSize(TEXT_WIDTH, SWT.DEFAULT).y;
		if (currentHeight != preferredHeight) {
			GridData data = (GridData) text.getLayoutData();
			data.heightHint = preferredHeight;
			shell.pack();
		}
	});
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}