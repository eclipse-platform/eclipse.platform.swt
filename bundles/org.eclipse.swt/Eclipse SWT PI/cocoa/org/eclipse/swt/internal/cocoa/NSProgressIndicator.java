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

public class NSProgressIndicator extends NSView {

public NSProgressIndicator() {
	super();
}

public NSProgressIndicator(int id) {
	super(id);
}

public void animate(id sender) {
	OS.objc_msgSend(this.id, OS.sel_animate_1, sender != null ? sender.id : 0);
}

public double animationDelay() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_animationDelay);
}

public int controlSize() {
	return OS.objc_msgSend(this.id, OS.sel_controlSize);
}

public int controlTint() {
	return OS.objc_msgSend(this.id, OS.sel_controlTint);
}

public double doubleValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_doubleValue);
}

public void incrementBy(double delta) {
	OS.objc_msgSend(this.id, OS.sel_incrementBy_1, delta);
}

public boolean isBezeled() {
	return OS.objc_msgSend(this.id, OS.sel_isBezeled) != 0;
}

public boolean isDisplayedWhenStopped() {
	return OS.objc_msgSend(this.id, OS.sel_isDisplayedWhenStopped) != 0;
}

public boolean isIndeterminate() {
	return OS.objc_msgSend(this.id, OS.sel_isIndeterminate) != 0;
}

public double maxValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_maxValue);
}

public double minValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_minValue);
}

public void setAnimationDelay(double delay) {
	OS.objc_msgSend(this.id, OS.sel_setAnimationDelay_1, delay);
}

public void setBezeled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBezeled_1, flag);
}

public void setControlSize(int size) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_1, size);
}

public void setControlTint(int tint) {
	OS.objc_msgSend(this.id, OS.sel_setControlTint_1, tint);
}

public void setDisplayedWhenStopped(boolean isDisplayed) {
	OS.objc_msgSend(this.id, OS.sel_setDisplayedWhenStopped_1, isDisplayed);
}

public void setDoubleValue(double doubleValue) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleValue_1, doubleValue);
}

public void setIndeterminate(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setIndeterminate_1, flag);
}

public void setMaxValue(double newMaximum) {
	OS.objc_msgSend(this.id, OS.sel_setMaxValue_1, newMaximum);
}

public void setMinValue(double newMinimum) {
	OS.objc_msgSend(this.id, OS.sel_setMinValue_1, newMinimum);
}

public void setStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setStyle_1, style);
}

public void setUsesThreadedAnimation(boolean threadedAnimation) {
	OS.objc_msgSend(this.id, OS.sel_setUsesThreadedAnimation_1, threadedAnimation);
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public void startAnimation(id sender) {
	OS.objc_msgSend(this.id, OS.sel_startAnimation_1, sender != null ? sender.id : 0);
}

public void stopAnimation(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stopAnimation_1, sender != null ? sender.id : 0);
}

public int style() {
	return OS.objc_msgSend(this.id, OS.sel_style);
}

public boolean usesThreadedAnimation() {
	return OS.objc_msgSend(this.id, OS.sel_usesThreadedAnimation) != 0;
}

}
