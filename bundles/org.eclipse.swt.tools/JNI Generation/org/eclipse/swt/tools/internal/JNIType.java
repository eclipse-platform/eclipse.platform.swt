/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
