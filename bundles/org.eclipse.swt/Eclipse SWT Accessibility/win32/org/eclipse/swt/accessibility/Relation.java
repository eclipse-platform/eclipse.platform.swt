/*******************************************************************************
 * Copyright (c) 2009, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

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

	long getAddress() {
		/* The address of a Relation is the address of its IAccessibleRelation COMObject. */
		if (objIAccessibleRelation == null) createIAccessibleRelation();
		return objIAccessibleRelation.getAddress();
	}

	void createIAccessibleRelation() {
		objIAccessibleRelation = new COMObject(new int[] {2,0,0,1,1,1,2,3}) {
			@Override
			public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
			@Override
			public long method1(long[] args) {return AddRef();}
			@Override
			public long method2(long[] args) {return Release();}
			@Override
			public long method3(long[] args) {return get_relationType(args[0]);}
			@Override
			public long method4(long[] args) {return get_localizedRelationType(args[0]);}
			@Override
			public long method5(long[] args) {return get_nTargets(args[0]);}
			@Override
			public long method6(long[] args) {return get_target((int)args[0], args[1]);}
			@Override
			public long method7(long[] args) {return get_targets((int)args[0], args[1], args[2]);}
		};
	}

	/* QueryInterface([in] iid, [out] ppvObject)
	 * Ownership of ppvObject transfers from callee to caller so reference count on ppvObject
	 * must be incremented before returning.  Caller is responsible for releasing ppvObject.
	 */
	int QueryInterface(long iid, long ppvObject) {
		GUID guid = new GUID();
		COM.MoveMemory(guid, iid, GUID.sizeof);

		if (COM.IsEqualGUID(guid, COM.IIDIUnknown) || COM.IsEqualGUID(guid, COM.IIDIAccessibleRelation)) {
			OS.MoveMemory(ppvObject, new long[] { getAddress() }, C.PTR_SIZEOF);
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
	int get_relationType(long pbstrRelationType) {
		setString(pbstrRelationType, relationTypeString[type]);
		return COM.S_OK;
	}

	/* IAccessibleRelation::get_localizedRelationType([out] pbstrLocalizedRelationType) */
	int get_localizedRelationType(long pbstrLocalizedRelationType) {
		setString(pbstrLocalizedRelationType, localizedRelationTypeString[type]);
		return COM.S_OK;
	}

	/* IAccessibleRelation::get_nTargets([out] pNTargets) */
	int get_nTargets(long pNTargets) {
		OS.MoveMemory(pNTargets, new int [] { targets.length }, 4);
		return COM.S_OK;
	}

	/* IAccessibleRelation::get_target([in] targetIndex, [out] ppTarget) */
	int get_target(int targetIndex, long ppTarget) {
		if (targetIndex < 0 || targetIndex >= targets.length) return COM.E_INVALIDARG;
		Accessible target = targets[targetIndex];
		target.AddRef();
		OS.MoveMemory(ppTarget, new long[] { target.getAddress() }, C.PTR_SIZEOF);
		return COM.S_OK;
	}

	/* IAccessibleRelation::get_targets([in] maxTargets, [out] ppTargets, [out] pNTargets) */
	int get_targets(int maxTargets, long ppTargets, long pNTargets) {
		int count = Math.min(targets.length, maxTargets);
		for (int i = 0; i < count; i++) {
			Accessible target = targets[i];
			target.AddRef();
			OS.MoveMemory(ppTargets + i * C.PTR_SIZEOF, new long[] { target.getAddress() }, C.PTR_SIZEOF);
		}
		OS.MoveMemory(pNTargets, new int [] { count }, 4);
		return COM.S_OK;
	}

	void addTarget(Accessible target) {
		if (containsTarget(target)) return;
		Accessible[] newTargets = new Accessible[targets.length + 1];
		System.arraycopy(targets, 0, newTargets, 0, targets.length);
		newTargets[targets.length] = target;
		targets = newTargets;
	}

	boolean containsTarget(Accessible searched) {
		for (Accessible target : targets) {
			if (target == searched) return true;
		}
		return false;
	}

	void removeTarget(Accessible searched) {
		if (!containsTarget(searched)) return;
		Accessible[] newTargets = new Accessible[targets.length - 1];
		int j = 0;
		for (Accessible target : targets) {
			if (target != searched) {
				newTargets[j++] = target;
			}
		}
		targets = newTargets;
	}

	boolean hasTargets() {
		return targets.length > 0;
	}

	// setString copied from Accessible class
	void setString(long psz, String string) {
		char[] data = (string + "\0").toCharArray();
		long ptr = COM.SysAllocString(data);
		OS.MoveMemory(psz, new long [] { ptr }, C.PTR_SIZEOF);
	}
}
