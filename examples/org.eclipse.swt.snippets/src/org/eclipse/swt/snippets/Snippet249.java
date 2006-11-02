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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet249 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setBounds (10, 10, 300, 200);
	// create the composite that the pages will share
	final Composite contentPanel = new Composite (shell, SWT.BORDER);
	contentPanel.setBounds (100, 10, 190, 90);
	final StackLayout layout = new StackLayout ();
	contentPanel.setLayout (layout);

	// create the first page's content
	final Composite page1 = new Composite (contentPanel, SWT.NONE);
	page1.setLayout (new RowLayout ());
	Label label = new Label (page1, SWT.NONE);
	label.setText ("Label on page 1");
	label.pack ();

	// create the second page's content	
	final Composite page2 = new Composite (contentPanel, SWT.NONE);
	page2.setLayout (new RowLayout ());
	Button button = new Button (page2, SWT.NONE);
	button.setText ("Button on page 2");
	button.pack ();

	// create the buttons that will switch between the pages
	Button page1button = new Button (shell, SWT.PUSH);
	page1button.setText ("Page 1");
	page1button.setBounds (10, 10, 80, 25);
	page1button.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event event) {
			layout.topControl = page1;
			contentPanel.layout ();
		}
	});
	Button page2button = new Button (shell, SWT.PUSH);
	page2button.setText ("Page 2");
	page2button.setBounds (10, 40, 80, 25);
	page2button.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			layout.topControl = page2;
			contentPanel.layout();
		}
	});

	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 
