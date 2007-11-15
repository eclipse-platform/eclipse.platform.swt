package org.eclipse.swt.internal.cocoa;

public class NSAssertionHandler extends NSObject {

public NSAssertionHandler() {
	super();
}

public NSAssertionHandler(int id) {
	super(id);
}

public static NSAssertionHandler currentHandler() {
	int result = OS.objc_msgSend(OS.class_NSAssertionHandler, OS.sel_currentHandler);
	return result != 0 ? new NSAssertionHandler(result) : null;
}

public void handleFailureInFunction(NSString functionName, NSString fileName, int line, NSString description) {
	OS.objc_msgSend(this.id, OS.sel_handleFailureInFunction_1file_1lineNumber_1description_1, functionName != null ? functionName.id : 0, fileName != null ? fileName.id : 0, line, description != null ? description.id : 0);
}

public void handleFailureInMethod(int selector, id object, NSString fileName, int line, NSString description) {
	OS.objc_msgSend(this.id, OS.sel_handleFailureInMethod_1object_1file_1lineNumber_1description_1, selector, object != null ? object.id : 0, fileName != null ? fileName.id : 0, line, description != null ? description.id : 0);
}

}
