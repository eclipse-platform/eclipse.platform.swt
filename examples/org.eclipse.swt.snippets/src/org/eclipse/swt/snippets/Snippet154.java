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
 * example snippet: embed a JTable in SWT (no flicker)
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;
import javax.swing.*;
import java.util.Vector;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.awt.SWT_AWT;

public class Snippet154 {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		Composite composite = new Composite(shell, SWT.NO_BACKGROUND | SWT.EMBEDDED);
		
		/* Create and setting up frame */
		Frame frame = SWT_AWT.new_Frame(composite);
		Panel panel = new Panel(new BorderLayout());
		frame.add(panel);
		JPanel root = new JPanel();
		root.setLayout(new BorderLayout());
		panel.add(root);

		/* Creating components */
		int nrows = 1000, ncolumns = 10;
		Vector rows = new Vector();
		for (int i = 0; i < nrows; i++) {
			Vector row = new Vector();
			for (int j = 0; j < ncolumns; j++) {
				row.addElement("Item " + i + "-" + j);
			}
			rows.addElement(row);
		}
		Vector columns = new Vector();
		for (int i = 0; i < ncolumns; i++) {
			columns.addElement("Column " + i);
		}
		JTable table = new JTable(rows, columns);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.createDefaultColumnsFromModel();
		JScrollPane scrollPane = new JScrollPane(table);
		root.add(scrollPane);
		
		shell.open();
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
