/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSSegmentedCell extends NSActionCell {

public NSSegmentedCell() {
	super();
}

public NSSegmentedCell(long id) {
	super(id);
}

public NSSegmentedCell(id id) {
	super(id);
}

public void setEnabled(boolean enabled, long segment) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_forSegment_, enabled, segment);
}

public void setImage(NSImage image, long segment) {
	OS.objc_msgSend(this.id, OS.sel_setImage_forSegment_, image != null ? image.id : 0, segment);
}

public void setLabel(NSString label, long segment) {
	OS.objc_msgSend(this.id, OS.sel_setLabel_forSegment_, label != null ? label.id : 0, segment);
}

public void setMenu(NSMenu menu, long segment) {
	OS.objc_msgSend(this.id, OS.sel_setMenu_forSegment_, menu != null ? menu.id : 0, segment);
}

public void setSegmentCount(long segmentCount) {
	OS.objc_msgSend(this.id, OS.sel_setSegmentCount_, segmentCount);
}

public void setSegmentStyle(long segmentStyle) {
	OS.objc_msgSend(this.id, OS.sel_setSegmentStyle_, segmentStyle);
}

public void setSelected(boolean selected, long segment) {
	OS.objc_msgSend(this.id, OS.sel_setSelected_forSegment_, selected, segment);
}

public void setSelectedSegment(long selectedSegment) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedSegment_, selectedSegment);
}

public void setTag(long tag, long segment) {
	OS.objc_msgSend(this.id, OS.sel_setTag_forSegment_, tag, segment);
}

public void setToolTip(NSString toolTip, long segment) {
	OS.objc_msgSend(this.id, OS.sel_setToolTip_forSegment_, toolTip != null ? toolTip.id : 0, segment);
}

public void setTrackingMode(long trackingMode) {
	OS.objc_msgSend(this.id, OS.sel_setTrackingMode_, trackingMode);
}

public void setWidth(double width, long segment) {
	OS.objc_msgSend(this.id, OS.sel_setWidth_forSegment_, width, segment);
}

}
