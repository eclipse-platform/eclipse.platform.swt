package org.eclipse.swt.internal.cocoa;

public class NSPipe extends NSObject {

public NSPipe() {
	super();
}

public NSPipe(int id) {
	super(id);
}

public NSFileHandle fileHandleForReading() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileHandleForReading);
	return result != 0 ? new NSFileHandle(result) : null;
}

public NSFileHandle fileHandleForWriting() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileHandleForWriting);
	return result != 0 ? new NSFileHandle(result) : null;
}

public static id pipe() {
	int result = OS.objc_msgSend(OS.class_NSPipe, OS.sel_pipe);
	return result != 0 ? new id(result) : null;
}

}
