/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSUndoManager extends NSObject {

public NSUndoManager() {
	super();
}

public NSUndoManager(long id) {
	super(id);
}

public NSUndoManager(id id) {
	super(id);
}

public void beginUndoGrouping() {
	OS.objc_msgSend(this.id, OS.sel_beginUndoGrouping);
}

public boolean canRedo() {
	return OS.objc_msgSend_bool(this.id, OS.sel_canRedo);
}

public boolean canUndo() {
	return OS.objc_msgSend_bool(this.id, OS.sel_canUndo);
}

public void endUndoGrouping() {
	OS.objc_msgSend(this.id, OS.sel_endUndoGrouping);
}

public void redo() {
	OS.objc_msgSend(this.id, OS.sel_redo);
}

public void undo() {
	OS.objc_msgSend(this.id, OS.sel_undo);
}

}
