/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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
 * Tool tip snippet: show a Label's tool tip iff it's not fully visible
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet278 {

public static void main (String [] args) {
	final Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setBounds (10, 10, 300, 100);
	shell.setLayout (new FillLayout ());
	final Label label = new Label (shell, SWT.NONE);
	label.setText ("resize the Shell then hover over this Label");
	label.addListener (SWT.MouseEnter, new Listener () {
		public void handleEvent (Event event) {
			/* 
			 * Map Rectangles to Display coordinates to ensure that
			 * right-to-left contexts will work as well.
			 */
			Composite parent = label.getParent ();
			Point requiredLabelSize = label.computeSize (SWT.DEFAULT, SWT.DEFAULT);
			Point labelLoc = label.getLocation ();
			Rectangle requiredRect = new Rectangle (labelLoc.x, labelLoc.y, requiredLabelSize.x, requiredLabelSize.y);
			requiredRect = display.map (parent, null, requiredRect);
			Rectangle availableRect = parent.getClientArea ();
			availableRect = display.map (parent, null, availableRect);
			boolean isFullyVisible = requiredRect.equals (availableRect.intersection (requiredRect));
			System.out.println ("Label is fully visible: " + isFullyVisible);
			label.setToolTipText (isFullyVisible ? null : label.getText ());
		}
	});
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

}
