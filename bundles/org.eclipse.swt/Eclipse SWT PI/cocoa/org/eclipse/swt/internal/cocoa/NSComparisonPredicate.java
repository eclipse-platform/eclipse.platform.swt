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

public class NSComparisonPredicate extends NSPredicate {

public NSComparisonPredicate() {
	super();
}

public NSComparisonPredicate(int id) {
	super(id);
}

public int comparisonPredicateModifier() {
	return OS.objc_msgSend(this.id, OS.sel_comparisonPredicateModifier);
}

public int customSelector() {
	return OS.objc_msgSend(this.id, OS.sel_customSelector);
}

public NSComparisonPredicate initWithLeftExpression_rightExpression_customSelector_(NSExpression lhs, NSExpression rhs, int selector) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithLeftExpression_1rightExpression_1customSelector_1, lhs != null ? lhs.id : 0, rhs != null ? rhs.id : 0, selector);
	return result != 0 ? this : null;
}

public NSComparisonPredicate initWithLeftExpression_rightExpression_modifier_type_options_(NSExpression lhs, NSExpression rhs, int modifier, int type, int options) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithLeftExpression_1rightExpression_1modifier_1type_1options_1, lhs != null ? lhs.id : 0, rhs != null ? rhs.id : 0, modifier, type, options);
	return result != 0 ? this : null;
}

public NSExpression leftExpression() {
	int result = OS.objc_msgSend(this.id, OS.sel_leftExpression);
	return result != 0 ? new NSExpression(result) : null;
}

public int options() {
	return OS.objc_msgSend(this.id, OS.sel_options);
}

public int predicateOperatorType() {
	return OS.objc_msgSend(this.id, OS.sel_predicateOperatorType);
}

public static NSPredicate static_predicateWithLeftExpression_rightExpression_customSelector_(NSExpression lhs, NSExpression rhs, int selector) {
	int result = OS.objc_msgSend(OS.class_NSComparisonPredicate, OS.sel_predicateWithLeftExpression_1rightExpression_1customSelector_1, lhs != null ? lhs.id : 0, rhs != null ? rhs.id : 0, selector);
	return result != 0 ? new NSPredicate(result) : null;
}

public static NSPredicate static_predicateWithLeftExpression_rightExpression_modifier_type_options_(NSExpression lhs, NSExpression rhs, int modifier, int type, int options) {
	int result = OS.objc_msgSend(OS.class_NSComparisonPredicate, OS.sel_predicateWithLeftExpression_1rightExpression_1modifier_1type_1options_1, lhs != null ? lhs.id : 0, rhs != null ? rhs.id : 0, modifier, type, options);
	return result != 0 ? new NSPredicate(result) : null;
}

public NSExpression rightExpression() {
	int result = OS.objc_msgSend(this.id, OS.sel_rightExpression);
	return result != 0 ? new NSExpression(result) : null;
}

}
