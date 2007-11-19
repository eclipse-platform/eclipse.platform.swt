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

public class NSCompoundPredicate extends NSPredicate {

public NSCompoundPredicate() {
	super();
}

public NSCompoundPredicate(int id) {
	super(id);
}

public static NSPredicate andPredicateWithSubpredicates(NSArray subpredicates) {
	int result = OS.objc_msgSend(OS.class_NSCompoundPredicate, OS.sel_andPredicateWithSubpredicates_1, subpredicates != null ? subpredicates.id : 0);
	return result != 0 ? new NSPredicate(result) : null;
}

public int compoundPredicateType() {
	return OS.objc_msgSend(this.id, OS.sel_compoundPredicateType);
}

public id initWithType(int type, NSArray subpredicates) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithType_1subpredicates_1, type, subpredicates != null ? subpredicates.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSPredicate notPredicateWithSubpredicate(NSPredicate predicate) {
	int result = OS.objc_msgSend(OS.class_NSCompoundPredicate, OS.sel_notPredicateWithSubpredicate_1, predicate != null ? predicate.id : 0);
	return result != 0 ? new NSPredicate(result) : null;
}

public static NSPredicate orPredicateWithSubpredicates(NSArray subpredicates) {
	int result = OS.objc_msgSend(OS.class_NSCompoundPredicate, OS.sel_orPredicateWithSubpredicates_1, subpredicates != null ? subpredicates.id : 0);
	return result != 0 ? new NSPredicate(result) : null;
}

public NSArray subpredicates() {
	int result = OS.objc_msgSend(this.id, OS.sel_subpredicates);
	return result != 0 ? new NSArray(result) : null;
}

}
