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

public IWebMutableURLRequest (long /*int*/ address) {
	super (address);
}

//public int setHTTPBody (long /*int*/ data) {
//	return COM.VtblCall (21, getAddress (), data);
//}

//public int setHTTPBodyStream (long /*int*/ data) {
//	return COM.VtblCall (22, getAddress (), data);
//}

public int setHTTPMethod (int post) {
	return COM.VtblCall (23, getAddress (), post);
}

public int setURL (long /*int*/ theUrl) {
	return COM.VtblCall (27, getAddress (), theUrl);
}

public int setValue (long /*int*/ value, long /*int*/ field) {
	return COM.VtblCall (28, getAddress (), value, field);
}

public int setAllowsAnyHTTPSCertificate () {
	return COM.VtblCall (29, getAddress ());
}

}
