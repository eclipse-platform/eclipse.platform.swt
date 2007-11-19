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

public class NSDocument extends NSObject {

public NSDocument() {
	super();
}

public NSDocument(int id) {
	super(id);
}

public void addWindowController(NSWindowController windowController) {
	OS.objc_msgSend(this.id, OS.sel_addWindowController_1, windowController != null ? windowController.id : 0);
}

public void autosaveDocumentWithDelegate(id delegate, int didAutosaveSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_autosaveDocumentWithDelegate_1didAutosaveSelector_1contextInfo_1, delegate != null ? delegate.id : 0, didAutosaveSelector, contextInfo);
}

public NSURL autosavedContentsFileURL() {
	int result = OS.objc_msgSend(this.id, OS.sel_autosavedContentsFileURL);
	return result != 0 ? new NSURL(result) : null;
}

public NSString autosavingFileType() {
	int result = OS.objc_msgSend(this.id, OS.sel_autosavingFileType);
	return result != 0 ? new NSString(result) : null;
}

public void canCloseDocumentWithDelegate(id delegate, int shouldCloseSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_canCloseDocumentWithDelegate_1shouldCloseSelector_1contextInfo_1, delegate != null ? delegate.id : 0, shouldCloseSelector, contextInfo);
}

public void close() {
	OS.objc_msgSend(this.id, OS.sel_close);
}

public NSData dataOfType(NSString typeName, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_dataOfType_1error_1, typeName != null ? typeName.id : 0, outError);
	return result != 0 ? new NSData(result) : null;
}

public NSData dataRepresentationOfType(NSString type) {
	int result = OS.objc_msgSend(this.id, OS.sel_dataRepresentationOfType_1, type != null ? type.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public NSString displayName() {
	int result = OS.objc_msgSend(this.id, OS.sel_displayName);
	return result != 0 ? new NSString(result) : null;
}

public NSDictionary fileAttributesToWriteToFile(NSString fullDocumentPath, NSString documentTypeName, int saveOperationType) {
	int result = OS.objc_msgSend(this.id, OS.sel_fileAttributesToWriteToFile_1ofType_1saveOperation_1, fullDocumentPath != null ? fullDocumentPath.id : 0, documentTypeName != null ? documentTypeName.id : 0, saveOperationType);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary fileAttributesToWriteToURL(NSURL absoluteURL, NSString typeName, int saveOperation, NSURL absoluteOriginalContentsURL, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_fileAttributesToWriteToURL_1ofType_1forSaveOperation_1originalContentsURL_1error_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0, saveOperation, absoluteOriginalContentsURL != null ? absoluteOriginalContentsURL.id : 0, outError);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDate fileModificationDate() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileModificationDate);
	return result != 0 ? new NSDate(result) : null;
}

public NSString fileName() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileName);
	return result != 0 ? new NSString(result) : null;
}

public NSString fileNameExtensionForType(NSString typeName, int saveOperation) {
	int result = OS.objc_msgSend(this.id, OS.sel_fileNameExtensionForType_1saveOperation_1, typeName != null ? typeName.id : 0, saveOperation);
	return result != 0 ? new NSString(result) : null;
}

public boolean fileNameExtensionWasHiddenInLastRunSavePanel() {
	return OS.objc_msgSend(this.id, OS.sel_fileNameExtensionWasHiddenInLastRunSavePanel) != 0;
}

public NSString fileType() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileType);
	return result != 0 ? new NSString(result) : null;
}

public NSString fileTypeFromLastRunSavePanel() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileTypeFromLastRunSavePanel);
	return result != 0 ? new NSString(result) : null;
}

public NSURL fileURL() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileURL);
	return result != 0 ? new NSURL(result) : null;
}

public NSFileWrapper fileWrapperOfType(NSString typeName, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_fileWrapperOfType_1error_1, typeName != null ? typeName.id : 0, outError);
	return result != 0 ? new NSFileWrapper(result) : null;
}

public NSFileWrapper fileWrapperRepresentationOfType(NSString type) {
	int result = OS.objc_msgSend(this.id, OS.sel_fileWrapperRepresentationOfType_1, type != null ? type.id : 0);
	return result != 0 ? new NSFileWrapper(result) : null;
}

public id handleCloseScriptCommand(NSCloseCommand command) {
	int result = OS.objc_msgSend(this.id, OS.sel_handleCloseScriptCommand_1, command != null ? command.id : 0);
	return result != 0 ? new id(result) : null;
}

