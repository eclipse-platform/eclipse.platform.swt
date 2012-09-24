/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class IDispatchEx extends IDispatch {

public IDispatchEx (long /*int*/ address) {
	super (address);
}

public int GetDispID (long /*int*/ bstrName, int grfdex, int[] pid) {
	return COM.VtblCall (7, address, bstrName, grfdex, pid);
}

public int InvokeEx (int id, int lcid, int wFlags, DISPPARAMS pdp, long /*int*/ pvarRes, EXCEPINFO pei, long /*int*/ pspCaller) {
	return COM.VtblCall (8, address, id, lcid, wFlags, pdp, pvarRes, pei, pspCaller);
}

public int DeleteMemberByName (long /*int*/ bstrName, int grfdex) {
	return COM.VtblCall (9, address, bstrName, grfdex);
}

public int DeleteMemberByDispID (int id) {
	return COM.VtblCall (10, address, id);
}

public int GetMemberProperties (int id, int grfdexFetch, int[] pgrfdex) {
	return COM.VtblCall (11, address, id, grfdexFetch, pgrfdex);
}

public int GetMemberName (int id, long /*int*/[] pbstrName) {
	return COM.VtblCall (12, address, id, pbstrName);
}

public int GetNextDispID (int grfdex, int id, int[] pid) {
	return COM.VtblCall (13, address, grfdex, id, pid);
}

public int GetNameSpaceParent (long /*int*/[] ppunk) {
	return COM.VtblCall (14, address, ppunk);
}
}
