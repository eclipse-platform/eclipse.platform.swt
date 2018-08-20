/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
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

public interface JNIParameter extends JNIItem {

	public static final String[] FLAGS = {FLAG_NO_IN, FLAG_NO_OUT, FLAG_CRITICAL, FLAG_INIT, FLAG_STRUCT, FLAG_UNICODE, FLAG_SENTINEL, FLAG_OBJECT};

public String getCast();

public JNIMethod getMethod();

public int getParameter();

public JNIClass getTypeClass();

public JNIType getType();

public JNIType getType64();

public void setCast(String str);
}
