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
 * StackLayout example snippet: use a StackLayout to switch between Composites.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet249 {

	static int pageNum = -1;

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Rectangle clientArea = shell.getClientArea();
	shell.setBounds (clientArea.x + 10, clientArea.y + 10, 300, 200);
	// create the composite that the pages will share
	final Composite contentPanel = new Composite (shell, SWT.BORDER);
	contentPanel.setBounds (clientArea.x + 100, clientArea.y + 10, 190, 90);
	final StackLayout layout = new StackLayout ();
	contentPanel.setLayout (layout);

	// create the first page's content
	final Composite page0 = new Composite (contentPanel, SWT.NONE);
	page0.setLayout (new RowLayout ());
	Label label = new Label (page0, SWT.NONE);
	label.setText ("Label on page 1");
	label.pack ();

	// create the second page's content	
	final Composite page1 = new Composite (contentPanel, SWT.NONE);
	page1.setLayout (new RowLayout ());
	Button button = new Button (page1, SWT.NONE);
	button.setText ("Button on page 2");
	button.pack ();

	// create the button that will switch between the pages
	Button pageButton = new Button (shell, SWT.PUSH);
	pageButton.setText ("Push");
	pageButton.setBounds (clientArea.x + 10, clientArea.y + 10, 80, 25);
	pageButton.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event event) {
			pageNum = ++pageNum % 2;
			layout.topControl = pageNum == 0 ? page0 : page1;
			contentPanel.layout ();
		}
	});

	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 
