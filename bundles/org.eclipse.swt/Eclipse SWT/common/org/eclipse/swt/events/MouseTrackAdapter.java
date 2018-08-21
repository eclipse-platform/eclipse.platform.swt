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


/**
 * This adapter class provides default implementations for the
 * methods described by the <code>MouseTrackListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>MouseEvent</code>s which
 * occur as the mouse pointer passes (or hovers) over controls can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 * <p>
 * An alternative to this class are the static helper methods
 * {@link MouseTrackListener#mouseEnterAdapter(java.util.function.Consumer)},
 * {@link MouseTrackListener#mouseExitAdapter(java.util.function.Consumer)}
 * and
 * {@link MouseTrackListener#mouseHoverAdapter(java.util.function.Consumer)},
 * which accept a lambda expression or a method reference that implements the event consumer.
 * </p>
 *
 * @see MouseTrackListener
 * @see MouseEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public abstract class MouseTrackAdapter implements MouseTrackListener {

/**
 * Sent when the mouse pointer passes into the area of
 * the screen covered by a control.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the mouse enter
 */
@Override
public void mouseEnter(MouseEvent e) {
}

/**
 * Sent when the mouse pointer passes out of the area of
 * the screen covered by a control.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the mouse exit
 */
@Override
public void mouseExit(MouseEvent e) {
}

/**
 * Sent when the mouse pointer hovers (that is, stops moving
 * for an (operating system specified) period of time) over
 * a control.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the hover
 */
@Override
public void mouseHover(MouseEvent e) {
}
}
