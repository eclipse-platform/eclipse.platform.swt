/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

public class NativeFunction {
	String name;
	int callCount;
	
public NativeFunction(String name, int callCount) {
	this.name = name;
	this.callCount = callCount;
}

void subtract(NativeFunction func) {
	this.callCount -= func.callCount;
}

public int getCallCount() {
	return callCount;
}

public String getName() {
	return name;
}
}
