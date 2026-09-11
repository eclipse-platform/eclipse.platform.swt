/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.canvasext;

import org.eclipse.swt.widgets.*;

/**
 * Defines a factory interface for creating external canvas handlers.
 * Implementations of this interface can be provided to enable support for
 * different types of canvas extensions based on the style of the canvas and the
 * availability of an extension factory.
 */
public interface IExternalCanvasFactory {

	IExternalCanvasHandler createCanvasExtension(Canvas c);

}
