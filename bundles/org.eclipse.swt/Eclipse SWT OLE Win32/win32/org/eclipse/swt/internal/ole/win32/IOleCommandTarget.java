package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class IOleCommandTarget extends IUnknown {
/**
 * IOleCommandTarget constructor comment.
 * @param address int
 */
public IOleCommandTarget(int address) {
	super(address);
}
public int Exec(
	GUID pguidCmdGroup,  // Pointer to command group
	int  nCmdID,         // Identifier of command to execute
	int  nCmdExecOpt,    // Options for executing the command
	int  pvaIn,          // Pointer to input arguments
	int  pvaOut          // Pointer to command output
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
