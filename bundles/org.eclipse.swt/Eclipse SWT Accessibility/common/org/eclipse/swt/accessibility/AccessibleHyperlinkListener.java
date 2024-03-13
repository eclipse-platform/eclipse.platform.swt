/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;

import org.eclipse.swt.internal.SWTEventListener;

/**
 * Classes which implement this interface provide methods
 * that handle AccessibleHyperlink events.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to an accessible using the
 * <code>addAccessibleHyperlinkListener</code> method and removed using
 * the <code>removeAccessibleHyperlinkListener</code> method.
 * </p>
 *
 * @see AccessibleHyperlinkAdapter
 * @see AccessibleHyperlinkEvent
 *
 * @since 3.6
 */
public interface AccessibleHyperlinkListener extends SWTEventListener {
	/**
	 * Returns the anchor for the link at the specified index.
	 *
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] index - a 0 based index identifying the anchor if this object
	 * 		has more than one link, as in the case of an image map</li>
	 * <li>[typical out] result - the returned anchor</li>
	 * <li>[optional out] accessible - the returned anchor.
	 * 		Note: The returned anchor can either be a string or an accessible.
	 * 		For example, for a text link this could be the substring of the containing string
	 * 		where the substring is overridden with link behavior, and for an image link this could be
	 * 		the accessible for the image.</li>
	 * </ul>
	 */
	public void getAnchor(AccessibleHyperlinkEvent e);

	/**
	 * Returns the target of the link at the specified index.
	 *
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] index - a 0 based index identifying the anchor if this object
	 * 		has more than one link, as in the case of an image map</li>
	 * <li>[typical out] result - the returned target</li>
	 * <li>[optional out] accessible - the returned target.
	 * 		Note: The returned target can either be a string or an accessible.
	 * 		For example, this could be a string URI, or the accessible for the target
	 * 		object to be activated when the link is activated.</li>
	 * </ul>
	 */
	public void getAnchorTarget(AccessibleHyperlinkEvent e);

	/**
	 * Returns the 0 based character offset at which the textual representation of the hyperlink starts.
	 * <p>
	 * The returned value is related to the AccessibleTextExtended interface of the object that
	 * owns this hyperlink.
	 * </p>
	 *
	 * @param e an event object containing the following fields:<ul>
	 * <li>[out] index</li>
	 * </ul>
	 */
	public void getStartIndex(AccessibleHyperlinkEvent e);

	/**
	 * Returns the 0 based character offset at which the textual representation of the hyperlink ends.
	 * <p>
	 * The returned value is related to the AccessibleTextExtended interface of the object that
	 * owns this hyperlink. The character at the index is not part of the hypertext.
	 * </p>
	 *
	 * @param e an event object containing the following fields:<ul>
	 * <li>[out] index</li>
	 * </ul>
	 */
	public void getEndIndex(AccessibleHyperlinkEvent e);
}
