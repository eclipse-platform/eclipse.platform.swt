/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

public interface JNIMethod extends JNIItem {

	public static final String[] FLAGS = {FLAG_NO_GEN, FLAG_ADDRESS, FLAG_CONST, FLAG_DYNAMIC, FLAG_JNI, FLAG_CAST, FLAG_CPP, FLAG_NEW, FLAG_DELETE, FLAG_GCNEW, FLAG_GCOBJECT, FLAG_SETTER, FLAG_GETTER, FLAG_ADDER};
	
public String getName();

public int getModifiers();

public boolean isNativeUnique();

public JNIParameter[] getParameters();

public JNIType getReturnType();

public JNIType getReturnType64();

public JNIType[] getParameterTypes();

public JNIType[] getParameterTypes64();

public JNIClass getDeclaringClass();

public String getAccessor();

public String getExclude();

public void setAccessor(String str);

public void setExclude(String str);
}
