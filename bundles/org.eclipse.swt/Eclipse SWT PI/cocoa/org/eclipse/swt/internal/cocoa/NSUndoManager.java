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

public class NSUndoManager extends NSObject {

public NSUndoManager() {
	super();
}

public NSUndoManager(int id) {
	super(id);
}

public void beginUndoGrouping() {
	OS.objc_msgSend(this.id, OS.sel_beginUndoGrouping);
}

public boolean canRedo() {
	return OS.objc_msgSend(this.id, OS.sel_canRedo) != 0;
}

public boolean canUndo() {
	return OS.objc_msgSend(this.id, OS.sel_canUndo) != 0;
}

public void disableUndoRegistration() {
	OS.objc_msgSend(this.id, OS.sel_disableUndoRegistration);
}

public void enableUndoRegistration() {
	OS.objc_msgSend(this.id, OS.sel_enableUndoRegistration);
}

public void endUndoGrouping() {
	OS.objc_msgSend(this.id, OS.sel_endUndoGrouping);
}

public int groupingLevel() {
	return OS.objc_msgSend(this.id, OS.sel_groupingLevel);
}

public boolean groupsByEvent() {
	return OS.objc_msgSend(this.id, OS.sel_groupsByEvent) != 0;
}

public boolean isRedoing() {
	return OS.objc_msgSend(this.id, OS.sel_isRedoing) != 0;
}

public boolean isUndoRegistrationEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isUndoRegistrationEnabled) != 0;
}

public boolean isUndoing() {
	return OS.objc_msgSend(this.id, OS.sel_isUndoing) != 0;
}

public int levelsOfUndo() {
	return OS.objc_msgSend(this.id, OS.sel_levelsOfUndo);
}

public id prepareWithInvocationTarget(id target) {
	int result = OS.objc_msgSend(this.id, OS.sel_prepareWithInvocationTarget_1, target != null ? target.id : 0);
	return result != 0 ? new id(result) : null;
}

public void redo() {
	OS.objc_msgSend(this.id, OS.sel_redo);
}

public NSString redoActionName() {
	int result = OS.objc_msgSend(this.id, OS.sel_redoActionName);
	return result != 0 ? new NSString(result) : null;
}

public NSString redoMenuItemTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_redoMenuItemTitle);
	return result != 0 ? new NSString(result) : null;
}

public NSString redoMenuTitleForUndoActionName(NSString actionName) {
	int result = OS.objc_msgSend(this.id, OS.sel_redoMenuTitleForUndoActionName_1, actionName != null ? actionName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public void registerUndoWithTarget(id target, int selector, id anObject) {
	OS.objc_msgSend(this.id, OS.sel_registerUndoWithTarget_1selector_1object_1, target != null ? target.id : 0, selector, anObject != null ? anObject.id : 0);
}

public void removeAllActions() {
	OS.objc_msgSend(this.id, OS.sel_removeAllActions);
}

public void removeAllActionsWithTarget(id target) {
	OS.objc_msgSend(this.id, OS.sel_removeAllActionsWithTarget_1, target != null ? target.id : 0);
}

public NSArray runLoopModes() {
	int result = OS.objc_msgSend(this.id, OS.sel_runLoopModes);
	return result != 0 ? new NSArray(result) : null;
}

public void setActionName(NSString actionName) {
	OS.objc_msgSend(this.id, OS.sel_setActionName_1, actionName != null ? actionName.id : 0);
}

public void setGroupsByEvent(boolean groupsByEvent) {
	OS.objc_msgSend(this.id, OS.sel_setGroupsByEvent_1, groupsByEvent);
}

public void setLevelsOfUndo(int levels) {
	OS.objc_msgSend(this.id, OS.sel_setLevelsOfUndo_1, levels);
}

public void setRunLoopModes(NSArray runLoopModes) {
	OS.objc_msgSend(this.id, OS.sel_setRunLoopModes_1, runLoopModes != null ? runLoopModes.id : 0);
}

public void undo() {
	OS.objc_msgSend(this.id, OS.sel_undo);
}

public NSString undoActionName() {
	int result = OS.objc_msgSend(this.id, OS.sel_undoActionName);
	return result != 0 ? new NSString(result) : null;
}

public NSString undoMenuItemTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_undoMenuItemTitle);
	return result != 0 ? new NSString(result) : null;
}

public NSString undoMenuTitleForUndoActionName(NSString actionName) {
	int result = OS.objc_msgSend(this.id, OS.sel_undoMenuTitleForUndoActionName_1, actionName != null ? actionName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public void undoNestedGroup() {
	OS.objc_msgSend(this.id, OS.sel_undoNestedGroup);
}

}
