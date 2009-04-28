/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
 * Show row/column position of current selection in an Excel sheet.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;

public class Snippet305 {
	static int SheetSelectionChange = 0x00000616;
	static String IID_AppEvents = "{00024413-0000-0000-C000-000000000046}";

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Excel Sheet Selection Example");
		shell.setLayout(new FillLayout());
		OleAutomation application;
		try {
			OleFrame frame = new OleFrame(shell, SWT.NONE);
			OleControlSite controlSite = new OleControlSite(frame, SWT.NONE, "Excel.Sheet");
			controlSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
			
			OleAutomation excelSheet = new OleAutomation(controlSite);
			int[] dispIDs = excelSheet.getIDsOfNames(new String[] { "Application" });
			Variant pVarResult = excelSheet.getProperty(dispIDs[0]);
			application = pVarResult.getAutomation();
			pVarResult.dispose();
			excelSheet.dispose();
			
			OleListener listener = new OleListener() {
				public void handleEvent(OleEvent e) {
					// SheetSelectionChange(ByVal Sh As Object, ByVal Target As Excel.Range)
					Variant[] args = e.arguments;
					// OleAutomation sheet = args[1].getAutomation(); // Excel.Sheet
					OleAutomation range = args[0].getAutomation(); // Excel.Range
					int[] dispIDs = range.getIDsOfNames(new String[] { "Row" });
					Variant pVarResult = range.getProperty(dispIDs[0]);
					int row = pVarResult.getInt();
					dispIDs = range.getIDsOfNames(new String[] { "Column" });
					pVarResult = range.getProperty(dispIDs[0]);
					int column = pVarResult.getInt();
					range.dispose();
					System.out.println("row=" + row + " column=" + column);
					for (int i = 0; i < args.length; i++) {
						args[i].dispose();
					}
				}
			};
			controlSite.addEventListener(application, IID_AppEvents, SheetSelectionChange, listener);
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
		if (application != null) application.dispose();
		display.dispose();
	}
}
