/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;

/**
 * WARNING: API UNDER CONSTRUCTION
 * 
 * This adapter class provides default implementations for the
 * methods in the <code>AccessibleAttributeListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>AccessibleAttribute</code> events can
 * extend this class and override only the methods that they are
 * interested in.
 * </p>
 *
 * @see AccessibleAttributeListener
 * @see AccessibleAttributeEvent
 * @see AccessibleTextAttributeEvent
 *
 * @since 3.6
 */
public class AccessibleAttributeAdapter implements AccessibleAttributeListener {
	/**
	 * Returns attributes specific to this Accessible object.
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[out] topMargin the top margin in pixels
	 * <li>[out] bottomMargin the bottom margin in pixels
	 * <li>[out] leftMargin the left margin in pixels
	 * <li>[out] rightMargin the right margin in pixels
	 * <li>[out] tabStops an array of pixel locations
	 * <li>[out] justify whether or not to justify the text
	 * <li>[out] alignment one of <code>SWT#LEFT</code>, <code>SWT#RIGHT</code> or <code>SWT#CENTER</code>
	 * <li>[out] indent the indent in pixels
	 * </ul>
	 */
	public void getAttributes(AccessibleAttributeEvent e) {}

	/**
	 * Returns text attributes specific to this Accessible object.
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] offset the text offset (0 based)
	 * <li>[out] start the starting offset of the character range
	 * 		over which all text attributes match those of offset (0 based)
	 * <li>[out] end the offset of the first character past the character range
	 * 		over which all text attributes match those of offset (0 based)
	 * <li>[out] textStyle the TextStyle of the character range
	 * </ul>
	 */
	public void getTextAttributes(AccessibleTextAttributeEvent e) {}
}
