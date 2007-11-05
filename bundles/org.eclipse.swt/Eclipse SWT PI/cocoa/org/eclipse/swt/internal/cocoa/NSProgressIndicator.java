package org.eclipse.swt.internal.cocoa;

public class NSProgressIndicator extends NSView {
	
public NSProgressIndicator() {
	super(0);
}

public NSProgressIndicator(int id) {
	super(id);
}

public int get_class() {
	return OS.class_NSProgressIndicator;
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

public void startAnimation(NSObject sender) {
	OS.objc_msgSend(id, OS.sel_startAnimation_1, sender != null ? sender.id : 0);
}

public void setDoubleValue(double value) {
	OS.objc_msgSend(id, OS.sel_setDoubleValue_1, value);
}

public void setIndeterminate(boolean flag) {
	OS.objc_msgSend(id, OS.sel_setIndeterminate_1, flag ? 1 : 0);
}


public void setMinValue(double value) {
	OS.objc_msgSend(id, OS.sel_setMinValue_1, value);
}

public void setMaxValue(double value) {
	OS.objc_msgSend(id, OS.sel_setMaxValue_1, value);
}

}
