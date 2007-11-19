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

public class NSSound extends NSObject {

public NSSound() {
	super();
}

public NSSound(int id) {
	super(id);
}

public static boolean canInitWithPasteboard(NSPasteboard pasteboard) {
	return OS.objc_msgSend(OS.class_NSSound, OS.sel_canInitWithPasteboard_1, pasteboard != null ? pasteboard.id : 0) != 0;
}

public NSArray channelMapping() {
	int result = OS.objc_msgSend(this.id, OS.sel_channelMapping);
	return result != 0 ? new NSArray(result) : null;
}

public double currentTime() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_currentTime);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public double duration() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_duration);
}

public id initWithContentsOfFile(NSString path, boolean byRef) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfFile_1byReference_1, path != null ? path.id : 0, byRef);
	return result != 0 ? new id(result) : null;
}

public id initWithContentsOfURL(NSURL url, boolean byRef) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1byReference_1, url != null ? url.id : 0, byRef);
	return result != 0 ? new id(result) : null;
}

public id initWithData(NSData data) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1, data != null ? data.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithPasteboard(NSPasteboard pasteboard) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithPasteboard_1, pasteboard != null ? pasteboard.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isPlaying() {
	return OS.objc_msgSend(this.id, OS.sel_isPlaying) != 0;
}

public boolean loops() {
	return OS.objc_msgSend(this.id, OS.sel_loops) != 0;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public boolean pause() {
	return OS.objc_msgSend(this.id, OS.sel_pause) != 0;
}

public boolean play() {
	return OS.objc_msgSend(this.id, OS.sel_play) != 0;
}

public NSString playbackDeviceIdentifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_playbackDeviceIdentifier);
	return result != 0 ? new NSString(result) : null;
}

public boolean resume() {
	return OS.objc_msgSend(this.id, OS.sel_resume) != 0;
}

public void setChannelMapping(NSArray channelMapping) {
	OS.objc_msgSend(this.id, OS.sel_setChannelMapping_1, channelMapping != null ? channelMapping.id : 0);
}

public void setCurrentTime(double seconds) {
	OS.objc_msgSend(this.id, OS.sel_setCurrentTime_1, seconds);
}

public void setDelegate(id aDelegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, aDelegate != null ? aDelegate.id : 0);
}

public void setLoops(boolean val) {
	OS.objc_msgSend(this.id, OS.sel_setLoops_1, val);
}

public boolean setName(NSString string) {
	return OS.objc_msgSend(this.id, OS.sel_setName_1, string != null ? string.id : 0) != 0;
}

public void setPlaybackDeviceIdentifier(NSString deviceUID) {
	OS.objc_msgSend(this.id, OS.sel_setPlaybackDeviceIdentifier_1, deviceUID != null ? deviceUID.id : 0);
}

public void setVolume(float volume) {
	OS.objc_msgSend(this.id, OS.sel_setVolume_1, volume);
}

public static id soundNamed(NSString name) {
	int result = OS.objc_msgSend(OS.class_NSSound, OS.sel_soundNamed_1, name != null ? name.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSArray soundUnfilteredFileTypes() {
	int result = OS.objc_msgSend(OS.class_NSSound, OS.sel_soundUnfilteredFileTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray soundUnfilteredPasteboardTypes() {
	int result = OS.objc_msgSend(OS.class_NSSound, OS.sel_soundUnfilteredPasteboardTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray soundUnfilteredTypes() {
	int result = OS.objc_msgSend(OS.class_NSSound, OS.sel_soundUnfilteredTypes);
	return result != 0 ? new NSArray(result) : null;
}

public boolean stop() {
	return OS.objc_msgSend(this.id, OS.sel_stop) != 0;
}

public float volume() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_volume);
}

public void writeToPasteboard(NSPasteboard pasteboard) {
	OS.objc_msgSend(this.id, OS.sel_writeToPasteboard_1, pasteboard != null ? pasteboard.id : 0);
}

}
