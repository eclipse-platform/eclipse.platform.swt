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

public class NSLogicalTest extends NSScriptWhoseTest {

public NSLogicalTest() {
	super();
}

public NSLogicalTest(int id) {
	super(id);
}

public id initAndTestWithTests(NSArray subTests) {
	int result = OS.objc_msgSend(this.id, OS.sel_initAndTestWithTests_1, subTests != null ? subTests.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initNotTestWithTest(NSScriptWhoseTest subTest) {
	int result = OS.objc_msgSend(this.id, OS.sel_initNotTestWithTest_1, subTest != null ? subTest.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initOrTestWithTests(NSArray subTests) {
	int result = OS.objc_msgSend(this.id, OS.sel_initOrTestWithTests_1, subTests != null ? subTests.id : 0);
	return result != 0 ? new id(result) : null;
}

}
