/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.internal.ole.win32.*;

class Relation {
	Accessible accessible;
	COMObject objIAccessibleRelation;
	int refCount;
	int type;
	Accessible[] targets;
	static final String[] relationTypeString = {
		"controlledBy",		//$NON-NLS-1$
		"controllerFor",	//$NON-NLS-1$
		"describedBy",		//$NON-NLS-1$
		"descriptionFor",	//$NON-NLS-1$
		"embeddedBy",		//$NON-NLS-1$
		"embeds",			//$NON-NLS-1$
		"flowsFrom",		//$NON-NLS-1$
		"flowsTo",			//$NON-NLS-1$
		"labelFor",			//$NON-NLS-1$
		"labelledBy",		//$NON-NLS-1$
		"memberOf",			//$NON-NLS-1$
		"nodeChildOf",		//$NON-NLS-1$
		"parentWindowOf",	//$NON-NLS-1$
		"popupFor",			//$NON-NLS-1$
		"subwindowOf",		//$NON-NLS-1$
	};
	static final String[] localizedRelationTypeString = {
		SWT.getMessage("SWT_Controlled_By"),	//$NON-NLS-1$
		SWT.getMessage("SWT_Controller_For"),	//$NON-NLS-1$
		SWT.getMessage("SWT_Described_By"),		//$NON-NLS-1$
		SWT.getMessage("SWT_Description_For"),	//$NON-NLS-1$
		SWT.getMessage("SWT_Embedded_By"),		//$NON-NLS-1$
		SWT.getMessage("SWT_Embeds"),			//$NON-NLS-1$
		SWT.getMessage("SWT_Flows_From"),		//$NON-NLS-1$
		SWT.getMessage("SWT_Flows_To"),			//$NON-NLS-1$
		SWT.getMessage("SWT_Label_For"),		//$NON-NLS-1$
		SWT.getMessage("SWT_Labelled_By"),		//$NON-NLS-1$
		SWT.getMessage("SWT_Member_Of"),		//$NON-NLS-1$
		SWT.getMessage("SWT_Node_Child_Of"),	//$NON-NLS-1$
		SWT.getMessage("SWT_Parent_Window_Of"),	//$NON-NLS-1$
		SWT.getMessage("SWT_Popup_For"),		//$NON-NLS-1$
		SWT.getMessage("SWT_Subwindow_Of"),		//$NON-NLS-1$
	};

	Relation(Accessible accessible, int type) {
		this.accessible = accessible;
		this.type = type;
		this.targets = new Accessible[0];
		AddRef();
	}
	
	long /*int*/ getAddress() {
		/* The address of a Relation is the address of its IAccessibleRelation COMObject. */
		if (objIAccessibleRelation == null) createIAccessibleRelation();
		return objIAccessibleRelation.getAddress();
	}
	
	void createIAccessibleRelation() {
		objIAccessibleRelation = new COMObject(new int[] {2,0,0,1,1,1,2,3}) {
			public long /*int*/ method0(long /*int*/[] args) {return QueryInterface(args[0], args[1]);}
			public long /*int*/ method1(long /*int*/[] args) {return AddRef();}
			public long /*int*/ method2(long /*int*/[] args) {return Release();}
			public long /*int*/ method3(long /*int*/[] args) {return get_relationType(args[0]);}
			public long /*int*/ method4(long /*int*/[] args) {return get_localizedRelationType(args[0]);}
			public long /*int*/ method5(long /*int*/[] args) {return get_nTargets(args[0]);}
			public long /*int*/ method6(long /*int*/[] args) {return get_target((int)/*64*/args[0], args[1]);}
			public long /*int*/ method7(long /*int*/[] args) {return get_targets((int)/*64*/args[0], args[1], args[2]);}
		};
	}

	/* QueryInterface([in] iid, [out] ppvObject)
	 * Ownership of ppvObject transfers from callee to caller so reference count on ppvObject 
	 * must be incremented before returning.  Caller is responsible for releasing ppvObject.
	 */
	int QueryInterface(long /*int*/ iid, long /*int*/ ppvObject) {
		GUID guid = new GUID();
		COM.MoveMemory(guid, iid, GUID.sizeof);

		if (COM.IsEqualGUID(guid, COM.IIDIUnknown) || COM.IsEqualGUID(guid, COM.IIDIAccessibleRelation)) {
			COM.MoveMemory(ppvObject, new long /*int*/[] { getAddress() }, OS.PTR_SIZEOF);
			AddRef();
			return COM.S_OK;
		}

		return COM.E_NOINTERFACE;
	}

	int AddRef() {
		refCount++;
		return refCount;
	}

	int Release() {
		refCount--;

		if (refCount == 0) {
			if (objIAccessibleRelation != null)
				objIAccessibleRelation.dispose();
			objIAccessibleRelation = null;
		}
		return refCount;
	}

	/* IAccessibleRelation::get_relationType([out] pbstrRelationType) */
	int get_relationType(long /*int*/ pbstrRelationType) {
		setString(pbstrRelationType, relationTypeString[type]);
		return COM.S_OK;
	}

	/* IAccessibleRelation::get_localizedRelationType([out] pbstrLocalizedRelationType) */
	int get_localizedRelationType(long /*int*/ pbstrLocalizedRelationType) {
		setString(pbstrLocalizedRelationType, localizedRelationTypeString[type]);
		return COM.S_OK;
	}

	/* IAccessibleRelation::get_nTargets([out] pNTargets) */
	int get_nTargets(long /*int*/ pNTargets) {
		COM.MoveMemory(pNTargets, new int [] { targets.length }, 4);
		return COM.S_OK;
	}

	/* IAccessibleRelation::get_target([in] targetIndex, [out] ppTarget) */
	int get_target(int targetIndex, long /*int*/ ppTarget) {
		if (targetIndex < 0 || targetIndex >= targets.length) return COM.E_INVALIDARG;
		Accessible target = targets[targetIndex];
		target.AddRef();
		COM.MoveMemory(ppTarget, new long /*int*/[] { target.getAddress() }, OS.PTR_SIZEOF);
		return COM.S_OK;
	}

	/* IAccessibleRelation::get_targets([in] maxTargets, [out] ppTargets, [out] pNTargets) */
	int get_targets(int maxTargets, long /*int*/ ppTargets, long /*int*/ pNTargets) {
		int count = Math.min(targets.length, maxTargets);
		for (int i = 0; i < count; i++) {
			Accessible target = targets[i];
			target.AddRef();
			COM.MoveMemory(ppTargets + i * OS.PTR_SIZEOF, new long /*int*/[] { target.getAddress() }, OS.PTR_SIZEOF);
		}
		COM.MoveMemory(pNTargets, new int [] { count }, 4);
		return COM.S_OK;
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

	boolean hasTargets() {
		return targets.length > 0;
	}

	// setString copied from Accessible class
	void setString(long /*int*/ psz, String string) {
		char[] data = (string + "\0").toCharArray();
		long /*int*/ ptr = COM.SysAllocString(data);
		COM.MoveMemory(psz, new long /*int*/ [] { ptr }, OS.PTR_SIZEOF);
	}
}
