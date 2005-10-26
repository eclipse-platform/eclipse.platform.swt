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
 * example snippet: embed a JTable in SWT (no flicker)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.0
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
		
		/*
		* Set a Windows specific AWT property that prevents heavyweight
		* components from erasing their background. Note that this
		* is a global property and cannot be scoped. It might not be
		* suitable for your application.
		*/
		try {
			System.setProperty("sun.awt.noerasebackground","true");
		} catch (NoSuchMethodError error) {}

		/* Create and setting up frame */
		Frame frame = SWT_AWT.new_Frame(composite);
		Panel panel = new Panel(new BorderLayout()) {
			public void update(java.awt.Graphics g) {
				/* Do not erase the background */ 
				paint(g);
			}
		};
		frame.add(panel);
		JRootPane root = new JRootPane();
		panel.add(root);
		java.awt.Container contentPane = root.getContentPane();

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
		contentPane.setLayout(new BorderLayout());
		contentPane.add(scrollPane);
		
		shell.open();
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
