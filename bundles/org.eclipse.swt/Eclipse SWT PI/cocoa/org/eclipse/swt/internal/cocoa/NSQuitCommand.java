package org.eclipse.swt.internal.cocoa;

public class NSQuitCommand extends NSScriptCommand {

public NSQuitCommand() {
	super();
}

public NSQuitCommand(int id) {
	super(id);
}

public int saveOptions() {
	return OS.objc_msgSend(this.id, OS.sel_saveOptions);
}

}
