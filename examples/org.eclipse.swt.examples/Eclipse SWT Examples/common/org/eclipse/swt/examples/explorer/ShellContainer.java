package org.eclipse.swt.examples.explorer;/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.widgets.*;

public class ShellContainer {
	private ShellContents shellContents;
	private Shell shell;
	
	/**
	 * Constructs a ShellViewer for a ShellContents
	 * 
	 * @param shellContents the ShellContents to display inside the viewer
	 */
	public ShellContainer(ShellContents shellContents) {
		this.shellContents = shellContents;
	}
	
	/**
	 * Creates and manages an embedded ShellContents.
	 */
	public void run() {
		Display display = new Display();
		shell = new Shell();
		shellContents.createShellContents(this);
		shell.open();
		while (! shell.isDisposed()) {
			if (! display.readAndDispatch()) display.sleep();
		}		shell = null;
		shellContents.dispose();
		display.dispose();
	}

	/**
	 * Closes a ShellViewer
	 */
	public void close() {
		if (shell != null && ! shell.isDisposed()) shell.dispose();
	}
	/**	 * Gets the Shell associated with this container	 * 	 * @return the Shell instance or null if none (i.e. run() not yet called)	 */	public Shell getShell() {		return shell;	}}
