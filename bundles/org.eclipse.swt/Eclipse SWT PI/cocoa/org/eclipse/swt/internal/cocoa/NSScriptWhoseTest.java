package org.eclipse.swt.internal.cocoa;

public class NSScriptWhoseTest extends NSObject {

public NSScriptWhoseTest() {
	super();
}

public NSScriptWhoseTest(int id) {
	super(id);
}

public boolean isTrue() {
	return OS.objc_msgSend(this.id, OS.sel_isTrue) != 0;
}

}
