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

public class NSFontDescriptor extends NSObject {

public NSFontDescriptor() {
	super();
}

public NSFontDescriptor(int id) {
	super(id);
}

public NSDictionary fontAttributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_fontAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSFontDescriptor fontDescriptorByAddingAttributes(NSDictionary attributes) {
	int result = OS.objc_msgSend(this.id, OS.sel_fontDescriptorByAddingAttributes_1, attributes != null ? attributes.id : 0);
	return result == this.id ? this : (result != 0 ? new NSFontDescriptor(result) : null);
}

public NSFontDescriptor fontDescriptorWithFace(NSString newFace) {
	int result = OS.objc_msgSend(this.id, OS.sel_fontDescriptorWithFace_1, newFace != null ? newFace.id : 0);
	return result == this.id ? this : (result != 0 ? new NSFontDescriptor(result) : null);
}

public NSFontDescriptor fontDescriptorWithFamily(NSString newFamily) {
	int result = OS.objc_msgSend(this.id, OS.sel_fontDescriptorWithFamily_1, newFamily != null ? newFamily.id : 0);
	return result == this.id ? this : (result != 0 ? new NSFontDescriptor(result) : null);
}

public static NSFontDescriptor fontDescriptorWithFontAttributes(NSDictionary attributes) {
	int result = OS.objc_msgSend(OS.class_NSFontDescriptor, OS.sel_fontDescriptorWithFontAttributes_1, attributes != null ? attributes.id : 0);
	return result != 0 ? new NSFontDescriptor(result) : null;
}

public NSFontDescriptor fontDescriptorWithMatrix(NSAffineTransform matrix) {
	int result = OS.objc_msgSend(this.id, OS.sel_fontDescriptorWithMatrix_1, matrix != null ? matrix.id : 0);
	return result == this.id ? this : (result != 0 ? new NSFontDescriptor(result) : null);
}

public static NSFontDescriptor static_fontDescriptorWithName_matrix_(NSString fontName, NSAffineTransform matrix) {
	int result = OS.objc_msgSend(OS.class_NSFontDescriptor, OS.sel_fontDescriptorWithName_1matrix_1, fontName != null ? fontName.id : 0, matrix != null ? matrix.id : 0);
	return result != 0 ? new NSFontDescriptor(result) : null;
}

public static NSFontDescriptor static_fontDescriptorWithName_size_(NSString fontName, float size) {
	int result = OS.objc_msgSend(OS.class_NSFontDescriptor, OS.sel_fontDescriptorWithName_1size_1, fontName != null ? fontName.id : 0, size);
	return result != 0 ? new NSFontDescriptor(result) : null;
}

public NSFontDescriptor fontDescriptorWithSize(float newPointSize) {
	int result = OS.objc_msgSend(this.id, OS.sel_fontDescriptorWithSize_1, newPointSize);
	return result == this.id ? this : (result != 0 ? new NSFontDescriptor(result) : null);
}

public NSFontDescriptor fontDescriptorWithSymbolicTraits(int symbolicTraits) {
	int result = OS.objc_msgSend(this.id, OS.sel_fontDescriptorWithSymbolicTraits_1, symbolicTraits);
	return result == this.id ? this : (result != 0 ? new NSFontDescriptor(result) : null);
}

public id initWithFontAttributes(NSDictionary attributes) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFontAttributes_1, attributes != null ? attributes.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSFontDescriptor matchingFontDescriptorWithMandatoryKeys(NSSet mandatoryKeys) {
	int result = OS.objc_msgSend(this.id, OS.sel_matchingFontDescriptorWithMandatoryKeys_1, mandatoryKeys != null ? mandatoryKeys.id : 0);
	return result == this.id ? this : (result != 0 ? new NSFontDescriptor(result) : null);
}

public NSArray matchingFontDescriptorsWithMandatoryKeys(NSSet mandatoryKeys) {
	int result = OS.objc_msgSend(this.id, OS.sel_matchingFontDescriptorsWithMandatoryKeys_1, mandatoryKeys != null ? mandatoryKeys.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSAffineTransform matrix() {
	int result = OS.objc_msgSend(this.id, OS.sel_matrix);
	return result != 0 ? new NSAffineTransform(result) : null;
}

public id objectForKey(NSString anAttribute) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectForKey_1, anAttribute != null ? anAttribute.id : 0);
	return result != 0 ? new id(result) : null;
}

public float pointSize() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_pointSize);
}

public NSString postscriptName() {
	int result = OS.objc_msgSend(this.id, OS.sel_postscriptName);
	return result != 0 ? new NSString(result) : null;
}

public int symbolicTraits() {
	return OS.objc_msgSend(this.id, OS.sel_symbolicTraits);
}

}
