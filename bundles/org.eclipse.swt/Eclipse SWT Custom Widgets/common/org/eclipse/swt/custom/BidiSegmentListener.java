/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
package org.eclipse.swt.custom;

import org.eclipse.swt.internal.*;

/**
 * This listener interface may be implemented in order to receive
 * BidiSegmentEvents.
 * @see BidiSegmentEvent
 */
@FunctionalInterface
public interface BidiSegmentListener extends SWTEventListener {

/**
 * This method is called when a line needs to be reordered for
 * measuring or rendering in a bidi locale.
 * <p>
 * The following event fields are used:<ul>
 * <li>event.lineOffset line start offset (input)</li>
 * <li>event.lineText line text (input)</li>
 * <li>event.segments text segments that should be reordered separately (output)</li>
 * <li>event.segmentsChars characters that should be inserted (output, optional)</li>
 * </ul>
 *
 * @param event the given event
 * @see BidiSegmentEvent
 */
public void lineGetSegments(BidiSegmentEvent event);

}

