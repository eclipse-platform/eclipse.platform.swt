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

public class ClassData extends ItemData {
	
	Class clazz;

public ClassData(Class clazz, String str) {
	super(str);
	this.clazz = clazz;
}
	
public static String[] getAllFlags() {
	return new String[]{FLAG_NO_GEN, FLAG_CPP};
}

public Class getClazz() {
	return clazz;
}

public String getExclude() {
	return (String)getParam("exclude");
}

public void setExclude(String str) { 
	setParam("exclude", str);
}

}
