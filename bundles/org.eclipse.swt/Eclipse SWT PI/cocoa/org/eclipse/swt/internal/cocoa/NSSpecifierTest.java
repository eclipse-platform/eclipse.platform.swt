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

public class NSSpecifierTest extends NSScriptWhoseTest {

public NSSpecifierTest() {
	super();
}

public NSSpecifierTest(int id) {
	super(id);
}

public id initWithObjectSpecifier(NSScriptObjectSpecifier obj1, int compOp, id obj2) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithObjectSpecifier_1comparisonOperator_1testObject_1, obj1 != null ? obj1.id : 0, compOp, obj2 != null ? obj2.id : 0);
	return result != 0 ? new id(result) : null;
}

}
