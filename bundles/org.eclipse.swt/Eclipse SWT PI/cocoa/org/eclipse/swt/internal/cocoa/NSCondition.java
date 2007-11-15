package org.eclipse.swt.internal.cocoa;

public class NSCondition extends NSObject {

public NSCondition() {
	super();
}

public NSCondition(int id) {
	super(id);
}

public void broadcast() {
	OS.objc_msgSend(this.id, OS.sel_broadcast);
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public void setName(NSString n) {
	OS.objc_msgSend(this.id, OS.sel_setName_1, n != null ? n.id : 0);
}

public void signal() {
	OS.objc_msgSend(this.id, OS.sel_signal);
}

//public void wait() {
//	OS.objc_msgSend(this.id, OS.sel_wait);
//}

public boolean waitUntilDate(NSDate limit) {
	return OS.objc_msgSend(this.id, OS.sel_waitUntilDate_1, limit != null ? limit.id : 0) != 0;
}

}
