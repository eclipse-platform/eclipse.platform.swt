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

public class NSMutableIndexSet extends NSIndexSet {

public NSMutableIndexSet() {
	super();
}

public NSMutableIndexSet(long id) {
	super(id);
}

public NSMutableIndexSet(id id) {
	super(id);
}

public void addIndex(long value) {
	OS.objc_msgSend(this.id, OS.sel_addIndex_, value);
}

}
