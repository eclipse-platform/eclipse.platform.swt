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
 * Composite example snippet: set the tab traversal order of children
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Snippet75 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout (new RowLayout ());
	
	Composite c1 = new Composite (shell, SWT.BORDER);
	c1.setLayout (new RowLayout ());
	Button b1 = new Button (c1, SWT.PUSH);
	b1.setText ("B1");
	Button [] radios = new Button [3];
	for (int i=0; i<radios.length; i++) {
		radios [i] = new Button (c1, SWT.RADIO);
		radios [i].setText ("R" + (i ==1 ? "&" : "") + i);
	}
	Button b2 = new Button (c1, SWT.PUSH);
	b2.setText ("B2");
	List l1 = new List (c1, SWT.SINGLE | SWT.BORDER);
	l1.setItems (new String [] {"L1"});
	Button b3 = new Button (c1, SWT.PUSH);
	b3.setText ("B&3");
	Button b3_1 = new Button (c1, SWT.PUSH);
	b3_1.setText ("B3_1");
	
	Composite c2 = new Composite (shell, SWT.BORDER);
	c2.setLayout (new RowLayout ());
	Button b4 = new Button (c2, SWT.PUSH);
	b4.setText ("B&4");
	Button b5 = new Button (c2, SWT.PUSH);
	b5.setText ("B&5");
	
	List l2 = new List (shell, SWT.SINGLE | SWT.BORDER);
	l2.setItems (new String [] {"L2"});
	
	ToolBar tb1 = new ToolBar (shell, SWT.FLAT | SWT.BORDER);
	ToolItem i1 = new ToolItem (tb1, SWT.RADIO);
	i1.setText ("I1");
	ToolItem i2 = new ToolItem (tb1, SWT.RADIO);
	i2.setText ("I&2");
	Combo combo1 = new Combo (tb1, SWT.READ_ONLY | SWT.BORDER);
	combo1.setItems (new String [] {"C1"});
	combo1.setText ("C1");
	combo1.pack ();
	ToolItem i3 = new ToolItem (tb1, SWT.SEPARATOR);
	i3.setWidth (combo1.getSize ().x);
	i3.setControl (combo1);
	i3.setText ("I3");
	ToolItem i4 = new ToolItem (tb1, SWT.PUSH);
	i4.setText ("I4");
	ToolItem i5 = new ToolItem (tb1, SWT.CHECK);
	i5.setText ("I5");
	
	Button b6 = new Button (shell, SWT.PUSH);
	b6.setText ("B&6");
	
	Composite c4 = new Composite (shell, SWT.BORDER);
	c4.setSize (32, 32);
	Composite c5 = new Composite (c4, SWT.BORDER);
	c5.setSize (20, 20);

	Control [] list1 = new Control [] {b2, b1, b3_1, b3};
	c1.setTabList (list1);
	Control [] list2 = new Control [] {c1, b6, tb1, c4, c2, l2};
	shell.setTabList (list2);

	shell.pack ();
	shell.open ();
	
	Control [] x = c1.getChildren ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
} 
