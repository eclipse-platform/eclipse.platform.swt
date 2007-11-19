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

public class NSResponder extends NSObject {

public NSResponder() {
	super();
}

public NSResponder(int id) {
	super(id);
}

public boolean acceptsFirstResponder() {
	return OS.objc_msgSend(this.id, OS.sel_acceptsFirstResponder) != 0;
}

public boolean becomeFirstResponder() {
	return OS.objc_msgSend(this.id, OS.sel_becomeFirstResponder) != 0;
}

public void cancelOperation(id sender) {
	OS.objc_msgSend(this.id, OS.sel_cancelOperation_1, sender != null ? sender.id : 0);
}

public void capitalizeWord(id sender) {
	OS.objc_msgSend(this.id, OS.sel_capitalizeWord_1, sender != null ? sender.id : 0);
}

public void centerSelectionInVisibleArea(id sender) {
	OS.objc_msgSend(this.id, OS.sel_centerSelectionInVisibleArea_1, sender != null ? sender.id : 0);
}

public void changeCaseOfLetter(id sender) {
	OS.objc_msgSend(this.id, OS.sel_changeCaseOfLetter_1, sender != null ? sender.id : 0);
}

public void complete(id sender) {
	OS.objc_msgSend(this.id, OS.sel_complete_1, sender != null ? sender.id : 0);
}

public void cursorUpdate(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_cursorUpdate_1, event != null ? event.id : 0);
}

public void deleteBackward(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deleteBackward_1, sender != null ? sender.id : 0);
}

public void deleteBackwardByDecomposingPreviousCharacter(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deleteBackwardByDecomposingPreviousCharacter_1, sender != null ? sender.id : 0);
}

public void deleteForward(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deleteForward_1, sender != null ? sender.id : 0);
}

public void deleteToBeginningOfLine(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deleteToBeginningOfLine_1, sender != null ? sender.id : 0);
}

public void deleteToBeginningOfParagraph(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deleteToBeginningOfParagraph_1, sender != null ? sender.id : 0);
}

public void deleteToEndOfLine(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deleteToEndOfLine_1, sender != null ? sender.id : 0);
}

public void deleteToEndOfParagraph(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deleteToEndOfParagraph_1, sender != null ? sender.id : 0);
}

public void deleteToMark(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deleteToMark_1, sender != null ? sender.id : 0);
}

public void deleteWordBackward(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deleteWordBackward_1, sender != null ? sender.id : 0);
}

public void deleteWordForward(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deleteWordForward_1, sender != null ? sender.id : 0);
}

public void doCommandBySelector(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_doCommandBySelector_1, aSelector);
}

public void flagsChanged(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_flagsChanged_1, theEvent != null ? theEvent.id : 0);
}

public void flushBufferedKeyEvents() {
	OS.objc_msgSend(this.id, OS.sel_flushBufferedKeyEvents);
}

public void helpRequested(NSEvent eventPtr) {
	OS.objc_msgSend(this.id, OS.sel_helpRequested_1, eventPtr != null ? eventPtr.id : 0);
}

public void indent(id sender) {
	OS.objc_msgSend(this.id, OS.sel_indent_1, sender != null ? sender.id : 0);
}

public void insertBacktab(id sender) {
	OS.objc_msgSend(this.id, OS.sel_insertBacktab_1, sender != null ? sender.id : 0);
}

public void insertContainerBreak(id sender) {
	OS.objc_msgSend(this.id, OS.sel_insertContainerBreak_1, sender != null ? sender.id : 0);
}

public void insertLineBreak(id sender) {
	OS.objc_msgSend(this.id, OS.sel_insertLineBreak_1, sender != null ? sender.id : 0);
}

public void insertNewline(id sender) {
	OS.objc_msgSend(this.id, OS.sel_insertNewline_1, sender != null ? sender.id : 0);
}

public void insertNewlineIgnoringFieldEditor(id sender) {
	OS.objc_msgSend(this.id, OS.sel_insertNewlineIgnoringFieldEditor_1, sender != null ? sender.id : 0);
}

public void insertParagraphSeparator(id sender) {
	OS.objc_msgSend(this.id, OS.sel_insertParagraphSeparator_1, sender != null ? sender.id : 0);
}

public void insertTab(id sender) {
	OS.objc_msgSend(this.id, OS.sel_insertTab_1, sender != null ? sender.id : 0);
}

public void insertTabIgnoringFieldEditor(id sender) {
	OS.objc_msgSend(this.id, OS.sel_insertTabIgnoringFieldEditor_1, sender != null ? sender.id : 0);
}

public void insertText(id insertString) {
	OS.objc_msgSend(this.id, OS.sel_insertText_1, insertString != null ? insertString.id : 0);
}

public int interfaceStyle() {
	return OS.objc_msgSend(this.id, OS.sel_interfaceStyle);
}

public void interpretKeyEvents(NSArray eventArray) {
	OS.objc_msgSend(this.id, OS.sel_interpretKeyEvents_1, eventArray != null ? eventArray.id : 0);
}

