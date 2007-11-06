package org.eclipse.swt.internal.cocoa;

public class NSRecursiveLock extends NSObject {

public NSRecursiveLock() {
	super();
}

public NSRecursiveLock(int id) {
	super(id);
}

public boolean lockBeforeDate(NSDate limit) {
	return OS.objc_msgSend(this.id, OS.sel_lockBeforeDate_1, limit != null ? limit.id : 0) != 0;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public void setName(NSString n) {
	OS.objc_msgSend(this.id, OS.sel_setName_1, n != null ? n.id : 0);
}

public boolean tryLock() {
	return OS.objc_msgSend(this.id, OS.sel_tryLock) != 0;
}

}
