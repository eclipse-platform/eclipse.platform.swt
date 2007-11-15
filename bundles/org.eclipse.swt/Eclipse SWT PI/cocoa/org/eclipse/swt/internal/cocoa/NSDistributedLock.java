package org.eclipse.swt.internal.cocoa;

public class NSDistributedLock extends NSObject {

public NSDistributedLock() {
	super();
}

public NSDistributedLock(int id) {
	super(id);
}

public void breakLock() {
	OS.objc_msgSend(this.id, OS.sel_breakLock);
}

public id initWithPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithPath_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSDate lockDate() {
	int result = OS.objc_msgSend(this.id, OS.sel_lockDate);
	return result != 0 ? new NSDate(result) : null;
}

public static NSDistributedLock lockWithPath(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSDistributedLock, OS.sel_lockWithPath_1, path != null ? path.id : 0);
	return result != 0 ? new NSDistributedLock(result) : null;
}

public boolean tryLock() {
	return OS.objc_msgSend(this.id, OS.sel_tryLock) != 0;
}

public void unlock() {
	OS.objc_msgSend(this.id, OS.sel_unlock);
}

}
