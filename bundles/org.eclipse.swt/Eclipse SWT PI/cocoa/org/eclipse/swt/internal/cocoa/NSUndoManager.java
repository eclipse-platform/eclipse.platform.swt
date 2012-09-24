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

public class NSUndoManager extends NSObject {

public NSUndoManager() {
	super();
}

public NSUndoManager(long /*int*/ id) {
	super(id);
}

public NSUndoManager(id id) {
	super(id);
}

public boolean canRedo() {
	return OS.objc_msgSend_bool(this.id, OS.sel_canRedo);
}

public boolean canUndo() {
	return OS.objc_msgSend_bool(this.id, OS.sel_canUndo);
}

public void redo() {
	OS.objc_msgSend(this.id, OS.sel_redo);
}

public void undo() {
	OS.objc_msgSend(this.id, OS.sel_undo);
}

}
