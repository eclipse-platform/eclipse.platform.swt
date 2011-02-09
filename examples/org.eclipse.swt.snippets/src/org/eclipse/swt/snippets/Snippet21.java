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
 * Canvas example snippet: implement tab traversal (behave like a tab group)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet21 {

public static void main (String [] args) {
	Display display = new Display ();
	final Color red = display.getSystemColor (SWT.COLOR_RED);
	final Color blue = display.getSystemColor (SWT.COLOR_BLUE);
	Shell shell = new Shell (display);
	Button b = new Button (shell, SWT.PUSH);
	Rectangle clientArea = shell.getClientArea ();
	b.setBounds (clientArea.x + 10, clientArea.y + 10, 100, 32);
	b.setText ("Button");
	shell.setDefaultButton (b);
	final Canvas c = new Canvas (shell, SWT.BORDER);
	c.setBounds (10, 50, 100, 32);
	c.addListener (SWT.Traverse, new Listener () {
		public void handleEvent (Event e) {
			switch (e.detail) {
				/* Do tab group traversal */
				case SWT.TRAVERSE_ESCAPE:
				case SWT.TRAVERSE_RETURN:
				case SWT.TRAVERSE_TAB_NEXT:	
				case SWT.TRAVERSE_TAB_PREVIOUS:
				case SWT.TRAVERSE_PAGE_NEXT:	
				case SWT.TRAVERSE_PAGE_PREVIOUS:
					e.doit = true;
					break;
			}
		}
	});
	c.addListener (SWT.FocusIn, new Listener () {
		public void handleEvent (Event e) {
			c.setBackground (red);
		}
	});
	c.addListener (SWT.FocusOut, new Listener () {
		public void handleEvent (Event e) {
			c.setBackground (blue);
		}
	});
	c.addListener (SWT.KeyDown, new Listener () {
		public void handleEvent (Event e) {
			System.out.println ("KEY");
		}
	});
	Text t = new Text (shell, SWT.SINGLE | SWT.BORDER);
	t.setBounds (10, 85, 100, 32);

	Text r = new Text (shell, SWT.MULTI | SWT.BORDER);
	r.setBounds (10, 120, 100, 32);
	
	c.setFocus ();
	shell.setSize (200, 200);
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 
