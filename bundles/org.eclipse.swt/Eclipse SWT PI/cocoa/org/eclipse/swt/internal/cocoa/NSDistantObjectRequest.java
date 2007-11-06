package org.eclipse.swt.internal.cocoa;

public class NSDistantObjectRequest extends NSObject {

public NSDistantObjectRequest() {
	super();
}

public NSDistantObjectRequest(int id) {
	super(id);
}

public NSConnection connection() {
	int result = OS.objc_msgSend(this.id, OS.sel_connection);
	return result != 0 ? new NSConnection(result) : null;
}

public id conversation() {
	int result = OS.objc_msgSend(this.id, OS.sel_conversation);
	return result != 0 ? new id(result) : null;
}

public NSInvocation invocation() {
	int result = OS.objc_msgSend(this.id, OS.sel_invocation);
	return result != 0 ? new NSInvocation(result) : null;
}

public void replyWithException(NSException exception) {
	OS.objc_msgSend(this.id, OS.sel_replyWithException_1, exception != null ? exception.id : 0);
}

}