public id handlePrintScriptCommand(NSScriptCommand command) {
	int result = OS.objc_msgSend(this.id, OS.sel_handlePrintScriptCommand_1, command != null ? command.id : 0);
	return result != 0 ? new id(result) : null;
}

public id handleSaveScriptCommand(NSScriptCommand command) {
	int result = OS.objc_msgSend(this.id, OS.sel_handleSaveScriptCommand_1, command != null ? command.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean hasUnautosavedChanges() {
	return OS.objc_msgSend(this.id, OS.sel_hasUnautosavedChanges) != 0;
}

public boolean hasUndoManager() {
	return OS.objc_msgSend(this.id, OS.sel_hasUndoManager) != 0;
}

public NSDocument initForURL(NSURL absoluteDocumentURL, NSURL absoluteDocumentContentsURL, NSString typeName, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_initForURL_1withContentsOfURL_1ofType_1error_1, absoluteDocumentURL != null ? absoluteDocumentURL.id : 0, absoluteDocumentContentsURL != null ? absoluteDocumentContentsURL.id : 0, typeName != null ? typeName.id : 0, outError);
	return result != 0 ? this : null;
}

public NSDocument initWithContentsOfFile(NSString absolutePath, NSString typeName) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfFile_1ofType_1, absolutePath != null ? absolutePath.id : 0, typeName != null ? typeName.id : 0);
	return result != 0 ? this : null;
}

public NSDocument initWithContentsOfURL_ofType_(NSURL absoluteURL, NSString typeName) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1ofType_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0);
	return result != 0 ? this : null;
}

public NSDocument initWithContentsOfURL_ofType_error_(NSURL absoluteURL, NSString typeName, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1ofType_1error_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0, outError);
	return result != 0 ? this : null;
}

public NSDocument initWithType(NSString typeName, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithType_1error_1, typeName != null ? typeName.id : 0, outError);
	return result != 0 ? this : null;
}

public boolean isDocumentEdited() {
	return OS.objc_msgSend(this.id, OS.sel_isDocumentEdited) != 0;
}

public static boolean isNativeType(NSString type) {
	return OS.objc_msgSend(OS.class_NSDocument, OS.sel_isNativeType_1, type != null ? type.id : 0) != 0;
}

public boolean keepBackupFile() {
	return OS.objc_msgSend(this.id, OS.sel_keepBackupFile) != 0;
}

public NSString lastComponentOfFileName() {
	int result = OS.objc_msgSend(this.id, OS.sel_lastComponentOfFileName);
	return result != 0 ? new NSString(result) : null;
}

public boolean loadDataRepresentation(NSData data, NSString type) {
	return OS.objc_msgSend(this.id, OS.sel_loadDataRepresentation_1ofType_1, data != null ? data.id : 0, type != null ? type.id : 0) != 0;
}

public boolean loadFileWrapperRepresentation(NSFileWrapper wrapper, NSString type) {
	return OS.objc_msgSend(this.id, OS.sel_loadFileWrapperRepresentation_1ofType_1, wrapper != null ? wrapper.id : 0, type != null ? type.id : 0) != 0;
}

public void makeWindowControllers() {
	OS.objc_msgSend(this.id, OS.sel_makeWindowControllers);
}

public NSScriptObjectSpecifier objectSpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectSpecifier);
	return result != 0 ? new NSScriptObjectSpecifier(result) : null;
}

public boolean preparePageLayout(NSPageLayout pageLayout) {
	return OS.objc_msgSend(this.id, OS.sel_preparePageLayout_1, pageLayout != null ? pageLayout.id : 0) != 0;
}

public boolean prepareSavePanel(NSSavePanel savePanel) {
	return OS.objc_msgSend(this.id, OS.sel_prepareSavePanel_1, savePanel != null ? savePanel.id : 0) != 0;
}

public boolean presentError_(NSError error) {
	return OS.objc_msgSend(this.id, OS.sel_presentError_1, error != null ? error.id : 0) != 0;
}

public void presentError_modalForWindow_delegate_didPresentSelector_contextInfo_(NSError error, NSWindow window, id delegate, int didPresentSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_presentError_1modalForWindow_1delegate_1didPresentSelector_1contextInfo_1, error != null ? error.id : 0, window != null ? window.id : 0, delegate != null ? delegate.id : 0, didPresentSelector, contextInfo);
}

