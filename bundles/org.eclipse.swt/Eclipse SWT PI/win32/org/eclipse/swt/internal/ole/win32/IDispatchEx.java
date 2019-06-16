/*******************************************************************************
 * Copyright (c) 2008, 2017 IBM Corporation and others.
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
package org.eclipse.swt.internal.ole.win32;

public class IDispatchEx extends IDispatch {

public IDispatchEx (long address) {
	super (address);
}

public int InvokeEx (int id, int lcid, int wFlags, DISPPARAMS pdp, long pvarRes, EXCEPINFO pei, long pspCaller) {
	return COM.VtblCall (8, address, id, lcid, wFlags, pdp, pvarRes, pei, pspCaller);
}

}
