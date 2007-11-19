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

public class NSDecimalNumberHandler extends NSObject {

public NSDecimalNumberHandler() {
	super();
}

public NSDecimalNumberHandler(int id) {
	super(id);
}

public static id decimalNumberHandlerWithRoundingMode(int roundingMode, short scale, boolean exact, boolean overflow, boolean underflow, boolean divideByZero) {
	int result = OS.objc_msgSend(OS.class_NSDecimalNumberHandler, OS.sel_decimalNumberHandlerWithRoundingMode_1scale_1raiseOnExactness_1raiseOnOverflow_1raiseOnUnderflow_1raiseOnDivideByZero_1, roundingMode, scale, exact, overflow, underflow, divideByZero);
	return result != 0 ? new id(result) : null;
}

public static id defaultDecimalNumberHandler() {
	int result = OS.objc_msgSend(OS.class_NSDecimalNumberHandler, OS.sel_defaultDecimalNumberHandler);
	return result != 0 ? new id(result) : null;
}

public id initWithRoundingMode(int roundingMode, short scale, boolean exact, boolean overflow, boolean underflow, boolean divideByZero) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithRoundingMode_1scale_1raiseOnExactness_1raiseOnOverflow_1raiseOnUnderflow_1raiseOnDivideByZero_1, roundingMode, scale, exact, overflow, underflow, divideByZero);
	return result != 0 ? new id(result) : null;
}

}
