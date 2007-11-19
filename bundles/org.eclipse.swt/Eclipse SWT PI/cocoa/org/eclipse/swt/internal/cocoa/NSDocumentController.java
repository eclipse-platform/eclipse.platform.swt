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

public class NSDocumentController extends NSObject {

public NSDocumentController() {
	super();
}

public NSDocumentController(int id) {
	super(id);
}

public NSArray URLsFromRunningOpenPanel() {
	int result = OS.objc_msgSend(this.id, OS.sel_URLsFromRunningOpenPanel);
	return result != 0 ? new NSArray(result) : null;
}

public void addDocument(NSDocument document) {
	OS.objc_msgSend(this.id, OS.sel_addDocument_1, document != null ? document.id : 0);
}

public double autosavingDelay() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_autosavingDelay);
}

public void clearRecentDocuments(id sender) {
	OS.objc_msgSend(this.id, OS.sel_clearRecentDocuments_1, sender != null ? sender.id : 0);
}

public void closeAllDocumentsWithDelegate(id delegate, int didCloseAllSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_closeAllDocumentsWithDelegate_1didCloseAllSelector_1contextInfo_1, delegate != null ? delegate.id : 0, didCloseAllSelector, contextInfo);
}

public NSString currentDirectory() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentDirectory);
	return result != 0 ? new NSString(result) : null;
}

public id currentDocument() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentDocument);
	return result != 0 ? new id(result) : null;
}

public NSString defaultType() {
	int result = OS.objc_msgSend(this.id, OS.sel_defaultType);
	return result != 0 ? new NSString(result) : null;
}

public NSString displayNameForType(NSString typeName) {
	int result = OS.objc_msgSend(this.id, OS.sel_displayNameForType_1, typeName != null ? typeName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public int documentClassForType(NSString typeName) {
	return OS.objc_msgSend(this.id, OS.sel_documentClassForType_1, typeName != null ? typeName.id : 0);
}

public NSArray documentClassNames() {
	int result = OS.objc_msgSend(this.id, OS.sel_documentClassNames);
	return result != 0 ? new NSArray(result) : null;
}

public id documentForFileName(NSString fileName) {
	int result = OS.objc_msgSend(this.id, OS.sel_documentForFileName_1, fileName != null ? fileName.id : 0);
	return result != 0 ? new id(result) : null;
}

public id documentForURL(NSURL absoluteURL) {
	int result = OS.objc_msgSend(this.id, OS.sel_documentForURL_1, absoluteURL != null ? absoluteURL.id : 0);
	return result != 0 ? new id(result) : null;
}

public id documentForWindow(NSWindow window) {
	int result = OS.objc_msgSend(this.id, OS.sel_documentForWindow_1, window != null ? window.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSArray documents() {
	int result = OS.objc_msgSend(this.id, OS.sel_documents);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray fileExtensionsFromType(NSString typeName) {
	int result = OS.objc_msgSend(this.id, OS.sel_fileExtensionsFromType_1, typeName != null ? typeName.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray fileNamesFromRunningOpenPanel() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileNamesFromRunningOpenPanel);
	return result != 0 ? new NSArray(result) : null;
}

public boolean hasEditedDocuments() {
	return OS.objc_msgSend(this.id, OS.sel_hasEditedDocuments) != 0;
}

public id makeDocumentForURL(NSURL absoluteDocumentURL, NSURL absoluteDocumentContentsURL, NSString typeName, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_makeDocumentForURL_1withContentsOfURL_1ofType_1error_1, absoluteDocumentURL != null ? absoluteDocumentURL.id : 0, absoluteDocumentContentsURL != null ? absoluteDocumentContentsURL.id : 0, typeName != null ? typeName.id : 0, outError);
	return result != 0 ? new id(result) : null;
}

public id makeDocumentWithContentsOfFile(NSString fileName, NSString type) {
	int result = OS.objc_msgSend(this.id, OS.sel_makeDocumentWithContentsOfFile_1ofType_1, fileName != null ? fileName.id : 0, type != null ? type.id : 0);
	return result != 0 ? new id(result) : null;
}

public id makeDocumentWithContentsOfURL_ofType_(NSURL url, NSString type) {
	int result = OS.objc_msgSend(this.id, OS.sel_makeDocumentWithContentsOfURL_1ofType_1, url != null ? url.id : 0, type != null ? type.id : 0);
	return result != 0 ? new id(result) : null;
}

public id makeDocumentWithContentsOfURL_ofType_error_(NSURL absoluteURL, NSString typeName, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_makeDocumentWithContentsOfURL_1ofType_1error_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0, outError);
	return result != 0 ? new id(result) : null;
}

public id makeUntitledDocumentOfType_(NSString type) {
	int result = OS.objc_msgSend(this.id, OS.sel_makeUntitledDocumentOfType_1, type != null ? type.id : 0);
	return result != 0 ? new id(result) : null;
}

public id makeUntitledDocumentOfType_error_(NSString typeName, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_makeUntitledDocumentOfType_1error_1, typeName != null ? typeName.id : 0, outError);
	return result != 0 ? new id(result) : null;
}

public int maximumRecentDocumentCount() {
	return OS.objc_msgSend(this.id, OS.sel_maximumRecentDocumentCount);
}

public void newDocument(id sender) {
	OS.objc_msgSend(this.id, OS.sel_newDocument_1, sender != null ? sender.id : 0);
}

public void noteNewRecentDocument(NSDocument document) {
	OS.objc_msgSend(this.id, OS.sel_noteNewRecentDocument_1, document != null ? document.id : 0);
}

public void noteNewRecentDocumentURL(NSURL absoluteURL) {
	OS.objc_msgSend(this.id, OS.sel_noteNewRecentDocumentURL_1, absoluteURL != null ? absoluteURL.id : 0);
}

public void openDocument(id sender) {
	OS.objc_msgSend(this.id, OS.sel_openDocument_1, sender != null ? sender.id : 0);
}

public id openDocumentWithContentsOfFile(NSString fileName, boolean display) {
	int result = OS.objc_msgSend(this.id, OS.sel_openDocumentWithContentsOfFile_1display_1, fileName != null ? fileName.id : 0, display);
	return result != 0 ? new id(result) : null;
}

public id openDocumentWithContentsOfURL_display_(NSURL url, boolean display) {
	int result = OS.objc_msgSend(this.id, OS.sel_openDocumentWithContentsOfURL_1display_1, url != null ? url.id : 0, display);
	return result != 0 ? new id(result) : null;
}

public id openDocumentWithContentsOfURL_display_error_(NSURL absoluteURL, boolean displayDocument, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_openDocumentWithContentsOfURL_1display_1error_1, absoluteURL != null ? absoluteURL.id : 0, displayDocument, outError);
	return result != 0 ? new id(result) : null;
}

public id openUntitledDocumentAndDisplay(boolean displayDocument, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_openUntitledDocumentAndDisplay_1error_1, displayDocument, outError);
	return result != 0 ? new id(result) : null;
}

