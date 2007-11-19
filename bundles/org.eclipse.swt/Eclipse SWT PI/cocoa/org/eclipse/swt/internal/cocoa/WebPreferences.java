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

public class WebPreferences extends NSObject {

public WebPreferences() {
	super();
}

public WebPreferences(int id) {
	super(id);
}

//public boolean allowsAnimatedImageLooping() {
//	return OS.objc_msgSend(this.id, OS.sel_allowsAnimatedImageLooping) != 0;
//}
//
//public boolean allowsAnimatedImages() {
//	return OS.objc_msgSend(this.id, OS.sel_allowsAnimatedImages) != 0;
//}

//public boolean arePlugInsEnabled() {
//	return OS.objc_msgSend(this.id, OS.sel_arePlugInsEnabled) != 0;
//}
//
//public boolean autosaves() {
//	return OS.objc_msgSend(this.id, OS.sel_autosaves) != 0;
//}

//public int cacheModel() {
//	return OS.objc_msgSend(this.id, OS.sel_cacheModel);
//}
//
//public NSString cursiveFontFamily() {
//	int result = OS.objc_msgSend(this.id, OS.sel_cursiveFontFamily);
//	return result != 0 ? new NSString(result) : null;
//}

//public int defaultFixedFontSize() {
//	return OS.objc_msgSend(this.id, OS.sel_defaultFixedFontSize);
//}
//
//public int defaultFontSize() {
//	return OS.objc_msgSend(this.id, OS.sel_defaultFontSize);
//}
//
//public NSString defaultTextEncodingName() {
//	int result = OS.objc_msgSend(this.id, OS.sel_defaultTextEncodingName);
//	return result != 0 ? new NSString(result) : null;
//}

//public NSString fantasyFontFamily() {
//	int result = OS.objc_msgSend(this.id, OS.sel_fantasyFontFamily);
//	return result != 0 ? new NSString(result) : null;
//}
//
//public NSString fixedFontFamily() {
//	int result = OS.objc_msgSend(this.id, OS.sel_fixedFontFamily);
//	return result != 0 ? new NSString(result) : null;
//}

public NSString identifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_identifier);
	return result != 0 ? new NSString(result) : null;
}

public id initWithIdentifier(NSString anIdentifier) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithIdentifier_1, anIdentifier != null ? anIdentifier.id : 0);
	return result != 0 ? new id(result) : null;
}

//public boolean isJavaEnabled() {
//	return OS.objc_msgSend(this.id, OS.sel_isJavaEnabled) != 0;
//}

//public boolean isJavaScriptEnabled() {
//	return OS.objc_msgSend(this.id, OS.sel_isJavaScriptEnabled) != 0;
//}
//
//public boolean javaScriptCanOpenWindowsAutomatically() {
//	return OS.objc_msgSend(this.id, OS.sel_javaScriptCanOpenWindowsAutomatically) != 0;
//}
//
//public boolean loadsImagesAutomatically() {
//	return OS.objc_msgSend(this.id, OS.sel_loadsImagesAutomatically) != 0;
//}
//
//public int minimumFontSize() {
//	return OS.objc_msgSend(this.id, OS.sel_minimumFontSize);
//}
//
//public int minimumLogicalFontSize() {
//	return OS.objc_msgSend(this.id, OS.sel_minimumLogicalFontSize);
//}
//
//public boolean privateBrowsingEnabled() {
//	return OS.objc_msgSend(this.id, OS.sel_privateBrowsingEnabled) != 0;
//}
//
//public NSString sansSerifFontFamily() {
//	int result = OS.objc_msgSend(this.id, OS.sel_sansSerifFontFamily);
//	return result != 0 ? new NSString(result) : null;
//}
//
//public NSString serifFontFamily() {
//	int result = OS.objc_msgSend(this.id, OS.sel_serifFontFamily);
//	return result != 0 ? new NSString(result) : null;
//}
//
//public void setAllowsAnimatedImageLooping(boolean flag) {
//	OS.objc_msgSend(this.id, OS.sel_setAllowsAnimatedImageLooping_1, flag);
//}
//
//public void setAllowsAnimatedImages(boolean flag) {
//	OS.objc_msgSend(this.id, OS.sel_setAllowsAnimatedImages_1, flag);
//}
//
//public void setAutosaves(boolean flag) {
//	OS.objc_msgSend(this.id, OS.sel_setAutosaves_1, flag);
//}
//
//public void setCacheModel(int cacheModel) {
//	OS.objc_msgSend(this.id, OS.sel_setCacheModel_1, cacheModel);
//}
//
//public void setCursiveFontFamily(NSString family) {
//	OS.objc_msgSend(this.id, OS.sel_setCursiveFontFamily_1, family != null ? family.id : 0);
//}
//
//public void setDefaultFixedFontSize(int size) {
//	OS.objc_msgSend(this.id, OS.sel_setDefaultFixedFontSize_1, size);
//}
//
//public void setDefaultFontSize(int size) {
//	OS.objc_msgSend(this.id, OS.sel_setDefaultFontSize_1, size);
//}
//
//public void setDefaultTextEncodingName(NSString encoding) {
//	OS.objc_msgSend(this.id, OS.sel_setDefaultTextEncodingName_1, encoding != null ? encoding.id : 0);
//}
//
//public void setFantasyFontFamily(NSString family) {
//	OS.objc_msgSend(this.id, OS.sel_setFantasyFontFamily_1, family != null ? family.id : 0);
//}
//
//public void setFixedFontFamily(NSString family) {
//	OS.objc_msgSend(this.id, OS.sel_setFixedFontFamily_1, family != null ? family.id : 0);
//}

