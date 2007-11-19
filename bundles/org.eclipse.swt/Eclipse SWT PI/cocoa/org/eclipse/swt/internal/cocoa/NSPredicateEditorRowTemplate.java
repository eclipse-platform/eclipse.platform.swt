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

public class NSPredicateEditorRowTemplate extends NSObject {

public NSPredicateEditorRowTemplate() {
	super();
}

public NSPredicateEditorRowTemplate(int id) {
	super(id);
}

public NSArray compoundTypes() {
	int result = OS.objc_msgSend(this.id, OS.sel_compoundTypes);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray displayableSubpredicatesOfPredicate(NSPredicate predicate) {
	int result = OS.objc_msgSend(this.id, OS.sel_displayableSubpredicatesOfPredicate_1, predicate != null ? predicate.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public id initWithCompoundTypes(NSArray compoundTypes) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCompoundTypes_1, compoundTypes != null ? compoundTypes.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithLeftExpressions_rightExpressionAttributeType_modifier_operators_options_(NSArray leftExpressions, int attributeType, int modifier, NSArray operators, int options) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithLeftExpressions_1rightExpressionAttributeType_1modifier_1operators_1options_1, leftExpressions != null ? leftExpressions.id : 0, attributeType, modifier, operators != null ? operators.id : 0, options);
	return result != 0 ? new id(result) : null;
}

public id initWithLeftExpressions_rightExpressions_modifier_operators_options_(NSArray leftExpressions, NSArray rightExpressions, int modifier, NSArray operators, int options) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithLeftExpressions_1rightExpressions_1modifier_1operators_1options_1, leftExpressions != null ? leftExpressions.id : 0, rightExpressions != null ? rightExpressions.id : 0, modifier, operators != null ? operators.id : 0, options);
	return result != 0 ? new id(result) : null;
}

public NSArray leftExpressions() {
	int result = OS.objc_msgSend(this.id, OS.sel_leftExpressions);
	return result != 0 ? new NSArray(result) : null;
}

public double matchForPredicate(NSPredicate predicate) {
	return OS.objc_msgSend_fpret(this.id, OS.sel_matchForPredicate_1, predicate != null ? predicate.id : 0);
}

public int modifier() {
	return OS.objc_msgSend(this.id, OS.sel_modifier);
}

public NSArray operators() {
	int result = OS.objc_msgSend(this.id, OS.sel_operators);
	return result != 0 ? new NSArray(result) : null;
}

public int options() {
	return OS.objc_msgSend(this.id, OS.sel_options);
}

public NSPredicate predicateWithSubpredicates(NSArray subpredicates) {
	int result = OS.objc_msgSend(this.id, OS.sel_predicateWithSubpredicates_1, subpredicates != null ? subpredicates.id : 0);
	return result != 0 ? new NSPredicate(result) : null;
}

public int rightExpressionAttributeType() {
	return OS.objc_msgSend(this.id, OS.sel_rightExpressionAttributeType);
}

public NSArray rightExpressions() {
	int result = OS.objc_msgSend(this.id, OS.sel_rightExpressions);
	return result != 0 ? new NSArray(result) : null;
}

public void setPredicate(NSPredicate predicate) {
	OS.objc_msgSend(this.id, OS.sel_setPredicate_1, predicate != null ? predicate.id : 0);
}

public NSArray templateViews() {
	int result = OS.objc_msgSend(this.id, OS.sel_templateViews);
	return result != 0 ? new NSArray(result) : null;
}

//public static NSArray templatesWithAttributeKeyPaths(NSArray keyPaths, NSEntityDescription entityDescription) {
//	int result = OS.objc_msgSend(OS.class_NSPredicateEditorRowTemplate, OS.sel_templatesWithAttributeKeyPaths_1inEntityDescription_1, keyPaths != null ? keyPaths.id : 0, entityDescription != null ? entityDescription.id : 0);
//	return result != 0 ? new NSArray(result) : null;
//}

}
