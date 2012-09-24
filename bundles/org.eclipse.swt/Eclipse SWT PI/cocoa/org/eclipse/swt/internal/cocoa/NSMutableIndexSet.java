/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSMutableIndexSet extends NSIndexSet {

public NSMutableIndexSet() {
	super();
}

public NSMutableIndexSet(long /*int*/ id) {
	super(id);
}

public NSMutableIndexSet(id id) {
	super(id);
}

public void addIndex(long /*int*/ value) {
	OS.objc_msgSend(this.id, OS.sel_addIndex_, value);
}

public void removeIndex(long /*int*/ value) {
	OS.objc_msgSend(this.id, OS.sel_removeIndex_, value);
}

public static id indexSetWithIndex(long /*int*/ value) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSMutableIndexSet, OS.sel_indexSetWithIndex_, value);
	return result != 0 ? new id(result) : null;
}

}
