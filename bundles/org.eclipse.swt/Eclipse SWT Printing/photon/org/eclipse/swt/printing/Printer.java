package org.eclipse.swt.printing;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*; 

public final class Printer extends Device {
	PrinterData data;
	int printContext, xScreen, xDrawable;
	int defaultFontList;

public static PrinterData[] getPrinterList() {
	PrinterData printerList[] = new PrinterData[0];
	return printerList;
}

static PrinterData getDefaultPrinterData() {
	/* Use the first printer in the list as the default */
	PrinterData[] list = getPrinterList();
	if (list.length == 0) {
		/* no printers */
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	return list[0];
}

public Printer() {
	this(getDefaultPrinterData());
}

public Printer(PrinterData data) {
	super(data);
}


public int internal_new_GC(GCData data) {
	return 0;
}

public void internal_dispose_GC(int xGC, GCData data) {
}

public boolean startJob(String jobName) {
	return true;
}

public void endJob() {
}

public void cancelJob() {
}

public boolean startPage() {
	return true;
}

public void endPage() {
}

public Point getDPI() {
	return new Point(0, 0);
}

public Rectangle getBounds() {
	return null;
}

public Rectangle getClientArea() {
	return null;
}

public Rectangle computeTrim(int x, int y, int width, int height) {
	return new Rectangle(0,0,0,0);
}

public PrinterData getPrinterData() {
	return data;
}

}
