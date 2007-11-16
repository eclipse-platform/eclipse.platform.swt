package org.eclipse.swt.internal.cocoa;

public class WebDocumentRepresentation extends NSObject {

public WebDocumentRepresentation(int id) {
	super(id);
}

public NSString documentSource() {
	int id = OS.objc_msgSend(this.id, OS.sel_documentSource);
	return id != 0 ? new NSString(id) : null;
}
}
