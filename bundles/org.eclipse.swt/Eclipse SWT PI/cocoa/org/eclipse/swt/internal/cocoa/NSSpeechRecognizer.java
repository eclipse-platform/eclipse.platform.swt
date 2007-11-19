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

public class NSSpeechRecognizer extends NSObject {

public NSSpeechRecognizer() {
	super();
}

public NSSpeechRecognizer(int id) {
	super(id);
}

public boolean blocksOtherRecognizers() {
	return OS.objc_msgSend(this.id, OS.sel_blocksOtherRecognizers) != 0;
}

public NSArray commands() {
	int result = OS.objc_msgSend(this.id, OS.sel_commands);
	return result != 0 ? new NSArray(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public NSString displayedCommandsTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_displayedCommandsTitle);
	return result != 0 ? new NSString(result) : null;
}

public boolean listensInForegroundOnly() {
	return OS.objc_msgSend(this.id, OS.sel_listensInForegroundOnly) != 0;
}

public void setBlocksOtherRecognizers(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBlocksOtherRecognizers_1, flag);
}

public void setCommands(NSArray commands) {
	OS.objc_msgSend(this.id, OS.sel_setCommands_1, commands != null ? commands.id : 0);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setDisplayedCommandsTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setDisplayedCommandsTitle_1, title != null ? title.id : 0);
}

public void setListensInForegroundOnly(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setListensInForegroundOnly_1, flag);
}

public void startListening() {
	OS.objc_msgSend(this.id, OS.sel_startListening);
}

public void stopListening() {
	OS.objc_msgSend(this.id, OS.sel_stopListening);
}

}
