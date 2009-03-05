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
 * Adding an accessible listener to provide state information
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet162 {

final static String STATE = "CheckedIndices";

public static void main (String [] args) {
	final Display display = new Display ();
	Image checkedImage = getCheckedImage (display);
	Image uncheckedImage = getUncheckedImage (display);
	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	final Table table = new Table (shell, SWT.FULL_SELECTION | SWT.BORDER);
	TableColumn column1 = new TableColumn (table, SWT.NONE);
	TableColumn column2 = new TableColumn (table, SWT.NONE);
	TableColumn column3 = new TableColumn (table, SWT.NONE);
	TableItem item1 = new TableItem (table, SWT.NONE);
	item1.setText (new String [] {"first item", "a", "b"});
	item1.setImage (1, uncheckedImage);
	item1.setImage (2, uncheckedImage);
	item1.setData (STATE, null);
	TableItem item2 = new TableItem (table, SWT.NONE);
	item2.setText (new String [] {"second item", "c", "d"});
	item2.setImage (1, uncheckedImage);
	item2.setImage (2, checkedImage);
	item2.setData (STATE, new int [] {2});
	TableItem item3 = new TableItem (table, SWT.NONE);
	item3.setText (new String [] {"third", "e", "f"});
	item3.setImage (1, checkedImage);
	item3.setImage (2, checkedImage);
	item3.setData (STATE, new int [] {1, 2});
	column1.pack ();
	column2.pack ();
	column3.pack ();
	Accessible accessible = table.getAccessible ();
	accessible.addAccessibleListener (new AccessibleAdapter () {
		public void getName (AccessibleEvent e) {
			/* The first column of a table item is returned in the "name" property. */
			if (e.childID >= 0 && e.childID < table.getItemCount ()) {
				TableItem item = table.getItem (e.childID);
				e.result = item.getText (0);
			}
		}
		public void getDescription (AccessibleEvent e) {
			/* The names of all columns of a table item except the first are returned in the "description" property. */
			if (e.childID >= 0 && e.childID < table.getItemCount ()) {
				TableItem item = table.getItem (e.childID);
				e.result = "";
				for (int i = 1; i < table.getColumnCount (); i++) {
					e.result += item.getText (i) + " " + (isChecked(item, i) ? "checked" : "unchecked");
					if (i + 1 < table.getColumnCount()) e.result += ", ";
				}
			}
		}
	});
	accessible.addAccessibleControlListener (new AccessibleControlAdapter () {
		public void getState (AccessibleControlEvent e) {
			if (e.childID >= 0 && e.childID < table.getItemCount ()) {
				TableItem item = table.getItem (e.childID);
				for (int i = 1; i < table.getColumnCount (); i++) {
					if (isChecked(item, i)) {
						e.detail |= ACC.STATE_CHECKED;
					}
				}
			}
		}
	});
	shell.pack();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	checkedImage.dispose ();
	uncheckedImage.dispose ();
	display.dispose ();
}

static Image getCheckedImage (Display display) {
	Image image = new Image (display, 16, 16);
	GC gc = new GC (image);
	gc.setBackground (display.getSystemColor (SWT.COLOR_YELLOW));
	gc.fillOval (0, 0, 16, 16);
	gc.setForeground (display.getSystemColor (SWT.COLOR_DARK_GREEN));
	gc.drawLine (0, 0, 16, 16);
	gc.drawLine (16, 0, 0, 16);
	gc.dispose ();
	return image;
}

static Image getUncheckedImage (Display display) {
	Image image = new Image (display, 16, 16);
	GC gc = new GC (image);
	gc.setBackground (display.getSystemColor (SWT.COLOR_YELLOW));
	gc.fillOval (0, 0, 16, 16);
	gc.dispose ();
	return image;
}

static boolean isChecked(TableItem item, int columnIndex) {
	int [] data = (int []) item.getData (STATE);
	if (data != null) {
		for (int i = 0; i < data.length; i++) {
			if (data [i] == columnIndex) {
				return true;
			}
		}
	}
	return false;
}
}