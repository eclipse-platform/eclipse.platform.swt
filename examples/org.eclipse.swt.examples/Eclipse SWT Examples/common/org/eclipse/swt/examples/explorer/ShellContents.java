package org.eclipse.swt.examples.explorer;/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.widgets.*;

public interface ShellContents {
	/**
	 * Construct the UI
	 * 
	 * @param container the ShellContainer managing the Shell we are rendering inside
	 */
	public void createShellContents(ShellContainer shellContainer);
	
	/**
	 * Dispose of allocated platform and operating system resources
	 */
	public void dispose();
}
