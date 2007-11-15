package org.eclipse.swt.internal.cocoa;

public class NSOperationQueue extends NSObject {

public NSOperationQueue() {
	super();
}

public NSOperationQueue(int id) {
	super(id);
}

public void addOperation(NSOperation op) {
	OS.objc_msgSend(this.id, OS.sel_addOperation_1, op != null ? op.id : 0);
}

public void cancelAllOperations() {
	OS.objc_msgSend(this.id, OS.sel_cancelAllOperations);
}

public boolean isSuspended() {
	return OS.objc_msgSend(this.id, OS.sel_isSuspended) != 0;
}

public int maxConcurrentOperationCount() {
	return OS.objc_msgSend(this.id, OS.sel_maxConcurrentOperationCount);
}

public NSArray operations() {
	int result = OS.objc_msgSend(this.id, OS.sel_operations);
	return result != 0 ? new NSArray(result) : null;
}

public void setMaxConcurrentOperationCount(int cnt) {
	OS.objc_msgSend(this.id, OS.sel_setMaxConcurrentOperationCount_1, cnt);
}

public void setSuspended(boolean b) {
	OS.objc_msgSend(this.id, OS.sel_setSuspended_1, b);
}

public void waitUntilAllOperationsAreFinished() {
	OS.objc_msgSend(this.id, OS.sel_waitUntilAllOperationsAreFinished);
}

}
