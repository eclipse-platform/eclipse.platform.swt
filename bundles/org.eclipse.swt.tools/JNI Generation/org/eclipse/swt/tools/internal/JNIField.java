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

public interface JNIField extends JNIItem {
	
public static final String[] FLAGS = {FLAG_NO_GEN, FLAG_NO_WINCE};

public String getName();
	
public int getModifiers();

public JNIType getType();

public JNIType getType64();

public JNIClass getDeclaringClass();

public String getAccessor();

public String getCast();

public String getExclude();

public void setAccessor(String str);

public void setCast(String str);

public void setExclude(String str);
}
