package org.eclipse.swt.internal.cocoa;

public class WebOpenPanelResultListener extends NSObject {

public WebOpenPanelResultListener(int id) {
	super(id);
}

public void cancel() {
	OS.objc_msgSend(id, OS.sel_cancel);
}

public void chooseFilename(NSString string) {
	OS.objc_msgSend(id, OS.sel_chooseFilename_1, string != null ? string.id : 0);
}
}
