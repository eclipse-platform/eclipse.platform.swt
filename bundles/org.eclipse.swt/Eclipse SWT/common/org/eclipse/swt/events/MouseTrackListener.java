/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.events;


import java.util.function.*;

import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide methods
 * that deal with the events that are generated as the mouse
 * pointer passes (or hovers) over controls.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addMouseTrackListener</code> method and removed using
 * the <code>removeMouseTrackListener</code> method. When the
 * mouse pointer passes into or out of the area of the screen
 * covered by a control or pauses while over a control, the
 * appropriate method will be invoked.
 * </p>
 *
 * @see MouseTrackAdapter
 * @see MouseEvent
 */
public interface MouseTrackListener extends SWTEventListener {

/**
 * Sent when the mouse pointer passes into the area of
 * the screen covered by a control.
 *
 * @param e an event containing information about the mouse enter
 */
void mouseEnter(MouseEvent e);

/**
 * Sent when the mouse pointer passes out of the area of
 * the screen covered by a control.
 *
 * @param e an event containing information about the mouse exit
 */
void mouseExit(MouseEvent e);

/**
 * Sent when the mouse pointer hovers (that is, stops moving
 * for an (operating system specified) period of time) over
 * a control.
 *
 * @param e an event containing information about the hover
 */
void mouseHover(MouseEvent e);

/**
 * Static helper method to create a <code>MouseTrackListener</code> for the
 * {@link #mouseEnter(MouseEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return MouseTrackListener
 * @since 3.107
 */
static MouseTrackListener mouseEnterAdapter(Consumer<MouseEvent> c) {
	return new MouseTrackAdapter() {
		@Override
		public void mouseEnter(MouseEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>MouseTrackListener</code> for the
 * {@link #mouseExit(MouseEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return MouseTrackListener
 * @since 3.107
 */
static MouseTrackListener mouseExitAdapter(Consumer<MouseEvent> c) {
	return new MouseTrackAdapter() {
		@Override
		public void mouseExit(MouseEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>MouseTrackListener</code> for the
 * {@link #mouseHover(MouseEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return MouseTrackListener
 * @since 3.107
 */
static MouseTrackListener mouseHoverAdapter(Consumer<MouseEvent> c) {
	return new MouseTrackAdapter() {
		@Override
		public void mouseHover(MouseEvent e) {
			c.accept(e);
		}
	};
}
}
