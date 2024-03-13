/*******************************************************************************
 * Copyright (c) 2000, 2016s IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 502576
 *******************************************************************************/
package org.eclipse.swt.events;

import org.eclipse.swt.internal.*;

/**
 * This listener interface may be implemented in order to receive
 * SegmentEvents.
 * @see SegmentEvent
 *
 * @since 3.8
 */
@FunctionalInterface
public interface SegmentListener extends SWTEventListener {

/**
 * This method is called when text content is being modified.
 * <p>
 * The following event fields are used:<ul>
 * <li>event.lineText text content (input)</li>
 * <li>event.segments text offsets for segment characters (output)</li>
 * <li>event.segmentsChars characters that should be inserted (output, optional)</li>
 * </ul>
 *
 * @param event the given event
 * @see SegmentEvent
 */
void getSegments(SegmentEvent event);

}