public void printDocument(id sender) {
	OS.objc_msgSend(this.id, OS.sel_printDocument_1, sender != null ? sender.id : 0);
}

public void printDocumentWithSettings(NSDictionary printSettings, boolean showPrintPanel, id delegate, int didPrintSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_printDocumentWithSettings_1showPrintPanel_1delegate_1didPrintSelector_1contextInfo_1, printSettings != null ? printSettings.id : 0, showPrintPanel, delegate != null ? delegate.id : 0, didPrintSelector, contextInfo);
}

public NSPrintInfo printInfo() {
	int result = OS.objc_msgSend(this.id, OS.sel_printInfo);
	return result != 0 ? new NSPrintInfo(result) : null;
}

public NSPrintOperation printOperationWithSettings(NSDictionary printSettings, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_printOperationWithSettings_1error_1, printSettings != null ? printSettings.id : 0, outError);
	return result != 0 ? new NSPrintOperation(result) : null;
}

public void printShowingPrintPanel(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_printShowingPrintPanel_1, flag);
}

public boolean readFromData(NSData data, NSString typeName, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_readFromData_1ofType_1error_1, data != null ? data.id : 0, typeName != null ? typeName.id : 0, outError) != 0;
}

public boolean readFromFile(NSString fileName, NSString type) {
	return OS.objc_msgSend(this.id, OS.sel_readFromFile_1ofType_1, fileName != null ? fileName.id : 0, type != null ? type.id : 0) != 0;
}

public boolean readFromFileWrapper(NSFileWrapper fileWrapper, NSString typeName, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_readFromFileWrapper_1ofType_1error_1, fileWrapper != null ? fileWrapper.id : 0, typeName != null ? typeName.id : 0, outError) != 0;
}

public boolean readFromURL_ofType_(NSURL url, NSString type) {
	return OS.objc_msgSend(this.id, OS.sel_readFromURL_1ofType_1, url != null ? url.id : 0, type != null ? type.id : 0) != 0;
}

public boolean readFromURL_ofType_error_(NSURL absoluteURL, NSString typeName, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_readFromURL_1ofType_1error_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0, outError) != 0;
}

public static NSArray readableTypes() {
	int result = OS.objc_msgSend(OS.class_NSDocument, OS.sel_readableTypes);
	return result != 0 ? new NSArray(result) : null;
}

public void removeWindowController(NSWindowController windowController) {
	OS.objc_msgSend(this.id, OS.sel_removeWindowController_1, windowController != null ? windowController.id : 0);
}

public void revertDocumentToSaved(id sender) {
	OS.objc_msgSend(this.id, OS.sel_revertDocumentToSaved_1, sender != null ? sender.id : 0);
}

public boolean revertToContentsOfURL(NSURL absoluteURL, NSString typeName, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_revertToContentsOfURL_1ofType_1error_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0, outError) != 0;
}

public boolean revertToSavedFromFile(NSString fileName, NSString type) {
	return OS.objc_msgSend(this.id, OS.sel_revertToSavedFromFile_1ofType_1, fileName != null ? fileName.id : 0, type != null ? type.id : 0) != 0;
}

public boolean revertToSavedFromURL(NSURL url, NSString type) {
	return OS.objc_msgSend(this.id, OS.sel_revertToSavedFromURL_1ofType_1, url != null ? url.id : 0, type != null ? type.id : 0) != 0;
}

public int runModalPageLayoutWithPrintInfo_(NSPrintInfo printInfo) {
	return OS.objc_msgSend(this.id, OS.sel_runModalPageLayoutWithPrintInfo_1, printInfo != null ? printInfo.id : 0);
}

public void runModalPageLayoutWithPrintInfo_delegate_didRunSelector_contextInfo_(NSPrintInfo printInfo, id delegate, int didRunSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_runModalPageLayoutWithPrintInfo_1delegate_1didRunSelector_1contextInfo_1, printInfo != null ? printInfo.id : 0, delegate != null ? delegate.id : 0, didRunSelector, contextInfo);
}

public void runModalPrintOperation(NSPrintOperation printOperation, id delegate, int didRunSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_runModalPrintOperation_1delegate_1didRunSelector_1contextInfo_1, printOperation != null ? printOperation.id : 0, delegate != null ? delegate.id : 0, didRunSelector, contextInfo);
}

public void runModalSavePanelForSaveOperation(int saveOperation, id delegate, int didSaveSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_runModalSavePanelForSaveOperation_1delegate_1didSaveSelector_1contextInfo_1, saveOperation, delegate != null ? delegate.id : 0, didSaveSelector, contextInfo);
}

