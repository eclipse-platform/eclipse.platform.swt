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

public class NSSpellChecker extends NSObject {

public NSSpellChecker() {
	super();
}

public NSSpellChecker(int id) {
	super(id);
}

public NSView accessoryView() {
	int result = OS.objc_msgSend(this.id, OS.sel_accessoryView);
	return result != 0 ? new NSView(result) : null;
}

public NSArray availableLanguages() {
	int result = OS.objc_msgSend(this.id, OS.sel_availableLanguages);
	return result != 0 ? new NSArray(result) : null;
}

public NSRange checkGrammarOfString(NSString stringToCheck, int startingOffset, NSString language, boolean wrapFlag, int tag, int details) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_checkGrammarOfString_1startingAt_1language_1wrap_1inSpellDocumentWithTag_1details_1, stringToCheck != null ? stringToCheck.id : 0, startingOffset, language != null ? language.id : 0, wrapFlag, tag, details);
	return result;
}

public NSRange checkSpellingOfString_startingAt_(NSString stringToCheck, int startingOffset) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_checkSpellingOfString_1startingAt_1, stringToCheck != null ? stringToCheck.id : 0, startingOffset);
	return result;
}

public NSRange checkSpellingOfString_startingAt_language_wrap_inSpellDocumentWithTag_wordCount_(NSString stringToCheck, int startingOffset, NSString language, boolean wrapFlag, int tag, int wordCount) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_checkSpellingOfString_1startingAt_1language_1wrap_1inSpellDocumentWithTag_1wordCount_1, stringToCheck != null ? stringToCheck.id : 0, startingOffset, language != null ? language.id : 0, wrapFlag, tag, wordCount);
	return result;
}

public void closeSpellDocumentWithTag(int tag) {
	OS.objc_msgSend(this.id, OS.sel_closeSpellDocumentWithTag_1, tag);
}

public NSArray completionsForPartialWordRange(NSRange range, NSString string, NSString language, int tag) {
	int result = OS.objc_msgSend(this.id, OS.sel_completionsForPartialWordRange_1inString_1language_1inSpellDocumentWithTag_1, range, string != null ? string.id : 0, language != null ? language.id : 0, tag);
	return result != 0 ? new NSArray(result) : null;
}

public int countWordsInString(NSString stringToCount, NSString language) {
	return OS.objc_msgSend(this.id, OS.sel_countWordsInString_1language_1, stringToCount != null ? stringToCount.id : 0, language != null ? language.id : 0);
}

public void forgetWord(NSString word) {
	OS.objc_msgSend(this.id, OS.sel_forgetWord_1, word != null ? word.id : 0);
}

public NSArray guessesForWord(NSString word) {
	int result = OS.objc_msgSend(this.id, OS.sel_guessesForWord_1, word != null ? word.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public boolean hasLearnedWord(NSString word) {
	return OS.objc_msgSend(this.id, OS.sel_hasLearnedWord_1, word != null ? word.id : 0) != 0;
}

public void ignoreWord(NSString wordToIgnore, int tag) {
	OS.objc_msgSend(this.id, OS.sel_ignoreWord_1inSpellDocumentWithTag_1, wordToIgnore != null ? wordToIgnore.id : 0, tag);
}

public NSArray ignoredWordsInSpellDocumentWithTag(int tag) {
	int result = OS.objc_msgSend(this.id, OS.sel_ignoredWordsInSpellDocumentWithTag_1, tag);
	return result != 0 ? new NSArray(result) : null;
}

public NSString language() {
	int result = OS.objc_msgSend(this.id, OS.sel_language);
	return result != 0 ? new NSString(result) : null;
}

public void learnWord(NSString word) {
	OS.objc_msgSend(this.id, OS.sel_learnWord_1, word != null ? word.id : 0);
}

public void setAccessoryView(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_setAccessoryView_1, aView != null ? aView.id : 0);
}

public void setIgnoredWords(NSArray words, int tag) {
	OS.objc_msgSend(this.id, OS.sel_setIgnoredWords_1inSpellDocumentWithTag_1, words != null ? words.id : 0, tag);
}

public boolean setLanguage(NSString language) {
	return OS.objc_msgSend(this.id, OS.sel_setLanguage_1, language != null ? language.id : 0) != 0;
}

public void setWordFieldStringValue(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setWordFieldStringValue_1, aString != null ? aString.id : 0);
}

public static NSSpellChecker sharedSpellChecker() {
	int result = OS.objc_msgSend(OS.class_NSSpellChecker, OS.sel_sharedSpellChecker);
	return result != 0 ? new NSSpellChecker(result) : null;
}

public static boolean sharedSpellCheckerExists() {
	return OS.objc_msgSend(OS.class_NSSpellChecker, OS.sel_sharedSpellCheckerExists) != 0;
}

public NSPanel spellingPanel() {
	int result = OS.objc_msgSend(this.id, OS.sel_spellingPanel);
	return result != 0 ? new NSPanel(result) : null;
}

public static int uniqueSpellDocumentTag() {
	return OS.objc_msgSend(OS.class_NSSpellChecker, OS.sel_uniqueSpellDocumentTag);
}

public void unlearnWord(NSString word) {
	OS.objc_msgSend(this.id, OS.sel_unlearnWord_1, word != null ? word.id : 0);
}

public void updateSpellingPanelWithGrammarString(NSString string, NSDictionary detail) {
	OS.objc_msgSend(this.id, OS.sel_updateSpellingPanelWithGrammarString_1detail_1, string != null ? string.id : 0, detail != null ? detail.id : 0);
}

public void updateSpellingPanelWithMisspelledWord(NSString word) {
	OS.objc_msgSend(this.id, OS.sel_updateSpellingPanelWithMisspelledWord_1, word != null ? word.id : 0);
}

}
