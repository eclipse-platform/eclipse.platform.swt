/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
 * Listen for events in Excel (win32 only)
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;

public class Snippet199 {
	static String IID_AppEvents = "{00024413-0000-0000-C000-000000000046}";
	// Event ID
	static int NewWorkbook            = 0x0000061d;
	static int SheetSelectionChange   = 0x00000616;
	static int SheetBeforeDoubleClick = 0x00000617;
	static int SheetBeforeRightClick  = 0x00000618;
	static int SheetActivate          = 0x00000619;
	static int SheetDeactivate        = 0x0000061a;
	static int SheetCalculate         = 0x0000061b;
	static int SheetChange            = 0x0000061c;
	static int WorkbookOpen           = 0x0000061f;
	static int WorkbookActivate       = 0x00000620;
	static int WorkbookDeactivate     = 0x00000621;
	static int WorkbookBeforeClose    = 0x00000622;
	static int WorkbookBeforeSave     = 0x00000623;
	static int WorkbookBeforePrint    = 0x00000624;
	static int WorkbookNewSheet       = 0x00000625;
	static int WorkbookAddinInstall   = 0x00000626;
	static int WorkbookAddinUninstall = 0x00000627;
	static int WindowResize           = 0x00000612;
	static int WindowActivate         = 0x00000614;
	static int WindowDeactivate       = 0x00000615;
	static int SheetFollowHyperlink   = 0x0000073e;

 public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	OleControlSite controlSite;
	try {
		OleFrame frame = new OleFrame(shell, SWT.NONE);
		controlSite = new OleControlSite(frame, SWT.NONE, "Excel.Sheet");
		controlSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
	} catch (SWTError e) {
		System.out.println("Unable to open activeX control");
		display.dispose();
		return;
	}
	shell.open();

	OleAutomation excelSheet = new OleAutomation(controlSite);
	int[] dispIDs = excelSheet.getIDsOfNames(new String[] {"Application"});
	Variant pVarResult = excelSheet.getProperty(dispIDs[0]);
	OleAutomation application = pVarResult.getAutomation();
	pVarResult.dispose();
	excelSheet.dispose();
	
	int eventID = SheetSelectionChange;
	OleListener listener = new OleListener() {
		public void handleEvent (OleEvent e) {
			System.out.println("selection has changed");
			// two arguments which must be released (row and column)
			Variant[] args = e.arguments;
			for (int i = 0; i < args.length; i++) {
				System.out.println(args[i]);
				args [i].dispose();
			}
		}
	};
	controlSite.addEventListener(application, IID_AppEvents, eventID, listener);
	
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	application.dispose();
	display.dispose ();
}
}