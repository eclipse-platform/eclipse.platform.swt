package org.eclipse.swt.internal.cocoa;

public class NSRangeSpecifier extends NSScriptObjectSpecifier {

public NSRangeSpecifier() {
	super();
}

public NSRangeSpecifier(int id) {
	super(id);
}

public NSScriptObjectSpecifier endSpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_endSpecifier);
	return result != 0 ? new NSScriptObjectSpecifier(result) : null;
}

public id initWithContainerClassDescription(NSScriptClassDescription classDesc, NSScriptObjectSpecifier container, NSString property, NSScriptObjectSpecifier startSpec, NSScriptObjectSpecifier endSpec) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContainerClassDescription_1containerSpecifier_1key_1startSpecifier_1endSpecifier_1, classDesc != null ? classDesc.id : 0, container != null ? container.id : 0, property != null ? property.id : 0, startSpec != null ? startSpec.id : 0, endSpec != null ? endSpec.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setEndSpecifier(NSScriptObjectSpecifier endSpec) {
	OS.objc_msgSend(this.id, OS.sel_setEndSpecifier_1, endSpec != null ? endSpec.id : 0);
}

public void setStartSpecifier(NSScriptObjectSpecifier startSpec) {
	OS.objc_msgSend(this.id, OS.sel_setStartSpecifier_1, startSpec != null ? startSpec.id : 0);
}

public NSScriptObjectSpecifier startSpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_startSpecifier);
	return result != 0 ? new NSScriptObjectSpecifier(result) : null;
}

}
