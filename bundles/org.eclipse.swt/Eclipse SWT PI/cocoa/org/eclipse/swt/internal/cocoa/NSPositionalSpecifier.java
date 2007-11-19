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

public class NSPositionalSpecifier extends NSObject {

public NSPositionalSpecifier() {
	super();
}

public NSPositionalSpecifier(int id) {
	super(id);
}

public void evaluate() {
	OS.objc_msgSend(this.id, OS.sel_evaluate);
}

public NSPositionalSpecifier initWithPosition(int position, NSScriptObjectSpecifier specifier) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithPosition_1objectSpecifier_1, position, specifier != null ? specifier.id : 0);
	return result != 0 ? this : null;
}

public id insertionContainer() {
	int result = OS.objc_msgSend(this.id, OS.sel_insertionContainer);
	return result != 0 ? new id(result) : null;
}

public int insertionIndex() {
	return OS.objc_msgSend(this.id, OS.sel_insertionIndex);
}

public NSString insertionKey() {
	int result = OS.objc_msgSend(this.id, OS.sel_insertionKey);
	return result != 0 ? new NSString(result) : null;
}

public boolean insertionReplaces() {
	return OS.objc_msgSend(this.id, OS.sel_insertionReplaces) != 0;
}

public NSScriptObjectSpecifier objectSpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectSpecifier);
	return result != 0 ? new NSScriptObjectSpecifier(result) : null;
}

public int position() {
	return OS.objc_msgSend(this.id, OS.sel_position);
}

public void setInsertionClassDescription(NSScriptClassDescription classDescription) {
	OS.objc_msgSend(this.id, OS.sel_setInsertionClassDescription_1, classDescription != null ? classDescription.id : 0);
}

}
