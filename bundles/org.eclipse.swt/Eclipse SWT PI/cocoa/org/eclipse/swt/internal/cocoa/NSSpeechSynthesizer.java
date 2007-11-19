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

public class NSSpeechSynthesizer extends NSObject {

public NSSpeechSynthesizer() {
	super();
}

public NSSpeechSynthesizer(int id) {
	super(id);
}

public void addSpeechDictionary(NSDictionary speechDictionary) {
	OS.objc_msgSend(this.id, OS.sel_addSpeechDictionary_1, speechDictionary != null ? speechDictionary.id : 0);
}

public static NSDictionary attributesForVoice(NSString voice) {
	int result = OS.objc_msgSend(OS.class_NSSpeechSynthesizer, OS.sel_attributesForVoice_1, voice != null ? voice.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public static NSArray availableVoices() {
	int result = OS.objc_msgSend(OS.class_NSSpeechSynthesizer, OS.sel_availableVoices);
	return result != 0 ? new NSArray(result) : null;
}

public void continueSpeaking() {
	OS.objc_msgSend(this.id, OS.sel_continueSpeaking);
}

public static NSString defaultVoice() {
	int result = OS.objc_msgSend(OS.class_NSSpeechSynthesizer, OS.sel_defaultVoice);
	return result != 0 ? new NSString(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public id initWithVoice(NSString voice) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithVoice_1, voice != null ? voice.id : 0);
	return result != 0 ? new id(result) : null;
}

public static boolean isAnyApplicationSpeaking() {
	return OS.objc_msgSend(OS.class_NSSpeechSynthesizer, OS.sel_isAnyApplicationSpeaking) != 0;
}

public boolean isSpeaking() {
	return OS.objc_msgSend(this.id, OS.sel_isSpeaking) != 0;
}

public id objectForProperty(NSString property, int outError) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectForProperty_1error_1, property != null ? property.id : 0, outError);
	return result != 0 ? new id(result) : null;
}

public void pauseSpeakingAtBoundary(int boundary) {
	OS.objc_msgSend(this.id, OS.sel_pauseSpeakingAtBoundary_1, boundary);
}

public NSString phonemesFromText(NSString text) {
	int result = OS.objc_msgSend(this.id, OS.sel_phonemesFromText_1, text != null ? text.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public float rate() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_rate);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public boolean setObject(id object, NSString property, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_setObject_1forProperty_1error_1, object != null ? object.id : 0, property != null ? property.id : 0, outError) != 0;
}

public void setRate(float rate) {
	OS.objc_msgSend(this.id, OS.sel_setRate_1, rate);
}

public void setUsesFeedbackWindow(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setUsesFeedbackWindow_1, flag);
}

public boolean setVoice(NSString voice) {
	return OS.objc_msgSend(this.id, OS.sel_setVoice_1, voice != null ? voice.id : 0) != 0;
}

public void setVolume(float volume) {
	OS.objc_msgSend(this.id, OS.sel_setVolume_1, volume);
}

public boolean startSpeakingString_(NSString string) {
	return OS.objc_msgSend(this.id, OS.sel_startSpeakingString_1, string != null ? string.id : 0) != 0;
}

public boolean startSpeakingString_toURL_(NSString string, NSURL url) {
	return OS.objc_msgSend(this.id, OS.sel_startSpeakingString_1toURL_1, string != null ? string.id : 0, url != null ? url.id : 0) != 0;
}

public void stopSpeaking() {
	OS.objc_msgSend(this.id, OS.sel_stopSpeaking);
}

public void stopSpeakingAtBoundary(int boundary) {
	OS.objc_msgSend(this.id, OS.sel_stopSpeakingAtBoundary_1, boundary);
}

public boolean usesFeedbackWindow() {
	return OS.objc_msgSend(this.id, OS.sel_usesFeedbackWindow) != 0;
}

public NSString voice() {
	int result = OS.objc_msgSend(this.id, OS.sel_voice);
	return result != 0 ? new NSString(result) : null;
}

public float volume() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_volume);
}

}
