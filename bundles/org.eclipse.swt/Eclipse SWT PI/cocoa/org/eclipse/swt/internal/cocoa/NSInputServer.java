package org.eclipse.swt.internal.cocoa;

public class NSInputServer extends NSObject {

public NSInputServer() {
	super();
}

public NSInputServer(int id) {
	super(id);
}

public id initWithDelegate(id aDelegate, NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDelegate_1name_1, aDelegate != null ? aDelegate.id : 0, name != null ? name.id : 0);
	return result != 0 ? new id(result) : null;
}

}
