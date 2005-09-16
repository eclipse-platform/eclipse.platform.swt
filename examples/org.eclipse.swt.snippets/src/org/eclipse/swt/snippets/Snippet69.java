/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
 * FormLayout example snippet: center a label and single line text using a form layout
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Snippet69 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Label label = new Label (shell, SWT.NONE | SWT.BORDER);
	label.setText ("Name");
	Text text = new Text (shell, SWT.NONE);

	FormLayout layout = new FormLayout ();
	layout.marginWidth = layout.marginHeight = 5;
	shell.setLayout (layout);

	GC gc = new GC (text);
	FontMetrics fm = gc.getFontMetrics ();
	int charWidth = fm.getAverageCharWidth ();
	int width = text.computeSize (charWidth * 8, SWT.DEFAULT).x;
	gc.dispose ();
	FormData data = new FormData (width, SWT.DEFAULT);
	text.setLayoutData (data);
	data.left = new FormAttachment (label, 5);
	data.top = new FormAttachment (label, 0, SWT.CENTER);
	
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 
