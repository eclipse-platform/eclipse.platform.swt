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

public class NSExpression extends NSObject {

public NSExpression() {
	super();
}

public NSExpression(int id) {
	super(id);
}

public NSArray arguments() {
	int result = OS.objc_msgSend(this.id, OS.sel_arguments);
	return result != 0 ? new NSArray(result) : null;
}

public id collection() {
	int result = OS.objc_msgSend(this.id, OS.sel_collection);
	return result != 0 ? new id(result) : null;
}

public id constantValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_constantValue);
	return result != 0 ? new id(result) : null;
}

public static NSExpression expressionForAggregate(NSArray subexpressions) {
	int result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForAggregate_1, subexpressions != null ? subexpressions.id : 0);
	return result != 0 ? new NSExpression(result) : null;
}

public static NSExpression expressionForConstantValue(id obj) {
	int result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForConstantValue_1, obj != null ? obj.id : 0);
	return result != 0 ? new NSExpression(result) : null;
}

public static NSExpression expressionForEvaluatedObject() {
	int result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForEvaluatedObject);
	return result != 0 ? new NSExpression(result) : null;
}

public static NSExpression static_expressionForFunction_arguments_(NSString name, NSArray parameters) {
	int result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForFunction_1arguments_1, name != null ? name.id : 0, parameters != null ? parameters.id : 0);
	return result != 0 ? new NSExpression(result) : null;
}

public static NSExpression static_expressionForFunction_selectorName_arguments_(NSExpression target, NSString name, NSArray parameters) {
	int result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForFunction_1selectorName_1arguments_1, target != null ? target.id : 0, name != null ? name.id : 0, parameters != null ? parameters.id : 0);
	return result != 0 ? new NSExpression(result) : null;
}

public static NSExpression expressionForIntersectSet(NSExpression left, NSExpression right) {
	int result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForIntersectSet_1with_1, left != null ? left.id : 0, right != null ? right.id : 0);
	return result != 0 ? new NSExpression(result) : null;
}

public static NSExpression expressionForKeyPath(NSString keyPath) {
	int result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForKeyPath_1, keyPath != null ? keyPath.id : 0);
	return result != 0 ? new NSExpression(result) : null;
}

public static NSExpression expressionForMinusSet(NSExpression left, NSExpression right) {
	int result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForMinusSet_1with_1, left != null ? left.id : 0, right != null ? right.id : 0);
	return result != 0 ? new NSExpression(result) : null;
}

public static NSExpression expressionForSubquery(NSExpression expression, NSString variable, id predicate) {
	int result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForSubquery_1usingIteratorVariable_1predicate_1, expression != null ? expression.id : 0, variable != null ? variable.id : 0, predicate != null ? predicate.id : 0);
	return result != 0 ? new NSExpression(result) : null;
}

public static NSExpression expressionForUnionSet(NSExpression left, NSExpression right) {
	int result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForUnionSet_1with_1, left != null ? left.id : 0, right != null ? right.id : 0);
	return result != 0 ? new NSExpression(result) : null;
}

public static NSExpression expressionForVariable(NSString string) {
	int result = OS.objc_msgSend(OS.class_NSExpression, OS.sel_expressionForVariable_1, string != null ? string.id : 0);
	return result != 0 ? new NSExpression(result) : null;
}

public int expressionType() {
	return OS.objc_msgSend(this.id, OS.sel_expressionType);
}

public id expressionValueWithObject(id object, NSMutableDictionary context) {
	int result = OS.objc_msgSend(this.id, OS.sel_expressionValueWithObject_1context_1, object != null ? object.id : 0, context != null ? context.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString function() {
	int result = OS.objc_msgSend(this.id, OS.sel_function);
	return result != 0 ? new NSString(result) : null;
}

public id initWithExpressionType(int type) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithExpressionType_1, type);
	return result != 0 ? new id(result) : null;
}

public NSString keyPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_keyPath);
	return result != 0 ? new NSString(result) : null;
}

public NSExpression leftExpression() {
	int result = OS.objc_msgSend(this.id, OS.sel_leftExpression);
	return result == this.id ? this : (result != 0 ? new NSExpression(result) : null);
}

public NSExpression operand() {
	int result = OS.objc_msgSend(this.id, OS.sel_operand);
	return result == this.id ? this : (result != 0 ? new NSExpression(result) : null);
}

public NSPredicate predicate() {
	int result = OS.objc_msgSend(this.id, OS.sel_predicate);
	return result != 0 ? new NSPredicate(result) : null;
}

public NSExpression rightExpression() {
	int result = OS.objc_msgSend(this.id, OS.sel_rightExpression);
	return result == this.id ? this : (result != 0 ? new NSExpression(result) : null);
}

public NSString variable() {
	int result = OS.objc_msgSend(this.id, OS.sel_variable);
	return result != 0 ? new NSString(result) : null;
}

}
