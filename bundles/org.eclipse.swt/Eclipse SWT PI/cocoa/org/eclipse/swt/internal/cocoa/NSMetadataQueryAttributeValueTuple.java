package org.eclipse.swt.internal.cocoa;

public class NSMetadataQueryAttributeValueTuple extends NSObject {

public NSMetadataQueryAttributeValueTuple() {
	super();
}

public NSMetadataQueryAttributeValueTuple(int id) {
	super(id);
}

public NSString attribute() {
	int result = OS.objc_msgSend(this.id, OS.sel_attribute);
	return result != 0 ? new NSString(result) : null;
}

public int count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public id value() {
	int result = OS.objc_msgSend(this.id, OS.sel_value);
	return result != 0 ? new id(result) : null;
}

}