public void keyDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_keyDown_1, theEvent != null ? theEvent.id : 0);
}

public void keyUp(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_keyUp_1, theEvent != null ? theEvent.id : 0);
}

public void lowercaseWord(id sender) {
	OS.objc_msgSend(this.id, OS.sel_lowercaseWord_1, sender != null ? sender.id : 0);
}

public NSMenu menu() {
	int result = OS.objc_msgSend(this.id, OS.sel_menu);
	return result != 0 ? new NSMenu(result) : null;
}

public void mouseDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseDown_1, theEvent != null ? theEvent.id : 0);
}

public void mouseDragged(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseDragged_1, theEvent != null ? theEvent.id : 0);
}

public void mouseEntered(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseEntered_1, theEvent != null ? theEvent.id : 0);
}

public void mouseExited(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseExited_1, theEvent != null ? theEvent.id : 0);
}

public void mouseMoved(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseMoved_1, theEvent != null ? theEvent.id : 0);
}

public void mouseUp(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseUp_1, theEvent != null ? theEvent.id : 0);
}

public void moveBackward(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveBackward_1, sender != null ? sender.id : 0);
}

public void moveBackwardAndModifySelection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveBackwardAndModifySelection_1, sender != null ? sender.id : 0);
}

public void moveDown(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveDown_1, sender != null ? sender.id : 0);
}

public void moveDownAndModifySelection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveDownAndModifySelection_1, sender != null ? sender.id : 0);
}

public void moveForward(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveForward_1, sender != null ? sender.id : 0);
}

public void moveForwardAndModifySelection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveForwardAndModifySelection_1, sender != null ? sender.id : 0);
}

public void moveLeft(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveLeft_1, sender != null ? sender.id : 0);
}

public void moveLeftAndModifySelection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveLeftAndModifySelection_1, sender != null ? sender.id : 0);
}

public void moveRight(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveRight_1, sender != null ? sender.id : 0);
}

public void moveRightAndModifySelection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveRightAndModifySelection_1, sender != null ? sender.id : 0);
}

public void moveToBeginningOfDocument(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveToBeginningOfDocument_1, sender != null ? sender.id : 0);
}

public void moveToBeginningOfLine(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveToBeginningOfLine_1, sender != null ? sender.id : 0);
}

public void moveToBeginningOfParagraph(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveToBeginningOfParagraph_1, sender != null ? sender.id : 0);
}

public void moveToEndOfDocument(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveToEndOfDocument_1, sender != null ? sender.id : 0);
}

public void moveToEndOfLine(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveToEndOfLine_1, sender != null ? sender.id : 0);
}

public void moveToEndOfParagraph(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveToEndOfParagraph_1, sender != null ? sender.id : 0);
}

public void moveUp(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveUp_1, sender != null ? sender.id : 0);
}

public void moveUpAndModifySelection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveUpAndModifySelection_1, sender != null ? sender.id : 0);
}

public void moveWordBackward(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveWordBackward_1, sender != null ? sender.id : 0);
}

public void moveWordBackwardAndModifySelection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveWordBackwardAndModifySelection_1, sender != null ? sender.id : 0);
}

public void moveWordForward(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveWordForward_1, sender != null ? sender.id : 0);
}

public void moveWordForwardAndModifySelection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveWordForwardAndModifySelection_1, sender != null ? sender.id : 0);
}

public void moveWordLeft(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveWordLeft_1, sender != null ? sender.id : 0);
}

public void moveWordLeftAndModifySelection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveWordLeftAndModifySelection_1, sender != null ? sender.id : 0);
}

public void moveWordRight(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveWordRight_1, sender != null ? sender.id : 0);
}

public void moveWordRightAndModifySelection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveWordRightAndModifySelection_1, sender != null ? sender.id : 0);
}

public NSResponder nextResponder() {
	int result = OS.objc_msgSend(this.id, OS.sel_nextResponder);
	return result == this.id ? this : (result != 0 ? new NSResponder(result) : null);
}

public void noResponderFor(int eventSelector) {
	OS.objc_msgSend(this.id, OS.sel_noResponderFor_1, eventSelector);
}

public void otherMouseDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_otherMouseDown_1, theEvent != null ? theEvent.id : 0);
}

public void otherMouseDragged(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_otherMouseDragged_1, theEvent != null ? theEvent.id : 0);
}

public void otherMouseUp(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_otherMouseUp_1, theEvent != null ? theEvent.id : 0);
}

public void pageDown(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pageDown_1, sender != null ? sender.id : 0);
}

public void pageUp(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pageUp_1, sender != null ? sender.id : 0);
}

public boolean performKeyEquivalent(NSEvent theEvent) {
	return OS.objc_msgSend(this.id, OS.sel_performKeyEquivalent_1, theEvent != null ? theEvent.id : 0) != 0;
}

public boolean performMnemonic(NSString theString) {
	return OS.objc_msgSend(this.id, OS.sel_performMnemonic_1, theString != null ? theString.id : 0) != 0;
}

