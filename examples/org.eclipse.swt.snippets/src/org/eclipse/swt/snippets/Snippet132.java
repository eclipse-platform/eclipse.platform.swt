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
 * Printing example snippet: print "Hello World!" in black, outlined in red, to default printer
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.printing.*;

public class Snippet132 {

public static void main (String [] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.open ();
	PrinterData data = Printer.getDefaultPrinterData();
	if (data == null) {
		System.out.println("Warning: No default printer.");
		display.dispose();
		return;
	}
	Printer printer = new Printer(data);
	if (printer.startJob("SWT Printing Snippet")) {
		Color black = printer.getSystemColor(SWT.COLOR_BLACK);
		Color white = printer.getSystemColor(SWT.COLOR_WHITE);
		Color red = printer.getSystemColor(SWT.COLOR_RED);
		Rectangle trim = printer.computeTrim(0, 0, 0, 0);
		Point dpi = printer.getDPI();
		int leftMargin = dpi.x + trim.x; // one inch from left side of paper
		if (leftMargin < 0) leftMargin = -trim.x;  // make sure to print on the printable area 
		int topMargin = dpi.y / 2 + trim.y; // one-half inch from top edge of paper
		if (topMargin < 0) topMargin = -trim.y;  // make sure to print on the printable area 
		GC gc = new GC(printer);
		if (printer.startPage()) {
			gc.setBackground(white);
			gc.setForeground(black);
			String testString = "Hello World!";
			Point extent = gc.stringExtent(testString);
			gc.drawString(testString, leftMargin, topMargin);
			gc.setForeground(red);
			gc.drawRectangle(leftMargin, topMargin, extent.x, extent.y);
			printer.endPage();
		}
		gc.dispose();
		printer.endJob();
	}
	printer.dispose();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose();
}
}
