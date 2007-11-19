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

public class NSWhoseSpecifier extends NSScriptObjectSpecifier {

public NSWhoseSpecifier() {
	super();
}

public NSWhoseSpecifier(int id) {
	super(id);
}

public int endSubelementIdentifier() {
	return OS.objc_msgSend(this.id, OS.sel_endSubelementIdentifier);
}

public int endSubelementIndex() {
	return OS.objc_msgSend(this.id, OS.sel_endSubelementIndex);
}

public id initWithContainerClassDescription(NSScriptClassDescription classDesc, NSScriptObjectSpecifier container, NSString property, NSScriptWhoseTest test) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContainerClassDescription_1containerSpecifier_1key_1test_1, classDesc != null ? classDesc.id : 0, container != null ? container.id : 0, property != null ? property.id : 0, test != null ? test.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setEndSubelementIdentifier(int subelement) {
	OS.objc_msgSend(this.id, OS.sel_setEndSubelementIdentifier_1, subelement);
}

public void setEndSubelementIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_setEndSubelementIndex_1, index);
}

public void setStartSubelementIdentifier(int subelement) {
	OS.objc_msgSend(this.id, OS.sel_setStartSubelementIdentifier_1, subelement);
}

public void setStartSubelementIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_setStartSubelementIndex_1, index);
}

public void setTest(NSScriptWhoseTest test) {
	OS.objc_msgSend(this.id, OS.sel_setTest_1, test != null ? test.id : 0);
}

public int startSubelementIdentifier() {
	return OS.objc_msgSend(this.id, OS.sel_startSubelementIdentifier);
}

public int startSubelementIndex() {
	return OS.objc_msgSend(this.id, OS.sel_startSubelementIndex);
}

public NSScriptWhoseTest test() {
	int result = OS.objc_msgSend(this.id, OS.sel_test);
	return result != 0 ? new NSScriptWhoseTest(result) : null;
}

}
