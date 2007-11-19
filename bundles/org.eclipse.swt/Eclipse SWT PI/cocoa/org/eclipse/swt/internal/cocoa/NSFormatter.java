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

public class NSFormatter extends NSObject {

public NSFormatter() {
	super();
}

public NSFormatter(int id) {
	super(id);
}

public NSAttributedString attributedStringForObjectValue(id obj, NSDictionary attrs) {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedStringForObjectValue_1withDefaultAttributes_1, obj != null ? obj.id : 0, attrs != null ? attrs.id : 0);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSString editingStringForObjectValue(id obj) {
	int result = OS.objc_msgSend(this.id, OS.sel_editingStringForObjectValue_1, obj != null ? obj.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public boolean getObjectValue(int obj, NSString string, int error) {
	return OS.objc_msgSend(this.id, OS.sel_getObjectValue_1forString_1errorDescription_1, obj, string != null ? string.id : 0, error) != 0;
}

public boolean isPartialStringValid_newEditingString_errorDescription_(NSString partialString, int newString, int error) {
	return OS.objc_msgSend(this.id, OS.sel_isPartialStringValid_1newEditingString_1errorDescription_1, partialString != null ? partialString.id : 0, newString, error) != 0;
}

public boolean isPartialStringValid_proposedSelectedRange_originalString_originalSelectedRange_errorDescription_(int partialStringPtr, int proposedSelRangePtr, NSString origString, NSRange origSelRange, int error) {
	return OS.objc_msgSend(this.id, OS.sel_isPartialStringValid_1proposedSelectedRange_1originalString_1originalSelectedRange_1errorDescription_1, partialStringPtr, proposedSelRangePtr, origString != null ? origString.id : 0, origSelRange, error) != 0;
}

public NSString stringForObjectValue(id obj) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringForObjectValue_1, obj != null ? obj.id : 0);
	return result != 0 ? new NSString(result) : null;
}

}
