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

public class NSMutableIndexSet extends NSIndexSet {

public NSMutableIndexSet() {
	super();
}

public NSMutableIndexSet(int id) {
	super(id);
}

public void addIndex(int value) {
	OS.objc_msgSend(this.id, OS.sel_addIndex_1, value);
}

public void addIndexes(NSIndexSet indexSet) {
	OS.objc_msgSend(this.id, OS.sel_addIndexes_1, indexSet != null ? indexSet.id : 0);
}

public void addIndexesInRange(NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_addIndexesInRange_1, range);
}

public void removeAllIndexes() {
	OS.objc_msgSend(this.id, OS.sel_removeAllIndexes);
}

public void removeIndex(int value) {
	OS.objc_msgSend(this.id, OS.sel_removeIndex_1, value);
}

public void removeIndexes(NSIndexSet indexSet) {
	OS.objc_msgSend(this.id, OS.sel_removeIndexes_1, indexSet != null ? indexSet.id : 0);
}

public void removeIndexesInRange(NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_removeIndexesInRange_1, range);
}

public void shiftIndexesStartingAtIndex(int index, int delta) {
	OS.objc_msgSend(this.id, OS.sel_shiftIndexesStartingAtIndex_1by_1, index, delta);
}

}
