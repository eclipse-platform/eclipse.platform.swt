package org.eclipse.swt.internal.cocoa;

public class NSNibControlConnector extends NSObject {

public NSNibControlConnector() {
	super();
}

public NSNibControlConnector(int id) {
	super(id);
}

public void establishConnection() {
	OS.objc_msgSend(this.id, OS.sel_establishConnection);
}

}
