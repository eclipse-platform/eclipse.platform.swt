package org.eclipse.swt.internal.cocoa;

public class NSProcessInfo extends NSObject {

public NSProcessInfo() {
	super();
}

public NSProcessInfo(int id) {
	super(id);
}

public int activeProcessorCount() {
	return OS.objc_msgSend(this.id, OS.sel_activeProcessorCount);
}

public NSArray arguments() {
	int result = OS.objc_msgSend(this.id, OS.sel_arguments);
	return result != 0 ? new NSArray(result) : null;
}

public NSDictionary environment() {
	int result = OS.objc_msgSend(this.id, OS.sel_environment);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSString globallyUniqueString() {
	int result = OS.objc_msgSend(this.id, OS.sel_globallyUniqueString);
	return result != 0 ? new NSString(result) : null;
}

public NSString hostName() {
	int result = OS.objc_msgSend(this.id, OS.sel_hostName);
	return result != 0 ? new NSString(result) : null;
}

public int operatingSystem() {
	return OS.objc_msgSend(this.id, OS.sel_operatingSystem);
}

public NSString operatingSystemName() {
	int result = OS.objc_msgSend(this.id, OS.sel_operatingSystemName);
	return result != 0 ? new NSString(result) : null;
}

public NSString operatingSystemVersionString() {
	int result = OS.objc_msgSend(this.id, OS.sel_operatingSystemVersionString);
	return result != 0 ? new NSString(result) : null;
}

public long physicalMemory() {
	return (long)OS.objc_msgSend(this.id, OS.sel_physicalMemory);
}

public int processIdentifier() {
	return OS.objc_msgSend(this.id, OS.sel_processIdentifier);
}

public static NSProcessInfo processInfo() {
	int result = OS.objc_msgSend(OS.class_NSProcessInfo, OS.sel_processInfo);
	return result != 0 ? new NSProcessInfo(result) : null;
}

public NSString processName() {
	int result = OS.objc_msgSend(this.id, OS.sel_processName);
	return result != 0 ? new NSString(result) : null;
}

public int processorCount() {
	return OS.objc_msgSend(this.id, OS.sel_processorCount);
}

public void setProcessName(NSString newName) {
	OS.objc_msgSend(this.id, OS.sel_setProcessName_1, newName != null ? newName.id : 0);
}

}
