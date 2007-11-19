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

public class WebView extends NSView {

public WebView() {
	super();
}

public WebView(int id) {
	super(id);
}

public static NSArray MIMETypesShownAsHTML() {
	int result = OS.objc_msgSend(OS.class_WebView, OS.sel_MIMETypesShownAsHTML);
	return result != 0 ? new NSArray(result) : null;
}

public id UIDelegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_UIDelegate);
	return result != 0 ? new id(result) : null;
}

public static NSURL URLFromPasteboard(NSPasteboard pasteboard) {
	int result = OS.objc_msgSend(OS.class_WebView, OS.sel_URLFromPasteboard_1, pasteboard != null ? pasteboard.id : 0);
	return result != 0 ? new NSURL(result) : null;
}

public static NSString URLTitleFromPasteboard(NSPasteboard pasteboard) {
	int result = OS.objc_msgSend(OS.class_WebView, OS.sel_URLTitleFromPasteboard_1, pasteboard != null ? pasteboard.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public void alignCenter(id sender) {
	OS.objc_msgSend(this.id, OS.sel_alignCenter_1, sender != null ? sender.id : 0);
}

public void alignJustified(id sender) {
	OS.objc_msgSend(this.id, OS.sel_alignJustified_1, sender != null ? sender.id : 0);
}

public void alignLeft(id sender) {
	OS.objc_msgSend(this.id, OS.sel_alignLeft_1, sender != null ? sender.id : 0);
}

public void alignRight(id sender) {
	OS.objc_msgSend(this.id, OS.sel_alignRight_1, sender != null ? sender.id : 0);
}

public NSString applicationNameForUserAgent() {
	int result = OS.objc_msgSend(this.id, OS.sel_applicationNameForUserAgent);
	return result != 0 ? new NSString(result) : null;
}

//public void applyStyle(DOMCSSStyleDeclaration style) {
//	OS.objc_msgSend(this.id, OS.sel_applyStyle_1, style != null ? style.id : 0);
//}
//
//public WebBackForwardList backForwardList() {
//	int result = OS.objc_msgSend(this.id, OS.sel_backForwardList);
//	return result != 0 ? new WebBackForwardList(result) : null;
//}

public boolean canGoBack() {
	return OS.objc_msgSend(this.id, OS.sel_canGoBack) != 0;
}

public boolean canGoForward() {
	return OS.objc_msgSend(this.id, OS.sel_canGoForward) != 0;
}

public boolean canMakeTextLarger() {
	return OS.objc_msgSend(this.id, OS.sel_canMakeTextLarger) != 0;
}

public boolean canMakeTextSmaller() {
	return OS.objc_msgSend(this.id, OS.sel_canMakeTextSmaller) != 0;
}

public boolean canMakeTextStandardSize() {
	return OS.objc_msgSend(this.id, OS.sel_canMakeTextStandardSize) != 0;
}

public static boolean canShowMIMEType(NSString MIMEType) {
	return OS.objc_msgSend(OS.class_WebView, OS.sel_canShowMIMEType_1, MIMEType != null ? MIMEType.id : 0) != 0;
}

public static boolean canShowMIMETypeAsHTML(NSString MIMEType) {
	return OS.objc_msgSend(OS.class_WebView, OS.sel_canShowMIMETypeAsHTML_1, MIMEType != null ? MIMEType.id : 0) != 0;
}

public void changeAttributes(id sender) {
	OS.objc_msgSend(this.id, OS.sel_changeAttributes_1, sender != null ? sender.id : 0);
}

public void changeColor(id sender) {
	OS.objc_msgSend(this.id, OS.sel_changeColor_1, sender != null ? sender.id : 0);
}

public void changeDocumentBackgroundColor(id sender) {
	OS.objc_msgSend(this.id, OS.sel_changeDocumentBackgroundColor_1, sender != null ? sender.id : 0);
}

public void changeFont(id sender) {
	OS.objc_msgSend(this.id, OS.sel_changeFont_1, sender != null ? sender.id : 0);
}

public void checkSpelling(id sender) {
	OS.objc_msgSend(this.id, OS.sel_checkSpelling_1, sender != null ? sender.id : 0);
}

public void close() {
	OS.objc_msgSend(this.id, OS.sel_close);
}

//public DOMCSSStyleDeclaration computedStyleForElement(DOMElement element, NSString pseudoElement) {
//	int result = OS.objc_msgSend(this.id, OS.sel_computedStyleForElement_1pseudoElement_1, element != null ? element.id : 0, pseudoElement != null ? pseudoElement.id : 0);
//	return result != 0 ? new DOMCSSStyleDeclaration(result) : null;
//}

public void copy(id sender) {
	OS.objc_msgSend(this.id, OS.sel_copy_1, sender != null ? sender.id : 0);
}

public void copyFont(id sender) {
	OS.objc_msgSend(this.id, OS.sel_copyFont_1, sender != null ? sender.id : 0);
}

public NSString customTextEncodingName() {
	int result = OS.objc_msgSend(this.id, OS.sel_customTextEncodingName);
	return result != 0 ? new NSString(result) : null;
}

public NSString customUserAgent() {
	int result = OS.objc_msgSend(this.id, OS.sel_customUserAgent);
	return result != 0 ? new NSString(result) : null;
}

public void cut(id sender) {
	OS.objc_msgSend(this.id, OS.sel_cut_1, sender != null ? sender.id : 0);
}

public void delete(id sender) {
	OS.objc_msgSend(this.id, OS.sel_delete_1, sender != null ? sender.id : 0);
}

public void deleteSelection() {
	OS.objc_msgSend(this.id, OS.sel_deleteSelection);
}

public id downloadDelegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_downloadDelegate);
	return result != 0 ? new id(result) : null;
}

