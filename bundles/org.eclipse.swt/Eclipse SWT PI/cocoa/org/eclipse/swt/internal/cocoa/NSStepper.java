package org.eclipse.swt.internal.cocoa;

public class NSStepper extends NSControl {

public NSStepper() {
	super();
}

public NSStepper(int id) {
	super(id);
}

public boolean autorepeat() {
	return OS.objc_msgSend(this.id, OS.sel_autorepeat) != 0;
}

public double increment() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_increment);
}

public double maxValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_maxValue);
}

public double minValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_minValue);
}

public void setAutorepeat(boolean autorepeat) {
	OS.objc_msgSend(this.id, OS.sel_setAutorepeat_1, autorepeat);
}

public void setIncrement(double increment) {
	OS.objc_msgSend(this.id, OS.sel_setIncrement_1, increment);
}

public void setMaxValue(double maxValue) {
	OS.objc_msgSend(this.id, OS.sel_setMaxValue_1, maxValue);
}

public void setMinValue(double minValue) {
	OS.objc_msgSend(this.id, OS.sel_setMinValue_1, minValue);
}

public void setValueWraps(boolean valueWraps) {
	OS.objc_msgSend(this.id, OS.sel_setValueWraps_1, valueWraps);
}

public boolean valueWraps() {
	return OS.objc_msgSend(this.id, OS.sel_valueWraps) != 0;
}

}
