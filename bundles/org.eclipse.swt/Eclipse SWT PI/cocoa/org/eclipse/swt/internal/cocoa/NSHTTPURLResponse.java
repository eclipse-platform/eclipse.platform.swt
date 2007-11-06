package org.eclipse.swt.internal.cocoa;

public class NSHTTPURLResponse extends NSURLResponse {

public NSHTTPURLResponse() {
	super();
}

public NSHTTPURLResponse(int id) {
	super(id);
}

public NSDictionary allHeaderFields() {
	int result = OS.objc_msgSend(this.id, OS.sel_allHeaderFields);
	return result != 0 ? new NSDictionary(result) : null;
}

public static NSString localizedStringForStatusCode(int statusCode) {
	int result = OS.objc_msgSend(OS.class_NSHTTPURLResponse, OS.sel_localizedStringForStatusCode_1, statusCode);
	return result != 0 ? new NSString(result) : null;
}

public int statusCode() {
	return OS.objc_msgSend(this.id, OS.sel_statusCode);
}

}
