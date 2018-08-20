/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 502845
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Adding an accessible listener to provide emulated state information
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet162 {

	final static String[] ITEM_NAMES = {"first item", "second", "third", "fourth", "fifth"};

public static void main (String [] args) {
	Display display = new Display ();
	final Image checkedImage = getStateImage (display, true);
	final Image uncheckedImage = getStateImage (display, false);

	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());

	final Table table = new Table (shell, SWT.FULL_SELECTION | SWT.BORDER);
	for (int i = 0; i < ITEM_NAMES.length; i++) {
		TableItem item = new TableItem (table, SWT.NONE);
		item.setText (ITEM_NAMES[i]);
		item.setImage (i % 2 == 0 ? checkedImage : uncheckedImage);
	}
	table.addSelectionListener(widgetDefaultSelectedAdapter(e -> {
			TableItem item = (TableItem)e.item;
			item.setImage(item.getImage() == checkedImage ? uncheckedImage : checkedImage);
		}));

	table.getAccessible ().addAccessibleControlListener (new AccessibleControlAdapter () {
		@Override
		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_CHECKBUTTON;
		}
		@Override
		public void getState (AccessibleControlEvent e) {
			if (e.childID >= 0 && e.childID < table.getItemCount ()) {
				TableItem item = table.getItem (e.childID);
				if (item.getImage() == checkedImage) {
					e.detail |= ACC.STATE_CHECKED;
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

static Image getStateImage (Display display, boolean checked) {
	Image image = new Image (display, 16, 16);
	GC gc = new GC (image);
	gc.setBackground (display.getSystemColor (SWT.COLOR_YELLOW));
	gc.fillOval (0, 0, 16, 16);
	if (checked) {
		gc.setForeground (display.getSystemColor (SWT.COLOR_DARK_GREEN));
		gc.drawLine (0, 0, 16, 16);
		gc.drawLine (16, 0, 0, 16);
	}
	gc.dispose ();
	return image;
}
}