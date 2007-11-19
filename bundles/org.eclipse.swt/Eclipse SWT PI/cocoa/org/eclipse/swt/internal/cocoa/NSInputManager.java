/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSInputManager extends NSObject {

public NSInputManager() {
	super();
}

public NSInputManager(int id) {
	super(id);
}

public static NSInputManager currentInputManager() {
	int result = OS.objc_msgSend(OS.class_NSInputManager, OS.sel_currentInputManager);
	return result != 0 ? new NSInputManager(result) : null;
}

public static void cycleToNextInputLanguage(id sender) {
	OS.objc_msgSend(OS.class_NSInputManager, OS.sel_cycleToNextInputLanguage_1, sender != null ? sender.id : 0);
}

public static void cycleToNextInputServerInLanguage(id sender) {
	OS.objc_msgSend(OS.class_NSInputManager, OS.sel_cycleToNextInputServerInLanguage_1, sender != null ? sender.id : 0);
}

public boolean handleMouseEvent(NSEvent theMouseEvent) {
	return OS.objc_msgSend(this.id, OS.sel_handleMouseEvent_1, theMouseEvent != null ? theMouseEvent.id : 0) != 0;
}

public NSImage image() {
	int result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public NSInputManager initWithName(NSString inputServerName, NSString hostName) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithName_1host_1, inputServerName != null ? inputServerName.id : 0, hostName != null ? hostName.id : 0);
	return result == this.id ? this : (result != 0 ? new NSInputManager(result) : null);
}

public NSString language() {
	int result = OS.objc_msgSend(this.id, OS.sel_language);
	return result != 0 ? new NSString(result) : null;
}

public NSString localizedInputManagerName() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedInputManagerName);
	return result != 0 ? new NSString(result) : null;
}

public void markedTextAbandoned(id cli) {
	OS.objc_msgSend(this.id, OS.sel_markedTextAbandoned_1, cli != null ? cli.id : 0);
}

public void markedTextSelectionChanged(NSRange newSel, id cli) {
	OS.objc_msgSend(this.id, OS.sel_markedTextSelectionChanged_1client_1, newSel, cli != null ? cli.id : 0);
}

public NSInputServer server() {
	int result = OS.objc_msgSend(this.id, OS.sel_server);
	return result != 0 ? new NSInputServer(result) : null;
}

public boolean wantsToDelayTextChangeNotifications() {
	return OS.objc_msgSend(this.id, OS.sel_wantsToDelayTextChangeNotifications) != 0;
}

public boolean wantsToHandleMouseEvents() {
	return OS.objc_msgSend(this.id, OS.sel_wantsToHandleMouseEvents) != 0;
}

public boolean wantsToInterpretAllKeystrokes() {
	return OS.objc_msgSend(this.id, OS.sel_wantsToInterpretAllKeystrokes) != 0;
}

}
