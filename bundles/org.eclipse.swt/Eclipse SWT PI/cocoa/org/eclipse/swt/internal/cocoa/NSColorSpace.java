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

public class NSColorSpace extends NSObject {

public NSColorSpace() {
	super();
}

public NSColorSpace(int id) {
	super(id);
}

public int CGColorSpace() {
	return OS.objc_msgSend(this.id, OS.sel_CGColorSpace);
}

public NSData ICCProfileData() {
	int result = OS.objc_msgSend(this.id, OS.sel_ICCProfileData);
	return result != 0 ? new NSData(result) : null;
}

public static NSColorSpace adobeRGB1998ColorSpace() {
	int result = OS.objc_msgSend(OS.class_NSColorSpace, OS.sel_adobeRGB1998ColorSpace);
	return result != 0 ? new NSColorSpace(result) : null;
}

public int colorSpaceModel() {
	return OS.objc_msgSend(this.id, OS.sel_colorSpaceModel);
}

public int colorSyncProfile() {
	return OS.objc_msgSend(this.id, OS.sel_colorSyncProfile);
}

public static NSColorSpace deviceCMYKColorSpace() {
	int result = OS.objc_msgSend(OS.class_NSColorSpace, OS.sel_deviceCMYKColorSpace);
	return result != 0 ? new NSColorSpace(result) : null;
}

public static NSColorSpace deviceGrayColorSpace() {
	int result = OS.objc_msgSend(OS.class_NSColorSpace, OS.sel_deviceGrayColorSpace);
	return result != 0 ? new NSColorSpace(result) : null;
}

public static NSColorSpace deviceRGBColorSpace() {
	int result = OS.objc_msgSend(OS.class_NSColorSpace, OS.sel_deviceRGBColorSpace);
	return result != 0 ? new NSColorSpace(result) : null;
}

public static NSColorSpace genericCMYKColorSpace() {
	int result = OS.objc_msgSend(OS.class_NSColorSpace, OS.sel_genericCMYKColorSpace);
	return result != 0 ? new NSColorSpace(result) : null;
}

public static NSColorSpace genericGrayColorSpace() {
	int result = OS.objc_msgSend(OS.class_NSColorSpace, OS.sel_genericGrayColorSpace);
	return result != 0 ? new NSColorSpace(result) : null;
}

public static NSColorSpace genericRGBColorSpace() {
	int result = OS.objc_msgSend(OS.class_NSColorSpace, OS.sel_genericRGBColorSpace);
	return result != 0 ? new NSColorSpace(result) : null;
}

public NSColorSpace initWithCGColorSpace(int cgColorSpace) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCGColorSpace_1, cgColorSpace);
	return result != 0 ? this : null;
}

public NSColorSpace initWithColorSyncProfile(int prof) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithColorSyncProfile_1, prof);
	return result != 0 ? this : null;
}

public NSColorSpace initWithICCProfileData(NSData iccData) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithICCProfileData_1, iccData != null ? iccData.id : 0);
	return result != 0 ? this : null;
}

public NSString localizedName() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedName);
	return result != 0 ? new NSString(result) : null;
}

public int numberOfColorComponents() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfColorComponents);
}

public static NSColorSpace sRGBColorSpace() {
	int result = OS.objc_msgSend(OS.class_NSColorSpace, OS.sel_sRGBColorSpace);
	return result != 0 ? new NSColorSpace(result) : null;
}

}
