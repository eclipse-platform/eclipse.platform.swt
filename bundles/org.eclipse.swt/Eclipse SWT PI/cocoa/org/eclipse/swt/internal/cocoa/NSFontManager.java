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

public class NSFontManager extends NSObject {

public NSFontManager() {
	super();
}

public NSFontManager(int id) {
	super(id);
}

public int action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public boolean addCollection(NSString collectionName, int collectionOptions) {
	return OS.objc_msgSend(this.id, OS.sel_addCollection_1options_1, collectionName != null ? collectionName.id : 0, collectionOptions) != 0;
}

public void addFontDescriptors(NSArray descriptors, NSString collectionName) {
	OS.objc_msgSend(this.id, OS.sel_addFontDescriptors_1toCollection_1, descriptors != null ? descriptors.id : 0, collectionName != null ? collectionName.id : 0);
}

public void addFontTrait(id sender) {
	OS.objc_msgSend(this.id, OS.sel_addFontTrait_1, sender != null ? sender.id : 0);
}

public NSArray availableFontFamilies() {
	int result = OS.objc_msgSend(this.id, OS.sel_availableFontFamilies);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray availableFontNamesMatchingFontDescriptor(NSFontDescriptor descriptor) {
	int result = OS.objc_msgSend(this.id, OS.sel_availableFontNamesMatchingFontDescriptor_1, descriptor != null ? descriptor.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray availableFontNamesWithTraits(int someTraits) {
	int result = OS.objc_msgSend(this.id, OS.sel_availableFontNamesWithTraits_1, someTraits);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray availableFonts() {
	int result = OS.objc_msgSend(this.id, OS.sel_availableFonts);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray availableMembersOfFontFamily(NSString fam) {
	int result = OS.objc_msgSend(this.id, OS.sel_availableMembersOfFontFamily_1, fam != null ? fam.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray collectionNames() {
	int result = OS.objc_msgSend(this.id, OS.sel_collectionNames);
	return result != 0 ? new NSArray(result) : null;
}

public NSDictionary convertAttributes(NSDictionary attributes) {
	int result = OS.objc_msgSend(this.id, OS.sel_convertAttributes_1, attributes != null ? attributes.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSFont convertFont_(NSFont fontObj) {
	int result = OS.objc_msgSend(this.id, OS.sel_convertFont_1, fontObj != null ? fontObj.id : 0);
	return result != 0 ? new NSFont(result) : null;
}

public NSFont convertFont_toFace_(NSFont fontObj, NSString typeface) {
	int result = OS.objc_msgSend(this.id, OS.sel_convertFont_1toFace_1, fontObj != null ? fontObj.id : 0, typeface != null ? typeface.id : 0);
	return result != 0 ? new NSFont(result) : null;
}

public NSFont convertFont_toFamily_(NSFont fontObj, NSString family) {
	int result = OS.objc_msgSend(this.id, OS.sel_convertFont_1toFamily_1, fontObj != null ? fontObj.id : 0, family != null ? family.id : 0);
	return result != 0 ? new NSFont(result) : null;
}

public NSFont convertFont_toHaveTrait_(NSFont fontObj, int trait) {
	int result = OS.objc_msgSend(this.id, OS.sel_convertFont_1toHaveTrait_1, fontObj != null ? fontObj.id : 0, trait);
	return result != 0 ? new NSFont(result) : null;
}

public NSFont convertFont_toNotHaveTrait_(NSFont fontObj, int trait) {
	int result = OS.objc_msgSend(this.id, OS.sel_convertFont_1toNotHaveTrait_1, fontObj != null ? fontObj.id : 0, trait);
	return result != 0 ? new NSFont(result) : null;
}

public NSFont convertFont_toSize_(NSFont fontObj, float size) {
	int result = OS.objc_msgSend(this.id, OS.sel_convertFont_1toSize_1, fontObj != null ? fontObj.id : 0, size);
	return result != 0 ? new NSFont(result) : null;
}

public int convertFontTraits(int traits) {
	return OS.objc_msgSend(this.id, OS.sel_convertFontTraits_1, traits);
}

public NSFont convertWeight(boolean upFlag, NSFont fontObj) {
	int result = OS.objc_msgSend(this.id, OS.sel_convertWeight_1ofFont_1, upFlag, fontObj != null ? fontObj.id : 0);
	return result != 0 ? new NSFont(result) : null;
}

public int currentFontAction() {
	return OS.objc_msgSend(this.id, OS.sel_currentFontAction);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public NSArray fontDescriptorsInCollection(NSString collectionNames) {
	int result = OS.objc_msgSend(this.id, OS.sel_fontDescriptorsInCollection_1, collectionNames != null ? collectionNames.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSMenu fontMenu(boolean create) {
	int result = OS.objc_msgSend(this.id, OS.sel_fontMenu_1, create);
	return result != 0 ? new NSMenu(result) : null;
}

public boolean fontNamed(NSString fName, int someTraits) {
	return OS.objc_msgSend(this.id, OS.sel_fontNamed_1hasTraits_1, fName != null ? fName.id : 0, someTraits) != 0;
}

public NSFontPanel fontPanel(boolean create) {
	int result = OS.objc_msgSend(this.id, OS.sel_fontPanel_1, create);
	return result != 0 ? new NSFontPanel(result) : null;
}

public NSFont fontWithFamily(NSString family, int traits, int weight, float size) {
	int result = OS.objc_msgSend(this.id, OS.sel_fontWithFamily_1traits_1weight_1size_1, family != null ? family.id : 0, traits, weight, size);
	return result != 0 ? new NSFont(result) : null;
}

public boolean isEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isEnabled) != 0;
}

public boolean isMultiple() {
	return OS.objc_msgSend(this.id, OS.sel_isMultiple) != 0;
}

public NSString localizedNameForFamily(NSString family, NSString faceKey) {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedNameForFamily_1face_1, family != null ? family.id : 0, faceKey != null ? faceKey.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public void modifyFont(id sender) {
	OS.objc_msgSend(this.id, OS.sel_modifyFont_1, sender != null ? sender.id : 0);
}

public void modifyFontViaPanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_modifyFontViaPanel_1, sender != null ? sender.id : 0);
}

public void orderFrontFontPanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFrontFontPanel_1, sender != null ? sender.id : 0);
}

public void orderFrontStylesPanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFrontStylesPanel_1, sender != null ? sender.id : 0);
}

public boolean removeCollection(NSString collectionName) {
	return OS.objc_msgSend(this.id, OS.sel_removeCollection_1, collectionName != null ? collectionName.id : 0) != 0;
}

public void removeFontDescriptor(NSFontDescriptor descriptor, NSString collection) {
	OS.objc_msgSend(this.id, OS.sel_removeFontDescriptor_1fromCollection_1, descriptor != null ? descriptor.id : 0, collection != null ? collection.id : 0);
}

public void removeFontTrait(id sender) {
	OS.objc_msgSend(this.id, OS.sel_removeFontTrait_1, sender != null ? sender.id : 0);
}

public NSFont selectedFont() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedFont);
	return result != 0 ? new NSFont(result) : null;
}

public boolean sendAction() {
	return OS.objc_msgSend(this.id, OS.sel_sendAction) != 0;
}

public void setAction(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setAction_1, aSelector);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_1, flag);
}

public static void setFontManagerFactory(int factoryId) {
	OS.objc_msgSend(OS.class_NSFontManager, OS.sel_setFontManagerFactory_1, factoryId);
}

public void setFontMenu(NSMenu newMenu) {
	OS.objc_msgSend(this.id, OS.sel_setFontMenu_1, newMenu != null ? newMenu.id : 0);
}

public static void setFontPanelFactory(int factoryId) {
	OS.objc_msgSend(OS.class_NSFontManager, OS.sel_setFontPanelFactory_1, factoryId);
}

public void setSelectedAttributes(NSDictionary attributes, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedAttributes_1isMultiple_1, attributes != null ? attributes.id : 0, flag);
}

public void setSelectedFont(NSFont fontObj, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedFont_1isMultiple_1, fontObj != null ? fontObj.id : 0, flag);
}

public void setTarget(id aTarget) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_1, aTarget != null ? aTarget.id : 0);
}

public static NSFontManager sharedFontManager() {
	int result = OS.objc_msgSend(OS.class_NSFontManager, OS.sel_sharedFontManager);
	return result != 0 ? new NSFontManager(result) : null;
}

public id target() {
	int result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

public int traitsOfFont(NSFont fontObj) {
	return OS.objc_msgSend(this.id, OS.sel_traitsOfFont_1, fontObj != null ? fontObj.id : 0);
}

public int weightOfFont(NSFont fontObj) {
	return OS.objc_msgSend(this.id, OS.sel_weightOfFont_1, fontObj != null ? fontObj.id : 0);
}

}
