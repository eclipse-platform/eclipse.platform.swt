/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Michael Bangas (Vector Informatik GmbH) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.util.*;

/**
 * A registry for managing the instance of an {@link SVGRasterizer} implementation.
 * This allows for the registration and retrieval of a single rasterizer instance.
 *
 * @since 4.0
 */
class SVGRasterizerRegistry {

	/**
     * The instance of the registered {@link SVGRasterizer}.
     */
	private static final SVGRasterizer RASTERIZER = ServiceLoader.load(SVGRasterizer.class).findFirst().orElse(null);

	/**
     * Retrieves the currently registered {@link SVGRasterizer} implementation.
     *
     * @return the registered {@link SVGRasterizer}, or {@code null} if no implementation
     *         has been registered.
     */
	public static SVGRasterizer getRasterizer() {
		return RASTERIZER;
	}
}