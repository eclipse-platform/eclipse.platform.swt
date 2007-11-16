package org.eclipse.swt.internal.cocoa;

public class WebPolicyDecisionListener extends NSObject {

public WebPolicyDecisionListener(int id) {
	super(id);
}
	
public void download() {
	OS.objc_msgSend(id, OS.sel_download);
}

public void use() {
	OS.objc_msgSend(id, OS.sel_use);
}

public void ignore() {
	OS.objc_msgSend(id, OS.sel_ignore);
}
}
