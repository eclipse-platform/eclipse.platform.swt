/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Create a virtual table and add 1000 entries to it every 500 ms.
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import java.util.*;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet151 {

static int[] values = new int[0];
static Thread thread;

public static void main (String [] args) {
	final Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	final Table table = new Table(shell, SWT.BORDER | SWT.VIRTUAL);
	table.addListener(SWT.SetData, new Listener() {
		public void handleEvent(Event e) {
			TableItem item = (TableItem)e.item;
			int index = table.indexOf(item);
			item.setText("item "+values[index]);
		}
	});
	final Random random = new Random();
	thread = new Thread() {
		public void run() {
			int count = 0;
			while (count++ < 2000) {
				if (table.isDisposed()) return;
				// add 1000 random numbers to a sorted array
				for (int j = 0; j < 1000; j++) {
					int next  = random.nextInt();
					int[] newItems = new int[values.length + 1];
					int index = 0;
					for (int i = 0; i < values.length; i++) {
						if (values[i] > next) break;
						index++;
					}
					System.arraycopy(values, 0, newItems, 0, index);
					newItems[index] = next;
					System.arraycopy(values, index, newItems, index+1, values.length-index);
					values = newItems;
				}
				display.syncExec(new Runnable() {
					public void run() {
						if (table.isDisposed()) return;
						table.setItemCount(values.length);
						table.clearAll();
						// bug 69398 on Windows
						table.redraw();
					}
				});
				try {Thread.sleep(500);} catch (Throwable t) {}
			}
		}
	};
	thread.start();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
