package org.eclipse.swt.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
