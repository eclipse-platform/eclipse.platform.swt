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

public class NSAnimation extends NSObject {

public NSAnimation() {
	super();
}

public NSAnimation(int id) {
	super(id);
}

public void addProgressMark(float progressMark) {
	OS.objc_msgSend(this.id, OS.sel_addProgressMark_1, progressMark);
}

public int animationBlockingMode() {
	return OS.objc_msgSend(this.id, OS.sel_animationBlockingMode);
}

public int animationCurve() {
	return OS.objc_msgSend(this.id, OS.sel_animationCurve);
}

public void clearStartAnimation() {
	OS.objc_msgSend(this.id, OS.sel_clearStartAnimation);
}

public void clearStopAnimation() {
	OS.objc_msgSend(this.id, OS.sel_clearStopAnimation);
}

public float currentProgress() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_currentProgress);
}

public float currentValue() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_currentValue);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public double duration() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_duration);
}

public float frameRate() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_frameRate);
}

public NSAnimation initWithDuration(double duration, int animationCurve) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDuration_1animationCurve_1, duration, animationCurve);
	return result != 0 ? this : null;
}

public boolean isAnimating() {
	return OS.objc_msgSend(this.id, OS.sel_isAnimating) != 0;
}

public NSArray progressMarks() {
	int result = OS.objc_msgSend(this.id, OS.sel_progressMarks);
	return result != 0 ? new NSArray(result) : null;
}

public void removeProgressMark(float progressMark) {
	OS.objc_msgSend(this.id, OS.sel_removeProgressMark_1, progressMark);
}

public NSArray runLoopModesForAnimating() {
	int result = OS.objc_msgSend(this.id, OS.sel_runLoopModesForAnimating);
	return result != 0 ? new NSArray(result) : null;
}

public void setAnimationBlockingMode(int animationBlockingMode) {
	OS.objc_msgSend(this.id, OS.sel_setAnimationBlockingMode_1, animationBlockingMode);
}

public void setAnimationCurve(int curve) {
	OS.objc_msgSend(this.id, OS.sel_setAnimationCurve_1, curve);
}

public void setCurrentProgress(float progress) {
	OS.objc_msgSend(this.id, OS.sel_setCurrentProgress_1, progress);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setDuration(double duration) {
	OS.objc_msgSend(this.id, OS.sel_setDuration_1, duration);
}

public void setFrameRate(float framesPerSecond) {
	OS.objc_msgSend(this.id, OS.sel_setFrameRate_1, framesPerSecond);
}

public void setProgressMarks(NSArray progressMarks) {
	OS.objc_msgSend(this.id, OS.sel_setProgressMarks_1, progressMarks != null ? progressMarks.id : 0);
}

public void startAnimation() {
	OS.objc_msgSend(this.id, OS.sel_startAnimation);
}

public void startWhenAnimation(NSAnimation animation, float startProgress) {
	OS.objc_msgSend(this.id, OS.sel_startWhenAnimation_1reachesProgress_1, animation != null ? animation.id : 0, startProgress);
}

public void stopAnimation() {
	OS.objc_msgSend(this.id, OS.sel_stopAnimation);
}

public void stopWhenAnimation(NSAnimation animation, float stopProgress) {
	OS.objc_msgSend(this.id, OS.sel_stopWhenAnimation_1reachesProgress_1, animation != null ? animation.id : 0, stopProgress);
}

}
