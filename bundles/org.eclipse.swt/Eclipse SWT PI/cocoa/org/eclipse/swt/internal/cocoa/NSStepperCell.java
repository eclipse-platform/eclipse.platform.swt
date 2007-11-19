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

public class NSStepperCell extends NSActionCell {

public NSStepperCell() {
	super();
}

public NSStepperCell(int id) {
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