public boolean drawsBackground() {
	return OS.objc_msgSend(this.id, OS.sel_drawsBackground) != 0;
}

//public DOMRange editableDOMRangeForPoint(NSPoint point) {
//	int result = OS.objc_msgSend(this.id, OS.sel_editableDOMRangeForPoint_1, point);
//	return result != 0 ? new DOMRange(result) : null;
//}

public id editingDelegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_editingDelegate);
	return result != 0 ? new id(result) : null;
}

public NSDictionary elementAtPoint(NSPoint point) {
	int result = OS.objc_msgSend(this.id, OS.sel_elementAtPoint_1, point);
	return result != 0 ? new NSDictionary(result) : null;
}

public double estimatedProgress() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_estimatedProgress);
}

public id frameLoadDelegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_frameLoadDelegate);
	return result != 0 ? new id(result) : null;
}

public boolean goBack() {
	return OS.objc_msgSend(this.id, OS.sel_goBack) != 0;
}

public void goBack_(id sender) {
	OS.objc_msgSend(this.id, OS.sel_goBack_1, sender != null ? sender.id : 0);
}

public boolean goForward() {
	return OS.objc_msgSend(this.id, OS.sel_goForward) != 0;
}

public void goForward_(id sender) {
	OS.objc_msgSend(this.id, OS.sel_goForward_1, sender != null ? sender.id : 0);
}
//
//public boolean goToBackForwardItem(WebHistoryItem item) {
//	return OS.objc_msgSend(this.id, OS.sel_goToBackForwardItem_1, item != null ? item.id : 0) != 0;
//}

public NSString groupName() {
	int result = OS.objc_msgSend(this.id, OS.sel_groupName);
	return result != 0 ? new NSString(result) : null;
}

public NSWindow hostWindow() {
	int result = OS.objc_msgSend(this.id, OS.sel_hostWindow);
	return result != 0 ? new NSWindow(result) : null;
}

public id initWithFrame(NSRect frame, NSString frameName, NSString groupName) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_1frameName_1groupName_1, frame, frameName != null ? frameName.id : 0, groupName != null ? groupName.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isContinuousSpellCheckingEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isContinuousSpellCheckingEnabled) != 0;
}

public boolean isEditable() {
	return OS.objc_msgSend(this.id, OS.sel_isEditable) != 0;
}

public boolean isLoading() {
	return OS.objc_msgSend(this.id, OS.sel_isLoading) != 0;
}

public WebFrame mainFrame() {
	int result = OS.objc_msgSend(this.id, OS.sel_mainFrame);
	return result != 0 ? new WebFrame(result) : null;
}

//public DOMDocument mainFrameDocument() {
//	int result = OS.objc_msgSend(this.id, OS.sel_mainFrameDocument);
//	return result != 0 ? new DOMDocument(result) : null;
//}

public NSImage mainFrameIcon() {
	int result = OS.objc_msgSend(this.id, OS.sel_mainFrameIcon);
	return result != 0 ? new NSImage(result) : null;
}