public id openUntitledDocumentOfType(NSString type, boolean display) {
	int result = OS.objc_msgSend(this.id, OS.sel_openUntitledDocumentOfType_1display_1, type != null ? type.id : 0, display);
	return result != 0 ? new id(result) : null;
}

public boolean presentError_(NSError error) {
	return OS.objc_msgSend(this.id, OS.sel_presentError_1, error != null ? error.id : 0) != 0;
}

public void presentError_modalForWindow_delegate_didPresentSelector_contextInfo_(NSError error, NSWindow window, id delegate, int didPresentSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_presentError_1modalForWindow_1delegate_1didPresentSelector_1contextInfo_1, error != null ? error.id : 0, window != null ? window.id : 0, delegate != null ? delegate.id : 0, didPresentSelector, contextInfo);
}

public NSArray recentDocumentURLs() {
	int result = OS.objc_msgSend(this.id, OS.sel_recentDocumentURLs);
	return result != 0 ? new NSArray(result) : null;
}

public void removeDocument(NSDocument document) {
	OS.objc_msgSend(this.id, OS.sel_removeDocument_1, document != null ? document.id : 0);
}

public boolean reopenDocumentForURL(NSURL absoluteDocumentURL, NSURL absoluteDocumentContentsURL, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_reopenDocumentForURL_1withContentsOfURL_1error_1, absoluteDocumentURL != null ? absoluteDocumentURL.id : 0, absoluteDocumentContentsURL != null ? absoluteDocumentContentsURL.id : 0, outError) != 0;
}

public void reviewUnsavedDocumentsWithAlertTitle(NSString title, boolean cancellable, id delegate, int didReviewAllSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_reviewUnsavedDocumentsWithAlertTitle_1cancellable_1delegate_1didReviewAllSelector_1contextInfo_1, title != null ? title.id : 0, cancellable, delegate != null ? delegate.id : 0, didReviewAllSelector, contextInfo);
}

public int runModalOpenPanel(NSOpenPanel openPanel, NSArray types) {
	return OS.objc_msgSend(this.id, OS.sel_runModalOpenPanel_1forTypes_1, openPanel != null ? openPanel.id : 0, types != null ? types.id : 0);
}

public void saveAllDocuments(id sender) {
	OS.objc_msgSend(this.id, OS.sel_saveAllDocuments_1, sender != null ? sender.id : 0);
}

public void setAutosavingDelay(double autosavingDelay) {
	OS.objc_msgSend(this.id, OS.sel_setAutosavingDelay_1, autosavingDelay);
}

public void setShouldCreateUI(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setShouldCreateUI_1, flag);
}

public static id sharedDocumentController() {
	int result = OS.objc_msgSend(OS.class_NSDocumentController, OS.sel_sharedDocumentController);
	return result != 0 ? new id(result) : null;
}

public boolean shouldCreateUI() {
	return OS.objc_msgSend(this.id, OS.sel_shouldCreateUI) != 0;
}

public NSString typeForContentsOfURL(NSURL inAbsoluteURL, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_typeForContentsOfURL_1error_1, inAbsoluteURL != null ? inAbsoluteURL.id : 0, outError);
	return result != 0 ? new NSString(result) : null;
}

public NSString typeFromFileExtension(NSString fileNameExtensionOrHFSFileType) {
	int result = OS.objc_msgSend(this.id, OS.sel_typeFromFileExtension_1, fileNameExtensionOrHFSFileType != null ? fileNameExtensionOrHFSFileType.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public boolean validateUserInterfaceItem(id  anItem) {
	return OS.objc_msgSend(this.id, OS.sel_validateUserInterfaceItem_1, anItem != null ? anItem.id : 0) != 0;
}

public NSError willPresentError(NSError error) {
	int result = OS.objc_msgSend(this.id, OS.sel_willPresentError_1, error != null ? error.id : 0);
	return result != 0 ? new NSError(result) : null;
}

}
