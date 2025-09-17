/*******************************************************************************
 * Copyright (c) 2006, 2025 IBM Corporation and others.
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

package org.eclipse.swt.examples.graphics;

/**
 * Used to perform an action after an item in a Menu has been selected.
 *
 * @see org.eclipse.swt.examples.graphics.ColorMenu
 * @see org.eclipse.swt.examples.graphics.GraphicsBackground
 */
public interface ColorListener {

	void setColor(GraphicsBackground gb);

}
