package org.eclipse.swt.internal.webkit;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;

public class IWebFramePrivate extends IUnknown {

public IWebFramePrivate(int /*long*/ address) {
	super(address);
}

public int frameBounds (int /*long*/[] result) {
	return COM.VtblCall (16, getAddress (), result);
}

public int getPrintedPageCount (int /*long*/ printDC, int [] pageCount) {
	return COM.VtblCall (9, getAddress (), printDC, pageCount);
}

public int setInPrintingMode (boolean value, int /*long*/ printDC) {
	return COM.VtblCall (8, getAddress (), value, printDC);
}

public int spoolPages (int /*long*/ printDC, int startPage, int endPage, int [] ctx) {
	return COM.VtblCall (10, getAddress (), printDC, startPage, endPage, ctx);
}

}
