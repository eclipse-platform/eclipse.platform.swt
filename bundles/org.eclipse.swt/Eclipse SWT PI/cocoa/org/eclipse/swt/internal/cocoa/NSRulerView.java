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

public class NSRulerView extends NSView {

public NSRulerView() {
	super();
}

public NSRulerView(int id) {
	super(id);
}

public NSView accessoryView() {
	int result = OS.objc_msgSend(this.id, OS.sel_accessoryView);
	return result != 0 ? new NSView(result) : null;
}

public void addMarker(NSRulerMarker marker) {
	OS.objc_msgSend(this.id, OS.sel_addMarker_1, marker != null ? marker.id : 0);
}

public float baselineLocation() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_baselineLocation);
}

public NSView clientView() {
	int result = OS.objc_msgSend(this.id, OS.sel_clientView);
	return result != 0 ? new NSView(result) : null;
}

public void drawHashMarksAndLabelsInRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_drawHashMarksAndLabelsInRect_1, rect);
}

public void drawMarkersInRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_drawMarkersInRect_1, rect);
}

public NSRulerView initWithScrollView(NSScrollView scrollView, int orientation) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithScrollView_1orientation_1, scrollView != null ? scrollView.id : 0, orientation);
	return result != 0 ? this : null;
}

public void invalidateHashMarks() {
	OS.objc_msgSend(this.id, OS.sel_invalidateHashMarks);
}

public boolean isFlipped() {
	return OS.objc_msgSend(this.id, OS.sel_isFlipped) != 0;
}

public NSArray markers() {
	int result = OS.objc_msgSend(this.id, OS.sel_markers);
	return result != 0 ? new NSArray(result) : null;
}

public NSString measurementUnits() {
	int result = OS.objc_msgSend(this.id, OS.sel_measurementUnits);
	return result != 0 ? new NSString(result) : null;
}

public void moveRulerlineFromLocation(float oldLocation, float newLocation) {
	OS.objc_msgSend(this.id, OS.sel_moveRulerlineFromLocation_1toLocation_1, oldLocation, newLocation);
}

public int orientation() {
	return OS.objc_msgSend(this.id, OS.sel_orientation);
}

public float originOffset() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_originOffset);
}

public static void registerUnitWithName(NSString unitName, NSString abbreviation, float conversionFactor, NSArray stepUpCycle, NSArray stepDownCycle) {
	OS.objc_msgSend(OS.class_NSRulerView, OS.sel_registerUnitWithName_1abbreviation_1unitToPointsConversionFactor_1stepUpCycle_1stepDownCycle_1, unitName != null ? unitName.id : 0, abbreviation != null ? abbreviation.id : 0, conversionFactor, stepUpCycle != null ? stepUpCycle.id : 0, stepDownCycle != null ? stepDownCycle.id : 0);
}

public void removeMarker(NSRulerMarker marker) {
	OS.objc_msgSend(this.id, OS.sel_removeMarker_1, marker != null ? marker.id : 0);
}

public float requiredThickness() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_requiredThickness);
}

public float reservedThicknessForAccessoryView() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_reservedThicknessForAccessoryView);
}

public float reservedThicknessForMarkers() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_reservedThicknessForMarkers);
}

public float ruleThickness() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_ruleThickness);
}

public NSScrollView scrollView() {
	int result = OS.objc_msgSend(this.id, OS.sel_scrollView);
	return result != 0 ? new NSScrollView(result) : null;
}

public void setAccessoryView(NSView accessory) {
	OS.objc_msgSend(this.id, OS.sel_setAccessoryView_1, accessory != null ? accessory.id : 0);
}

public void setClientView(NSView client) {
	OS.objc_msgSend(this.id, OS.sel_setClientView_1, client != null ? client.id : 0);
}

public void setMarkers(NSArray markers) {
	OS.objc_msgSend(this.id, OS.sel_setMarkers_1, markers != null ? markers.id : 0);
}

public void setMeasurementUnits(NSString unitName) {
	OS.objc_msgSend(this.id, OS.sel_setMeasurementUnits_1, unitName != null ? unitName.id : 0);
}

public void setOrientation(int orientation) {
	OS.objc_msgSend(this.id, OS.sel_setOrientation_1, orientation);
}

public void setOriginOffset(float offset) {
	OS.objc_msgSend(this.id, OS.sel_setOriginOffset_1, offset);
}

public void setReservedThicknessForAccessoryView(float thickness) {
	OS.objc_msgSend(this.id, OS.sel_setReservedThicknessForAccessoryView_1, thickness);
}

public void setReservedThicknessForMarkers(float thickness) {
	OS.objc_msgSend(this.id, OS.sel_setReservedThicknessForMarkers_1, thickness);
}

public void setRuleThickness(float thickness) {
	OS.objc_msgSend(this.id, OS.sel_setRuleThickness_1, thickness);
}

public void setScrollView(NSScrollView scrollView) {
	OS.objc_msgSend(this.id, OS.sel_setScrollView_1, scrollView != null ? scrollView.id : 0);
}

public boolean trackMarker(NSRulerMarker marker, NSEvent event) {
	return OS.objc_msgSend(this.id, OS.sel_trackMarker_1withMouseEvent_1, marker != null ? marker.id : 0, event != null ? event.id : 0) != 0;
}

}
