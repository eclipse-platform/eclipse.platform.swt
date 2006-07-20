/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.lang.reflect.Method;

public class ParameterData extends ItemData {
	
	Method method;
	int parameter;

public ParameterData(Method method, int parameter, String str) {
	super(str);
	this.method = method;
	this.parameter = parameter;
}

public static String[] getAllFlags() {
	return new String[]{FLAG_NO_IN, FLAG_NO_OUT, FLAG_CRITICAL, FLAG_INIT, FLAG_STRUCT, FLAG_UNICODE, FLAG_SENTINEL};
}

public String getCast() {
	String cast = ((String)getParam("cast")).trim();
	if (cast.length() > 0) {
		if (!cast.startsWith("(")) cast = "(" + cast;
		if (!cast.endsWith(")")) cast = cast + ")";
	}
	return cast;
}

public Method getMethod() {
	return method;
}

public int getParameter() {
	return parameter;
}

public void setCast(String str) {
	setParam("cast", str);
}

}
