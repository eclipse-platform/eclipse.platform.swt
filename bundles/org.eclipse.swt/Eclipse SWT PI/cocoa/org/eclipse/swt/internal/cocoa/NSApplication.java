package org.eclipse.swt.internal.cocoa;

public class NSApplication extends NSObject {
	
private NSApplication(int id) {
	super(id);
}

public int get_class() {
	return OS.class_NSApplication;
}

public static NSApplication sharedApplication() {
	int id = OS.objc_msgSend(OS.class_NSApplication, OS.sel_sharedApplication);
	if (id == 0) return null;
	return new NSApplication(id);
}

public void run() {
	OS.objc_msgSend(id, OS.sel_run);
}

public void stop(NSObject sender) {
	OS.objc_msgSend(id, OS.sel_stop_1, sender != null ? sender.id : 0);
}
}
