/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - viewport mode, visibility gate
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * VirtualPageSource example snippet: a 10,000,000-row virtual table whose rows
 * come from a slow source in blocks on worker threads. Rows not loaded yet show
 * a placeholder; the visible block is fetched first, then the overscan; blocks
 * that scroll out are cancelled. Paint never creates TableItem objects.
 *
 * For a list of all SWT example snippets see
 * https://eclipse.dev/eclipse/swt/snippets/
 *
 * @since 3.137
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet394 {

static final int COUNT = 10_000_000;

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setText ("Snippet 394");
	shell.setLayout (new GridLayout (1, false));

	Table table = new Table (shell, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
	table.setHeaderVisible (true);
	table.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
	String [] titles = {"Name", "City"};
	for (String title : titles) {
		TableColumn column = new TableColumn (table, SWT.NONE);
		column.setText (title);
		column.setWidth (200);
	}
	table.setItemCount (COUNT);

	/* 256 rows per block, at most 64 blocks in memory, 2 text columns. */
	VirtualPageSource source = new VirtualPageSource (256, 64, 2, VirtualPageSource.PLACEHOLDER_TEXT, request -> {
		Thread.sleep (5); // a slow source: 5 ms per block, on a worker thread
		for (int i = request.getStart (), end = i + request.getCount (); i < end; i++) {
			if (request.isCancelled ()) return; // scrolled out of the overscan
			request.setText (i, 0, "Person " + i);
			request.setText (i, 1, "City " + (i % 977));
		}
	});
	source.setPlaceholderText ("loading...");
	source.attach (table);

	Label status = new Label (shell, SWT.NONE);
	status.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
	Runnable[] update = new Runnable[1];
	update [0] = () -> {
		if (status.isDisposed ()) return;
		status.setText ("fetched " + source.getCount (VirtualPageSource.FETCHED)
			+ ", cancelled " + source.getCount (VirtualPageSource.CANCELLED)
			+ ", resident blocks " + source.getCount (VirtualPageSource.RESIDENT));
		display.timerExec (250, update [0]);
	};
	display.timerExec (250, update [0]);

	shell.setSize (520, 600);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