public NSString mainFrameTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_mainFrameTitle);
	return result != 0 ? new NSString(result) : null;
}

public NSString mainFrameURL() {
	int result = OS.objc_msgSend(this.id, OS.sel_mainFrameURL);
	return result != 0 ? new NSString(result) : null;
}

public boolean maintainsInactiveSelection() {
	return OS.objc_msgSend(this.id, OS.sel_maintainsInactiveSelection) != 0;
}

public void makeTextLarger(id sender) {
	OS.objc_msgSend(this.id, OS.sel_makeTextLarger_1, sender != null ? sender.id : 0);
}

public void makeTextSmaller(id sender) {
	OS.objc_msgSend(this.id, OS.sel_makeTextSmaller_1, sender != null ? sender.id : 0);
}

public void makeTextStandardSize(id sender) {
	OS.objc_msgSend(this.id, OS.sel_makeTextStandardSize_1, sender != null ? sender.id : 0);
}

public NSString mediaStyle() {
	int result = OS.objc_msgSend(this.id, OS.sel_mediaStyle);
	return result != 0 ? new NSString(result) : null;
}

public void moveDragCaretToPoint(NSPoint point) {
	OS.objc_msgSend(this.id, OS.sel_moveDragCaretToPoint_1, point);
}

public void moveToBeginningOfSentence(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveToBeginningOfSentence_1, sender != null ? sender.id : 0);
}

public void moveToBeginningOfSentenceAndModifySelection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveToBeginningOfSentenceAndModifySelection_1, sender != null ? sender.id : 0);
}

public void moveToEndOfSentence(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveToEndOfSentence_1, sender != null ? sender.id : 0);
}

public void moveToEndOfSentenceAndModifySelection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_moveToEndOfSentenceAndModifySelection_1, sender != null ? sender.id : 0);
}

public void paste(id sender) {
	OS.objc_msgSend(this.id, OS.sel_paste_1, sender != null ? sender.id : 0);
}

public void pasteAsPlainText(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pasteAsPlainText_1, sender != null ? sender.id : 0);
}

public void pasteAsRichText(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pasteAsRichText_1, sender != null ? sender.id : 0);
}

public void pasteFont(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pasteFont_1, sender != null ? sender.id : 0);
}

public NSArray pasteboardTypesForElement(NSDictionary element) {
	int result = OS.objc_msgSend(this.id, OS.sel_pasteboardTypesForElement_1, element != null ? element.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray pasteboardTypesForSelection() {
	int result = OS.objc_msgSend(this.id, OS.sel_pasteboardTypesForSelection);
	return result != 0 ? new NSArray(result) : null;
}

public void performFindPanelAction(id sender) {
	OS.objc_msgSend(this.id, OS.sel_performFindPanelAction_1, sender != null ? sender.id : 0);
}

public id policyDelegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_policyDelegate);
	return result != 0 ? new id(result) : null;
}

//public WebPreferences preferences() {
//	int result = OS.objc_msgSend(this.id, OS.sel_preferences);
//	return result != 0 ? new WebPreferences(result) : null;
//}

public NSString preferencesIdentifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_preferencesIdentifier);
	return result != 0 ? new NSString(result) : null;
}

public static void registerURLSchemeAsLocal(NSString scheme) {
	OS.objc_msgSend(OS.class_WebView, OS.sel_registerURLSchemeAsLocal_1, scheme != null ? scheme.id : 0);
}

public static void registerViewClass(int viewClass, int representationClass, NSString MIMEType) {
	OS.objc_msgSend(OS.class_WebView, OS.sel_registerViewClass_1representationClass_1forMIMEType_1, viewClass, representationClass, MIMEType != null ? MIMEType.id : 0);
}

public void reload(id sender) {
	OS.objc_msgSend(this.id, OS.sel_reload_1, sender != null ? sender.id : 0);
}

public void removeDragCaret() {
	OS.objc_msgSend(this.id, OS.sel_removeDragCaret);
}

//public void replaceSelectionWithArchive(WebArchive archive) {
//	OS.objc_msgSend(this.id, OS.sel_replaceSelectionWithArchive_1, archive != null ? archive.id : 0);
//}

