package org.eclipse.swt.examples.launcher;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.ui.IViewPart;import java.io.PrintWriter;/** * LaunchDelegate provide a means of encapsulating a method for launching a * variety of programs of different types along with information about these * programs. */
public interface LaunchDelegate {	/**	 * Launch the program described by this object.	 * 	 * @param hostView the IViewPart for the launcher program.  Use this to ensure that	 *        child programs are visible on the same display / Eclipse perspective as	 *        the launcher application	 * @param logWriter a PrintWriter to which run-log information will be written, must NOT be null	 * 	 * @return true iff the launch was (probably) successful	 */
	public boolean launch(IViewPart hostView, PrintWriter logWriter);
}
