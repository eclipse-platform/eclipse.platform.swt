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

public class NSPredicate extends NSObject {

public NSPredicate() {
	super();
}

public NSPredicate(int id) {
	super(id);
}

public boolean evaluateWithObject_(id object) {
	return OS.objc_msgSend(this.id, OS.sel_evaluateWithObject_1, object != null ? object.id : 0) != 0;
}

public boolean evaluateWithObject_substitutionVariables_(id object, NSDictionary bindings) {
	return OS.objc_msgSend(this.id, OS.sel_evaluateWithObject_1substitutionVariables_1, object != null ? object.id : 0, bindings != null ? bindings.id : 0) != 0;
}

public NSString predicateFormat() {
	int result = OS.objc_msgSend(this.id, OS.sel_predicateFormat);
	return result != 0 ? new NSString(result) : null;
}

public static NSPredicate static_predicateWithFormat_(NSString predicateWithFormat) {
	int result = OS.objc_msgSend(OS.class_NSPredicate, OS.sel_predicateWithFormat_1, predicateWithFormat != null ? predicateWithFormat.id : 0);
	return result != 0 ? new NSPredicate(result) : null;
}

public static NSPredicate static_predicateWithFormat_argumentArray_(NSString predicateFormat, NSArray arguments) {
	int result = OS.objc_msgSend(OS.class_NSPredicate, OS.sel_predicateWithFormat_1argumentArray_1, predicateFormat != null ? predicateFormat.id : 0, arguments != null ? arguments.id : 0);
	return result != 0 ? new NSPredicate(result) : null;
}

public static NSPredicate static_predicateWithFormat_arguments_(NSString predicateFormat, int argList) {
	int result = OS.objc_msgSend(OS.class_NSPredicate, OS.sel_predicateWithFormat_1arguments_1, predicateFormat != null ? predicateFormat.id : 0, argList);
	return result != 0 ? new NSPredicate(result) : null;
}

public NSPredicate predicateWithSubstitutionVariables(NSDictionary variables) {
	int result = OS.objc_msgSend(this.id, OS.sel_predicateWithSubstitutionVariables_1, variables != null ? variables.id : 0);
	return result == this.id ? this : (result != 0 ? new NSPredicate(result) : null);
}

public static NSPredicate predicateWithValue(boolean value) {
	int result = OS.objc_msgSend(OS.class_NSPredicate, OS.sel_predicateWithValue_1, value);
	return result != 0 ? new NSPredicate(result) : null;
}

}
