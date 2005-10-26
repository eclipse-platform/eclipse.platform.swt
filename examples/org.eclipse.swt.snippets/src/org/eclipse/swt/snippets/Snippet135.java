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
 * example snippet: embed Swing/AWT in SWT
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.0
 */
import java.awt.EventQueue;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.awt.SWT_AWT;

public class Snippet135 {

	static class FileTableModel extends AbstractTableModel {		
		File[] files;        
		String[] columnsName = {"Name", "Size", "Date Modified"};
		
		public FileTableModel (File[] files) {
			this.files = files;
		}
		public int getColumnCount () {
			return columnsName.length;
		}
		public Class getColumnClass (int col) {
			if (col == 1) return Long.class;
			if (col == 2) return Date.class;
			return String.class;
		}
		public int getRowCount () {
			return files == null ? 0 : files.length;
		}
		public Object getValueAt (int row, int col) {
			if (col == 0) return files[row].getName();
			if (col == 1) return new Long(files[row].length());
			if (col == 2) return new Date(files[row].lastModified());
			return "";
		}
		public String getColumnName (int col) {
			return columnsName[col];
		}
	}

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("SWT and Swing/AWT Example");

		Listener exitListener = new Listener() {
			public void handleEvent(Event e) {
				MessageBox dialog = new MessageBox(shell, SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION);
				dialog.setText("Question");
				dialog.setMessage("Exit?");
				if (e.type == SWT.Close) e.doit = false;
				if (dialog.open() != SWT.OK) return;
				shell.dispose();
			}
		};	
		Listener aboutListener = new Listener() {
			public void handleEvent(Event e) {
				final Shell s = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				s.setText("About");
				GridLayout layout = new GridLayout(1, false);
				layout.verticalSpacing = 20;
				layout.marginHeight = layout.marginWidth = 10;
				s.setLayout(layout);
				Label label = new Label(s, SWT.NONE);
				label.setText("SWT and AWT Example.");
				Button button = new Button(s, SWT.PUSH);
				button.setText("OK");
				GridData data = new GridData();
				data.horizontalAlignment = GridData.CENTER;
				button.setLayoutData(data);
				button.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						s.dispose();
					}
				});
				s.pack();
				Rectangle parentBounds = shell.getBounds();
				Rectangle bounds = s.getBounds();
				int x = parentBounds.x + (parentBounds.width - bounds.width) / 2;
				int y = parentBounds.y + (parentBounds.height - bounds.height) / 2;
				s.setLocation(x, y);
				s.open();
				while (!s.isDisposed()) {
					if (!display.readAndDispatch()) display.sleep();
				}
			}
		};
		shell.addListener(SWT.Close, exitListener);
		Menu mb = new Menu(shell, SWT.BAR);
		MenuItem fileItem = new MenuItem(mb, SWT.CASCADE);
		fileItem.setText("&File");
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(fileMenu);
		MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setText("&Exit\tCtrl+X");
		exitItem.setAccelerator(SWT.CONTROL + 'X');
		exitItem.addListener(SWT.Selection, exitListener);
		MenuItem aboutItem = new MenuItem(fileMenu, SWT.PUSH);
		aboutItem.setText("&About\tCtrl+A");
		aboutItem.setAccelerator(SWT.CONTROL + 'A');
		aboutItem.addListener(SWT.Selection, aboutListener);
		shell.setMenuBar(mb);

		RGB color = shell.getBackground().getRGB();
		Label separator1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		Label locationLb = new Label(shell, SWT.NONE);
		locationLb.setText("Location:");
		Composite locationComp = new Composite(shell, SWT.EMBEDDED);
		ToolBar toolBar = new ToolBar(shell, SWT.FLAT);
		ToolItem exitToolItem = new ToolItem(toolBar, SWT.PUSH);
		exitToolItem.setText("&Exit");
		exitToolItem.addListener(SWT.Selection, exitListener);
		ToolItem aboutToolItem = new ToolItem(toolBar, SWT.PUSH);
		aboutToolItem.setText("&About");
		aboutToolItem.addListener(SWT.Selection, aboutListener);
		Label separator2 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		final Composite comp = new Composite(shell, SWT.NONE);
		final Tree fileTree = new Tree(comp, SWT.SINGLE | SWT.BORDER);
		Sash sash = new Sash(comp, SWT.VERTICAL);
		Composite tableComp = new Composite(comp, SWT.EMBEDDED);
		Label separator3 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		Composite statusComp = new Composite(shell, SWT.EMBEDDED);

		java.awt.Frame locationFrame = SWT_AWT.new_Frame(locationComp);
		final java.awt.TextField locationText = new java.awt.TextField();
		locationFrame.add(locationText);

		java.awt.Frame fileTableFrame = SWT_AWT.new_Frame(tableComp);
		java.awt.Panel panel = new java.awt.Panel(new java.awt.BorderLayout());
		fileTableFrame.add(panel);
		final JTable fileTable = new JTable(new FileTableModel(null));
		fileTable.setDoubleBuffered(true);
		fileTable.setShowGrid(false);
		fileTable.createDefaultColumnsFromModel();
		JScrollPane scrollPane = new JScrollPane(fileTable);
		panel.add(scrollPane);

		java.awt.Frame statusFrame = SWT_AWT.new_Frame(statusComp);
		statusFrame.setBackground(new java.awt.Color(color.red, color.green, color.blue));
		final java.awt.Label statusLabel = new java.awt.Label();
		statusFrame.add(statusLabel);
		statusLabel.setText("Select a file");

		sash.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (e.detail == SWT.DRAG) return;
				GridData data = (GridData)fileTree.getLayoutData();
				Rectangle trim = fileTree.computeTrim(0, 0, 0, 0);
				data.widthHint = e.x - trim.width;
				comp.layout();
			}
		});

		File[] roots = File.listRoots();
		for (int i = 0; i < roots.length; i++) {
			File file = roots[i];
			TreeItem treeItem = new TreeItem(fileTree, SWT.NONE);
			treeItem.setText(file.getAbsolutePath());
			treeItem.setData(file);
			new TreeItem(treeItem, SWT.NONE);
		}
		fileTree.addListener(SWT.Expand, new Listener() {
			public void handleEvent(Event e) {
				TreeItem item = (TreeItem)e.item;
				if (item == null) return;
				if (item.getItemCount() == 1) {
					TreeItem firstItem = item.getItems()[0];
					if (firstItem.getData() != null) return;
					firstItem.dispose();
				} else {
					return;
				}
				File root = (File)item.getData();
				File[] files = root.listFiles();
				if (files == null) return;
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory()) {
						TreeItem treeItem = new TreeItem(item, SWT.NONE);
						treeItem.setText(file.getName());
						treeItem.setData(file);
						new TreeItem(treeItem, SWT.NONE);
					}
				}
			}
		});
		fileTree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TreeItem item = (TreeItem)e.item;
				if (item == null) return;
				final File root = (File)item.getData();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						statusLabel.setText(root.getAbsolutePath());
						locationText.setText(root.getAbsolutePath());
						fileTable.setModel(new FileTableModel(root.listFiles()));
					}
				});
			}
		});
		
		GridLayout layout = new GridLayout(4, false);
		layout.marginWidth = layout.marginHeight = 0;
		layout.horizontalSpacing = layout.verticalSpacing = 1;
		shell.setLayout(layout);
		GridData data;		
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 4;
		separator1.setLayoutData(data);
		data = new GridData();
		data.horizontalSpan = 1;
		data.horizontalIndent = 10;
		locationLb.setLayoutData(data);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		data.heightHint = locationText.getPreferredSize().height;
		locationComp.setLayoutData(data);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 1;
		toolBar.setLayoutData(data);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 4;
		separator2.setLayoutData(data);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 4;
		comp.setLayoutData(data);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 4;
		separator3.setLayoutData(data);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 4;
		data.heightHint = statusLabel.getPreferredSize().height;
		statusComp.setLayoutData(data);
		
		layout = new GridLayout(3, false);
		layout.marginWidth = layout.marginHeight = 0;
		layout.horizontalSpacing = layout.verticalSpacing = 1;
		comp.setLayout(layout);			
		data = new GridData(GridData.FILL_VERTICAL);
		data.widthHint = 200;
		fileTree.setLayoutData(data);		
		data = new GridData(GridData.FILL_VERTICAL);
		sash.setLayoutData(data);		
		data = new GridData(GridData.FILL_BOTH);
		tableComp.setLayoutData(data);

		shell.open();
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
