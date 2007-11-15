package org.eclipse.swt.internal.cocoa;

public class NSCloseCommand extends NSScriptCommand {

public NSCloseCommand() {
	super();
}

public NSCloseCommand(int id) {
	super(id);
}

public int saveOptions() {
	return OS.objc_msgSend(this.id, OS.sel_saveOptions);
}

}
