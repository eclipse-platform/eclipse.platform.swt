/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class OLECMDTEXT {
	public int cmdtextf;    
	public int cwActual;    
	public int cwBuf;    
	public short rgwz;

	// Note: this is a variable sized struct.  The last field rgwz can vary in size.
	// Currently we do not use this field and do not support accessing anything more
	// than the first char in the field.
}
