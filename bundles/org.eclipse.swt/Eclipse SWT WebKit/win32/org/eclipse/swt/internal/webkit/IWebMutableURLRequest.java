/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
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

public class IWebMutableURLRequest extends IWebURLRequest {

public IWebMutableURLRequest (int /*long*/ address) {
	super (address);
}

//public int setHTTPBody (int /*long*/ data) {
//	return COM.VtblCall (21, getAddress (), data);
//}

//public int setHTTPBodyStream (int /*long*/ data) {
//	return COM.VtblCall (22, getAddress (), data);
//}

public int setHTTPMethod (int post) {
	return COM.VtblCall (23, getAddress (), post);
}

public int setURL (int /*long*/ theUrl) {
	return COM.VtblCall (27, getAddress (), theUrl);
}

public int setValue (int /*long*/ value, int /*long*/ field) {
	return COM.VtblCall (28, getAddress (), value, field);
}

public int setAllowsAnyHTTPSCertificate () {
	return COM.VtblCall (29, getAddress ());
}

}
