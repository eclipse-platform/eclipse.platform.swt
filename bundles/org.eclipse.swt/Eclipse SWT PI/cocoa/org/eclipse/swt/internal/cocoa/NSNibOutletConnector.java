package org.eclipse.swt.internal.cocoa;

public class NSNibOutletConnector extends NSNibConnector {

public NSNibOutletConnector() {
	super();
}

public NSNibOutletConnector(int id) {
	super(id);
}

public void establishConnection() {
	OS.objc_msgSend(this.id, OS.sel_establishConnection);
}

}
