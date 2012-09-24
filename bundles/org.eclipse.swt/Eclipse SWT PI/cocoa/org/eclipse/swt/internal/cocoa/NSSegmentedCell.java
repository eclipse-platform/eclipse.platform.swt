/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSSegmentedCell extends NSActionCell {

public NSSegmentedCell() {
	super();
}

public NSSegmentedCell(long /*int*/ id) {
	super(id);
}

public NSSegmentedCell(id id) {
	super(id);
}

public void setEnabled(boolean enabled, long /*int*/ segment) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_forSegment_, enabled, segment);
}

public void setImage(NSImage image, long /*int*/ segment) {
	OS.objc_msgSend(this.id, OS.sel_setImage_forSegment_, image != null ? image.id : 0, segment);
}

public void setLabel(NSString label, long /*int*/ segment) {
	OS.objc_msgSend(this.id, OS.sel_setLabel_forSegment_, label != null ? label.id : 0, segment);
}

public void setMenu(NSMenu menu, long /*int*/ segment) {
	OS.objc_msgSend(this.id, OS.sel_setMenu_forSegment_, menu != null ? menu.id : 0, segment);
}

public void setSegmentCount(long /*int*/ count) {
	OS.objc_msgSend(this.id, OS.sel_setSegmentCount_, count);
}

public void setSegmentStyle(long /*int*/ segmentStyle) {
	OS.objc_msgSend(this.id, OS.sel_setSegmentStyle_, segmentStyle);
}

public void setSelected(boolean selected, long /*int*/ segment) {
	OS.objc_msgSend(this.id, OS.sel_setSelected_forSegment_, selected, segment);
}

public void setSelectedSegment(long /*int*/ selectedSegment) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedSegment_, selectedSegment);
}

public void setTag(long /*int*/ tag, long /*int*/ segment) {
	OS.objc_msgSend(this.id, OS.sel_setTag_forSegment_, tag, segment);
}

public void setToolTip(NSString toolTip, long /*int*/ segment) {
	OS.objc_msgSend(this.id, OS.sel_setToolTip_forSegment_, toolTip != null ? toolTip.id : 0, segment);
}

public void setTrackingMode(long /*int*/ trackingMode) {
	OS.objc_msgSend(this.id, OS.sel_setTrackingMode_, trackingMode);
}

public void setWidth(double /*float*/ width, long /*int*/ segment) {
	OS.objc_msgSend(this.id, OS.sel_setWidth_forSegment_, width, segment);
}

}
