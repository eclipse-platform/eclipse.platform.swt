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
 * Display snippet: map from control-relative to display-relative coordinates
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet276 {

public static void main (String[] args) {
	final Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setBounds (200, 200, 400, 400);
	Label label = new Label (shell, SWT.NONE);
	label.setText ("click in shell to print display-relative coordinate");
	Listener listener = event -> {
		Point point = new Point (event.x, event.y);
		System.out.println (display.map ((Control)event.widget, null, point));
	};
	shell.addListener (SWT.MouseDown, listener);
	label.addListener (SWT.MouseDown, listener);
	Rectangle clientArea = shell.getClientArea();
	label.setLocation(clientArea.x, clientArea.y);
	label.pack ();
	shell.open ();
	while (!shell.isDisposed ()){
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

}
