package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * Superinterface for all drawing objects.
 * All drawing objects know how to render themselved to the screen and can draw a
 * temporary version of themselves for previewing the general appearance of the
 * object onscreen before it gets committed.
 */
public abstract class Figure {
	/**
	 * Draws an object to the specified GC
	 * <p>
	 * The GC must be set up as follows (and must be returned to this state before returning)
	 * <ul>
	 *   <li>setXORMode(false)
	 * </ul>
	 * </p>
	 * 
	 * @param gc the GC to draw on
	 * @param offset the offset to add to virtual coordinates to get display coordinates
	 */
	public abstract void draw(GC gc, Point offset);

	/**
	 * Draws a preview copy of the object to the specified GC
	 * <p>
	 * The GC must be set up as follows (and must be returned to this state before returning)
	 * <ul>
	 *   <li>setXORMode(true)
	 *   <li>setForeground(new Color(display, 255, 255, 255))
	 *   <li>setBackground(new Color(display, 127, 127, 127))
	 * </ul>
	 * </p>
	 * 
	 * @param gc the GC to draw on
	 * @param offset the offset to add to virtual coordinates to get display coordinates
	 * @return object state that must be passed to erasePreview() later to erase this object
	 */
	public abstract Object drawPreview(GC gc, Point offset);

	/**
	 * Erases a preview copy of the object to the specified GC
	 * <p>
	 * Note that erasures are guaranteed to occur in the reverse order to the original drawing
	 * order and that the GC's contents will be as they were when the drawPreview() that supplied
	 * <code>rememberedState</code>returned.
	 * </p><p>
	 * The GC must be set up and restored as with drawPreview().
	 * </p>
	 * 
	 * @param gc the GC to draw on
	 * @param offset the offset to add to virtual coordinates to get display coordinates
	 * @param rememberedState the state returned by a previous drawPreview() using this instance
	 */
	public abstract void erasePreview(GC gc, Point offset, Object rememberedState);
}
