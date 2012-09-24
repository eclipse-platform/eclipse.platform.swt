/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class IOleCommandTarget extends IUnknown {
public IOleCommandTarget(long /*int*/ address) {
	super(address);
}
public int Exec(
	GUID pguidCmdGroup,  // Pointer to command group
	int  nCmdID,         // Identifier of command to execute
	int  nCmdExecOpt,    // Options for executing the command
	long /*int*/  pvaIn,        // Pointer to input arguments
	long /*int*/  pvaOut        // Pointer to command output
){
	return COM.VtblCall(4, address, pguidCmdGroup, nCmdID, nCmdExecOpt, pvaIn, pvaOut);
}
public int QueryStatus(
	GUID       pguidCmdGroup, // Pointer to command group
	int        cCmds,         // Number of commands in prgCmds array
	OLECMD     prgCmds,       // Array of commands
	OLECMDTEXT pCmdText       // Pointer to name or status of command
){
	// we only support querying for one command at a time
	if (cCmds > 1) return COM.E_INVALIDARG;
	return COM.VtblCall(3, address, pguidCmdGroup, cCmds, prgCmds, pCmdText);
}
}
