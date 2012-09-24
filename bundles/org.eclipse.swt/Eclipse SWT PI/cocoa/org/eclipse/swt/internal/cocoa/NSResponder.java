/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSResponder extends NSObject {

public NSResponder() {
	super();
}

public NSResponder(long /*int*/ id) {
	super(id);
}

public NSResponder(id id) {
	super(id);
}

public boolean acceptsFirstResponder() {
	return OS.objc_msgSend_bool(this.id, OS.sel_acceptsFirstResponder);
}

public boolean becomeFirstResponder() {
	return OS.objc_msgSend_bool(this.id, OS.sel_becomeFirstResponder);
}

public void beginGestureWithEvent(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_beginGestureWithEvent_, event != null ? event.id : 0);
}

public void cancelOperation(id sender) {
	OS.objc_msgSend(this.id, OS.sel_cancelOperation_, sender != null ? sender.id : 0);
}

public void cursorUpdate(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_cursorUpdate_, event != null ? event.id : 0);
}

public void doCommandBySelector(long /*int*/ aSelector) {
	OS.objc_msgSend(this.id, OS.sel_doCommandBySelector_, aSelector);
}

public void endGestureWithEvent(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_endGestureWithEvent_, event != null ? event.id : 0);
}

public void flagsChanged(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_flagsChanged_, theEvent != null ? theEvent.id : 0);
}

public void helpRequested(NSEvent eventPtr) {
	OS.objc_msgSend(this.id, OS.sel_helpRequested_, eventPtr != null ? eventPtr.id : 0);
}

public void insertText(id insertString) {
	OS.objc_msgSend(this.id, OS.sel_insertText_, insertString != null ? insertString.id : 0);
}

public void interpretKeyEvents(NSArray eventArray) {
	OS.objc_msgSend(this.id, OS.sel_interpretKeyEvents_, eventArray != null ? eventArray.id : 0);
}

public void keyDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_keyDown_, theEvent != null ? theEvent.id : 0);
}

public void keyUp(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_keyUp_, theEvent != null ? theEvent.id : 0);
}

public void magnifyWithEvent(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_magnifyWithEvent_, event != null ? event.id : 0);
}

public void mouseDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseDown_, theEvent != null ? theEvent.id : 0);
}

public void mouseDragged(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseDragged_, theEvent != null ? theEvent.id : 0);
}

public void mouseEntered(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseEntered_, theEvent != null ? theEvent.id : 0);
}

public void mouseExited(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseExited_, theEvent != null ? theEvent.id : 0);
}

public void mouseMoved(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseMoved_, theEvent != null ? theEvent.id : 0);
}

public void mouseUp(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseUp_, theEvent != null ? theEvent.id : 0);
}

public void moveToBeginningOfParagraph(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveToBeginningOfParagraph_, sender != null ? sender.id : 0);
}

public void moveToEndOfParagraph(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveToEndOfParagraph_, sender != null ? sender.id : 0);
}

public void moveUp(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveUp_, sender != null ? sender.id : 0);
}

public void noResponderFor(long /*int*/ eventSelector) {
	OS.objc_msgSend(this.id, OS.sel_noResponderFor_, eventSelector);
}

public void otherMouseDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_otherMouseDown_, theEvent != null ? theEvent.id : 0);
}

public void otherMouseDragged(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_otherMouseDragged_, theEvent != null ? theEvent.id : 0);
}

public void otherMouseUp(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_otherMouseUp_, theEvent != null ? theEvent.id : 0);
}

public void pageDown(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pageDown_, sender != null ? sender.id : 0);
}

public void pageUp(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pageUp_, sender != null ? sender.id : 0);
}

public boolean performKeyEquivalent(NSEvent theEvent) {
	return OS.objc_msgSend_bool(this.id, OS.sel_performKeyEquivalent_, theEvent != null ? theEvent.id : 0);
}

public boolean resignFirstResponder() {
	return OS.objc_msgSend_bool(this.id, OS.sel_resignFirstResponder);
}

public void rightMouseDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_rightMouseDown_, theEvent != null ? theEvent.id : 0);
}

public void rightMouseDragged(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_rightMouseDragged_, theEvent != null ? theEvent.id : 0);
}

public void rightMouseUp(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_rightMouseUp_, theEvent != null ? theEvent.id : 0);
}

public void rotateWithEvent(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_rotateWithEvent_, event != null ? event.id : 0);
}

public void scrollWheel(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_scrollWheel_, theEvent != null ? theEvent.id : 0);
}

public void swipeWithEvent(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_swipeWithEvent_, event != null ? event.id : 0);
}

public void touchesBeganWithEvent(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_touchesBeganWithEvent_, event != null ? event.id : 0);
}

public void touchesCancelledWithEvent(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_touchesCancelledWithEvent_, event != null ? event.id : 0);
}

public void touchesEndedWithEvent(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_touchesEndedWithEvent_, event != null ? event.id : 0);
}

public void touchesMovedWithEvent(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_touchesMovedWithEvent_, event != null ? event.id : 0);
}

public NSUndoManager undoManager() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_undoManager);
	return result != 0 ? new NSUndoManager(result) : null;
}

public id validRequestorForSendType(NSString sendType, NSString returnType) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_validRequestorForSendType_returnType_, sendType != null ? sendType.id : 0, returnType != null ? returnType.id : 0);
	return result != 0 ? new id(result) : null;
}

}
