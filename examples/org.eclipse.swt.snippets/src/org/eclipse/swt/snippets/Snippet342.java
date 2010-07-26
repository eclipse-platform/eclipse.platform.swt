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
 * GridLayout snippet: grow/shrink a wrappable Text's height to show its
 * content as it changes
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet342 {

public static void main(String[] args) {
	final int TEXT_WIDTH = 100;
	
	Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setLayout(new GridLayout());
	final Text text = new Text(shell, SWT.MULTI | SWT.WRAP | SWT.BORDER);
	text.setLayoutData(new GridData(TEXT_WIDTH, SWT.DEFAULT));
	text.addListener(SWT.Modify, new Listener() {
		public void handleEvent(Event event) {
			int currentHeight = text.getSize().y;
			int preferredHeight = text.computeSize(TEXT_WIDTH, SWT.DEFAULT).y;
			if (currentHeight != preferredHeight) {
				GridData data = (GridData)text.getLayoutData();
				data.heightHint = preferredHeight;
				shell.pack();
			}
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