public void setJavaEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setJavaEnabled_1, flag);
}

//public void setJavaScriptCanOpenWindowsAutomatically(boolean flag) {
//	OS.objc_msgSend(this.id, OS.sel_setJavaScriptCanOpenWindowsAutomatically_1, flag);
//}
//
//public void setJavaScriptEnabled(boolean flag) {
//	OS.objc_msgSend(this.id, OS.sel_setJavaScriptEnabled_1, flag);
//}
//
//public void setLoadsImagesAutomatically(boolean flag) {
//	OS.objc_msgSend(this.id, OS.sel_setLoadsImagesAutomatically_1, flag);
//}
//
//public void setMinimumFontSize(int size) {
//	OS.objc_msgSend(this.id, OS.sel_setMinimumFontSize_1, size);
//}
//
//public void setMinimumLogicalFontSize(int size) {
//	OS.objc_msgSend(this.id, OS.sel_setMinimumLogicalFontSize_1, size);
//}
//
//public void setPlugInsEnabled(boolean flag) {
//	OS.objc_msgSend(this.id, OS.sel_setPlugInsEnabled_1, flag);
//}
//
//public void setPrivateBrowsingEnabled(boolean flag) {
//	OS.objc_msgSend(this.id, OS.sel_setPrivateBrowsingEnabled_1, flag);
//}
//
//public void setSansSerifFontFamily(NSString family) {
//	OS.objc_msgSend(this.id, OS.sel_setSansSerifFontFamily_1, family != null ? family.id : 0);
//}
//
//public void setSerifFontFamily(NSString family) {
//	OS.objc_msgSend(this.id, OS.sel_setSerifFontFamily_1, family != null ? family.id : 0);
//}
//
//public void setShouldPrintBackgrounds(boolean flag) {
//	OS.objc_msgSend(this.id, OS.sel_setShouldPrintBackgrounds_1, flag);
//}
//
//public void setStandardFontFamily(NSString family) {
//	OS.objc_msgSend(this.id, OS.sel_setStandardFontFamily_1, family != null ? family.id : 0);
//}
//
//public void setTabsToLinks(boolean flag) {
//	OS.objc_msgSend(this.id, OS.sel_setTabsToLinks_1, flag);
//}
//
//public void setUserStyleSheetEnabled(boolean flag) {
//	OS.objc_msgSend(this.id, OS.sel_setUserStyleSheetEnabled_1, flag);
//}
//
//public void setUserStyleSheetLocation(NSURL URL) {
//	OS.objc_msgSend(this.id, OS.sel_setUserStyleSheetLocation_1, URL != null ? URL.id : 0);
//}
//
//public void setUsesPageCache(boolean usesPageCache) {
//	OS.objc_msgSend(this.id, OS.sel_setUsesPageCache_1, usesPageCache);
//}
//
//public boolean shouldPrintBackgrounds() {
//	return OS.objc_msgSend(this.id, OS.sel_shouldPrintBackgrounds) != 0;
//}
//
//public NSString standardFontFamily() {
//	int result = OS.objc_msgSend(this.id, OS.sel_standardFontFamily);
//	return result != 0 ? new NSString(result) : null;
//}
//
public static WebPreferences standardPreferences() {
	int result = OS.objc_msgSend(OS.class_WebPreferences, OS.sel_standardPreferences);
	return result != 0 ? new WebPreferences(result) : null;
}
//
//public boolean tabsToLinks() {
//	return OS.objc_msgSend(this.id, OS.sel_tabsToLinks) != 0;
//}
//
//public boolean userStyleSheetEnabled() {
//	return OS.objc_msgSend(this.id, OS.sel_userStyleSheetEnabled) != 0;
//}
//
//public NSURL userStyleSheetLocation() {
//	int result = OS.objc_msgSend(this.id, OS.sel_userStyleSheetLocation);
//	return result != 0 ? new NSURL(result) : null;
//}
//
//public boolean usesPageCache() {
//	return OS.objc_msgSend(this.id, OS.sel_usesPageCache) != 0;
//}

}
