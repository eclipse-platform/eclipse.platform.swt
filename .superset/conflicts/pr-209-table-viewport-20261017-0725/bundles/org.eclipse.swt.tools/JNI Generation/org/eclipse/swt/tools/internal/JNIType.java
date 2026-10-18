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

public interface JNIType {

public boolean isPrimitive();

public boolean isArray();

public JNIType getComponentType();

public boolean isType(String type);

public String getName();

public String getSimpleName();

public String getTypeSignature();

public String getTypeSignature1();

public String getTypeSignature2();

public String getTypeSignature3();

public String getTypeSignature4(boolean struct);

}
