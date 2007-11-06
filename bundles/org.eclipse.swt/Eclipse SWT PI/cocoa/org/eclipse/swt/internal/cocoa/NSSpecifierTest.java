package org.eclipse.swt.internal.cocoa;

public class NSSpecifierTest extends NSScriptWhoseTest {

public NSSpecifierTest() {
	super();
}

public NSSpecifierTest(int id) {
	super(id);
}

public id initWithObjectSpecifier(NSScriptObjectSpecifier obj1, int compOp, id obj2) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithObjectSpecifier_1comparisonOperator_1testObject_1, obj1 != null ? obj1.id : 0, compOp, obj2 != null ? obj2.id : 0);
	return result != 0 ? new id(result) : null;
}

}
