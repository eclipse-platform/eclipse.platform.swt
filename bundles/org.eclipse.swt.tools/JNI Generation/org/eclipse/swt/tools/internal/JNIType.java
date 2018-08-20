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

public String getTypeSignature(boolean define);

public String getTypeSignature1(boolean define);

public String getTypeSignature2(boolean define);

public String getTypeSignature3(boolean define);

public String getTypeSignature4(boolean define, boolean struct);

}
