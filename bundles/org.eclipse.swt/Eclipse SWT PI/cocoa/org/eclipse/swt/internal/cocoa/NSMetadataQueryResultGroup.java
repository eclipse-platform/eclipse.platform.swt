package org.eclipse.swt.internal.cocoa;

public class NSMetadataQueryResultGroup extends NSObject {

public NSMetadataQueryResultGroup() {
	super();
}

public NSMetadataQueryResultGroup(int id) {
	super(id);
}

public NSString attribute() {
	int result = OS.objc_msgSend(this.id, OS.sel_attribute);
	return result != 0 ? new NSString(result) : null;
}

public id resultAtIndex(int idx) {
	int result = OS.objc_msgSend(this.id, OS.sel_resultAtIndex_1, idx);
	return result != 0 ? new id(result) : null;
}

public int resultCount() {
	return OS.objc_msgSend(this.id, OS.sel_resultCount);
}

public NSArray results() {
	int result = OS.objc_msgSend(this.id, OS.sel_results);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray subgroups() {
	int result = OS.objc_msgSend(this.id, OS.sel_subgroups);
	return result != 0 ? new NSArray(result) : null;
}

public id value() {
	int result = OS.objc_msgSend(this.id, OS.sel_value);
	return result != 0 ? new id(result) : null;
}

}