public void replaceSelectionWithMarkupString(NSString markupString) {
	OS.objc_msgSend(this.id, OS.sel_replaceSelectionWithMarkupString_1, markupString != null ? markupString.id : 0);
}

//public void replaceSelectionWithNode(DOMNode node) {
//	OS.objc_msgSend(this.id, OS.sel_replaceSelectionWithNode_1, node != null ? node.id : 0);
//}

public void replaceSelectionWithText(NSString text) {
	OS.objc_msgSend(this.id, OS.sel_replaceSelectionWithText_1, text != null ? text.id : 0);
}

public id resourceLoadDelegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_resourceLoadDelegate);
	return result != 0 ? new id(result) : null;
}

//public boolean searchFor(NSString string, boolean forward, boolean caseFlag, boolean wrapFlag) {
//	return OS.objc_msgSend(this.id, OS.sel_searchFor_1direction_1caseSensitive_1wrap_1, string != null ? string.id : 0, forward, caseFlag, wrapFlag) != 0;
//}

public void selectSentence(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectSentence_1, sender != null ? sender.id : 0);
}

//public DOMRange selectedDOMRange() {
//	int result = OS.objc_msgSend(this.id, OS.sel_selectedDOMRange);
//	return result != 0 ? new DOMRange(result) : null;
//}

//public WebFrame selectedFrame() {
//	int result = OS.objc_msgSend(this.id, OS.sel_selectedFrame);
//	return result != 0 ? new WebFrame(result) : null;
//}

public int selectionAffinity() {
	return OS.objc_msgSend(this.id, OS.sel_selectionAffinity);
}

public void setApplicationNameForUserAgent(NSString applicationName) {
	OS.objc_msgSend(this.id, OS.sel_setApplicationNameForUserAgent_1, applicationName != null ? applicationName.id : 0);
}

public void setContinuousSpellCheckingEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setContinuousSpellCheckingEnabled_1, flag);
}

public void setCustomTextEncodingName(NSString encodingName) {
	OS.objc_msgSend(this.id, OS.sel_setCustomTextEncodingName_1, encodingName != null ? encodingName.id : 0);
}

public void setCustomUserAgent(NSString userAgentString) {
	OS.objc_msgSend(this.id, OS.sel_setCustomUserAgent_1, userAgentString != null ? userAgentString.id : 0);
}

public void setDownloadDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDownloadDelegate_1, delegate != null ? delegate.id : 0);
}

public void setDrawsBackground(boolean drawsBackround) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_1, drawsBackround);
}

public void setEditable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_1, flag);
}

public void setEditingDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setEditingDelegate_1, delegate != null ? delegate.id : 0);
}

public void setFrameLoadDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setFrameLoadDelegate_1, delegate != null ? delegate.id : 0);
}

public void setGroupName(NSString groupName) {
	OS.objc_msgSend(this.id, OS.sel_setGroupName_1, groupName != null ? groupName.id : 0);
}

public void setHostWindow(NSWindow hostWindow) {
	OS.objc_msgSend(this.id, OS.sel_setHostWindow_1, hostWindow != null ? hostWindow.id : 0);
}

public static void setMIMETypesShownAsHTML(NSArray MIMETypes) {
	OS.objc_msgSend(OS.class_WebView, OS.sel_setMIMETypesShownAsHTML_1, MIMETypes != null ? MIMETypes.id : 0);
}

public void setMainFrameURL(NSString URLString) {
	OS.objc_msgSend(this.id, OS.sel_setMainFrameURL_1, URLString != null ? URLString.id : 0);
}

public void setMaintainsBackForwardList(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setMaintainsBackForwardList_1, flag);
}

public void setMediaStyle(NSString mediaStyle) {
	OS.objc_msgSend(this.id, OS.sel_setMediaStyle_1, mediaStyle != null ? mediaStyle.id : 0);
}

public void setPolicyDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setPolicyDelegate_1, delegate != null ? delegate.id : 0);
}

//public void setPreferences(WebPreferences prefs) {
//	OS.objc_msgSend(this.id, OS.sel_setPreferences_1, prefs != null ? prefs.id : 0);
//}

public void setPreferencesIdentifier(NSString anIdentifier) {
	OS.objc_msgSend(this.id, OS.sel_setPreferencesIdentifier_1, anIdentifier != null ? anIdentifier.id : 0);
}

public void setResourceLoadDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setResourceLoadDelegate_1, delegate != null ? delegate.id : 0);
}

