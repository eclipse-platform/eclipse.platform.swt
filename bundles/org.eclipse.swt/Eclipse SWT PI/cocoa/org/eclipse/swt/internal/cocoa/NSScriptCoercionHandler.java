package org.eclipse.swt.internal.cocoa;

public class NSScriptCoercionHandler extends NSObject {

public NSScriptCoercionHandler() {
	super();
}

public NSScriptCoercionHandler(int id) {
	super(id);
}

public id coerceValue(id value, int toClass) {
	int result = OS.objc_msgSend(this.id, OS.sel_coerceValue_1toClass_1, value != null ? value.id : 0, toClass);
	return result != 0 ? new id(result) : null;
}

public void registerCoercer(id coercer, int selector, int fromClass, int toClass) {
	OS.objc_msgSend(this.id, OS.sel_registerCoercer_1selector_1toConvertFromClass_1toClass_1, coercer != null ? coercer.id : 0, selector, fromClass, toClass);
}

public static NSScriptCoercionHandler sharedCoercionHandler() {
	int result = OS.objc_msgSend(OS.class_NSScriptCoercionHandler, OS.sel_sharedCoercionHandler);
	return result != 0 ? new NSScriptCoercionHandler(result) : null;
}

}
