/*******************************************************************************
 * Copyright (c) 2023 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Vector Informatik GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

/**
 * A registry for managing the instance of an {@link SVGRasterizer} implementation.
 * This allows for the registration and retrieval of a single rasterizer instance.
 *
 * @since 3.129
 */
public class SVGRasterizerRegistry {

	/**
     * The instance of the registered {@link SVGRasterizer}.
     */
	private static SVGRasterizer rasterizer;

	 /**
     * Registers the provided implementation of {@link SVGRasterizer}.
     * If a rasterizer has already been registered, subsequent calls to this method
     * will have no effect.
     *
     * @param implementation the {@link SVGRasterizer} implementation to register.
     */
	public static void register(SVGRasterizer implementation) {
		if (rasterizer == null) {
			rasterizer = implementation;
		}
	}

	/**
     * Retrieves the currently registered {@link SVGRasterizer} implementation.
     *
     * @return the registered {@link SVGRasterizer}, or {@code null} if no implementation
     *         has been registered.
     */
	public static SVGRasterizer getRasterizer() {
		return rasterizer;
	}
}