//public void setSelectedDOMRange(DOMRange range, int selectionAffinity) {
//	OS.objc_msgSend(this.id, OS.sel_setSelectedDOMRange_1affinity_1, range != null ? range.id : 0, selectionAffinity);
//}

public void setShouldCloseWithWindow(boolean close) {
	OS.objc_msgSend(this.id, OS.sel_setShouldCloseWithWindow_1, close);
}

public void setSmartInsertDeleteEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSmartInsertDeleteEnabled_1, flag);
}

public void setTextSizeMultiplier(float multiplier) {
	OS.objc_msgSend(this.id, OS.sel_setTextSizeMultiplier_1, multiplier);
}

//public void setTypingStyle(DOMCSSStyleDeclaration style) {
//	OS.objc_msgSend(this.id, OS.sel_setTypingStyle_1, style != null ? style.id : 0);
//}

public void setUIDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setUIDelegate_1, delegate != null ? delegate.id : 0);
}

public boolean shouldCloseWithWindow() {
	return OS.objc_msgSend(this.id, OS.sel_shouldCloseWithWindow) != 0;
}

public void showGuessPanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_showGuessPanel_1, sender != null ? sender.id : 0);
}

public boolean smartInsertDeleteEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_smartInsertDeleteEnabled) != 0;
}

public int spellCheckerDocumentTag() {
	return OS.objc_msgSend(this.id, OS.sel_spellCheckerDocumentTag);
}

public void startSpeaking(id sender) {
	OS.objc_msgSend(this.id, OS.sel_startSpeaking_1, sender != null ? sender.id : 0);
}

public void stopLoading(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stopLoading_1, sender != null ? sender.id : 0);
}

public void stopSpeaking(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stopSpeaking_1, sender != null ? sender.id : 0);
}

public NSString stringByEvaluatingJavaScriptFromString(NSString script) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByEvaluatingJavaScriptFromString_1, script != null ? script.id : 0);
	return result != 0 ? new NSString(result) : null;
}

//public DOMCSSStyleDeclaration styleDeclarationWithText(NSString text) {
//	int result = OS.objc_msgSend(this.id, OS.sel_styleDeclarationWithText_1, text != null ? text.id : 0);
//	return result != 0 ? new DOMCSSStyleDeclaration(result) : null;
//}

public boolean supportsTextEncoding() {
	return OS.objc_msgSend(this.id, OS.sel_supportsTextEncoding) != 0;
}

public void takeStringURLFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeStringURLFrom_1, sender != null ? sender.id : 0);
}

public float textSizeMultiplier() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_textSizeMultiplier);
}

public void toggleContinuousSpellChecking(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleContinuousSpellChecking_1, sender != null ? sender.id : 0);
}

public void toggleSmartInsertDelete(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleSmartInsertDelete_1, sender != null ? sender.id : 0);
}

//public DOMCSSStyleDeclaration typingStyle() {
//	int result = OS.objc_msgSend(this.id, OS.sel_typingStyle);
//	return result != 0 ? new DOMCSSStyleDeclaration(result) : null;
//}

public NSUndoManager undoManager() {
	int result = OS.objc_msgSend(this.id, OS.sel_undoManager);
	return result != 0 ? new NSUndoManager(result) : null;
}

public NSString userAgentForURL(NSURL URL) {
	int result = OS.objc_msgSend(this.id, OS.sel_userAgentForURL_1, URL != null ? URL.id : 0);
	return result != 0 ? new NSString(result) : null;
}

//public WebScriptObject windowScriptObject() {
//	int result = OS.objc_msgSend(this.id, OS.sel_windowScriptObject);
//	return result != 0 ? new WebScriptObject(result) : null;
//}

public void writeElement(NSDictionary element, NSArray types, NSPasteboard pasteboard) {
	OS.objc_msgSend(this.id, OS.sel_writeElement_1withPasteboardTypes_1toPasteboard_1, element != null ? element.id : 0, types != null ? types.id : 0, pasteboard != null ? pasteboard.id : 0);
}

public void writeSelectionWithPasteboardTypes(NSArray types, NSPasteboard pasteboard) {
	OS.objc_msgSend(this.id, OS.sel_writeSelectionWithPasteboardTypes_1toPasteboard_1, types != null ? types.id : 0, pasteboard != null ? pasteboard.id : 0);
}

}