public void runPageLayout(id sender) {
	OS.objc_msgSend(this.id, OS.sel_runPageLayout_1, sender != null ? sender.id : 0);
}

public void saveDocument(id sender) {
	OS.objc_msgSend(this.id, OS.sel_saveDocument_1, sender != null ? sender.id : 0);
}

public void saveDocumentAs(id sender) {
	OS.objc_msgSend(this.id, OS.sel_saveDocumentAs_1, sender != null ? sender.id : 0);
}

public void saveDocumentTo(id sender) {
	OS.objc_msgSend(this.id, OS.sel_saveDocumentTo_1, sender != null ? sender.id : 0);
}

public void saveDocumentWithDelegate(id delegate, int didSaveSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_saveDocumentWithDelegate_1didSaveSelector_1contextInfo_1, delegate != null ? delegate.id : 0, didSaveSelector, contextInfo);
}

public void saveToFile(NSString fileName, int saveOperation, id delegate, int didSaveSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_saveToFile_1saveOperation_1delegate_1didSaveSelector_1contextInfo_1, fileName != null ? fileName.id : 0, saveOperation, delegate != null ? delegate.id : 0, didSaveSelector, contextInfo);
}

public void saveToURL_ofType_forSaveOperation_delegate_didSaveSelector_contextInfo_(NSURL absoluteURL, NSString typeName, int saveOperation, id delegate, int didSaveSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_saveToURL_1ofType_1forSaveOperation_1delegate_1didSaveSelector_1contextInfo_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0, saveOperation, delegate != null ? delegate.id : 0, didSaveSelector, contextInfo);
}

public boolean saveToURL_ofType_forSaveOperation_error_(NSURL absoluteURL, NSString typeName, int saveOperation, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_saveToURL_1ofType_1forSaveOperation_1error_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0, saveOperation, outError) != 0;
}

public void setAutosavedContentsFileURL(NSURL absoluteURL) {
	OS.objc_msgSend(this.id, OS.sel_setAutosavedContentsFileURL_1, absoluteURL != null ? absoluteURL.id : 0);
}

public void setFileModificationDate(NSDate modificationDate) {
	OS.objc_msgSend(this.id, OS.sel_setFileModificationDate_1, modificationDate != null ? modificationDate.id : 0);
}

public void setFileName(NSString fileName) {
	OS.objc_msgSend(this.id, OS.sel_setFileName_1, fileName != null ? fileName.id : 0);
}

public void setFileType(NSString typeName) {
	OS.objc_msgSend(this.id, OS.sel_setFileType_1, typeName != null ? typeName.id : 0);
}

public void setFileURL(NSURL absoluteURL) {
	OS.objc_msgSend(this.id, OS.sel_setFileURL_1, absoluteURL != null ? absoluteURL.id : 0);
}

public void setHasUndoManager(boolean hasUndoManager) {
	OS.objc_msgSend(this.id, OS.sel_setHasUndoManager_1, hasUndoManager);
}

public void setLastComponentOfFileName(NSString str) {
	OS.objc_msgSend(this.id, OS.sel_setLastComponentOfFileName_1, str != null ? str.id : 0);
}

public void setPrintInfo(NSPrintInfo printInfo) {
	OS.objc_msgSend(this.id, OS.sel_setPrintInfo_1, printInfo != null ? printInfo.id : 0);
}

public void setUndoManager(NSUndoManager undoManager) {
	OS.objc_msgSend(this.id, OS.sel_setUndoManager_1, undoManager != null ? undoManager.id : 0);
}

public void setWindow(NSWindow window) {
	OS.objc_msgSend(this.id, OS.sel_setWindow_1, window != null ? window.id : 0);
}

public boolean shouldChangePrintInfo(NSPrintInfo newPrintInfo) {
	return OS.objc_msgSend(this.id, OS.sel_shouldChangePrintInfo_1, newPrintInfo != null ? newPrintInfo.id : 0) != 0;
}

public void shouldCloseWindowController(NSWindowController windowController, id delegate, int shouldCloseSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_shouldCloseWindowController_1delegate_1shouldCloseSelector_1contextInfo_1, windowController != null ? windowController.id : 0, delegate != null ? delegate.id : 0, shouldCloseSelector, contextInfo);
}

public boolean shouldRunSavePanelWithAccessoryView() {
	return OS.objc_msgSend(this.id, OS.sel_shouldRunSavePanelWithAccessoryView) != 0;
}

