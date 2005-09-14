/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl;

import org.eclipse.swt.*;

public class Library {

public static void loadLibrary (String name) {
	String platform = SWT.getPlatform ();	
	try {
		String newName = name + '-' + platform;
		System.loadLibrary (newName);
		return;
	} catch (UnsatisfiedLinkError e1) {		
		throw e1;
	}
}
}
