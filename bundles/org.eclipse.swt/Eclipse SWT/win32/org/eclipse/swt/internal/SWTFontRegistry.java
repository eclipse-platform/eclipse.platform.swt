/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import org.eclipse.swt.graphics.*;

/**
 * This class is used in the win32 implementation only to support
 * re-usage of fonts.
 * <p>
 * <b>IMPORTANT:</b> This class is <em>not</em> part of the public
 * API for SWT. It is marked public only so that it can be shared
 * within the packages provided by SWT. It is not available on all
 * platforms, and should never be called from application code.
 * </p>
 * @noreference This class is not intended to be referenced by clients
 */
public interface SWTFontRegistry {

	/**
	 * Returns a system font optimally suited for the specified zoom.
	 *
	 * @param zoom zoom in % of the standard resolution to determine the appropriate system font
	 * @return the system font best suited for the specified zoom
	 */
	Font getSystemFont(int zoom);

	/**
	 * Provides a font optimally suited for the specified zoom. Fonts created in this manner
	 * are managed by the font registry and should not be disposed of externally.
	 *
	 * @param fontData the data used to create the font
	 * @param zoom zoom in % of the standard resolution to determine the appropriate font
	 * @return the font best suited for the specified zoom
	 */
	Font getFont(FontData fontData, int zoom);

	/**
	 * Disposes all fonts managed by the font registry.
	 */
	void dispose();
}
