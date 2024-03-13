/*******************************************************************************
 * Copyright (c) 2008, 2016 IBM Corporation and others.
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
 * CaretEvents.
 *
 * @see CaretEvent
 *
 * @since 3.5
 */
@FunctionalInterface
public interface CaretListener extends SWTEventListener {

/**
 * This method is called after the caret offset is changed.
 *
 * The following event fields are used:<ul>
 * <li>event.caretOffset the new caret offset (input)</li>
 * </ul>
 *
 * @param event the given event
 *
 * @see CaretEvent
 */
public void caretMoved(CaretEvent event);

}

