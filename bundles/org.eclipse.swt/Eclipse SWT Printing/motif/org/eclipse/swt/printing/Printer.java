package org.eclipse.swt.printing;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*; 

public final class Printer extends Device {
	PrinterData data;
	int printContext, xScreen, xDrawable;
	int defaultFontList;

public static PrinterData[] getPrinterList() {
	SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
	/* Connect to the default X print server */
	//byte [] buffer = Converter.wcsToMbcs(null, XDefaultPrintServer, true);
	//int pdpy = OS.XOpenDisplay(buffer);
	int pdpy = xPrinter;
	if (pdpy == 0) {
		/* no print server */
		SWT.error(SWT.ERROR_IO);
	}

	/* Get the list of printers */
	int [] listCount = new int[1];
	int plist = OS.XpGetPrinterList(pdpy, null, listCount);
	int printerCount = listCount[0];
	if (plist == 0 || printerCount == 0) {
		/* no printers */
		//OS.XCloseDisplay(pdpy);
		SWT.error(SWT.ERROR_IO);
	}
    
	/* Copy the printer names into PrinterData objects */
	int [] stringPointers = new int [printerCount * 2];
	OS.memmove(stringPointers, plist, printerCount * 2 * 4);
	PrinterData printerList[] = new PrinterData[printerCount];
	for (int i = 0; i < printerCount; i++) {
		String name = "";
		int address = stringPointers[i * 2];
		if (address != 0) {
			int length = OS.strlen(address);
			byte[] buffer = new byte [length];
			OS.memmove(buffer, address, length);
			name = new String(Converter.mbcsToWcs(null, buffer));
		}
		printerList[i] = new PrinterData(Device.XDefaultPrintServer, name);
	}
	OS.XpFreePrinterList(plist);
	//OS.XCloseDisplay(pdpy);
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

protected void create(DeviceData deviceData) {
	SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
	data = (PrinterData)deviceData;

	/* Open the display for the X print server */
	//byte[] displayName = Converter.wcsToMbcs(null, data.driver, true);
	//xDisplay = OS.XOpenDisplay(displayName);
	xDisplay = xPrinter;
	if (xDisplay == 0) {
		/* no print server */
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
}

protected void init() {
	super.init();
	
	/* Create the printContext for the printer */
	byte[] name = Converter.wcsToMbcs(null, data.name, true);
	printContext = OS.XpCreateContext(xDisplay, name);
	if (printContext == OS.None) {
		/* can't create print context */
		//OS.XCloseDisplay(xDisplay);
		SWT.error(SWT.ERROR_NO_HANDLES);
	}

	/* Set the printContext into the display */
	OS.XpSetContext(xDisplay, printContext); 

	/* Get the printer's screen */
	xScreen = OS.XpGetScreenOfContext(xDisplay, printContext);
	
	/* Initialize Motif */
	int widgetClass = OS.TopLevelShellWidgetClass();
	int shellHandle = OS.XtAppCreateShell(null, null, widgetClass, xDisplay, null, 0);
	OS.XtDestroyWidget(shellHandle);
	
	/* Initialize the default font */
	byte [] buffer = Converter.wcsToMbcs(null, "-*-courier-medium-r-*-*-*-120-*-*-*-*-*-*", true);
	int fontListEntry = OS.XmFontListEntryLoad(xDisplay, buffer, 0, OS.XmFONTLIST_DEFAULT_TAG);
	if (fontListEntry == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	defaultFontList = OS.XmFontListAppendEntry(0, fontListEntry);
	OS.XmFontListEntryFree(new int[]{fontListEntry});
}

protected void destroy() {
	//if (xDisplay != 0) OS.XCloseDisplay(xDisplay);
}

public int internal_new_GC(GCData data) {
	if (xDrawable == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int xGC = OS.XCreateGC(xDisplay, xDrawable, 0, null);
	if (xGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (data != null) {
		data.device = this;
		data.display = xDisplay;
		data.drawable = xDrawable; // not valid until after startJob
		data.fontList = defaultFontList;
		data.colormap = OS.XDefaultColormapOfScreen(xScreen);
	}
	return xGC;
}

public void internal_dispose_GC(int xGC, GCData data) {
	OS.XFreeGC(xDisplay, xGC);
}

public boolean startJob(String jobName) {
	checkDevice();
	byte [] buffer = Converter.wcsToMbcs(null, "*job-name: " + jobName, true);
	OS.XpSetAttributes(xDisplay, printContext, OS.XPJobAttr, buffer, OS.XPAttrMerge);
	OS.XpStartJob(xDisplay, OS.XPSpool);

	/* Create the xDrawable */
	XRectangle rect = new XRectangle();
	short [] width = new short [1];
	short [] height = new short [1];
	OS.XpGetPageDimensions(xDisplay, printContext, width, height, rect);
	xDrawable = OS.XCreateWindow(xDisplay, OS.XRootWindowOfScreen(xScreen), 
		0, 0, rect.width, rect.height, 0,
		OS.CopyFromParent, OS.CopyFromParent, OS.CopyFromParent, 0, 0);
	return true;
}

public void endJob() {
	checkDevice();
	OS.XpEndJob(xDisplay);
	OS.XFlush(xDisplay);
}

public void cancelJob() {
	checkDevice();
	OS.XpCancelJob(xDisplay, true);
}

public boolean startPage() {
	checkDevice();
	OS.XpStartPage(xDisplay, xDrawable);
	return true;
}

public void endPage() {
	checkDevice();
	OS.XpEndPage(xDisplay);
}

public Point getDPI() {
	checkDevice();
	byte [] buffer = Converter.wcsToMbcs(null, "default-printer-resolution", true);
	int pool = OS.XpGetOneAttribute(xDisplay, printContext, OS.XPDocAttr, buffer);
    int length = OS.strlen(pool);
	buffer = new byte[length];
	OS.memmove(buffer, pool, length);
	OS.XtFree(pool);
	String resolution = new String(buffer, 0, buffer.length);
	int res = 300; // default
	if (resolution.length() == 0) {
		/* If we can't get the info from the DocAttrs, ask the printer. */
		buffer = Converter.wcsToMbcs(null, "printer-resolutions-supported", true);
		pool = OS.XpGetOneAttribute(xDisplay, printContext, OS.XPPrinterAttr, buffer);
    		length = OS.strlen(pool);
		buffer = new byte[length];
		OS.memmove(buffer, pool, length);
		OS.XtFree(pool);
		int n = 0;
		while (!Character.isWhitespace((char)buffer[n]) && n < buffer.length) n++;
		resolution = new String(buffer, 0, n);
	}
	if (resolution.length() != 0) {
		try {
			res = Integer.parseInt(resolution);
		} catch (NumberFormatException ex) {}
	}
	return new Point(res, res);
}

public Rectangle getBounds() {
	checkDevice();
	XRectangle rect = new XRectangle();
	short [] width = new short [1];
	short [] height = new short [1];
	OS.XpGetPageDimensions(xDisplay, printContext, width, height, rect);
	return new Rectangle(0, 0, width[0], height[0]);
}

public Rectangle getClientArea() {
	checkDevice();
	XRectangle rect = new XRectangle();
	OS.XpGetPageDimensions(xDisplay, printContext, new short [1], new short [1], rect);
	return new Rectangle(0, 0, rect.width, rect.height);
}

public Rectangle computeTrim(int x, int y, int width, int height) {
	checkDevice();
	XRectangle rect = new XRectangle();
	short [] paperWidth = new short [1];
	short [] paperHeight = new short [1];
	OS.XpGetPageDimensions(xDisplay, printContext, paperWidth, paperHeight, rect);
	int hTrim = paperWidth[0] - rect.width;
	int vTrim = paperHeight[0] - rect.height;
	return new Rectangle(x - rect.x, y - rect.y, width + hTrim, height + vTrim);
}

public PrinterData getPrinterData() {
	return data;
}

protected void checkDevice() {
	if (xDisplay == 0) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
}

protected void release() {
	super.release();
	if (defaultFontList != 0) {
		OS.XmFontListFree(defaultFontList);
		defaultFontList = 0;
	}
	if (printContext != 0) {
		OS.XpDestroyContext(xDisplay, printContext);
		printContext = 0;
	}
	if (xDrawable != 0) {
		OS.XDestroyWindow(xDisplay, xDrawable);
		xDrawable = 0;
	}
	xScreen = 0;
	data = null;
}

}
