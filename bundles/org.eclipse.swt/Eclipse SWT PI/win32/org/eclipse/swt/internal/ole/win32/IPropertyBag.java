/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;


public class IPropertyBag extends IUnknown {

public IPropertyBag (long /*int*/ address) {
	super (address);
}

public int Read (long /*int*/ pszPropName, long /*int*/ pVar, long /*int*/[] pErrorLog) {
	return COM.VtblCall (3, getAddress(), pszPropName, pVar, pErrorLog);
}

}
