/*******************************************************************************
 * Copyright (c) 2025 Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.DeviceData;

public abstract class DisplayCommon extends Device {

	private ColorProvider colorProvider;
	private RendererFactory rendererFactory;

	public DisplayCommon(DeviceData data) {
		super(data);

		colorProvider = DefaultColorProvider.createLightInstance();
		rendererFactory = new DefaultRendererFactory();
	}

	/**
	 * @noreference this is still experimental API and might be removed
	 */
	public final RendererFactory getRendererFactory() {
		return rendererFactory;
	}

	/**
	 * @noreference this is still experimental API and might be removed
	 */
	public final void setRendererFactory(RendererFactory rendererFactory) {
		if (rendererFactory == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		this.rendererFactory = rendererFactory;
	}

	/**
	 * Return the color provider used for custom-drawn controls.
	 * @return a non-null instance of the color provider
	 * @noreference this is still experimental API and might be removed
	 */
	public final ColorProvider getColorProvider() {
		return colorProvider;
	}

	/**
	 * Set the color provider used for custom-drawn controls.
	 * @param colorProvider a non-null color provider
	 * @noreference this is still experimental API and might be removed
	 */
	public final void setColorProvider(ColorProvider colorProvider) {
		if (colorProvider == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		this.colorProvider = colorProvider;
		// todo: redraw all (custom-drawn) widgets
	}
}