public boolean presentError_(NSError error) {
	return OS.objc_msgSend(this.id, OS.sel_presentError_1, error != null ? error.id : 0) != 0;
}

public void presentError_modalForWindow_delegate_didPresentSelector_contextInfo_(NSError error, NSWindow window, id delegate, int didPresentSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_presentError_1modalForWindow_1delegate_1didPresentSelector_1contextInfo_1, error != null ? error.id : 0, window != null ? window.id : 0, delegate != null ? delegate.id : 0, didPresentSelector, contextInfo);
}

public boolean resignFirstResponder() {
	return OS.objc_msgSend(this.id, OS.sel_resignFirstResponder) != 0;
}

public void rightMouseDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_rightMouseDown_1, theEvent != null ? theEvent.id : 0);
}

public void rightMouseDragged(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_rightMouseDragged_1, theEvent != null ? theEvent.id : 0);
}

public void rightMouseUp(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_rightMouseUp_1, theEvent != null ? theEvent.id : 0);
}

public void scrollLineDown(id sender) {
	OS.objc_msgSend(this.id, OS.sel_scrollLineDown_1, sender != null ? sender.id : 0);
}

public void scrollLineUp(id sender) {
	OS.objc_msgSend(this.id, OS.sel_scrollLineUp_1, sender != null ? sender.id : 0);
}

public void scrollPageDown(id sender) {
	OS.objc_msgSend(this.id, OS.sel_scrollPageDown_1, sender != null ? sender.id : 0);
}

public void scrollPageUp(id sender) {
	OS.objc_msgSend(this.id, OS.sel_scrollPageUp_1, sender != null ? sender.id : 0);
}

public void scrollWheel(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_scrollWheel_1, theEvent != null ? theEvent.id : 0);
}

public void selectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectAll_1, sender != null ? sender.id : 0);
}

public void selectLine(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectLine_1, sender != null ? sender.id : 0);
}

public void selectParagraph(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectParagraph_1, sender != null ? sender.id : 0);
}

public void selectToMark(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectToMark_1, sender != null ? sender.id : 0);
}

public void selectWord(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectWord_1, sender != null ? sender.id : 0);
}

public void setInterfaceStyle(int interfaceStyle) {
	OS.objc_msgSend(this.id, OS.sel_setInterfaceStyle_1, interfaceStyle);
}

public void setMark(id sender) {
	OS.objc_msgSend(this.id, OS.sel_setMark_1, sender != null ? sender.id : 0);
}

public void setMenu(NSMenu menu) {
	OS.objc_msgSend(this.id, OS.sel_setMenu_1, menu != null ? menu.id : 0);
}

public void setNextResponder(NSResponder aResponder) {
	OS.objc_msgSend(this.id, OS.sel_setNextResponder_1, aResponder != null ? aResponder.id : 0);
}

public boolean shouldBeTreatedAsInkEvent(NSEvent theEvent) {
	return OS.objc_msgSend(this.id, OS.sel_shouldBeTreatedAsInkEvent_1, theEvent != null ? theEvent.id : 0) != 0;
}

public void showContextHelp(id sender) {
	OS.objc_msgSend(this.id, OS.sel_showContextHelp_1, sender != null ? sender.id : 0);
}

public void swapWithMark(id sender) {
	OS.objc_msgSend(this.id, OS.sel_swapWithMark_1, sender != null ? sender.id : 0);
}

public void tabletPoint(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_tabletPoint_1, theEvent != null ? theEvent.id : 0);
}

public void tabletProximity(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_tabletProximity_1, theEvent != null ? theEvent.id : 0);
}

public void transpose(id sender) {
	OS.objc_msgSend(this.id, OS.sel_transpose_1, sender != null ? sender.id : 0);
}

public void transposeWords(id sender) {
	OS.objc_msgSend(this.id, OS.sel_transposeWords_1, sender != null ? sender.id : 0);
}

public boolean tryToPerform(int anAction, id anObject) {
	return OS.objc_msgSend(this.id, OS.sel_tryToPerform_1with_1, anAction, anObject != null ? anObject.id : 0) != 0;
}

public NSUndoManager undoManager() {
	int result = OS.objc_msgSend(this.id, OS.sel_undoManager);
	return result != 0 ? new NSUndoManager(result) : null;
}

public void uppercaseWord(id sender) {
	OS.objc_msgSend(this.id, OS.sel_uppercaseWord_1, sender != null ? sender.id : 0);
}

public id validRequestorForSendType(NSString sendType, NSString returnType) {
	int result = OS.objc_msgSend(this.id, OS.sel_validRequestorForSendType_1returnType_1, sendType != null ? sendType.id : 0, returnType != null ? returnType.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSError willPresentError(NSError error) {
	int result = OS.objc_msgSend(this.id, OS.sel_willPresentError_1, error != null ? error.id : 0);
	return result != 0 ? new NSError(result) : null;
}

public void yank(id sender) {
	OS.objc_msgSend(this.id, OS.sel_yank_1, sender != null ? sender.id : 0);
}

}
