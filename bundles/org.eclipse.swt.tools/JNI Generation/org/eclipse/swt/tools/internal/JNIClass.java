/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
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
package org.eclipse.swt.tools.internal;

public interface JNIClass extends JNIItem {
	
public static String[] FLAGS = {FLAG_NO_GEN, FLAG_CPP};

public String getName();

public String getSimpleName();

public JNIClass getSuperclass();

public JNIField[] getDeclaredFields();

public JNIMethod[] getDeclaredMethods();

public String getExclude();

public void setExclude(String str);
}
