package org.eclipse.swt.internal.cocoa;

public class NSCreateCommand extends NSScriptCommand {

public NSCreateCommand() {
	super();
}

public NSCreateCommand(int id) {
	super(id);
}

public NSScriptClassDescription createClassDescription() {
	int result = OS.objc_msgSend(this.id, OS.sel_createClassDescription);
	return result != 0 ? new NSScriptClassDescription(result) : null;
}

public NSDictionary resolvedKeyDictionary() {
	int result = OS.objc_msgSend(this.id, OS.sel_resolvedKeyDictionary);
	return result != 0 ? new NSDictionary(result) : null;
}

}
