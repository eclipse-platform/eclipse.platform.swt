/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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

public class NSIndexSet extends NSObject {

public NSIndexSet() {
	super();
}

public NSIndexSet(long id) {
	super(id);
}

public NSIndexSet(id id) {
	super(id);
}

public boolean containsIndex(long value) {
	return OS.objc_msgSend_bool(this.id, OS.sel_containsIndex_, value);
}

public long count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public long getIndexes(long[] indexBuffer, long bufferSize, long range) {
	return OS.objc_msgSend(this.id, OS.sel_getIndexes_maxCount_inIndexRange_, indexBuffer, bufferSize, range);
}

public NSIndexSet initWithIndex(long value) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithIndex_, value);
	return result == this.id ? this : (result != 0 ? new NSIndexSet(result) : null);
}

public NSIndexSet initWithIndexesInRange(NSRange range) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithIndexesInRange_, range);
	return result == this.id ? this : (result != 0 ? new NSIndexSet(result) : null);
}

}
