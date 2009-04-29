/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
 * Printer example snippet: print in landscape mode
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.5
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.printing.*;

public class Snippet318 {

public static void main(String [] args) {
	final Display display = new Display();
	PrinterData printerData = new PrinterData();
	printerData.orientation = PrinterData.LANDSCAPE;
	Printer printer = new Printer(printerData);
	Point dpi = printer.getDPI();
	if (printer.startJob("SWT Printing Snippet")) {
		GC gc = new GC(printer);
		if (printer.startPage()) {
			int oneInch = dpi.x;
			gc.drawString("Hello World!", oneInch, 2 * oneInch);
			gc.drawString("Printed on " + printerData.name + " using SWT on " + SWT.getPlatform(), oneInch, oneInch * 5/2);
			printer.endPage();
		}
		gc.dispose();
		printer.endJob();
	}
	printer.dispose();
	display.dispose();
}

}
