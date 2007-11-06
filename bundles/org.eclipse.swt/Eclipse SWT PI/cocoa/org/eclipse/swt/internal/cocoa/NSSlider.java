package org.eclipse.swt.internal.cocoa;

public class NSSlider extends NSControl {

public NSSlider() {
	super(0);
}
	
public NSSlider(int id) {
	super(id);
}

public int get_class() {
	return OS.class_NSSlider;
}

public double doubleValue() {
	return OS.objc_msgSend_fpret(id, OS.sel_doubleValue);
}

public double maxValue() {
	return OS.objc_msgSend_fpret(id, OS.sel_maxValue);
}

public double minValue() {
	return OS.objc_msgSend_fpret(id, OS.sel_minValue);
}

public void setDoubleValue(double value) {
	OS.objc_msgSend(id, OS.sel_setDoubleValue_1, value);
}

public void setMinValue(double value) {
	OS.objc_msgSend(id, OS.sel_setMinValue_1, value);
}

public void setMaxValue(double value) {
	OS.objc_msgSend(id, OS.sel_setMaxValue_1, value);
}
}
