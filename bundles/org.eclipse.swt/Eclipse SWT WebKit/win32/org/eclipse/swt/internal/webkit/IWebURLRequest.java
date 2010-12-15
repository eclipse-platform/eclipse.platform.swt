/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.webkit;


import org.eclipse.swt.internal.ole.win32.*;

public class IWebURLRequest extends IUnknown {

public IWebURLRequest (int /*long*/ address) {
	super (address);
}

public int HTTPBody (int /*long*/ []result) {
	return COM.VtblCall (6, getAddress (), result);
}

public int HTTPMethod (int /*long*/ []result) {
	return COM.VtblCall (8, getAddress (), result);
}

public int mutableCopy (int /*long*/ []result) {
	return COM.VtblCall (16, getAddress (), result);
}

public int URL (int /*long*/ []result) {
	return COM.VtblCall (13, getAddress (), result);
}

}
