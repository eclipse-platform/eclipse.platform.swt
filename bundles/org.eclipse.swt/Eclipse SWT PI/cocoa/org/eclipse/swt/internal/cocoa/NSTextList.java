package org.eclipse.swt.internal.cocoa;

public class NSTextList extends NSObject {

public NSTextList() {
	super();
}

public NSTextList(int id) {
	super(id);
}

public id initWithMarkerFormat(NSString format, int mask) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithMarkerFormat_1options_1, format != null ? format.id : 0, mask);
	return result != 0 ? new id(result) : null;
}

public int listOptions() {
	return OS.objc_msgSend(this.id, OS.sel_listOptions);
}

public NSString markerForItemNumber(int itemNum) {
	int result = OS.objc_msgSend(this.id, OS.sel_markerForItemNumber_1, itemNum);
	return result != 0 ? new NSString(result) : null;
}

public NSString markerFormat() {
	int result = OS.objc_msgSend(this.id, OS.sel_markerFormat);
	return result != 0 ? new NSString(result) : null;
}

}
