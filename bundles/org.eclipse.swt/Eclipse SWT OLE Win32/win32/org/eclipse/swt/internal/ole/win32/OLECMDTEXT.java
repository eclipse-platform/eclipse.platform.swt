package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class OLECMDTEXT {
	public int cmdtextf;    
	public int cwActual;    
	public int cwBuf;    
	public short rgwz;

	// Note: this is a variable sized struct.  The last field rgwz can vary in size.
	// Currently we do not use this field and do not support accessing anything more
	// than the first char in the field.
}
