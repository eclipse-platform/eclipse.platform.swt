package org.eclipse.swt.ole.win32;

import org.eclipse.swt.internal.ole.win32.COM;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class OleFunctionDescription {

	public int id;
	public String name;
	public OleParameterDescription[] args;
	public int optionalArgCount;
	public short returnType;
	public int invokeKind;
	public int funcKind;
	public short flags;
	public int callingConvention;
	public String documentation;
	public String helpFile;

}	
