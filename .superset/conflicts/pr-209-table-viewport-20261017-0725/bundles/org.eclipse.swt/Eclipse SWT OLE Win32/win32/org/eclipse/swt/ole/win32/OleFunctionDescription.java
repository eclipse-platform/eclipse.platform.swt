/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
package org.eclipse.swt.ole.win32;


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
