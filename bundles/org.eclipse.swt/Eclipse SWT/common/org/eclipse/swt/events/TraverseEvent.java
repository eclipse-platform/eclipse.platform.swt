package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class are sent as a result of
 * widget traversal actions.
 *
 * @see TraverseListener
 */

public class TraverseEvent extends KeyEvent {
	
	/**
	 * the type of traversal
	 * <p><ul>
	 * <li>{@link SWT#TRAVERSE_NONE}</li>
	 * <li>{@link SWT#TRAVERSE_ESCAPE}</li>
	 * <li>{@link SWT#TRAVERSE_RETURN}</li>
	 * <li>{@link SWT#TRAVERSE_TAB_NEXT}</li>
	 * <li>{@link SWT#TRAVERSE_TAB_PREVIOUS}</li>
	 * <li>{@link SWT#TRAVERSE_ARROW_NEXT}</li>
	 * <li>{@link SWT#TRAVERSE_ARROW_PREVIOUS}</li>
	 * <li>{@link SWT#TRAVERSE_MNEMONIC}</li>
	 * <li>{@link SWT#TRAVERSE_PAGE_NEXT}</li>
	 * <li>{@link SWT#TRAVERSE_PAGE_PREVIOUS}</li>
	 * </ul></p>
	 */
	public int detail;
	
	/**
	 * a flag indicating whether the operation should be allowed
	 */
	public boolean doit;

/**
 * Constructs a new instance of this class based on the
 * information in the given untyped event.
 *
 * @param e the untyped event containing the information
 */
public TraverseEvent(Event e) {
	super(e);
	this.doit = e.doit;
	this.detail = e.detail;
}

}
