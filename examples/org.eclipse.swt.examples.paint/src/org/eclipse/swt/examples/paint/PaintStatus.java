package org.eclipse.swt.examples.paint;import org.eclipse.swt.graphics.Point;import org.eclipse.swt.widgets.Text;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class PaintStatus {
	private Text statusText;
	private String actionInfo, messageInfo, coordInfo;
	
	/**
	 * Create a PaintStatus
	 * 
	 * @param statusText the handle of status bar text widget to use
	 */
	public PaintStatus(Text statusText) {
		this.statusText = statusText;
		clear();
	}

	/**
	 * Clear the status bar
	 */
	public void clear() {
		clearAction();
		clearMessage();
		clearCoord();
	}

	/**
	 * Set the status bar action text
	 *
	 * @param action the action in progress
	 */
	public void setAction(String action) {
		actionInfo = action;
		update();
	}
	
	/**
	 * Clear the status bar action text
	 */
	public void clearAction() {
		actionInfo = "";
		update();
	}

	/**
	 * Set the status bar message text
	 * 
	 * @param message the message to display
	 */
	public void setMessage(String message) {
		messageInfo = message;
		update();
	}
	
	/**
	 * Clear the status bar message text
	 */
	public void clearMessage() {
		messageInfo = "";
		update();
	}

	/**
	 * Set the coordinates in the status bar
	 * 
	 * @param coord the coordinates to display
	 */
	public void setCoord(Point coord) {
		coordInfo = PaintPlugin.getResourceString("status.Coord.format", new Object[]			{ new Integer(coord.x), new Integer(coord.y)});		update();
	}

	/**
	 * Set the coordinate range in the status bar
	 * 
	 * @param a the "from" coordinate
	 * @param b the "to" coordinate
	 */
	public void setCoordRange(Point a, Point b) {
		coordInfo = PaintPlugin.getResourceString("status.CoordRange.format", new Object[]			{ new Integer(a.x), new Integer(a.y), new Integer(b.x), new Integer(b.y)});		update();
	}

	/**
	 * Clear the coordinates in the status bar
	 */
	public void clearCoord() {
		coordInfo = "";
		update();
	}

	/**
	 * Update the display
	 */
	private void update() {
		statusText.setText(			PaintPlugin.getResourceString("status.Bar.format", new Object[]			{ actionInfo, messageInfo, coordInfo }));	}
}
