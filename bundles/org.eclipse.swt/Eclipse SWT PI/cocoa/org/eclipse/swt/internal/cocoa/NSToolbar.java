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

public class NSToolbar extends NSObject {

public NSToolbar() {
	super();
}

public NSToolbar(int id) {
	super(id);
}

public boolean allowsUserCustomization() {
	return OS.objc_msgSend(this.id, OS.sel_allowsUserCustomization) != 0;
}

public boolean autosavesConfiguration() {
	return OS.objc_msgSend(this.id, OS.sel_autosavesConfiguration) != 0;
}

public NSDictionary configurationDictionary() {
	int result = OS.objc_msgSend(this.id, OS.sel_configurationDictionary);
	return result != 0 ? new NSDictionary(result) : null;
}

public boolean customizationPaletteIsRunning() {
	return OS.objc_msgSend(this.id, OS.sel_customizationPaletteIsRunning) != 0;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public int displayMode() {
	return OS.objc_msgSend(this.id, OS.sel_displayMode);
}

public NSString identifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_identifier);
	return result != 0 ? new NSString(result) : null;
}

public id initWithIdentifier(NSString identifier) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithIdentifier_1, identifier != null ? identifier.id : 0);
	return result != 0 ? new id(result) : null;
}

public void insertItemWithItemIdentifier(NSString itemIdentifier, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertItemWithItemIdentifier_1atIndex_1, itemIdentifier != null ? itemIdentifier.id : 0, index);
}

public boolean isVisible() {
	return OS.objc_msgSend(this.id, OS.sel_isVisible) != 0;
}

public NSArray items() {
	int result = OS.objc_msgSend(this.id, OS.sel_items);
	return result != 0 ? new NSArray(result) : null;
}

public void removeItemAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removeItemAtIndex_1, index);
}

public void runCustomizationPalette(id sender) {
	OS.objc_msgSend(this.id, OS.sel_runCustomizationPalette_1, sender != null ? sender.id : 0);
}

public NSString selectedItemIdentifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedItemIdentifier);
	return result != 0 ? new NSString(result) : null;
}

public void setAllowsUserCustomization(boolean allowCustomization) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsUserCustomization_1, allowCustomization);
}

public void setAutosavesConfiguration(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutosavesConfiguration_1, flag);
}

public void setConfigurationFromDictionary(NSDictionary configDict) {
	OS.objc_msgSend(this.id, OS.sel_setConfigurationFromDictionary_1, configDict != null ? configDict.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setDisplayMode(int displayMode) {
	OS.objc_msgSend(this.id, OS.sel_setDisplayMode_1, displayMode);
}

public void setSelectedItemIdentifier(NSString itemIdentifier) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedItemIdentifier_1, itemIdentifier != null ? itemIdentifier.id : 0);
}

public void setShowsBaselineSeparator(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setShowsBaselineSeparator_1, flag);
}

public void setSizeMode(int sizeMode) {
	OS.objc_msgSend(this.id, OS.sel_setSizeMode_1, sizeMode);
}

public void setVisible(boolean shown) {
	OS.objc_msgSend(this.id, OS.sel_setVisible_1, shown);
}

public boolean showsBaselineSeparator() {
	return OS.objc_msgSend(this.id, OS.sel_showsBaselineSeparator) != 0;
}

public int sizeMode() {
	return OS.objc_msgSend(this.id, OS.sel_sizeMode);
}

public void validateVisibleItems() {
	OS.objc_msgSend(this.id, OS.sel_validateVisibleItems);
}

public NSArray visibleItems() {
	int result = OS.objc_msgSend(this.id, OS.sel_visibleItems);
	return result != 0 ? new NSArray(result) : null;
}

}
