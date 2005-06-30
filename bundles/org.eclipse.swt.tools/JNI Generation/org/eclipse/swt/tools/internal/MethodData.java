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

public class MethodData extends ItemData {
	
	Method method;

public MethodData(Method method, String str) {
	super(str);
	this.method = method;
}

public static String[] getAllFlags() {
	return new String[]{"no_gen", "address", "const", "dynamic", "jni", "cpp", "new", "delete"};
}

public Method getMethod() {
	return method;
}

public String getAccessor() {
	return (String)getParam("accessor");
}

public String getExclude() {
	return (String)getParam("exclude");
}

public void setAccessor(String str) { 
	setParam("accessor", str);
}

public void setExclude(String str) { 
	setParam("exclude", str);
}

}
