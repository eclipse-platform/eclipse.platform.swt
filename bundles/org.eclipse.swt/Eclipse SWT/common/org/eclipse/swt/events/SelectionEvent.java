package org.eclipse.swt.events;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

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
	 * extra detail information about the selection
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