public void showWindows() {
	OS.objc_msgSend(this.id, OS.sel_showWindows);
}

public NSUndoManager undoManager() {
	int result = OS.objc_msgSend(this.id, OS.sel_undoManager);
	return result != 0 ? new NSUndoManager(result) : null;
}

public void updateChangeCount(int change) {
	OS.objc_msgSend(this.id, OS.sel_updateChangeCount_1, change);
}

public boolean validateUserInterfaceItem(id  anItem) {
	return OS.objc_msgSend(this.id, OS.sel_validateUserInterfaceItem_1, anItem != null ? anItem.id : 0) != 0;
}

public NSError willPresentError(NSError error) {
	int result = OS.objc_msgSend(this.id, OS.sel_willPresentError_1, error != null ? error.id : 0);
	return result != 0 ? new NSError(result) : null;
}

public void windowControllerDidLoadNib(NSWindowController windowController) {
	OS.objc_msgSend(this.id, OS.sel_windowControllerDidLoadNib_1, windowController != null ? windowController.id : 0);
}

public void windowControllerWillLoadNib(NSWindowController windowController) {
	OS.objc_msgSend(this.id, OS.sel_windowControllerWillLoadNib_1, windowController != null ? windowController.id : 0);
}

public NSArray windowControllers() {
	int result = OS.objc_msgSend(this.id, OS.sel_windowControllers);
	return result != 0 ? new NSArray(result) : null;
}

public NSWindow windowForSheet() {
	int result = OS.objc_msgSend(this.id, OS.sel_windowForSheet);
	return result != 0 ? new NSWindow(result) : null;
}

public NSString windowNibName() {
	int result = OS.objc_msgSend(this.id, OS.sel_windowNibName);
	return result != 0 ? new NSString(result) : null;
}

public static NSArray writableTypes() {
	int result = OS.objc_msgSend(OS.class_NSDocument, OS.sel_writableTypes);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray writableTypesForSaveOperation(int saveOperation) {
	int result = OS.objc_msgSend(this.id, OS.sel_writableTypesForSaveOperation_1, saveOperation);
	return result != 0 ? new NSArray(result) : null;
}

public boolean writeSafelyToURL(NSURL absoluteURL, NSString typeName, int saveOperation, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_writeSafelyToURL_1ofType_1forSaveOperation_1error_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0, saveOperation, outError) != 0;
}

public boolean writeToFile_ofType_(NSString fileName, NSString type) {
	return OS.objc_msgSend(this.id, OS.sel_writeToFile_1ofType_1, fileName != null ? fileName.id : 0, type != null ? type.id : 0) != 0;
}

public boolean writeToFile_ofType_originalFile_saveOperation_(NSString fullDocumentPath, NSString documentTypeName, NSString fullOriginalDocumentPath, int saveOperationType) {
	return OS.objc_msgSend(this.id, OS.sel_writeToFile_1ofType_1originalFile_1saveOperation_1, fullDocumentPath != null ? fullDocumentPath.id : 0, documentTypeName != null ? documentTypeName.id : 0, fullOriginalDocumentPath != null ? fullOriginalDocumentPath.id : 0, saveOperationType) != 0;
}

public boolean writeToURL_ofType_(NSURL url, NSString type) {
	return OS.objc_msgSend(this.id, OS.sel_writeToURL_1ofType_1, url != null ? url.id : 0, type != null ? type.id : 0) != 0;
}

public boolean writeToURL_ofType_error_(NSURL absoluteURL, NSString typeName, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_writeToURL_1ofType_1error_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0, outError) != 0;
}

public boolean writeToURL_ofType_forSaveOperation_originalContentsURL_error_(NSURL absoluteURL, NSString typeName, int saveOperation, NSURL absoluteOriginalContentsURL, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_writeToURL_1ofType_1forSaveOperation_1originalContentsURL_1error_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0, saveOperation, absoluteOriginalContentsURL != null ? absoluteOriginalContentsURL.id : 0, outError) != 0;
}

public boolean writeWithBackupToFile(NSString fullDocumentPath, NSString documentTypeName, int saveOperationType) {
	return OS.objc_msgSend(this.id, OS.sel_writeWithBackupToFile_1ofType_1saveOperation_1, fullDocumentPath != null ? fullDocumentPath.id : 0, documentTypeName != null ? documentTypeName.id : 0, saveOperationType) != 0;
}

}
