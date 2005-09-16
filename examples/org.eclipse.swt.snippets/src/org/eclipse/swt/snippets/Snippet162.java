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
	final Table table = new Table (shell, SWT.BORDER);
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
			super.getName (e);
			if (e.childID >= 0 && e.childID < table.getItemCount ()) {
				TableItem item = table.getItem (e.childID);
				Point pt = display.getCursorLocation ();
				pt = display.map (null, table, pt);
				for (int i = 0; i < table.getColumnCount (); i++) {
					if (item.getBounds (i).contains (pt)) {
						int [] data = (int []) item.getData (STATE);
						boolean checked = false;
						if (data != null) {
							for (int j = 0; j < data.length; j++) {
								if (data [j] == i) {
									checked = true;
									break;
								}
							}
						}
						e.result = item.getText (i) + " " + (checked ? "checked" : "unchecked");
						break;
					}
				}
			}
		}
	});
	accessible.addAccessibleControlListener (new AccessibleControlAdapter () {
		public void getState (AccessibleControlEvent e) {
			super.getState (e);
			if (e.childID >= 0 && e.childID < table.getItemCount ()) {
				TableItem item = table.getItem (e.childID);
				int [] data = (int []) item.getData (STATE);
				if (data != null) {
					Point pt = display.getCursorLocation ();
					pt = display.map (null, table, pt);
					for (int i = 0; i < data.length; i++) {
						if (item.getBounds (data [i]).contains (pt)) {
							e.detail |= ACC.STATE_CHECKED;
							break;
						}
					}
				}
			}
		}
	});
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
}