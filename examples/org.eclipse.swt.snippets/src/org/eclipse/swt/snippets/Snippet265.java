/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;

/*
 * Open an OLE Windows Media Player.
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.3
 */
public class Snippet265 {
	static OleClientSite clientSite;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Media Player Example");
		shell.setLayout(new FillLayout());
		try {
			OleFrame frame = new OleFrame(shell, SWT.NONE);
			clientSite = new OleClientSite(frame, SWT.NONE, "WMPlayer.OCX");
			clientSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
			addFileMenu(frame);
		} catch (SWTError e) {
			System.out.println("Unable to open activeX control");
			display.dispose();
			return;
		}
		shell.setSize(800, 600);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	static void addFileMenu(OleFrame frame) {
		final Shell shell = frame.getShell();
		Menu menuBar = shell.getMenuBar();
		if (menuBar == null) {
			menuBar = new Menu(shell, SWT.BAR);
			shell.setMenuBar(menuBar);
		}
		MenuItem fileMenu = new MenuItem(menuBar, SWT.CASCADE);
		fileMenu.setText("&File");
		Menu menuFile = new Menu(fileMenu);
		fileMenu.setMenu(menuFile);
		frame.setFileMenus(new MenuItem[] { fileMenu });

		MenuItem menuFileOpen = new MenuItem(menuFile, SWT.CASCADE);
		menuFileOpen.setText("Open...");
		menuFileOpen.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				fileOpen();
			}
		});
		MenuItem menuFileExit = new MenuItem(menuFile, SWT.CASCADE);
		menuFileExit.setText("Exit");
		menuFileExit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
	}

	static void fileOpen() {
		FileDialog dialog = new FileDialog(clientSite.getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.wmv" });
		String filename = dialog.open();
		if (filename != null) {
			OleAutomation player = new OleAutomation(clientSite);
			int playURL[] = player.getIDsOfNames(new String[] { "URL" });
			if (playURL != null) {
				Variant theFile = new Variant(filename);
				player.setProperty(playURL[0], theFile);
			}
			player.dispose();
		}
	}
}