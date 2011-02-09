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
 * Scale example snippet: create a scale (maximum 40, page increment 5)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class Snippet45 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Scale scale = new Scale (shell, SWT.BORDER);
	Rectangle clientArea = shell.getClientArea ();
	scale.setBounds (clientArea.x, clientArea.y, 200, 64);
	scale.setMaximum (40);
	scale.setPageIncrement (5);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 
