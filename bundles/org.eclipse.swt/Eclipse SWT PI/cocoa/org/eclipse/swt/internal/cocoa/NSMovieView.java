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

public class NSMovieView extends NSView {

public NSMovieView() {
	super();
}

public NSMovieView(int id) {
	super(id);
}

public void clear(id sender) {
	OS.objc_msgSend(this.id, OS.sel_clear_1, sender != null ? sender.id : 0);
}

public void copy(id sender) {
	OS.objc_msgSend(this.id, OS.sel_copy_1, sender != null ? sender.id : 0);
}

public void cut(id sender) {
	OS.objc_msgSend(this.id, OS.sel_cut_1, sender != null ? sender.id : 0);
}

public void delete(id sender) {
	OS.objc_msgSend(this.id, OS.sel_delete_1, sender != null ? sender.id : 0);
}

public void gotoBeginning(id sender) {
	OS.objc_msgSend(this.id, OS.sel_gotoBeginning_1, sender != null ? sender.id : 0);
}

public void gotoEnd(id sender) {
	OS.objc_msgSend(this.id, OS.sel_gotoEnd_1, sender != null ? sender.id : 0);
}

public void gotoPosterFrame(id sender) {
	OS.objc_msgSend(this.id, OS.sel_gotoPosterFrame_1, sender != null ? sender.id : 0);
}

public boolean isControllerVisible() {
	return OS.objc_msgSend(this.id, OS.sel_isControllerVisible) != 0;
}

public boolean isEditable() {
	return OS.objc_msgSend(this.id, OS.sel_isEditable) != 0;
}

public boolean isMuted() {
	return OS.objc_msgSend(this.id, OS.sel_isMuted) != 0;
}

public boolean isPlaying() {
	return OS.objc_msgSend(this.id, OS.sel_isPlaying) != 0;
}

public int loopMode() {
	return OS.objc_msgSend(this.id, OS.sel_loopMode);
}

public NSMovie movie() {
	int result = OS.objc_msgSend(this.id, OS.sel_movie);
	return result != 0 ? new NSMovie(result) : null;
}

public int movieController() {
	return OS.objc_msgSend(this.id, OS.sel_movieController);
}

public NSRect movieRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_movieRect);
	return result;
}

public void paste(id sender) {
	OS.objc_msgSend(this.id, OS.sel_paste_1, sender != null ? sender.id : 0);
}

public boolean playsEveryFrame() {
	return OS.objc_msgSend(this.id, OS.sel_playsEveryFrame) != 0;
}

public boolean playsSelectionOnly() {
	return OS.objc_msgSend(this.id, OS.sel_playsSelectionOnly) != 0;
}

public float rate() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_rate);
}

public void resizeWithMagnification(float magnification) {
	OS.objc_msgSend(this.id, OS.sel_resizeWithMagnification_1, magnification);
}

public void selectAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectAll_1, sender != null ? sender.id : 0);
}

public void setEditable(boolean editable) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_1, editable);
}

public void setLoopMode(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setLoopMode_1, mode);
}

public void setMovie(NSMovie movie) {
	OS.objc_msgSend(this.id, OS.sel_setMovie_1, movie != null ? movie.id : 0);
}

public void setMuted(boolean mute) {
	OS.objc_msgSend(this.id, OS.sel_setMuted_1, mute);
}

public void setPlaysEveryFrame(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPlaysEveryFrame_1, flag);
}

public void setPlaysSelectionOnly(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPlaysSelectionOnly_1, flag);
}

public void setRate(float rate) {
	OS.objc_msgSend(this.id, OS.sel_setRate_1, rate);
}

public void setVolume(float volume) {
	OS.objc_msgSend(this.id, OS.sel_setVolume_1, volume);
}

public void showController(boolean show, boolean adjustSize) {
	OS.objc_msgSend(this.id, OS.sel_showController_1adjustingSize_1, show, adjustSize);
}

public NSSize sizeForMagnification(float magnification) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_sizeForMagnification_1, magnification);
	return result;
}

public void start(id sender) {
	OS.objc_msgSend(this.id, OS.sel_start_1, sender != null ? sender.id : 0);
}

public void stepBack(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stepBack_1, sender != null ? sender.id : 0);
}

public void stepForward(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stepForward_1, sender != null ? sender.id : 0);
}

public void stop(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stop_1, sender != null ? sender.id : 0);
}

public float volume() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_volume);
}

}
