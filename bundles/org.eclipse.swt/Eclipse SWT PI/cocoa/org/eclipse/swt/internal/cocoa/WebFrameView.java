package org.eclipse.swt.internal.cocoa;

public class WebFrameView extends NSObject {

public WebFrameView() {
	super();
}

public WebFrameView(int id) {
	super(id);
}

public boolean allowsScrolling() {
	return OS.objc_msgSend(this.id, OS.sel_allowsScrolling) != 0;
}

public boolean canPrintHeadersAndFooters() {
	return OS.objc_msgSend(this.id, OS.sel_canPrintHeadersAndFooters) != 0;
}

public NSView  documentView() {
	int result = OS.objc_msgSend(this.id, OS.sel_documentView);
	return result != 0 ? new NSView (result) : null;
}

public boolean documentViewShouldHandlePrint() {
	return OS.objc_msgSend(this.id, OS.sel_documentViewShouldHandlePrint) != 0;
}

public void printDocumentView() {
	OS.objc_msgSend(this.id, OS.sel_printDocumentView);
}

public NSPrintOperation printOperationWithPrintInfo(NSPrintInfo printInfo) {
	int result = OS.objc_msgSend(this.id, OS.sel_printOperationWithPrintInfo_1, printInfo != null ? printInfo.id : 0);
	return result != 0 ? new NSPrintOperation(result) : null;
}

public void setAllowsScrolling(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsScrolling_1, flag);
}

public WebFrame webFrame() {
	int result = OS.objc_msgSend(this.id, OS.sel_webFrame);
	return result != 0 ? new WebFrame(result) : null;
}

}
