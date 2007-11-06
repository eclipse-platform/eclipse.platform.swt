package org.eclipse.swt.internal.cocoa;

public class NSPropertyListSerialization extends NSObject {

public NSPropertyListSerialization() {
	super();
}

public NSPropertyListSerialization(int id) {
	super(id);
}

public static NSData dataFromPropertyList(id plist, int format, int errorString) {
	int result = OS.objc_msgSend(OS.class_NSPropertyListSerialization, OS.sel_dataFromPropertyList_1format_1errorDescription_1, plist != null ? plist.id : 0, format, errorString);
	return result != 0 ? new NSData(result) : null;
}

public static boolean propertyList(id plist, int format) {
	return OS.objc_msgSend(OS.class_NSPropertyListSerialization, OS.sel_propertyList_1isValidForFormat_1, plist != null ? plist.id : 0, format) != 0;
}

public static id propertyListFromData(NSData data, int opt, int format, int errorString) {
	int result = OS.objc_msgSend(OS.class_NSPropertyListSerialization, OS.sel_propertyListFromData_1mutabilityOption_1format_1errorDescription_1, data != null ? data.id : 0, opt, format, errorString);
	return result != 0 ? new id(result) : null;
}

}
