package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class are sent as a result of
 * widgets being selected.
 * <p>
 * Note: The fields that are filled in depend on the widget.
 * </p>
 *
 * @see SelectionListener
 */

public class SelectionEvent extends TypedEvent {
	
	/**
	 * the item that was selected
	 */
	public Widget item;
	
	/**
	 * extra detail information about the selection, depending on the widget
	 * <p><b>Sash</b><ul>
	 * <li>{@link SWT#DRAG}</li>
	 * </ul></p><p><b>ScrollBar and Slider</b><ul>
	 * <li>{@link SWT#DRAG}</li>
	 * <li>{@link SWT#HOME}</li>
	 * <li>{@link SWT#END}</li>
	 * <li>{@link SWT#ARROW_DOWN}</li>
	 * <li>{@link SWT#ARROW_UP}</li>
	 * <li>{@link SWT#PAGE_DOWN}</li>
	 * <li>{@link SWT#PAGE_UP}</li>
	 * </ul></p><p><b>Table, Tree and TableTree</b><ul>
	 * <li>{@link SWT#CHECK}</li>
	 * </ul></p><p><b>CoolItem and ToolItem</b><ul>
	 * <li>{@link SWT#ARROW}</li>
	 * </ul></p>
	 */
	public int detail;

	/**
	 * the x location of the selected area
	 */
	public int x;
	
	/**
	 * the y location of selected area
	 */
	public int y;
	
	/**
	 * the width of selected area
	 */
	public int width;
	
	/**
	 * the height of selected area
	 */
	public int height;

	/**
	 * the state of the keyboard modifier keys at the time
	 * the event was generated.
	 */
	public int stateMask;

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
public SelectionEvent(Event e) {
	super(e);
	this.item = e.item;
	this.x = e.x;
	this.y = e.y;
	this.width = e.width;
	this.height = e.height;
	this.detail = e.detail;
	this.stateMask = e.stateMask;
	this.doit = e.doit;
}

}

