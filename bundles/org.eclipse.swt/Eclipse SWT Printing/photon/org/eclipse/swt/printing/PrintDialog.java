package org.eclipse.swt.printing;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.photon.*;

public class PrintDialog extends Dialog {
	int scope = PrinterData.ALL_PAGES;
	int startPage = -1, endPage = -1;
	boolean printToFile = false;

public PrintDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
}
public PrintDialog (Shell parent, int style) {
	super (parent, style);
}
public PrinterData open() {
	/* Return the first printer in the list */
	PrinterData[] printers = Printer.getPrinterList();
	if (printers.length > 0) return printers[0];
	return null;
}
public int getScope() {
	return scope;
}
public void setScope(int scope) {
	this.scope = scope;
}
public int getStartPage() {
	return startPage;
}
public void setStartPage(int startPage) {
	this.startPage = startPage;
}
public int getEndPage() {
	return endPage;
}
public void setEndPage(int endPage) {
	this.endPage = endPage;
}
public boolean getPrintToFile() {
	return printToFile;
}
public void setPrintToFile(boolean printToFile) {
	this.printToFile = printToFile;
}
protected void checkSubclass() {
}
}
