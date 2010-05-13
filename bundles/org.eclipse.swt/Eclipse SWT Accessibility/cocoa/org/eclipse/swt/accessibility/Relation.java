/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;

import org.eclipse.swt.internal.cocoa.*;

class Relation {
	Accessible accessible;
	Accessible[] targets;
	int type;

	Relation(Accessible accessible, int type) {
		this.accessible = accessible;
		this.type = type;
		this.targets = new Accessible[0];
	}

	void addTarget(Accessible target) {
		if (containsTarget(target)) return;
		Accessible[] newTargets = new Accessible[targets.length + 1];
		System.arraycopy(targets, 0, newTargets, 0, targets.length);
		newTargets[targets.length] = target;
		targets = newTargets;
	}

	boolean containsTarget(Accessible target) {
		for (int i = 0; i < targets.length; i++) {
			if (targets[i] == target) return true;
		}
		return false;
	}

	void removeTarget(Accessible target) {
		if (!containsTarget(target)) return;
		Accessible[] newTargets = new Accessible[targets.length - 1];
		int j = 0;
		for (int i = 0; i < targets.length; i++) {
			if (targets[i] != target) {
				newTargets[j++] = targets[i];
			}
		}
		targets = newTargets;
	}

	id getTitleUIElement() {
		id result = null;
		for (int i = 0; i < targets.length; i++) {
			Accessible target = targets[i];			
			result = target.accessibleHandle(target);
		}
		return result;
	}

	id getServesAsTitleForUIElements() {
		NSMutableArray result = NSMutableArray.arrayWithCapacity(targets.length);
		for (int i = 0; i < targets.length; i++) {
			Accessible target = targets[i];			
			id accessibleElement = target.accessibleHandle(target);
			result.addObject(accessibleElement);
		}
		return result;
	}

	id getLinkedUIElements() {
		NSMutableArray result = NSMutableArray.arrayWithCapacity(targets.length);
		for (int i = 0; i < targets.length; i++) {
			Accessible target = targets[i];			
			id accessibleElement = target.accessibleHandle(target);
			result.addObject(accessibleElement);
		}
		return result;
	}
}
