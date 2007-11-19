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

public class NSSegmentedCell extends NSActionCell {

public NSSegmentedCell() {
	super();
}

public NSSegmentedCell(int id) {
	super(id);
}

public void drawSegment(int segment, NSRect frame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawSegment_1inFrame_1withView_1, segment, frame, controlView != null ? controlView.id : 0);
}

public NSImage imageForSegment(int segment) {
	int result = OS.objc_msgSend(this.id, OS.sel_imageForSegment_1, segment);
	return result != 0 ? new NSImage(result) : null;
}

public int imageScalingForSegment(int segment) {
	return OS.objc_msgSend(this.id, OS.sel_imageScalingForSegment_1, segment);
}

public int interiorBackgroundStyleForSegment(int segment) {
	return OS.objc_msgSend(this.id, OS.sel_interiorBackgroundStyleForSegment_1, segment);
}

public boolean isEnabledForSegment(int segment) {
	return OS.objc_msgSend(this.id, OS.sel_isEnabledForSegment_1, segment) != 0;
}

public boolean isSelectedForSegment(int segment) {
	return OS.objc_msgSend(this.id, OS.sel_isSelectedForSegment_1, segment) != 0;
}

public NSString labelForSegment(int segment) {
	int result = OS.objc_msgSend(this.id, OS.sel_labelForSegment_1, segment);
	return result != 0 ? new NSString(result) : null;
}

public void makeNextSegmentKey() {
	OS.objc_msgSend(this.id, OS.sel_makeNextSegmentKey);
}

public void makePreviousSegmentKey() {
	OS.objc_msgSend(this.id, OS.sel_makePreviousSegmentKey);
}

public NSMenu menuForSegment(int segment) {
	int result = OS.objc_msgSend(this.id, OS.sel_menuForSegment_1, segment);
	return result != 0 ? new NSMenu(result) : null;
}

public int segmentCount() {
	return OS.objc_msgSend(this.id, OS.sel_segmentCount);
}

public int segmentStyle() {
	return OS.objc_msgSend(this.id, OS.sel_segmentStyle);
}

public boolean selectSegmentWithTag(int tag) {
	return OS.objc_msgSend(this.id, OS.sel_selectSegmentWithTag_1, tag) != 0;
}

public int selectedSegment() {
	return OS.objc_msgSend(this.id, OS.sel_selectedSegment);
}

public void setEnabled(boolean enabled, int segment) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_1forSegment_1, enabled, segment);
}

public void setImage(NSImage image, int segment) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1forSegment_1, image != null ? image.id : 0, segment);
}

public void setImageScaling(int scaling, int segment) {
	OS.objc_msgSend(this.id, OS.sel_setImageScaling_1forSegment_1, scaling, segment);
}

public void setLabel(NSString label, int segment) {
	OS.objc_msgSend(this.id, OS.sel_setLabel_1forSegment_1, label != null ? label.id : 0, segment);
}

public void setMenu(NSMenu menu, int segment) {
	OS.objc_msgSend(this.id, OS.sel_setMenu_1forSegment_1, menu != null ? menu.id : 0, segment);
}

public void setSegmentCount(int count) {
	OS.objc_msgSend(this.id, OS.sel_setSegmentCount_1, count);
}

public void setSegmentStyle(int segmentStyle) {
	OS.objc_msgSend(this.id, OS.sel_setSegmentStyle_1, segmentStyle);
}

public void setSelected(boolean selected, int segment) {
	OS.objc_msgSend(this.id, OS.sel_setSelected_1forSegment_1, selected, segment);
}

public void setSelectedSegment(int selectedSegment) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedSegment_1, selectedSegment);
}

public void setTag(int tag, int segment) {
	OS.objc_msgSend(this.id, OS.sel_setTag_1forSegment_1, tag, segment);
}

public void setToolTip(NSString toolTip, int segment) {
	OS.objc_msgSend(this.id, OS.sel_setToolTip_1forSegment_1, toolTip != null ? toolTip.id : 0, segment);
}

public void setTrackingMode(int trackingMode) {
	OS.objc_msgSend(this.id, OS.sel_setTrackingMode_1, trackingMode);
}

public void setWidth(float width, int segment) {
	OS.objc_msgSend(this.id, OS.sel_setWidth_1forSegment_1, width, segment);
}

public int tagForSegment(int segment) {
	return OS.objc_msgSend(this.id, OS.sel_tagForSegment_1, segment);
}

public NSString toolTipForSegment(int segment) {
	int result = OS.objc_msgSend(this.id, OS.sel_toolTipForSegment_1, segment);
	return result != 0 ? new NSString(result) : null;
}

public int trackingMode() {
	return OS.objc_msgSend(this.id, OS.sel_trackingMode);
}

public float widthForSegment(int segment) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_widthForSegment_1, segment);
}

}
