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

public class NSValueTransformer extends NSObject {

public NSValueTransformer() {
	super();
}

public NSValueTransformer(int id) {
	super(id);
}

public static boolean allowsReverseTransformation() {
	return OS.objc_msgSend(OS.class_NSValueTransformer, OS.sel_allowsReverseTransformation) != 0;
}

public id reverseTransformedValue(id value) {
	int result = OS.objc_msgSend(this.id, OS.sel_reverseTransformedValue_1, value != null ? value.id : 0);
	return result != 0 ? new id(result) : null;
}

public static void setValueTransformer(NSValueTransformer transformer, NSString name) {
	OS.objc_msgSend(OS.class_NSValueTransformer, OS.sel_setValueTransformer_1forName_1, transformer != null ? transformer.id : 0, name != null ? name.id : 0);
}

public id transformedValue(id value) {
	int result = OS.objc_msgSend(this.id, OS.sel_transformedValue_1, value != null ? value.id : 0);
	return result != 0 ? new id(result) : null;
}

public static int transformedValueClass() {
	return OS.objc_msgSend(OS.class_NSValueTransformer, OS.sel_transformedValueClass);
}

public static NSValueTransformer valueTransformerForName(NSString name) {
	int result = OS.objc_msgSend(OS.class_NSValueTransformer, OS.sel_valueTransformerForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSValueTransformer(result) : null;
}

public static NSArray valueTransformerNames() {
	int result = OS.objc_msgSend(OS.class_NSValueTransformer, OS.sel_valueTransformerNames);
	return result != 0 ? new NSArray(result) : null;
}

}
